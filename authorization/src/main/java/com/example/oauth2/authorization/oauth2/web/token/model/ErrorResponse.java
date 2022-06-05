package com.example.oauth2.authorization.oauth2.web.token.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErrorResponse {

	@JsonProperty("error")
	private String error;
	
	@JsonProperty("error_description")
	private String errorDescription;

	//@JsonProperty("error_uri")
	//private URI errorURI;
}
