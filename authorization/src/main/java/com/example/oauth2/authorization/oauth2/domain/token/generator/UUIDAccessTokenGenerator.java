package com.example.oauth2.authorization.oauth2.domain.token.generator;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Base64Utils;

public class UUIDAccessTokenGenerator implements AccessTokenGenerator {

	@Override
	public String generate(UserDetails userDetails) {
		return Base64Utils.encodeToString(UUID.randomUUID().toString().getBytes()); 
	}

}
