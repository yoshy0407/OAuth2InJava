package com.example.oauth2.authorization.oauth2.domain.authorize.processor;

import java.net.URI;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oauth2.authorization.oauth2.domain.authorize.value.ResponseType;
import com.example.oauth2.authorization.oauth2.domain.token.TokenDomainService;
import com.example.oauth2.authorization.oauth2.domain.token.generator.AccessToken;
import com.example.oauth2.authorization.oauth2.web.authorize.AuthorizeEndpointRequest;

public class TokenAuthorizationProcessor extends AbstractAuthorizationProcessor {

	private final TokenDomainService tokenDomainService;
	
	public TokenAuthorizationProcessor(TokenDomainService tokenDomainService) {
		super(ResponseType.TOKEN);
		this.tokenDomainService = tokenDomainService;
	}
	
	@Override
	public URI authorize(AuthorizeEndpointRequest req) {
		UserDetails userDetails = getUserDetails();
		AccessToken accessToken = 
				this.tokenDomainService.generateAccessToken(req.getClientId(), userDetails, req.getScope());
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUri(req.getRedirectUri());
		builder
			.queryParam("access_token", accessToken.getAccessToken())
			.queryParam("token_type", accessToken.getTokenType())
			.queryParam("expires_in", accessToken.getTokenLifeTime());
		if (StringUtils.hasLength(req.getState())) {
			builder.queryParam("state", req.getState());
		}
		if (req.getScope().isPresent()) {
			builder.queryParam("scope", req.getScope().get().toString());
		}			
		
		return builder.build().toUri();
	}

	private UserDetails getUserDetails() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
