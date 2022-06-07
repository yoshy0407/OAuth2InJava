package com.example.oauth2.authorization.oauth2.domain.token.processor;

import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.oauth2.authorization.oauth2.domain.token.TokenDomainService;
import com.example.oauth2.authorization.oauth2.domain.token.generator.AccessToken;
import com.example.oauth2.authorization.oauth2.domain.token.util.TokenResponseBuilder;
import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.exception.token.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.web.token.TokenEndpointRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthorizationCodeTokenProcessor extends AbstractTokenProcessor {

	private final TokenDomainService tokenDomainService;
	
	private final ObjectMapper objectMapper;
	
	public AuthorizationCodeTokenProcessor(
			TokenDomainService tokenDomainService,
			ObjectMapper objectMapper,
			MessageSource messageSource) {
		super(GrantType.AUTHORIZATION_CODE, messageSource);
		this.tokenDomainService = tokenDomainService;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public JsonNode process(TokenEndpointRequest req) throws OAuth2TokenException {
		requestParamNotEmpty(req.getCode(), "code");
		this.tokenDomainService.authorizationCodeClientCheck(req);
		UserDetails userDetails = getUserDetails();
		AccessToken accessToken = 
				this.tokenDomainService.generateAccessToken(
						req.getClientId(), userDetails, req.getScope());
		String refreshTokenStr = 
				this.tokenDomainService.generateRefreshToken(
						req.getClientId(), userDetails, req.getScope());
		
		return TokenResponseBuilder.authorizationCode(objectMapper)
				.accessToken(accessToken.getAccessToken())
				.tokenType(accessToken.getTokenType())
				.expiresIn(accessToken.getTokenLifeTime())
				.refreshToken(refreshTokenStr)
				.scope(req.getScope().toString())
				.build();
	}
	
	private UserDetails getUserDetails() {
		SecurityContext context = SecurityContextHolder.getContext();
		return (UserDetails) context.getAuthentication().getPrincipal();
	}

}
