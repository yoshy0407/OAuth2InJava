package com.example.oauth2.authorization.oauth2.domain.authorize;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.oauth2.authorization.oauth2.domain.client.OAuth2Client;
import com.example.oauth2.authorization.oauth2.domain.client.OAuth2ClientRepository;
import com.example.oauth2.authorization.oauth2.web.authorize.AuthorizeEndpointRequest;

@Component
public class AuthorizeDomainService {

	private final OAuth2ClientRepository clientRepository;
	
	public AuthorizeDomainService(OAuth2ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}
	
	public boolean checkScope(AuthorizeEndpointRequest req, List<String> scope) {
		Optional<OAuth2Client> optClient = this.clientRepository.get(req.getClientId());
		if (optClient.isPresent()) {
			OAuth2Client client = optClient.get();
			return client.containScope(scope);	
		} else {
			return false;
		}
	}
}
