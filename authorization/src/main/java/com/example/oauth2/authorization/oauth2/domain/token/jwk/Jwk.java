package com.example.oauth2.authorization.oauth2.domain.token.jwk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;

public interface Jwk {

	public String getKeyId();
	
	public JsonNode toJson(ObjectMapper objectMapper);
	
	public JWSHeader createHeader();
	
	public JWSSigner createJwsSigner();
}
