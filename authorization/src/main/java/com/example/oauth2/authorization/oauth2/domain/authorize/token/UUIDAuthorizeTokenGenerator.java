package com.example.oauth2.authorization.oauth2.domain.authorize.token;

import java.util.UUID;

import com.example.oauth2.authorization.oauth2.domain.authorize.spi.AuthorizeTokenGenerator;

public class UUIDAuthorizeTokenGenerator implements AuthorizeTokenGenerator {

	@Override
	public String generate() {
		return UUID.randomUUID().toString();
	}

}
