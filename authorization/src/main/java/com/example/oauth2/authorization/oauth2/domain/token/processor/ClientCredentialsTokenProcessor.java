package com.example.oauth2.authorization.oauth2.domain.token.processor;


import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.oauth2.authorization.oauth2.domain.client.OAuth2ClientDomainService;
import com.example.oauth2.authorization.oauth2.domain.token.TokenDomainService;
import com.example.oauth2.authorization.oauth2.domain.token.generator.AccessToken;
import com.example.oauth2.authorization.oauth2.domain.token.util.TokenResponseBuilder;
import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.exception.client.ClientNotFoundException;
import com.example.oauth2.authorization.oauth2.exception.client.InvalidScopeException;
import com.example.oauth2.authorization.oauth2.exception.token.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.exception.token.TokenErrorCode;
import com.example.oauth2.authorization.oauth2.web.token.TokenEndpointRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ClientCredentialsTokenProcessor extends AbstractTokenProcessor {

	private final OAuth2ClientDomainService clientDomainService;
	
	private final TokenDomainService tokenDomainService;
	
	private final ObjectMapper objectMapper;
	
	
	public ClientCredentialsTokenProcessor(
			OAuth2ClientDomainService clientDomainService,
			TokenDomainService tokenDomainService,
			ObjectMapper objectMapper,
			MessageSource messageSource) {
		super(GrantType.CLIENT_CREDENTIALS, messageSource);
		this.clientDomainService = clientDomainService;
		this.tokenDomainService = tokenDomainService;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public JsonNode process(TokenEndpointRequest req) throws OAuth2TokenException {
		UserDetails userDetails = getUserDetails();
		if (req.getScope().isPresent()) {			
			try {
				this.clientDomainService.checkScope(req.getClientId(), req.getScope().get());
			} catch (ClientNotFoundException e) {
				throw new OAuth2TokenException(TokenErrorCode.INVALID_CLIENT, getMessageSource());
				
			} catch (InvalidScopeException e) {
				throw new OAuth2TokenException(TokenErrorCode.INVALID_SCOPE, getMessageSource());
				
			}
		}
		AccessToken accessToken = 
				this.tokenDomainService.generateAccessToken(req.getClientId(), userDetails, req.getScope());
		
		return TokenResponseBuilder.clientCredentials(objectMapper)
				.accessToken(accessToken.getAccessToken())
				.tokenType(accessToken.getTokenType())
				.expiresIn(accessToken.getTokenLifeTime())
				.scope(req.getScope().toString())
				.build();
	}

	private UserDetails getUserDetails() {
		SecurityContext context = SecurityContextHolder.getContext();
		return (UserDetails) context.getAuthentication().getPrincipal();
	}
}
