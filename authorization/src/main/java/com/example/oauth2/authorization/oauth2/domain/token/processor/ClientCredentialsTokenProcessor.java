package com.example.oauth2.authorization.oauth2.domain.token.processor;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.oauth2.authorization.oauth2.domain.client.OAuth2ClientApplicationService;
import com.example.oauth2.authorization.oauth2.domain.token.TokenDomainService;
import com.example.oauth2.authorization.oauth2.domain.token.util.TokenResponseBuilder;
import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.exception.OAuth2ClientException;
import com.example.oauth2.authorization.oauth2.exception.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.value.Message;
import com.example.oauth2.authorization.oauth2.value.Scope;
import com.example.oauth2.authorization.oauth2.web.token.TokenEndpointRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientCredentialsTokenProcessor implements TokenProcessor {

	private final OAuth2ClientApplicationService clientAppService;
	
	private final TokenDomainService tokenDomainService;
	
	private final ObjectMapper objectMapper;
	
	public ClientCredentialsTokenProcessor(
			OAuth2ClientApplicationService clientAppService,
			TokenDomainService tokenDomainService,
			ObjectMapper objectMapper) {
		this.clientAppService = clientAppService;
		this.tokenDomainService = tokenDomainService;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public boolean supports(GrantType grantType) throws OAuth2TokenException {
		if (grantType == null) {
			throw new OAuth2TokenException(HttpStatus.BAD_REQUEST, Message.MSG1001.resolveMessage("grant_type"));
		}
		return grantType.equals(GrantType.CLIENT_CREDENTIALS);
	}

	@Override
	public JsonNode process(TokenEndpointRequest req) throws OAuth2TokenException, OAuth2ClientException {
		UserDetails userDetails = getUserDetails();
		Scope scope = Scope.fromList(req.getScope());
		this.clientAppService.checkScope(req.getClientId(), scope);
		String accessTokenStr = 
				this.tokenDomainService.generateAccessToken(req.getClientId(), userDetails, Optional.ofNullable(scope));
		
		return TokenResponseBuilder.clientCredentials(objectMapper)
				.accessToken(accessTokenStr)
				.tokenType("Bearer")
				.scope(scope.toString())
				.build();
	}

	private UserDetails getUserDetails() {
		SecurityContext context = SecurityContextHolder.getContext();
		return (UserDetails) context.getAuthentication().getPrincipal();
	}
}
