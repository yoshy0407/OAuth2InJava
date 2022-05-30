package com.example.oauth2.authorization.oauth2.web.token;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth2.authorization.oauth2.domain.client.OAuth2ClientApplicationService;
import com.example.oauth2.authorization.oauth2.domain.token.TokenEndpointService;
import com.example.oauth2.authorization.oauth2.exception.NoRollbackException;
import com.example.oauth2.authorization.oauth2.exception.OAuth2ClientException;
import com.example.oauth2.authorization.oauth2.exception.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.web.token.model.ErrorResponse;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
public class TokenEndpointController {

	private final OAuth2ClientApplicationService clientService;
	
	private final TokenEndpointService service;
	
	public TokenEndpointController(
			TokenEndpointService service, 
			OAuth2ClientApplicationService clientService) {
		this.service = service;
		this.clientService = clientService;
	}
	
	//ここに認証が必要
	//Basic認証
	@PostMapping("/oauth2/token")
	public String token(TokenEndpointRequest req, @RequestHeader("Authorization") Optional<String> authorization) 
			throws OAuth2ClientException, OAuth2TokenException, NoRollbackException {
		clientAuthenticate(req, authorization);
		JsonNode json = this.service.generateToken(req);
		return json.toString();
		
	}
	
	private void clientAuthenticate(TokenEndpointRequest req, Optional<String> authorization) throws OAuth2ClientException {
		
		if (authorization.isPresent()) {
			this.clientService.authenticateBasic(authorization.get());
			return;
		}
		if (StringUtils.hasLength(req.getClientId())) {
			this.clientService.authenticate(req.getClientId(), req.getClientSecret());
		}
	}
	
	@ExceptionHandler({OAuth2TokenException.class, OAuth2ClientException.class, NoRollbackException.class})
	public ResponseEntity<ErrorResponse> handleOAuth2TokenException(OAuth2TokenException ex) {
		ErrorResponse res = new ErrorResponse();
		res.setError(ex.getMessage());
		
		return ResponseEntity.status(ex.getHttpStatus())
				.body(res);
	}
}
