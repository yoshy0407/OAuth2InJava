package com.example.oauth2.authorization.oauth2.domain.authorize.spi;

import java.net.URI;

import com.example.oauth2.authorization.oauth2.exception.OAuth2AuthorizationException;
import com.example.oauth2.authorization.oauth2.web.authorize.AuthorizeEndpointRequest;

public interface AuthorizeApplicationService {

	public void checkClient(AuthorizeEndpointRequest req) throws OAuth2AuthorizationException;
	
	public URI authorize(AuthorizeEndpointRequest req);
}
