package com.example.oauth2.authorization.oauth2.domain.token;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.oauth2.authorization.oauth2.domain.token.value.TokenType;
import com.example.oauth2.authorization.oauth2.value.Scope;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Token {

	@Getter
	private String token;
	
	private TokenType tokenType;
	
	private String clientId;

	private Scope scope;
	
	private String username;
	
	private LocalDateTime expireDateTime;
	
	public boolean equalsClientId(String clientId) {
		return this.clientId.equals(clientId);
	}
	
	public UserDetails resolveTokenUser(UserDetailsService service) {
		return service.loadUserByUsername(this.username);
	}
	
	public boolean validateToken(String token) {
		return expireDateTime.isAfter(LocalDateTime.now());
	}
	
	public ObjectNode createTokenIntrospectResponse(ObjectNode objectNode) {
		objectNode.put("active", false);
		//objectNode.put("iss", "");
		//objectNode.put("aud", "");
		objectNode.put("sub", this.username);
		//objectNode.put("username", "");
		objectNode.put("scope", this.scope.toString());
		objectNode.put("client_id", this.clientId);
		return objectNode;
	}
	
	public static Token of(
			String tokenStr,
			TokenType tokenType,
			Scope scope,
			String username,
			LocalDateTime expireDateTime) {
		Token token = new Token();
		token.token = tokenStr;
		token.tokenType = tokenType;
		token.scope = scope;
		token.username = username;
		token.expireDateTime = expireDateTime;
		return token;
	}
}
