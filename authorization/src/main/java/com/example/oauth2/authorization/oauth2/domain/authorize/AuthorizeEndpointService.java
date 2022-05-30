package com.example.oauth2.authorization.oauth2.domain.authorize;

import java.net.URI;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oauth2.authorization.oauth2.domain.authorize.processor.AuthorizationProcessor;
import com.example.oauth2.authorization.oauth2.exception.OAuth2AuthorizationException;
import com.example.oauth2.authorization.oauth2.exception.UnsupportedResponseType;
import com.example.oauth2.authorization.oauth2.web.authorize.AuthorizeEndpointRequest;

@Service
public class AuthorizeEndpointService {

	private final AuthorizeDomainService domainService;
	
	private final List<AuthorizationProcessor> processors;
	
	public AuthorizeEndpointService(
			AuthorizeDomainService domainService,
			List<AuthorizationProcessor> processors) {
		this.domainService = domainService;
		this.processors = processors;
	}

	public URI authorize(AuthorizeEndpointRequest req, List<String> scope) throws OAuth2AuthorizationException {
		boolean supported = false;
		URI redirectUri = null;
		
		if (!this.domainService.checkScope(req, scope)) {
			return UriComponentsBuilder.fromUri(req.getRedirectUri())
					.queryParam("error", "invalid_scope")
					.build().toUri();
		}
		
		for (AuthorizationProcessor processor : processors) {
			if (processor.supports(req.getResponseType())) {
				redirectUri = processor.authorize(req, scope);
				supported = true;
				break;
			}
		}
		
		if (!supported) {
			throw new UnsupportedResponseType();
		}
		
		return redirectUri;
	}
	
}
