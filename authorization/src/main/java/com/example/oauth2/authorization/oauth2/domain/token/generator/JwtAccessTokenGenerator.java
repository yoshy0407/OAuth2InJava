package com.example.oauth2.authorization.oauth2.domain.token.generator;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ReflectionUtils;

import com.example.oauth2.authorization.oauth2.domain.token.jwk.Jwk;
import com.example.oauth2.authorization.oauth2.domain.token.jwk.JwkStore;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;


public class JwtAccessTokenGenerator implements AccessTokenGenerator {

	private final long tokenLifeSecond;
	
	private final JwkStore jwkStore;

	private final JwtPayloadGenerator jwtPayloadGenerator;
	
	private final String useKeyId;
	
	public JwtAccessTokenGenerator(
			long tokenLifeSecond,
			JwkStore jwkStore, 
			JwtPayloadGenerator jwtPayloadGenerator, 
			String useKeyId) {
		this.tokenLifeSecond = tokenLifeSecond;
		this.jwkStore = jwkStore;
		this.jwtPayloadGenerator = jwtPayloadGenerator;
		this.useKeyId = useKeyId;
	}
	
	@Override
	public AccessToken generate(UserDetails userDetails) {

		Optional<Jwk> optJwk = this.jwkStore.get(Optional.of(useKeyId));
		
		if (optJwk.isEmpty()) {
			throw new IllegalStateException("指定されたkeyIdのJWKが存在しません");
		} 
			
		Jwk jwk = optJwk.get();
				
		JwtPayloadBuilder builder = JwtPayloadBuilder.create();
		
		this.jwtPayloadGenerator.setup(builder, userDetails, this.tokenLifeSecond);
		
		Payload payload = builder.toPayload();
		
		JWSObject jwsObject = new JWSObject(jwk.createHeader(), payload);
		try {
			jwsObject.sign(jwk.createJwsSigner());
		} catch (JOSEException e) {
			ReflectionUtils.rethrowRuntimeException(e);
		}
				
		AccessToken accessToken = new AccessToken();
		accessToken.setAccessToken(jwsObject.serialize());
		accessToken.setTokenType("Bearer");
		accessToken.setTokenLifeTime(0);
		return accessToken;
	}	
}
