package com.example.oauth2.authorization.oauth2.exception.client;

public class ClientNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ClientNotFoundException(String msg, Exception ex) {
		super(msg, ex);
	}

}
