package com.example.oauth2.authorization.oauth2.web.authorize;

import java.beans.ConstructorProperties;
import java.net.URI;
import java.util.List;

import com.example.oauth2.authorization.oauth2.domain.authorize.value.ResponseType;
import com.example.oauth2.authorization.oauth2.value.Scope;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthorizeEndpointRequest {

	@ConstructorProperties({
		"response_type",
		"client_id",
		"redirect_uri",
		"scope",
		"state",
		"code_challenge",
		"code_challenge_method"})
	public AuthorizeEndpointRequest(
			List<ResponseType> responseType,
			String clientId,
			URI redirectUri,
			Scope scope,
			String state,
			String codeChallenge,
			String codeChallengeMethod) {
		this.responseType = responseType;
		this.clientId = clientId;
		this.redirectUri = redirectUri;
		this.scope = scope;
		this.state = state;
		this.codeChallenge = codeChallenge;
		this.codeChallengeMethod = codeChallengeMethod;		
	}
	
	//必須
	private List<ResponseType> responseType;

	//必須
	private String clientId;
	
	//条件により必須
	private URI redirectUri;
	
	//任意
	private Scope scope;
	
	//推奨
	private String state;
	
	//任意
	private String codeChallenge;
	
	//任意
	private String codeChallengeMethod;
	
}
