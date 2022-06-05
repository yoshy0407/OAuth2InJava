package com.example.oauth2.authorization.oauth2.domain.authorize.processor;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oauth2.authorization.oauth2.domain.authorize.value.ResponseType;
import com.example.oauth2.authorization.oauth2.domain.token.TokenDomainService;
import com.example.oauth2.authorization.oauth2.domain.token.generator.AccessToken;
import com.example.oauth2.authorization.oauth2.exception.OAuth2AuthorizationException;
import com.example.oauth2.authorization.oauth2.value.Message;
import com.example.oauth2.authorization.oauth2.value.Scope;
import com.example.oauth2.authorization.oauth2.web.authorize.AuthorizeEndpointRequest;

public class TokenAuthorizationProcessor implements AuthorizationProcessor {

	private final TokenDomainService tokenDomainService;
	
	public TokenAuthorizationProcessor(
			TokenDomainService tokenDomainService) {
		this.tokenDomainService = tokenDomainService;
	}
	
	@Override
	public boolean supports(List<ResponseType> responseTypes) throws OAuth2AuthorizationException {
		if (responseTypes == null || responseTypes.isEmpty()) {
			throw new OAuth2AuthorizationException(
					HttpStatus.BAD_REQUEST, 
					Message.MSG1001.resolveMessage("response_type"));
		}
		return responseTypes.contains(ResponseType.TOKEN);
	}

	@Override
	public URI authorize(AuthorizeEndpointRequest req, List<String> scope) throws OAuth2AuthorizationException {
		if (!StringUtils.hasLength(req.getClientId())) {
			throw new OAuth2AuthorizationException(
					HttpStatus.BAD_REQUEST, 
					Message.MSG1001.resolveMessage("client_id"));			
		}

		//redirect_uriは条件によって必須

		Scope scopeVal = Scope.fromList(scope);
		UserDetails userDetails = getUserDetails();
		AccessToken accessToken = 
				this.tokenDomainService.generateAccessToken(req.getClientId(), userDetails, Optional.of(scopeVal));
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUri(req.getRedirectUri());
		builder
			.queryParam("access_token", accessToken.getAccessToken())
			.queryParam("token_type", accessToken.getTokenType())
			.queryParam("scope", scopeVal.toString());
			
		if (StringUtils.hasLength(req.getState())) {
			builder.queryParam("state", req.getState());
		}
		
		return builder.build().toUri();
	}

	private UserDetails getUserDetails() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
