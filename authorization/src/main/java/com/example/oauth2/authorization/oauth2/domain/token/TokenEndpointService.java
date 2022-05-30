package com.example.oauth2.authorization.oauth2.domain.token;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.oauth2.authorization.oauth2.domain.token.processor.TokenProcessor;
import com.example.oauth2.authorization.oauth2.exception.NoRollbackException;
import com.example.oauth2.authorization.oauth2.exception.OAuth2ClientException;
import com.example.oauth2.authorization.oauth2.exception.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.exception.UnsupportedGrantType;
import com.example.oauth2.authorization.oauth2.web.token.TokenEndpointRequest;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class TokenEndpointService {

	private final List<TokenProcessor> processors;

	public TokenEndpointService(List<TokenProcessor> processors) {
		this.processors = processors;
	}

	public JsonNode generateToken(TokenEndpointRequest req) 
			throws OAuth2TokenException, OAuth2ClientException, NoRollbackException {
		boolean supported = false;
		JsonNode json = null;
		for (TokenProcessor processor : this.processors) {
			if (processor.supports(req.getGrantType())) {
				json = processor.process(req);
			}
		}
		
		if (!supported) {
			throw new UnsupportedGrantType("unsupported_grant_type");
		}
		return json;
	}
}
