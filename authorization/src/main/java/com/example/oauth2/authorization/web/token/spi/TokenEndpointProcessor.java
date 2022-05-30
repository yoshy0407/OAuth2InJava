package com.example.oauth2.authorization.web.token.spi;

import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;

public interface TokenEndpointProcessor {

	public boolean supports(GrantType grantType);
}
