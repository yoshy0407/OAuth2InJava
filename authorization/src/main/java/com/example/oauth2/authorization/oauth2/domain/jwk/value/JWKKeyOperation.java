package com.example.oauth2.authorization.oauth2.domain.jwk.value;

public enum JWKKeyOperation {
	/**
	 * compute digital signature or MAC
	 */
	SIGN("sign"),
	/**
	 * verify digital signature or MAC
	 */
	VERIFY("verify"),
	/**
	 * encrypt content
	 */
	ENCRYPT("encrypt"),
	/**
	 * decrypt content and validate decryption, if applicable
	 */
	DECRYPT("decrypt"),
	/**
	 * encrypt key
	 */
	WRAP_KEY("wrapKey"),
	/**
	 * decrypt key and validate decryption, if applicable
	 */
	UNWRAP_KEY("unwrapKey"),
	/**
	 * derive key
	 */
	DERIVE_KEY("deriveKey"),
	/**
	 * derive bits not to be used as a key
	 */
	DERIVE_BITS("deriveBits");
	
	private final String value;
	
	private JWKKeyOperation(String value) {
		this.value = value;
	}
	
	public String toString() {
		return this.value;
	}
}
