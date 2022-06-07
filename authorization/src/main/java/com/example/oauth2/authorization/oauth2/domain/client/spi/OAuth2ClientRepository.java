package com.example.oauth2.authorization.oauth2.domain.client.spi;

import java.util.Optional;

import com.example.oauth2.authorization.oauth2.domain.client.OAuth2Client;

public interface OAuth2ClientRepository {

	public Optional<OAuth2Client> get(String clientId);
	
	public void save(OAuth2Client client);

	public void update(OAuth2Client client);

	public void remove(OAuth2Client client);
}
