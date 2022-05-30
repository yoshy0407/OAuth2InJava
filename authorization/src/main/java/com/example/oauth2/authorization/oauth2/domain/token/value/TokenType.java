package com.example.oauth2.authorization.oauth2.domain.token.value;

public enum TokenType {
	ACCESS_TOKEN(1),
	REFRESH_TOKEN(2);
	
	private final int typeNum;
	
	private TokenType(int num) {
		this.typeNum = num;
	}
	
	public int getValue() {
		return this.typeNum;
	}
	
	public static TokenType of(int num) {
		for (TokenType tokenType : TokenType.values()) {
			if (tokenType.getValue() == num) {
				return tokenType;
			}
		}
		throw new IllegalArgumentException("指定された数字のTokenTypeは存在しません。");
	}
}
