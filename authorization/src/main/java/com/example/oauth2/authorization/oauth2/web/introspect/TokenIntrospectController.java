package com.example.oauth2.authorization.oauth2.web.introspect;


import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth2.authorization.oauth2.domain.client.spi.OAuth2ClientApplicationService;
import com.example.oauth2.authorization.oauth2.domain.token.spi.TokenApplicationService;
import com.example.oauth2.authorization.oauth2.exception.token.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.web.token.model.ErrorResponse;

@RestController
public class TokenIntrospectController {

	private final OAuth2ClientApplicationService clientAppService;
	
	private final TokenApplicationService tokenEndpointService;
	
	public TokenIntrospectController(
			OAuth2ClientApplicationService clientAppService,
			TokenApplicationService tokenEndpointService) {
		this.clientAppService = clientAppService;
		this.tokenEndpointService = tokenEndpointService;
	}
	
	@PostMapping("/oauth2/introspect")
	public String introspect(
			@RequestParam("token") String token, 
			@RequestHeader("Authorization") String authorization) throws OAuth2TokenException {
		this.clientAppService.authenticate(Optional.ofNullable(authorization), null, null);
		this.tokenEndpointService.checkToken(token);
		return "";
	}
	
	@ExceptionHandler(OAuth2TokenException.class)
	public ResponseEntity<ErrorResponse> oauth2clientHandleError(OAuth2TokenException ex){
		ErrorResponse response = new ErrorResponse();
		response.setError(ex.getMessage());
		return ResponseEntity.status(ex.errorCode().getHttpStatus())
				.body(response);
	}
}
