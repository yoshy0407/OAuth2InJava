package com.example.oauth2.authorization.oauth2.exception;

import org.springframework.http.HttpStatus;

public class OAuth2AuthorizationException extends BaseOAuth2Exception {

	private static final long serialVersionUID = 1L;
	
	public OAuth2AuthorizationException(HttpStatus status, String message) {
		super(status, message);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
}
