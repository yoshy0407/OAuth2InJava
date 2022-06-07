package com.example.oauth2.authorization.oauth2.domain.authorize.spi;

import java.util.Optional;

import com.example.oauth2.authorization.oauth2.domain.authorize.AuthorizeRequest;


public interface AuthorizeRequestRepository {

	public Optional<AuthorizeRequest> get(String code);
	
	public void save(AuthorizeRequest req);

	public void update(AuthorizeRequest req);

	public void remove(AuthorizeRequest req);

}
