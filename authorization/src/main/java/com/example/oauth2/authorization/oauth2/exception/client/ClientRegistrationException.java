package com.example.oauth2.authorization.oauth2.exception.client;

import java.util.Locale;

import org.springframework.context.MessageSource;

public class ClientRegistrationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final ClientRegistrationErrorCode errorCode;
	
	private final MessageSource messageSource;
	
	private final Object[] args;
	
	public ClientRegistrationException(
			ClientRegistrationErrorCode errorCode,
			MessageSource messageSource,
			Object...args) {
		this.errorCode = errorCode;
		this.messageSource = messageSource;
		this.args = args;
	}

	public ClientRegistrationErrorCode errorCode() {
		return this.errorCode;
	}
	
	@Override
	public String getMessage() {
		return this.messageSource.getMessage(
				this.errorCode.messageId(), args, this.errorCode.defaultMessage(), Locale.getDefault());
	}
}
