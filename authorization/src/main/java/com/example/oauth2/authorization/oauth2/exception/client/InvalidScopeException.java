package com.example.oauth2.authorization.oauth2.exception.client;

public class InvalidScopeException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidScopeException(String msg, Exception ex) {
		super(msg, ex);
	}

}
