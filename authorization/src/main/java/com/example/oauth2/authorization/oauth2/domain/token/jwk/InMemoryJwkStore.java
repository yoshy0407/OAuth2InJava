package com.example.oauth2.authorization.oauth2.domain.token.jwk;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class InMemoryJwkStore implements JwkStore {

	private final ConcurrentHashMap<String, Jwk> storeMap =
			new ConcurrentHashMap<>();
	
	@Override
	public Optional<Jwk> get(Optional<String> keyId){
		if (keyId.isPresent()) {
			return Optional.ofNullable(this.storeMap.get(keyId.get()));		
		} else {
			return this.storeMap.entrySet().stream()
					.map(e -> e.getValue())
					.findFirst();
		}
	}
	
	@Override
	public List<Jwk> getAll() {
		return this.storeMap.entrySet().stream()
				.map(e -> e.getValue()).toList();
	}

	@Override
	public void save(Jwk jwk) {
		this.storeMap.put(jwk.getKeyId(), jwk);
	}

}
