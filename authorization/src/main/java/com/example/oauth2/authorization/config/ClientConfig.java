package com.example.oauth2.authorization.config;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.oauth2.authorization.oauth2.domain.client.InMemoryOAuth2ClientRepository;
import com.example.oauth2.authorization.oauth2.domain.client.OAuth2Client;
import com.example.oauth2.authorization.oauth2.domain.client.OAuth2ClientRepository;
import com.example.oauth2.authorization.oauth2.value.Scope;

@Configuration
public class ClientConfig {

	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public OAuth2ClientRepository clientRepository(PasswordEncoder passwordEncoder) {
		List<URI> uris = new ArrayList<>();
		uris.add(URI.create("http://localhost:9090/callback"));

		OAuth2Client client = 
				OAuth2Client.newInstance(
						"test-client", 
						"test-client-pass", 
						null,
						uris, 
						new Scope("foo bar hoge"),
						passwordEncoder);

		return new InMemoryOAuth2ClientRepository(client);
	}
}
