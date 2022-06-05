package com.example.oauth2.authorization.oauth2.web.client;

import java.net.URI;
import java.util.List;

import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.value.Scope;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRegisterRequest {

	@JsonProperty("client_name")
	private String clientName;
	
	@JsonProperty("log_uri")
	private URI logoUri;
	
	@JsonProperty("redirect_uris")
	private List<URI> redirectUris;
	
	@JsonProperty("token_endpoint_auth_method")	
	private String tokenEndpointAuthMethod;
	
	@JsonProperty("client_uri")
	private URI clientUri;
	
	@JsonProperty("grant_types")
	private List<GrantType> grantTypes;
	
	@JsonProperty("scope")
	private Scope scope;
}
