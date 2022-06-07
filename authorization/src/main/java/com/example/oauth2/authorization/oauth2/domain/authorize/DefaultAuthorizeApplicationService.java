package com.example.oauth2.authorization.oauth2.domain.authorize;

import java.net.URI;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.example.oauth2.authorization.oauth2.domain.authorize.processor.AuthorizationProcessor;
import com.example.oauth2.authorization.oauth2.domain.authorize.spi.AuthorizeApplicationService;
import com.example.oauth2.authorization.oauth2.domain.client.OAuth2ClientDomainService;
import com.example.oauth2.authorization.oauth2.exception.OAuth2AuthorizationException;
import com.example.oauth2.authorization.oauth2.exception.UnsupportedResponseType;
import com.example.oauth2.authorization.oauth2.exception.client.ClientNotFoundException;
import com.example.oauth2.authorization.oauth2.exception.client.InvalidRedirectUriException;
import com.example.oauth2.authorization.oauth2.exception.client.InvalidScopeException;
import com.example.oauth2.authorization.oauth2.web.authorize.AuthorizeEndpointRequest;

@Service
public class DefaultAuthorizeApplicationService implements AuthorizeApplicationService {

	private final OAuth2ClientDomainService domainService;

	private final List<AuthorizationProcessor> processors;

	public DefaultAuthorizeApplicationService(
			OAuth2ClientDomainService domainService,
			List<AuthorizationProcessor> processors) {
		this.domainService = domainService;
		this.processors = processors;
	}

	@Override
	public void checkClient(AuthorizeEndpointRequest req) throws OAuth2AuthorizationException {
		try {
			this.domainService.checkClient(req.getClientId(), req.getRedirectUri(), req.getScope());
		} catch (InvalidScopeException | InvalidRedirectUriException | ClientNotFoundException e) {
			throw new OAuth2AuthorizationException(e.getMessage());
		}
	}
	
	@Override
	public URI authorize(AuthorizeEndpointRequest req) {
		boolean supported = false;
		URI redirectUri = null;

		//入力チェック
		try {
			this.domainService.checkClient(req.getClientId(), req.getRedirectUri(), req.getScope());
		} catch (InvalidRedirectUriException | ClientNotFoundException e) {
			AuthorizeErrorUriBuilder builder = AuthorizeErrorUriBuilder.unauthorizedClient(req.getRedirectUri());
			builder.errorDescription(e.getMessage());
			ifNeedSetState(req, builder);
			return builder.toUri();
			
		} catch (InvalidScopeException e) {
			AuthorizeErrorUriBuilder builder = AuthorizeErrorUriBuilder.invalidScope(req.getRedirectUri());
			builder.errorDescription(e.getMessage());
			ifNeedSetState(req, builder);
			return builder.toUri();
		} 
		
		if (req.getResponseType() == null || req.getResponseType().isEmpty()) {
			AuthorizeErrorUriBuilder builder = AuthorizeErrorUriBuilder.invalidRequest(req.getRedirectUri())
					.errorDescription("response_typeに値が設定されていません。");
			ifNeedSetState(req, builder);
			return builder.toUri();
		}

		for (AuthorizationProcessor processor : processors) {
			if (processor.supports(req.getResponseType())) {
				redirectUri = processor.authorize(req);
				supported = true;
				break;
			}
		}

		if (!supported) {
			throw new UnsupportedResponseType();
		}

		return redirectUri;
	}

	private void ifNeedSetState(AuthorizeEndpointRequest req, AuthorizeErrorUriBuilder builder) {
		if (StringUtils.hasLength(req.getState())) {
			builder.state(req.getState());
		}
	}
}
