package com.example.oauth2.authorization.oauth2.web.jwk;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth2.authorization.oauth2.domain.token.jwk.Jwk;
import com.example.oauth2.authorization.oauth2.domain.token.jwk.JwkStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class JwkController {

	private final JwkStore jwkStore;
	
	private final ObjectMapper objectMapper;
	
	public JwkController(
			JwkStore jwkStore,
			ObjectMapper objectMapper) {
		this.jwkStore = jwkStore;
		this.objectMapper = objectMapper;
	}
	
	@GetMapping("/oauth2/jwks")
	public String get() throws JsonMappingException, JsonProcessingException {
		List<Jwk> jwkList = this.jwkStore.getAll();
		
		ObjectNode jsonObject = objectMapper.createObjectNode();
		
		ArrayNode arrayNode = jsonObject.putArray("keys");
		
		for (Jwk jwk : jwkList) {
			arrayNode.add(jwk.toJson(this.objectMapper));
		}
		return jsonObject.toString();
	}
	
}
