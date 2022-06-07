package com.example.oauth2.authorization.oauth2.web.token;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth2.authorization.oauth2.domain.client.spi.OAuth2ClientApplicationService;
import com.example.oauth2.authorization.oauth2.domain.token.spi.TokenApplicationService;
import com.example.oauth2.authorization.oauth2.exception.NoRollbackException;
import com.example.oauth2.authorization.oauth2.exception.token.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.web.token.model.ErrorResponse;
import com.fasterxml.jackson.databind.JsonNode;


@RestController
public class TokenEndpointController {

	private final OAuth2ClientApplicationService clientService;
	
	private final TokenApplicationService tokenService;
	
	public TokenEndpointController(
			TokenApplicationService tokenService, 
			OAuth2ClientApplicationService clientService) {
		this.tokenService = tokenService;
		this.clientService = clientService;
	}
	
	//ここに認証が必要
	//Basic認証
	@PostMapping("/oauth2/token")
	public String token(TokenEndpointRequest req, @RequestHeader("Authorization") Optional<String> authorization) 
			throws OAuth2TokenException, NoRollbackException {
		clientService.authenticate(authorization, req.getClientId(), req.getClientSecret());
		JsonNode json = this.tokenService.generateToken(req);
		return json.toString();
		
	}
	
	@ExceptionHandler({OAuth2TokenException.class})
	public ResponseEntity<ErrorResponse> handleOAuth2TokenException(OAuth2TokenException ex) {
		ErrorResponse res = new ErrorResponse();
		res.setError(ex.errorCode().code());
		res.setErrorDescription(ex.getMessage());

		//:TODO INVALID_CLIENTは、WW-Authenticationヘッダーを返さなければならない
		return ResponseEntity.status(ex.errorCode().getHttpStatus())
				.body(res);
	}
}
