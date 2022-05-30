package com.example.oauth2.authorization.oauth2.domain.authorize;

import java.util.Optional;


public interface AuthorizeRequestRepository {

	public Optional<AuthorizeRequest> get(String code);
	
	public void save(AuthorizeRequest req);

	public void update(AuthorizeRequest req);

	public void remove(AuthorizeRequest req);

}
