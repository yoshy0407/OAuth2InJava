package com.example.oauth2.authorization.oauth2.domain.token.processor;


import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.exception.NoRollbackException;
import com.example.oauth2.authorization.oauth2.exception.OAuth2ClientException;
import com.example.oauth2.authorization.oauth2.exception.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.web.token.TokenEndpointRequest;
import com.fasterxml.jackson.databind.JsonNode;

public interface TokenProcessor {

	public boolean supports(GrantType grantType) throws OAuth2TokenException;
	
	public JsonNode process(TokenEndpointRequest req) throws OAuth2TokenException, OAuth2ClientException, NoRollbackException;
}
