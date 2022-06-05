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
import com.example.oauth2.authorization.oauth2.domain.client.value.ClientAuthMethod;
import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
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

		List<GrantType> granttypes = new ArrayList<>();
		granttypes.add(GrantType.AUTHORIZATION_CODE);
		granttypes.add(GrantType.CLIENT_CREDENTIALS);
		granttypes.add(GrantType.PASSWORD);
		granttypes.add(GrantType.REFRESH_TOKEN);
		
		OAuth2Client client =
				OAuth2Client.createClient(
						"test-client", "test-client", null, null, "test-client-pass", uris, new Scope("foo bar hoge")
						, ClientAuthMethod.CLIENT_SECRET_BASIC, granttypes, passwordEncoder);
				
		return new InMemoryOAuth2ClientRepository(client);
	}
}
