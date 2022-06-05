package com.example.oauth2.authorization.oauth2.domain.token;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.oauth2.authorization.oauth2.domain.token.processor.TokenProcessor;
import com.example.oauth2.authorization.oauth2.exception.NoRollbackException;
import com.example.oauth2.authorization.oauth2.exception.UnsupportedGrantType;
import com.example.oauth2.authorization.oauth2.exception.token.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.web.token.TokenEndpointRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class TokenEndpointService {

	private final List<TokenProcessor> processors;

	private final TokenRepository tokenRepository;
	
	private final ObjectMapper objectMapper;
	
	public TokenEndpointService(
			List<TokenProcessor> processors,
			TokenRepository tokenRepository,
			ObjectMapper objectMapper) {
		this.processors = processors;
		this.tokenRepository = tokenRepository;
		this.objectMapper = objectMapper;
	}

	public JsonNode generateToken(TokenEndpointRequest req) 
			throws OAuth2TokenException, NoRollbackException {
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
	
	public JsonNode checkToken(String token) {
		Optional<Token> optToken = this.tokenRepository.get(token);
		if (optToken.isPresent()) {
			Token tokenObj = optToken.get();
			if (tokenObj.validateToken(token)) {
				ObjectNode objectNode = this.objectMapper.createObjectNode();
				return tokenObj.createTokenIntrospectResponse(objectNode);				
			} else {
				return createErrorResponse();
			}
		} else {
			return createErrorResponse();
		}
	}
	
	private ObjectNode createErrorResponse() {
		ObjectNode objectNode = this.objectMapper.createObjectNode();
		objectNode.put("active", false);
		return objectNode;		
	}
	
	public void revokeToken(String tokenStr, String clientId) {
		Optional<Token> optToken = this.tokenRepository.get(tokenStr);
		if (optToken.isPresent()) {
			Token token = optToken.get();
			if (token.equalsClientId(clientId)) {
				this.tokenRepository.remove(token);
			}
		}
	}
}
