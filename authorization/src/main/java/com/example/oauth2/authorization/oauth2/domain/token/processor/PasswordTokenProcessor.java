package com.example.oauth2.authorization.oauth2.domain.token.processor;


import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
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
public class PasswordTokenProcessor extends AbstractTokenProcessor {

	private final UserDetailsManager userDetailsManager;
	
	private final PasswordEncoder passwordEncoder;
	
	private final OAuth2ClientDomainService clientDomainService;
	
	private final TokenDomainService tokenDomainService;
	
	private final ObjectMapper objectMapper;
	
	public PasswordTokenProcessor(
			UserDetailsManager userDetailsManager,
			PasswordEncoder passwordEncoder,
			OAuth2ClientDomainService clientDomainService,
			TokenDomainService tokenDomainService,
			ObjectMapper objectMapper,
			MessageSource messageSource) {
		super(GrantType.PASSWORD, messageSource);
		this.userDetailsManager = userDetailsManager;
		this.passwordEncoder = passwordEncoder;
		this.clientDomainService = clientDomainService;
		this.tokenDomainService = tokenDomainService;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public JsonNode process(TokenEndpointRequest req) throws OAuth2TokenException {
		requestParamNotEmpty(req.getUsername(), "username");
		requestParamNotEmpty(req.getPassword(), "password");
		
		UserDetails userDetails = userDetailsManager.loadUserByUsername(req.getUsername());
		if (userDetails == null) {
			throw new OAuth2TokenException(TokenErrorCode.INVALID_CLIENT, getMessageSource());
		}
		
		if (!passwordEncoder.matches(req.getPassword(), userDetails.getPassword())) {
			throw new OAuth2TokenException(TokenErrorCode.INVALID_GRANT, getMessageSource());			
		}

		if (req.getScope().isPresent()) {
			try {
				clientDomainService.checkScope(req.getClientId(), req.getScope().get());
			} catch (ClientNotFoundException e) {
				throw new OAuth2TokenException(TokenErrorCode.INVALID_CLIENT, getMessageSource());
				
			} catch (InvalidScopeException e) {
				throw new OAuth2TokenException(TokenErrorCode.INVALID_SCOPE, getMessageSource());
				
			}			
		}
		
		AccessToken accessToken = 
				this.tokenDomainService.generateAccessToken(
						req.getClientId(), 
						userDetails,
						req.getScope());
		String refreshTokenStr = 
				this.tokenDomainService.generateRefreshToken(
						req.getClientId(),
						userDetails,
						req.getScope());
		
		TokenResponseBuilder builder = TokenResponseBuilder.password(objectMapper)
				.accessToken(accessToken.getAccessToken())
				.tokenType(accessToken.getTokenType())
				.expiresIn(accessToken.getTokenLifeTime())
				.refreshToken(refreshTokenStr);
		
		req.getScope().ifPresent(scope -> {
			builder.scope(scope.toString());
		});
		
		return builder.build();
		
	}

}
