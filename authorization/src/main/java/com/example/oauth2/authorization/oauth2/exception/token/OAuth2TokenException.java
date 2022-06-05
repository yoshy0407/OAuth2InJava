package com.example.oauth2.authorization.oauth2.exception.token;

import java.util.Locale;

import org.springframework.context.MessageSource;

public class OAuth2TokenException extends Exception {

	private static final long serialVersionUID = 1L;

	private final TokenErrorCode errorCode;
	
	private final MessageSource messageSource;
	
	private final Object[] args;

	public OAuth2TokenException(
			TokenErrorCode errorCode,
			MessageSource messageSource,
			Object...args) {
		this.errorCode = errorCode;
		this.messageSource = messageSource;
		this.args = args;
	}

	public TokenErrorCode errorCode() {
		return this.errorCode;
	}
	
	@Override
	public String getMessage() {
		return this.messageSource.getMessage(
				this.errorCode.messageId(), args, this.errorCode.defaultMessage(), Locale.getDefault());
	}

}
