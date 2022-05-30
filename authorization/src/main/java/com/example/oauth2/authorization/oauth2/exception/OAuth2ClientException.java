package com.example.oauth2.authorization.oauth2.exception;

import org.springframework.http.HttpStatus;

public class OAuth2ClientException extends BaseOAuth2Exception {

	private static final long serialVersionUID = 1L;

	public OAuth2ClientException(HttpStatus status, String message) {
		super(status, message);
	}

}
