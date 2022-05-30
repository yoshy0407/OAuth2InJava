package com.example.oauth2.authorization.oauth2.domain.client;

import java.net.URI;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.oauth2.authorization.oauth2.value.Scope;

import lombok.Getter;
import lombok.ToString;

@ToString
public class OAuth2Client {

	@Getter
	private String clientId;
	
	private String logoUri;
	
	private String encodedClientSecret;
	
	private List<URI> redirectUris;
	
	private Scope scope;

	public boolean matchSecret(String rawSecret, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(rawSecret, this.encodedClientSecret);
	}
	
	public boolean containsRedirectUri(URI redirectUri) {
		return this.redirectUris.contains(redirectUri);
	}
	
	public boolean containScope(Scope scope) {
		return this.scope.contains(scope);
	}
	
	public boolean containScope(List<String> scope) {
		return this.scope.contains(scope);
	}
	
	public static OAuth2Client of(
			String clientId,
			String logoUri,
			String encodedClientSecret,
			List<URI> redirectUris,
			Scope scope) {
		OAuth2Client client = new OAuth2Client();
		client.clientId = clientId;
		client.logoUri = logoUri;
		client.encodedClientSecret = encodedClientSecret;
		client.redirectUris = redirectUris;
		client.scope = scope;
		return client;
	}
	
	public static OAuth2Client newInstance(
			String clientId,
			String logoUri,
			String rawClientSecret,
			List<URI> redirectUris,
			Scope scope,
			PasswordEncoder passwordEncoder) {
		OAuth2Client client = new OAuth2Client();
		client.clientId = clientId;
		client.logoUri = logoUri;
		client.encodedClientSecret = passwordEncoder.encode(rawClientSecret);
		client.redirectUris = redirectUris;
		client.scope = scope;
		return client;
	}
}
