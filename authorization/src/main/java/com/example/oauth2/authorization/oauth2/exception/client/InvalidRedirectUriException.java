package com.example.oauth2.authorization.oauth2.exception.client;

public class InvalidRedirectUriException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidRedirectUriException(String msg, Exception ex) {
		super(msg, ex);
	}

}
