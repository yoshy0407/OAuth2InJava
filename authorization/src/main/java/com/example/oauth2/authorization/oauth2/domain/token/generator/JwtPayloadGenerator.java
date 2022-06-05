package com.example.oauth2.authorization.oauth2.domain.token.generator;


import org.springframework.security.core.userdetails.UserDetails;

public interface JwtPayloadGenerator {

	public void setup(JwtPayloadBuilder builder, UserDetails user, long tokenLifeSecond);
}
