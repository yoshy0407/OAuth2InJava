package com.example.oauth2.authorization.oauth2.web.authorize;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthorizeApprove {

	private String approve;
	
	private List<String> scope;
}
