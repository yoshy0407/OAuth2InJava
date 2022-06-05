package com.example.oauth2.authorization.oauth2.domain.token.generator;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;

public class DefaultJwtPayloadGenerator implements JwtPayloadGenerator {

	@Override
	public void setup(JwtPayloadBuilder builder, UserDetails user, long tokenLifeSecond) {
		LocalDateTime publishDateTime = LocalDateTime.now();
		LocalDateTime expireDateTime = publishDateTime.plusSeconds(tokenLifeSecond);
		
		builder
		.issuer(null)
		.subject(user.getUsername())
		.expireTime(expireDateTime)
		.issueAt(publishDateTime)
		.notBefore(publishDateTime)
		.jwtId(UUID.randomUUID().toString());
	}

}
