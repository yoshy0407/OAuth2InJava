package com.example.oauth2.authorization.oauth2.domain.token.jwk;

import org.springframework.util.ReflectionUtils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;

public class RsaJwk extends AbstractJwk {
	
	private final RSAKey rsaKey;
	
	public RsaJwk(JWSAlgorithm algorithm, RSAKey rsaKey) {
		super(algorithm, rsaKey);
		this.rsaKey = rsaKey;
	}
	
	@Override
	public JWSSigner createJwsSigner() {
		try {
			return new RSASSASigner(rsaKey);
		} catch (JOSEException e) {
			ReflectionUtils.rethrowRuntimeException(e);
		}
		return null;
	}


}
