package com.example.oauth2.authorization.oauth2.domain.token.processor;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.util.StringUtils;

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

public class PasswordTokenProcessor implements TokenProcessor {

	private final UserDetailsManager userDetailsManager;
	
	private final PasswordEncoder passwordEncoder;
	
	private final OAuth2ClientApplicationService clientAppService;
	
	private final TokenDomainService tokenDomainService;
	
	private final ObjectMapper objectMapper;
	
	public PasswordTokenProcessor(
			UserDetailsManager userDetailsManager,
			PasswordEncoder passwordEncoder,
			OAuth2ClientApplicationService clientAppService,
			TokenDomainService tokenDomainService,
			ObjectMapper objectMapper) {
		this.userDetailsManager = userDetailsManager;
		this.passwordEncoder = passwordEncoder;
		this.clientAppService = clientAppService;
		this.tokenDomainService = tokenDomainService;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public boolean supports(GrantType grantType) throws OAuth2TokenException {
		if (grantType == null) {
			throw new OAuth2TokenException(HttpStatus.BAD_REQUEST, Message.MSG1001.resolveMessage("grant_type"));
		}
		return grantType.equals(GrantType.PASSWORD);
	}

	@Override
	public JsonNode process(TokenEndpointRequest req) throws OAuth2TokenException, OAuth2ClientException {
		
		if (!StringUtils.hasLength(req.getUsername())) {
			throw new OAuth2TokenException(HttpStatus.BAD_REQUEST, Message.MSG1001.resolveMessage("username"));
		}
		if (!StringUtils.hasLength(req.getPassword())) {
			throw new OAuth2TokenException(HttpStatus.BAD_REQUEST, Message.MSG1001.resolveMessage("password"));
		}
		
		UserDetails userDetails = userDetailsManager.loadUserByUsername(req.getUsername());
		if (userDetails == null) {
			throw new OAuth2TokenException(HttpStatus.UNAUTHORIZED, "invalid_grant");
		}
		
		if (!passwordEncoder.matches(req.getPassword(), userDetails.getPassword())) {
			throw new OAuth2TokenException(HttpStatus.UNAUTHORIZED, "invalid_grant");			
		}
		
		Scope scope = Scope.fromList(req.getScope());
		clientAppService.checkScope(req.getClientId(), scope);
		
		String accessTokenStr = 
				this.tokenDomainService.generateAccessToken(
						req.getClientId(), 
						userDetails,
						Optional.ofNullable(scope));
		String refreshTokenStr = 
				this.tokenDomainService.generateRefreshToken(
						req.getClientId(),
						userDetails,
						Optional.ofNullable(scope));
		
		return TokenResponseBuilder.password(objectMapper)
				.accessToken(accessTokenStr)
				.tokenType("Bearer")
				.refreshToken(refreshTokenStr)
				.scope(scope.toString())
				.build();
	}

}
