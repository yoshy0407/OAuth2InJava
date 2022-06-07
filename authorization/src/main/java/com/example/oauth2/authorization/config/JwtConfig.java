package com.example.oauth2.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.oauth2.authorization.oauth2.domain.token.generator.AccessTokenGenerator;
import com.example.oauth2.authorization.oauth2.domain.token.generator.DefaultJwtPayloadGenerator;
import com.example.oauth2.authorization.oauth2.domain.token.generator.JwtAccessTokenGenerator;
import com.example.oauth2.authorization.oauth2.domain.token.generator.JwtPayloadGenerator;
import com.example.oauth2.authorization.oauth2.domain.token.jwk.InMemoryJwkStore;
import com.example.oauth2.authorization.oauth2.domain.token.jwk.JwkFactory;
import com.example.oauth2.authorization.oauth2.domain.token.jwk.JwkStore;
import com.example.oauth2.authorization.oauth2.domain.token.jwk.RsaJwk;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;

@Configuration
public class JwtConfig {

	@Bean
	public JwkStore jwkStore() throws JOSEException {
		JwkStore jwkStore = new InMemoryJwkStore();
		RSAKey rsaKey = JwkFactory.rsaJwk(2048)
			.keyID("jwt-rsa-key")
			.keyUse(KeyUse.SIGNATURE)
			.generate();
		RsaJwk jwk = new RsaJwk(JWSAlgorithm.RS256, rsaKey);
		jwkStore.save(jwk);
		return jwkStore;
	}
	
	@Bean
	public JwtPayloadGenerator jwtPayloadGenerator() {
		return new DefaultJwtPayloadGenerator();
	}
	
	@Bean
	public AccessTokenGenerator accessTokenGenerator(JwkStore jwkStore, JwtPayloadGenerator jwtPayloadGenerator) {
		//return new UUIDAccessTokenGenerator();
		//30分
		return new JwtAccessTokenGenerator(1800, jwkStore, jwtPayloadGenerator, "jwt-rsa-key");
	}
	
}
