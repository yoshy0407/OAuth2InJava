package com.example.oauth2.authorization.oauth2.domain.authorize;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.oauth2.authorization.oauth2.domain.authorize.spi.AuthorizeRequestRepository;

@Component
public class InMemoryAuthorizeRequestRepository implements AuthorizeRequestRepository {

	private final ConcurrentHashMap<String, AuthorizeRequest> storeMap =
			new ConcurrentHashMap<String, AuthorizeRequest>();
	
	public InMemoryAuthorizeRequestRepository(AuthorizeRequest...requests) {
		for (AuthorizeRequest req : requests) {
			this.storeMap.put(req.getCode(), req);
		}
	}
	
	@Override
	public Optional<AuthorizeRequest> get(String code) {
		return Optional.ofNullable(this.storeMap.get(code));
	}

	@Override
	public void save(AuthorizeRequest req) {
		this.storeMap.put(req.getCode(), req);
	}

	@Override
	public void update(AuthorizeRequest req) {
		this.storeMap.replace(req.getCode(), req);
	}

	@Override
	public void remove(AuthorizeRequest req) {
		this.storeMap.remove(req.getCode());
	}

}
