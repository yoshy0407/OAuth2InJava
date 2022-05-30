package com.example.oauth2.authorization.oauth2.domain.authorize.value;

public enum CodeChallengeMethod {
	PLAIN("plain"),
	S256("S256");
	
	private final String value;
	
	private CodeChallengeMethod(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
	public static CodeChallengeMethod of(String value) {
		for(CodeChallengeMethod method : CodeChallengeMethod.values()) {
			if (method.toString().equalsIgnoreCase(value)) {
				return method;
			}
		}
		throw new IllegalArgumentException("指定されたCODE_CHALLENGE_METHODは存在しません。");
	}
}
