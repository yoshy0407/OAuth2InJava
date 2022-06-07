package com.example.oauth2.authorization.oauth2.domain.client;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.example.oauth2.authorization.oauth2.domain.client.spi.OAuth2ClientRepository;

public class InMemoryOAuth2ClientRepository implements OAuth2ClientRepository {

	private final ConcurrentHashMap<String, OAuth2Client> storeMap
		= new ConcurrentHashMap<>();
	
	public InMemoryOAuth2ClientRepository(OAuth2Client...clients) {
		for (OAuth2Client client : clients) {
			this.storeMap.put(client.getClientId(), client);			
		}
	}
	
	@Override
	public Optional<OAuth2Client> get(String clientId) {
		return Optional.ofNullable(this.storeMap.get(clientId));
	}

	@Override
	public void save(OAuth2Client client) {
		this.storeMap.put(client.getClientId(), client);
	}

	@Override
	public void update(OAuth2Client client) {
		this.storeMap.replace(client.getClientId(), client);
	}

	@Override
	public void remove(OAuth2Client client) {
		this.storeMap.remove(client.getClientId());
	}

}
