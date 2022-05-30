package com.example.oauth2.authorization.oauth2.domain.jwk.value;

public enum JWKKeyType {
	/**
	 * 	Elliptic Curve
	 */
	EC("EC"),
	/**
	 * RSA
	 */
	RSA("RSA"),
	/**
	 * Octet sequence
	 */
	OCT("oct"),
	/**
	 * Octet String key pairs 
	 */
	OKP("OKP");
	
	private final String value;
	
	private JWKKeyType(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
