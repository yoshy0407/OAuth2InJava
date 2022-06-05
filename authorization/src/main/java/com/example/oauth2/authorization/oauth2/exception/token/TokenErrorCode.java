package com.example.oauth2.authorization.oauth2.exception.token;

import org.springframework.http.HttpStatus;

public enum TokenErrorCode {
	INVALID_REQUEST("invalid_request","リクエストに必要なパラメータが含まれていません", HttpStatus.BAD_REQUEST),
	INVALID_CLIENT("invalid_client","クライアント認証に失敗しました", HttpStatus.UNAUTHORIZED),
	INVALID_GRANT("invalid_grant","提供された認可グラントまたはリフレッシュトークンが不正です", HttpStatus.BAD_REQUEST),
	UNAUTHORIZED_CLIENT("unauthorized_client","クライアントが当該グラントタイプは使用できません", HttpStatus.BAD_REQUEST),
	UNSUPPORTED_GRANT_TYPE("unsupported_grant_type","サポートされていないグラントタイプです", HttpStatus.BAD_REQUEST),
	INVALID_SCOPE("invalid_scope","要求されたスコープが不正です", HttpStatus.BAD_REQUEST);
	
	private final String errorCode;
	
	private final String msg;
	
	private final HttpStatus status;
	
	private TokenErrorCode(String errorCode, String msg, HttpStatus status) {
		this.errorCode = errorCode;
		this.msg = msg;
		this.status = status;
	}
	
	public String code() {
		return errorCode;
	}
	
	public String messageId() {
		return String.format("oauth2.client.registration.%s", this.errorCode);
	}
	
	public String defaultMessage() {
		return msg;
	}
	
	public HttpStatus getHttpStatus() {
		return this.status;
	}

}
