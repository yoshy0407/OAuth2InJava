package com.example.oauth2.authorization.oauth2.domain.token;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.oauth2.authorization.oauth2.domain.token.value.TokenType;
import com.example.oauth2.authorization.oauth2.value.Scope;

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
	
	public boolean equalsClientId(String clientId) {
		return this.clientId.equals(clientId);
	}
	
	public UserDetails resolveTokenUser(UserDetailsService service) {
		return service.loadUserByUsername(this.username);
	}
	
	public static Token of(
			String tokenStr,
			TokenType tokenType,
			Scope scope,
			String username) {
		Token token = new Token();
		token.token = tokenStr;
		token.tokenType = tokenType;
		token.scope = scope;
		token.username = username;
		return token;
	}
}
