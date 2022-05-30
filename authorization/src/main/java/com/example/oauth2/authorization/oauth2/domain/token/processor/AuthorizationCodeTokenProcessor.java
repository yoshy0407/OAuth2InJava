package com.example.oauth2.authorization.oauth2.domain.token.processor;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.example.oauth2.authorization.oauth2.domain.token.TokenDomainService;
import com.example.oauth2.authorization.oauth2.domain.token.util.TokenResponseBuilder;
import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.exception.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.value.Message;
import com.example.oauth2.authorization.oauth2.value.Scope;
import com.example.oauth2.authorization.oauth2.web.token.TokenEndpointRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthorizationCodeTokenProcessor implements TokenProcessor {

	private final TokenDomainService tokenDomainService;
	
	private final ObjectMapper objectMapper;
	
	public AuthorizationCodeTokenProcessor(
			TokenDomainService tokenDomainService,
			ObjectMapper objectMapper) {
		this.tokenDomainService = tokenDomainService;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public boolean supports(GrantType grantType) throws OAuth2TokenException {
		if (grantType == null) {
			throw new OAuth2TokenException(HttpStatus.BAD_REQUEST, Message.MSG1001.resolveMessage("grant_type"));
		}
		return grantType.equals(GrantType.AUTHORIZATION_CODE);
	}

	@Override
	public JsonNode process(TokenEndpointRequest req) throws OAuth2TokenException {
		this.tokenDomainService.authorizationCodeClientCheck(req);
		UserDetails userDetails = getUserDetails();
		Scope scope = Scope.fromList(req.getScope());
		String accessTokenStr = 
				this.tokenDomainService.generateAccessToken(
						req.getClientId(), userDetails, Optional.ofNullable(scope));
		String refreshTokenStr = 
				this.tokenDomainService.generateRefreshToken(
						req.getClientId(), userDetails, Optional.ofNullable(scope));
		
		return TokenResponseBuilder.authorizationCode(objectMapper)
				.accessToken(accessTokenStr)
				.tokenType("Bearer")
				.refreshToken(refreshTokenStr)
				.scope(scope.toString())
				.build();
	}
	
	private UserDetails getUserDetails() {
		SecurityContext context = SecurityContextHolder.getContext();
		return (UserDetails) context.getAuthentication().getPrincipal();
	}

}
