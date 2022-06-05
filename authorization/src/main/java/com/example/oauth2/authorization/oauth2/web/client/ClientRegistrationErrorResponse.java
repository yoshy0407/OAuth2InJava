package com.example.oauth2.authorization.oauth2.web.client;

import com.example.oauth2.authorization.oauth2.exception.client.ClientRegistrationException;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ClientRegistrationErrorResponse {

	public ClientRegistrationErrorResponse(
			ClientRegistrationException ex) {
		this.error = ex.errorCode().code();
		this.errorDescription = ex.getMessage();		
	}
	
	@JsonProperty("error")
	private String error;
	
	@JsonProperty("error_description")
	private String errorDescription;
}
