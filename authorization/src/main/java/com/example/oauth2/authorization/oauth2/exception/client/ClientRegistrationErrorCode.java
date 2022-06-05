package com.example.oauth2.authorization.oauth2.exception.client;

public enum ClientRegistrationErrorCode {
	INVALID_REDIRECT_URI("invalid_redirect_uri", "The value of one or more redirection URIs is invalid."),
	INVALID_CLIENT_METADATA("invalid_client_metadata","The value of one of the client metadata fields is invalid "),
	INVALID_SOFTWARE_STATEMENT("invalid_software_statement","The software statement presented is invalid"),
	UNAPPROVED_SOFTWARE_STATEMENT("unapproved_software_statement","The software statement presented is not approved for use by this authorization server");
	
	private final String errorCode;
	
	private final String msg;
	
	private ClientRegistrationErrorCode(String errorCode, String msg) {
		this.errorCode = errorCode;
		this.msg = msg;
	}
	
	public String code() {
		return errorCode;
	}
	
	public String messageId() {
		return String.format("oauth2.client.registration.%s", this.errorCode);
	}
	
	public String defaultMessage() {
		return msg;
	}
}
