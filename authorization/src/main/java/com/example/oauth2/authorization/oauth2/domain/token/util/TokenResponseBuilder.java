package com.example.oauth2.authorization.oauth2.domain.token.util;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TokenResponseBuilder {

	private final ObjectMapper objectMapper;

	private final Consumer<ObjectNode> checkConsumer;

	private String accessToken;

	private String tokenType;

	private Optional<Long> expiresIn;

	private Optional<String> refreshToken;

	private Optional<String> scope;

	public TokenResponseBuilder(ObjectMapper objectMapper, Consumer<ObjectNode> checkConsumer) {
		this.objectMapper = objectMapper;
		this.checkConsumer = checkConsumer;
	}

	public TokenResponseBuilder accessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	public TokenResponseBuilder tokenType(String tokenType) {
		this.tokenType = tokenType;
		return this;
	}

	public TokenResponseBuilder expiresIn(Long expiresIn) {
		this.expiresIn = Optional.ofNullable(expiresIn);
		return this;
	}

	public TokenResponseBuilder refreshToken(String refreshToken) {
		this.refreshToken = Optional.ofNullable(refreshToken);
		return this;
	}

	public TokenResponseBuilder scope(String scope) {
		this.scope = Optional.ofNullable(scope);
		return this;
	}

	public JsonNode build() {
		ObjectNode json = this.objectMapper.createObjectNode();
		if (StringUtils.hasLength(accessToken)) {
			throw new IllegalArgumentException("access_tokenは設定する必要があります");
		}
		json.put("access_token", this.accessToken);
		if (StringUtils.hasLength(tokenType)) {
			throw new IllegalArgumentException("token＿typeは設定する必要があります");
		}
		json.put("token_type", this.tokenType);
		expiresIn.ifPresent(s -> json.put("expires_in", s));
		refreshToken.ifPresent(s -> json.put("refresh_token", s));
		scope.ifPresent(s -> json.put("scope", s));
		this.checkConsumer.accept(json);
		return json;
	}

	public static TokenResponseBuilder authorizationCode(ObjectMapper objectMapper) {
		return new TokenResponseBuilder(objectMapper, j -> {});
	}

	public static TokenResponseBuilder password(ObjectMapper objectMapper) {
		return new TokenResponseBuilder(objectMapper, j -> {});
	}

	public static TokenResponseBuilder clientCredentials(ObjectMapper objectMapper) {
		return new TokenResponseBuilder(
				objectMapper, 
				j -> {
					if (StringUtils.hasLength(j.get("refresh_token").asText())) {
						throw new IllegalArgumentException("クライアント・クレデンシャルフローでは、リフレッシュトークンを設定できません。");
					}
				});
	}

	public static TokenResponseBuilder refreshToken(ObjectMapper objectMapper) {
		return new TokenResponseBuilder(objectMapper, j -> {});
	}
	
}
