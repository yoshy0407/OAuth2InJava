package com.example.oauth2.authorization.oauth2.domain.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.oauth2.authorization.oauth2.domain.client.value.ClientAuthMethod;
import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.value.Scope;

class OAuth2ClientTest {

	PasswordEncoder passwordEncoder;

	OAuth2Client client;

	@BeforeEach
	void setup() {
		passwordEncoder = new BCryptPasswordEncoder();
		client = 
				OAuth2Client.createClient(
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
	}

	@Test
	void testMatchSecret() {
		assertThat(client.matchSecret("password", passwordEncoder)).isTrue();
		assertThat(client.matchSecret("password1", passwordEncoder)).isFalse();
	}

	@Test
	void testVerifyRedirectUri() {
		assertThat(client.verifyRedirectUri(URI.create("http://localhost/callback"))).isTrue();
		assertThat(client.verifyRedirectUri(URI.create("http://localhost/callback/test"))).isFalse();
	}

	@Test
	void testContainScope() {
		assertThat(client.containScope(new Scope("hoge fuga code"))).isTrue();
		assertThat(client.containScope(new Scope("hoge fuga"))).isTrue();
		assertThat(client.containScope(new Scope("hoge"))).isTrue();
		assertThat(client.containScope(new Scope("test"))).isFalse();
	}

	@Test
	void testVerifyGrantType() {
		assertThat(client.verifyGrantType(GrantType.AUTHORIZATION_CODE)).isTrue();
	}

	@Test
	void testEqualAuthMethod() {
		assertThat(client.equalAuthMethod(ClientAuthMethod.CLIENT_SECRET_BASIC)).isTrue();
		assertThat(client.equalAuthMethod(ClientAuthMethod.CLIENT_SECRET_POST)).isFalse();
	}

	@Test
	void testCreateClient() {
		OAuth2Client client = OAuth2Client.createClient(
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
	
		assertThat(client.isClient()).isTrue();
		assertThat(client.isResource()).isFalse();
	}

	@Test
	void testCreateResource() {
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
		
		assertThat(client.isClient()).isFalse();
		assertThat(client.isResource()).isTrue();

	}

}
