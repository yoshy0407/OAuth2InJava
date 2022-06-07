package com.example.oauth2.authorization.oauth2.domain.client.spi;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.example.oauth2.authorization.oauth2.domain.client.RegisterResult;
import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.exception.client.ClientRegistrationException;
import com.example.oauth2.authorization.oauth2.exception.token.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.value.Scope;

public interface OAuth2ClientApplicationService {

	public RegisterResult register(String clientName, URI logoUri, List<URI> redirectUris, String tokenEndpointAuthMethod,
			URI clientUri, List<GrantType> grantTypes, Scope scope) throws ClientRegistrationException;

	public void authenticate(Optional<String> authorization, String bodyClientId, String bodyClientSecret)
			throws OAuth2TokenException;

}