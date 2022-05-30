package com.example.oauth2.authorization.oauth2.value;

public enum Message {
	MSG1001("%sは必須パラメータです。"),
	MSG1002("%sが設定されている必要があります"),
	MSG1010("不正なリクエストです。");
	
	private final String msg;
	
	private Message(String msg) {
		this.msg = msg;
	}
	
	public String resolveMessage(Object...args) {
		return String.format(msg, args);
	}
}
