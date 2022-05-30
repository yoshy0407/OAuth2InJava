package com.example.oauth2.authorization.oauth2.domain.jwk.value;

public enum JWKPublicKeyUse {
	SIGNATURE("sig"),
	ENCRPTION("enc");
	
	private final String value;
	
	private JWKPublicKeyUse(String value) {
		this.value = value;
	}
	
	public String toString() {
		return this.value;
	}
}
