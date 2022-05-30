package com.example.oauth2.authorization.oauth2.domain.token.value;

public enum GrantType {
	AUTHORIZATION_CODE,
	PASSWORD,
	CLIENT_CREDENTIALS,
	REFRESH_TOKEN;
	
	public static GrantType of(String str) {
		for (GrantType grantType : GrantType.values()) {
			if (str.equalsIgnoreCase(grantType.toString())) {
				return grantType;
			}
		}
		throw new IllegalArgumentException("指定されたパラメータのGrantTypeは存在しません");
	}

}
