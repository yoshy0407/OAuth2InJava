package com.example.oauth2.authorization.oauth2.domain.client;

import java.net.URI;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.oauth2.authorization.oauth2.domain.client.spi.OAuth2ClientRepository;
import com.example.oauth2.authorization.oauth2.exception.client.ClientNotFoundException;
import com.example.oauth2.authorization.oauth2.exception.client.InvalidRedirectUriException;
import com.example.oauth2.authorization.oauth2.exception.client.InvalidScopeException;
import com.example.oauth2.authorization.oauth2.value.Scope;

@Service
public class OAuth2ClientDomainService {

	private final OAuth2ClientRepository repository;
	
	public OAuth2ClientDomainService(OAuth2ClientRepository repository) {
		this.repository = repository;
	}
	
	public void checkClient(String clientId, URI redirectUri, Optional<Scope> optScope) 
			throws InvalidScopeException, ClientNotFoundException, InvalidRedirectUriException {
		Optional<OAuth2Client> optClient = this.repository.get(clientId);
		if (optClient.isEmpty()) {
			throw new ClientNotFoundException("クライアントが存在しません", null);
		}

		OAuth2Client client = optClient.get();

		if (!client.verifyRedirectUri(redirectUri)) {
			throw new InvalidRedirectUriException("Invalid redirect URI", null);		
		}
		
		if (optScope.isPresent()) {
			if (!client.containScope(optScope.get())) {
				throw new InvalidScopeException("スコープの設定が不正です", null);			
			}						
		}
	}

	public void checkScope(String clientId, Scope scope) 
			throws ClientNotFoundException, InvalidScopeException {
		Optional<OAuth2Client> optClient = this.repository.get(clientId);
		if (optClient.isEmpty()) {
			throw new ClientNotFoundException("クライアントが存在しません", null);			
		}
		
		OAuth2Client client = optClient.get();
		
		if (!client.containScope(scope)) {
			throw new InvalidScopeException("スコープの設定が不正です。", null);
		}
	}

}
