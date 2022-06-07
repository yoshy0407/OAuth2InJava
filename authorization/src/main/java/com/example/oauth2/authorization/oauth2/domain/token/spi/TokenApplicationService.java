package com.example.oauth2.authorization.oauth2.domain.token.spi;

import com.example.oauth2.authorization.oauth2.exception.NoRollbackException;
import com.example.oauth2.authorization.oauth2.exception.token.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.web.token.TokenEndpointRequest;
import com.fasterxml.jackson.databind.JsonNode;

public interface TokenApplicationService {

	JsonNode generateToken(TokenEndpointRequest req) throws OAuth2TokenException, NoRollbackException;

	JsonNode checkToken(String token);

	void revokeToken(String tokenStr, String clientId);

}