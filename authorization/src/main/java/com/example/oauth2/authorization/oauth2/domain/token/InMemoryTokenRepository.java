package com.example.oauth2.authorization.oauth2.domain.token;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.oauth2.authorization.oauth2.domain.token.spi.TokenRepository;

@Component
public class InMemoryTokenRepository implements TokenRepository {

	private final ConcurrentHashMap<String, Token> storeMap
		= new ConcurrentHashMap<>();
	
	@Override
	public Optional<Token> get(String accessToken) {
		return Optional.ofNullable(this.storeMap.get(accessToken));
	}

	@Override
	public void save(Token token) {
		this.storeMap.put(token.getToken(), token);
	}

	@Override
	public void update(Token token) {
		this.storeMap.replace(token.getToken(), token);
	}

	@Override
	public void remove(Token token) {
		this.storeMap.remove(token.getToken());
		
	}

}
