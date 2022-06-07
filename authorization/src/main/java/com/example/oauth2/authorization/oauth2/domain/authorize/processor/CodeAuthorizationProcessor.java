package com.example.oauth2.authorization.oauth2.domain.authorize.processor;

import java.net.URI;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oauth2.authorization.oauth2.domain.authorize.AuthorizeRequest;
import com.example.oauth2.authorization.oauth2.domain.authorize.spi.AuthorizeRequestRepository;
import com.example.oauth2.authorization.oauth2.domain.authorize.spi.AuthorizeTokenGenerator;
import com.example.oauth2.authorization.oauth2.domain.authorize.value.ResponseType;
import com.example.oauth2.authorization.oauth2.exception.OAuth2AuthorizationException;
import com.example.oauth2.authorization.oauth2.web.authorize.AuthorizeEndpointRequest;

@Component
public class CodeAuthorizationProcessor extends AbstractAuthorizationProcessor {

	private final AuthorizeRequestRepository authReqRepository;
	
	private final AuthorizeTokenGenerator authTokenGenerator;
	
	public CodeAuthorizationProcessor(
			AuthorizeRequestRepository authReqRepository,
			AuthorizeTokenGenerator authTokenGenerator) {
		super(ResponseType.CODE);
		this.authReqRepository = authReqRepository;
		this.authTokenGenerator = authTokenGenerator;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws OAuth2AuthorizationException 
	 */
	@Override
	public URI authorize(AuthorizeEndpointRequest req) {
		// アクセストークンを作成
		//このアクセストークンを保存する
		String code = authTokenGenerator.generate();
		
		AuthorizeRequest authReq = AuthorizeRequest.of(
				code, 
				req.getResponseType(), 
				req.getRedirectUri(), 
				req.getClientId(), 
				req.getScope().get(), 
				req.getState());
		
		this.authReqRepository.save(authReq);
		
		return buildRedirectUri(code, req);
	}

	private URI buildRedirectUri(String code, AuthorizeEndpointRequest req) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUri(req.getRedirectUri())
			.queryParam("code", code);
		
		if (StringUtils.hasLength(req.getState())) {
			builder.queryParam("state", req.getState());
		}
			
		return 	builder.build().toUri();
	}

}
