package com.example.oauth2.authorization.oauth2.web.client;

import java.net.URI;
import java.util.List;

import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.value.Scope;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientRegistrationResponse {

	@JsonProperty("client_id")
	private String clientId;
	
	@JsonProperty("client_secret")
	private String clientSecret;
	
	//unixtime
	@JsonProperty("client_id_issued_at")
	private long clientIdIssuedAt;
	
	//unixtime
	//client_secretが発行された場合必須。
	//期限切れがない場合、0を設定
	@JsonProperty("client_secret_expires_at")
	private long clientSecretExpiresAt;
	
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
