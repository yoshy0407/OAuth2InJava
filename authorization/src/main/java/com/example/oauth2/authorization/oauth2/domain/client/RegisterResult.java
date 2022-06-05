package com.example.oauth2.authorization.oauth2.domain.client;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResult {
	
	private final String clientId;

	private final String clientSecret;
	
	private long clientIdIssuedAt;
	
	private long clientSecretExpiresAt = 0;

}
