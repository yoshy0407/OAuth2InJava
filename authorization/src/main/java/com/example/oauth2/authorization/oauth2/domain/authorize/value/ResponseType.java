package com.example.oauth2.authorization.oauth2.domain.authorize.value;

public enum ResponseType {
	NONE,
	CODE,
	TOKEN,
	ID_TOKEN;
	
	public static ResponseType of(String str) {
		for (ResponseType responseType : ResponseType.values()) {
			if (str.equalsIgnoreCase(responseType.toString())) {
				return responseType;
			}
		}
		throw new IllegalArgumentException("指定されたパラメータのResponseTypeは存在しません");
	}
}
