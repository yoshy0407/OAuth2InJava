package com.example.oauth2.authorization.oauth2.domain.token.generator;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessToken {

	private String accessToken;
	
	private String tokenType;

	private long tokenLifeTime;
	
}
