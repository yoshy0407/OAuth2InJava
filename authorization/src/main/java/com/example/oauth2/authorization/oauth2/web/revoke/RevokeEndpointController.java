package com.example.oauth2.authorization.oauth2.web.revoke;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth2.authorization.oauth2.domain.client.spi.OAuth2ClientApplicationService;
import com.example.oauth2.authorization.oauth2.domain.client.value.BasicAuthorization;
import com.example.oauth2.authorization.oauth2.domain.token.spi.TokenApplicationService;
import com.example.oauth2.authorization.oauth2.exception.token.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.web.token.model.ErrorResponse;

@RestController
public class RevokeEndpointController {

	private final OAuth2ClientApplicationService clientAppService;
	
	private final TokenApplicationService tokenEndpointService;
	
	public RevokeEndpointController(
			OAuth2ClientApplicationService clientAppService,
			TokenApplicationService tokenEndpointService) {
		this.clientAppService = clientAppService;
		this.tokenEndpointService = tokenEndpointService;
	}
	
	@PostMapping("/oauth2/revoke")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void revoke(
			@RequestParam("token") String token,
			@RequestHeader("Authorization") String authorization) throws OAuth2TokenException {
		BasicAuthorization basicAuthorization = new BasicAuthorization(authorization);
		this.clientAppService.authenticate(Optional.of(authorization), null, null);
		this.tokenEndpointService.revokeToken(token, basicAuthorization.getUsername());
	}
	
	@ExceptionHandler(OAuth2TokenException.class)
	public ResponseEntity<ErrorResponse> oauth2clientHandleError(OAuth2TokenException ex){
		ErrorResponse response = new ErrorResponse();
		response.setError(ex.getMessage());
		return ResponseEntity.status(ex.errorCode().getHttpStatus())
				.body(response);
	}

}
