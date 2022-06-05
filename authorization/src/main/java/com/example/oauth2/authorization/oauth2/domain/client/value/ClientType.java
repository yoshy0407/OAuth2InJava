package com.example.oauth2.authorization.oauth2.domain.client.value;


public enum ClientType {
	AUTH_CLIENT(1),
	RESOURCE(2);
	
	private final int typeNum;
	
	private ClientType(int num) {
		this.typeNum = num;
	}
	
	public int getValue() {
		return this.typeNum;
	}
	
	public static ClientType of(int num) {
		for (ClientType clientType : ClientType.values()) {
			if (clientType.getValue() == num) {
				return clientType;
			}
		}
		throw new IllegalArgumentException("指定された数字のClientTypeは存在しません。");
	}

}
