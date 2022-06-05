package com.example.oauth2.authorization.oauth2.domain.token.jwk;

import org.springframework.util.ReflectionUtils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.ECKey;

public class ECJwk extends AbstractJwk {

	private final ECKey ecKey;
	
	public ECJwk(JWSAlgorithm algorithm, ECKey ecKey) {
		super(algorithm, ecKey);
		this.ecKey = ecKey;
	}

	@Override
	public JWSSigner createJwsSigner() {
		try {
			return new ECDSASigner(ecKey);
		} catch (JOSEException e) {
			ReflectionUtils.rethrowRuntimeException(e);
		}
		return null;
	}

}
