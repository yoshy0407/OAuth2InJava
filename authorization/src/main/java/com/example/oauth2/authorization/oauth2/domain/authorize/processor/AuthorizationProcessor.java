package com.example.oauth2.authorization.oauth2.domain.authorize.processor;

import java.net.URI;
import java.util.List;

import com.example.oauth2.authorization.oauth2.domain.authorize.value.ResponseType;
import com.example.oauth2.authorization.oauth2.web.authorize.AuthorizeEndpointRequest;

/**
 * 認可エンドポイントの処理を行うインタフェースです
 * @author yoshiokahiroshi
 *
 */
public interface AuthorizationProcessor {

	public boolean supports(List<ResponseType> responseTypes);
	
	public URI authorize(AuthorizeEndpointRequest req);
}
