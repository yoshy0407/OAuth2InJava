package com.example.oauth2.authorization.oauth2.domain.client;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.oauth2.authorization.oauth2.domain.client.spi.OAuth2ClientRepository;
import com.example.oauth2.authorization.oauth2.domain.client.value.ClientAuthMethod;
import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.exception.client.ClientNotFoundException;
import com.example.oauth2.authorization.oauth2.exception.client.InvalidRedirectUriException;
import com.example.oauth2.authorization.oauth2.exception.client.InvalidScopeException;
import com.example.oauth2.authorization.oauth2.value.Scope;

class OAuth2ClientDomainServiceTest {

	OAuth2ClientDomainService service;

	OAuth2ClientRepository repository;
	
	PasswordEncoder passwordEncoder;
	
	@BeforeEach
	void setup() {
		this.repository = new InMemoryOAuth2ClientRepository();
		this.service = new OAuth2ClientDomainService(repository);
		this.passwordEncoder = new BCryptPasswordEncoder();
	}
	
	@Test
	void testCheckClient() {
		OAuth2Client client = OAuth2Client.createResource(
				"clientId", 
				"clientName", 
				URI.create("http://localhost/"), 
				URI.create("http://localhost/logo"), 
				"password", 
				Lists.list(URI.create("http://localhost/callback")), 
				new Scope("hoge fuga code"), 
				ClientAuthMethod.CLIENT_SECRET_BASIC, 
				Lists.list(GrantType.AUTHORIZATION_CODE), 
				passwordEncoder);
		repository.save(client);
		
		assertDoesNotThrow(() -> {
			this.service.checkClient("clientId", URI.create("http://localhost/callback"), Optional.of(new Scope("hoge")));
		});
	}
	
	@Test
	void testCheckClientClientNotFound() {
		assertThrows(ClientNotFoundException.class, () -> {
			this.service.checkClient("clientId", URI.create("http://localhost/callback"), Optional.of(new Scope("hoge")));			
		});
	}

	@Test
	void testCheckClientInvalidRedirectUri() {
		OAuth2Client client = OAuth2Client.createResource(
				"clientId", 
				"clientName", 
				URI.create("http://localhost/"), 
				URI.create("http://localhost/logo"), 
				"password", 
				Lists.list(URI.create("http://localhost/callback")), 
				new Scope("hoge fuga code"), 
				ClientAuthMethod.CLIENT_SECRET_BASIC, 
				Lists.list(GrantType.AUTHORIZATION_CODE), 
				passwordEncoder);
		repository.save(client);
		
		assertThrows(InvalidRedirectUriException.class, () -> {
			this.service.checkClient("clientId", URI.create("http://localhost/callback/test"), Optional.of(new Scope("hoge")));			
		});
	}
	
	@Test
	void testCheckClientInvalidScope() {
		OAuth2Client client = OAuth2Client.createResource(
				"clientId", 
				"clientName", 
				URI.create("http://localhost/"), 
				URI.create("http://localhost/logo"), 
				"password", 
				Lists.list(URI.create("http://localhost/callback")), 
				new Scope("hoge fuga code"), 
				ClientAuthMethod.CLIENT_SECRET_BASIC, 
				Lists.list(GrantType.AUTHORIZATION_CODE), 
				passwordEncoder);
		repository.save(client);
		
		assertThrows(InvalidScopeException.class, () -> {
			this.service.checkClient("clientId", URI.create("http://localhost/callback"), Optional.of(new Scope("test")));			
		});
	}

	@Test
	void testCheckScope() {
		OAuth2Client client = OAuth2Client.createResource(
				"clientId", 
				"clientName", 
				URI.create("http://localhost/"), 
				URI.create("http://localhost/logo"), 
				"password", 
				Lists.list(URI.create("http://localhost/callback")), 
				new Scope("hoge fuga code"), 
				ClientAuthMethod.CLIENT_SECRET_BASIC, 
				Lists.list(GrantType.AUTHORIZATION_CODE), 
				passwordEncoder);
		repository.save(client);
		
		assertDoesNotThrow(() -> {
			service.checkScope("clientId", new Scope("hoge"));
		});
	}
	
	@Test
	void testCheckScopeClientNotFound() {
		
		assertThrows(ClientNotFoundException.class, () -> {
			service.checkScope("test", new Scope("hoge"));
		});
	}
	
	@Test
	void testCheckScopeInvalidScope() {
		OAuth2Client client = OAuth2Client.createResource(
				"clientId", 
				"clientName", 
				URI.create("http://localhost/"), 
				URI.create("http://localhost/logo"), 
				"password", 
				Lists.list(URI.create("http://localhost/callback")), 
				new Scope("hoge fuga code"), 
				ClientAuthMethod.CLIENT_SECRET_BASIC, 
				Lists.list(GrantType.AUTHORIZATION_CODE), 
				passwordEncoder);
		repository.save(client);
		
		assertThrows(InvalidScopeException.class, () -> {
			service.checkScope("clientId", new Scope("test"));
		});
	}

}
