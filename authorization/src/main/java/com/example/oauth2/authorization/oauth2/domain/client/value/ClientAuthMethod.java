package com.example.oauth2.authorization.oauth2.domain.client.value;

public enum ClientAuthMethod {
	NONE("none"),
	CLIENT_SECRET_POST("client_secret_post"),
	CLIENT_SECRET_BASIC("client_secret_basic");
	
	private String value;
	
	private ClientAuthMethod(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
	public static ClientAuthMethod of(String value) {
		for (ClientAuthMethod authMethod : ClientAuthMethod.values()) {
			if (authMethod.toString().equalsIgnoreCase(value)) {
				return authMethod;
			}
		}
		throw new IllegalArgumentException("指定された数字のClientTypeは存在しません。");
	}

}
