package com.example.oauth2.authorization.oauth2.exception;

import org.springframework.http.HttpStatus;

public class BaseOAuth2Exception extends Exception {

	private static final long serialVersionUID = 1L;

	private final HttpStatus status;
	
	public BaseOAuth2Exception(HttpStatus status, String msg) {
		super(msg);
		this.status = status;
	}
	
	public HttpStatus getHttpStatus() {
		return this.status;
	}
}
