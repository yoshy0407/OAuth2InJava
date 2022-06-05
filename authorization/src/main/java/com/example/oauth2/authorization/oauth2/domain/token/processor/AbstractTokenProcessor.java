package com.example.oauth2.authorization.oauth2.domain.token.processor;

import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;

import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.exception.token.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.exception.token.TokenErrorCode;

public abstract class AbstractTokenProcessor implements TokenProcessor {

	private final GrantType grantType;
	
	private final MessageSource messageSource;
	
	protected AbstractTokenProcessor(GrantType grantType, MessageSource messageSource) {
		this.grantType = grantType;
		this.messageSource = messageSource;
	}
	
	@Override
	public boolean supports(GrantType grantType) throws OAuth2TokenException {
		requestParamNotNull(grantType, "grant_type");
		return grantType.equals(GrantType.AUTHORIZATION_CODE);
	}

	protected <T> void requestParamNotNull(T obj, String propertyName) throws OAuth2TokenException {
		if (grantType == null) {
			throw new OAuth2TokenException(TokenErrorCode.INVALID_REQUEST, this.messageSource, propertyName);
		}		
	}

	protected void requestParamNotEmpty(String str, String propertyName) throws OAuth2TokenException {
		if (!StringUtils.hasLength(str)) {
			throw new OAuth2TokenException(TokenErrorCode.INVALID_REQUEST, this.messageSource, propertyName);
		}		
	}
	
	protected MessageSource getMessageSource() {
		return this.messageSource;
	}
}
