package com.example.oauth2.authorization.oauth2.domain.token.jwk;

import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.jwk.JWK;

public abstract class AbstractJwk implements Jwk {

	private final JWSAlgorithm algorithm;
	
	private final JWK jwk;
	
	public AbstractJwk(JWSAlgorithm algorithm, JWK jwk) {
		this.algorithm = algorithm;
		this.jwk = jwk;
	}
	
	@Override
	public String getKeyId() {
		return this.jwk.getKeyID();
	}
	
	@Override
	public JsonNode toJson(ObjectMapper objectMapper) {
		try {
			return objectMapper.readTree(this.jwk.toJSONString());
		} catch (JsonProcessingException e) {
			ReflectionUtils.rethrowRuntimeException(e);
		}
		return null;
	}

	@Override
	public JWSHeader createHeader() {
		return new JWSHeader.Builder(algorithm)
				.jwk(jwk)
				.build();
	}
}
