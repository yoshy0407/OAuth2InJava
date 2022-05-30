package com.example.oauth2.authorization.oauth2.domain.authorize.processor;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oauth2.authorization.oauth2.domain.authorize.AuthorizeRequest;
import com.example.oauth2.authorization.oauth2.domain.authorize.AuthorizeRequestRepository;
import com.example.oauth2.authorization.oauth2.domain.authorize.value.ResponseType;
import com.example.oauth2.authorization.oauth2.exception.OAuth2AuthorizationException;
import com.example.oauth2.authorization.oauth2.value.Message;
import com.example.oauth2.authorization.oauth2.web.authorize.AuthorizeEndpointRequest;

@Component
public class CodeAuthorizationProcessor implements AuthorizationProcessor {

	private final AuthorizeRequestRepository authReqRepository;
	
	public CodeAuthorizationProcessor(AuthorizeRequestRepository authReqRepository) {
		this.authReqRepository = authReqRepository;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws OAuth2AuthorizationException 
	 */
	@Override
	public boolean supports(List<ResponseType> responseTypes) throws OAuth2AuthorizationException {
		if (responseTypes == null || responseTypes.isEmpty()) {
			throw new OAuth2AuthorizationException(
					HttpStatus.BAD_REQUEST, 
					Message.MSG1001.resolveMessage("response_type"));
		}
		return responseTypes.contains(ResponseType.CODE);
	}

	/**
	 * {@inheritDoc}
	 * @throws OAuth2AuthorizationException 
	 */
	@Override
	public URI authorize(AuthorizeEndpointRequest req, List<String> scope) throws OAuth2AuthorizationException {
		if (!StringUtils.hasLength(req.getClientId())) {
			throw new OAuth2AuthorizationException(
					HttpStatus.BAD_REQUEST, 
					Message.MSG1001.resolveMessage("client_id"));			
		}

		//redirect_uriは条件によって必須
		
		// アクセストークンを作成
		//このアクセストークンを保存する
		String code = Base64Utils.encodeToString(UUID.randomUUID().toString().getBytes());
		
		AuthorizeRequest authReq = AuthorizeRequest.of(
				code, 
				req.getResponseType(), 
				req.getRedirectUri(), 
				req.getClientId(), req.getScope(), 
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
