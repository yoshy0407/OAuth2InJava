package com.example.oauth2.authorization.oauth2.domain.authorize.processor;

import java.util.List;

import com.example.oauth2.authorization.oauth2.domain.authorize.value.ResponseType;

public abstract class AbstractAuthorizationProcessor implements AuthorizationProcessor {

	private final ResponseType responseType;
	
	protected AbstractAuthorizationProcessor(ResponseType responseType) {
		this.responseType = responseType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(List<ResponseType> responseTypes) throws IllegalArgumentException {
		return responseTypes.contains(this.responseType);
	}

}
