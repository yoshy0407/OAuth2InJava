package com.example.oauth2.authorization.oauth2.domain.token.generator;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Base64Utils;

public class UUIDAccessTokenGenerator implements AccessTokenGenerator {

	@Override
	public AccessToken generate(UserDetails userDetails) {
		String token = Base64Utils.encodeToString(UUID.randomUUID().toString().getBytes()); 
		AccessToken accessToken = new AccessToken();
		accessToken.setAccessToken(token);
		accessToken.setTokenType("Bearer");
		return accessToken;
	}

}
