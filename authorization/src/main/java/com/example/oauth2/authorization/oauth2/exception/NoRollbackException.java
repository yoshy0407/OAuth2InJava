package com.example.oauth2.authorization.oauth2.exception;

import org.springframework.http.HttpStatus;

public class NoRollbackException extends BaseOAuth2Exception {

	private static final long serialVersionUID = 1L;

	public NoRollbackException(HttpStatus status, String msg) {
		super(status, msg);
	}

}
