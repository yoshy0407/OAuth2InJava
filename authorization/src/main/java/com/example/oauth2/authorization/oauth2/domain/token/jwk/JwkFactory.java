package com.example.oauth2.authorization.oauth2.domain.token.jwk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.util.ReflectionUtils;

import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

public class JwkFactory {

	public static RSAKeyGenerator rsaJwk(int bit) {
		return new RSAKeyGenerator(bit);
	}
	
	public static RSAKey.Builder rsaFromKeyPair(KeyPair keyPair) {
		return new RSAKey.Builder((RSAPublicKey)keyPair.getPublic())
				.privateKey((RSAPrivateKey)keyPair.getPrivate());
	}

	/**
	 * 
	 * @param publicKey oublic_key.derなどの拡張子のファイル
	 * @param privateKey private_key.pk8などの拡張子のファイル
	 * @return
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 */
	public static RSAKey.Builder rsaFromFile(File publicKeyFile, File privateKeyFile) throws IOException, InvalidKeySpecException {
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			ReflectionUtils.rethrowRuntimeException(e);
		}
		byte[] publicKeyByteArray = Files.readAllBytes(publicKeyFile.toPath());
		RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyByteArray));
		byte[] privateKeyByteArray = Files.readAllBytes(privateKeyFile.toPath());
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(new X509EncodedKeySpec(privateKeyByteArray));
		
		return new RSAKey.Builder(rsaPublicKey)
				.privateKey(rsaPrivateKey);
	}
	
	public static ECKeyGenerator es256() {
		return new ECKeyGenerator(Curve.P_256);
	}

	public static ECKeyGenerator es384() {
		return new ECKeyGenerator(Curve.P_384);
	}
	
	public static ECKeyGenerator es521() {
		return new ECKeyGenerator(Curve.P_521);		
	}
}
