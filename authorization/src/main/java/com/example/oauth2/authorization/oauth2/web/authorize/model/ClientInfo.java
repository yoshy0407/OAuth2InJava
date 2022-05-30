package com.example.oauth2.authorization.oauth2.web.authorize.model;

import java.net.URI;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClientInfo {

	private String clientId;

	private String clientName;
	
	private URI clientUri;
	
	private String logoUri;
	
	private List<String> scopes;
}
