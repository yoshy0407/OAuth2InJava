package com.example.oauth2.authorization.oauth2.exception.client;

import org.springframework.http.HttpStatus;

import com.example.oauth2.authorization.oauth2.exception.BaseOAuth2Exception;

public class OAuth2ClientException extends BaseOAuth2Exception {

	private static final long serialVersionUID = 1L;

	public OAuth2ClientException(HttpStatus status, String message) {
		super(status, message);
	}

}
