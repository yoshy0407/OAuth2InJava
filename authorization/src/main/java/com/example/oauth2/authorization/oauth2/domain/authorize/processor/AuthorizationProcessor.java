package com.example.oauth2.authorization.oauth2.domain.authorize.processor;

import java.net.URI;
import java.util.List;

import com.example.oauth2.authorization.oauth2.domain.authorize.value.ResponseType;
import com.example.oauth2.authorization.oauth2.exception.OAuth2AuthorizationException;
import com.example.oauth2.authorization.oauth2.web.authorize.AuthorizeEndpointRequest;

public interface AuthorizationProcessor {

	public boolean supports( List<ResponseType> responseTypes) throws OAuth2AuthorizationException;
	
	public URI authorize(AuthorizeEndpointRequest req, List<String> scope) throws OAuth2AuthorizationException;
}
