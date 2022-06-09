package com.example.oauth2.authorization.oauth2.domain.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Base64Utils;

import com.example.oauth2.authorization.oauth2.domain.client.spi.OAuth2ClientRepository;
import com.example.oauth2.authorization.oauth2.domain.client.value.ClientAuthMethod;
import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.exception.client.ClientRegistrationErrorCode;
import com.example.oauth2.authorization.oauth2.exception.client.ClientRegistrationException;
import com.example.oauth2.authorization.oauth2.exception.token.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.exception.token.TokenErrorCode;
import com.example.oauth2.authorization.oauth2.value.Scope;

class DefaultOAuth2ClientApplicationServiceTest {

	DefaultOAuth2ClientApplicationService service;

	OAuth2ClientRepository repository;

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	MessageSource messageSource;

	@BeforeEach
	void setup() {
		this.messageSource = mock(MessageSource.class);
		this.repository = new InMemoryOAuth2ClientRepository();
		this.service = new DefaultOAuth2ClientApplicationService(repository, passwordEncoder, messageSource);
	}

	@Test
	void testRegister() throws ClientRegistrationException {

		RegisterResult result = 
				this.service.register(
						"clientName", 
						URI.create("http://localhost/logo"),
						Lists.list(URI.create("http://localhost/callback")),
						"client_secret_basic", 
						URI.create("http://localhost/"), 
						Lists.list(GrantType.AUTHORIZATION_CODE), 
						new Scope("hoge fuga test"));

		assertThat(result.getClientId()).isNotBlank();
		assertThat(result.getClientIdIssuedAt()).isNotNull();
		assertThat(result.getClientSecret()).isNotNull();
		assertThat(result.getClientSecretExpiresAt()).isNotNull();
	}


	@Test
	void testRegisterRedirectUriNull() throws ClientRegistrationException {

		when(messageSource.getMessage(eq(ClientRegistrationErrorCode.INVALID_REDIRECT_URI.messageId()), any(), any(), any()))
			.thenReturn("testMessage");
		
		ClientRegistrationException ex = assertThrows(ClientRegistrationException.class, () -> {
			this.service.register(
					"clientName", 
					URI.create("http://localhost/logo"),
					null,
					"client_secret_basic", 
					URI.create("http://localhost/"), 
					Lists.list(GrantType.AUTHORIZATION_CODE), 
					new Scope("hoge fuga test"));

		});
		
		assertThat(ex.errorCode()).isEqualTo(ClientRegistrationErrorCode.INVALID_REDIRECT_URI);
		assertThat(ex.getMessage()).isEqualTo("testMessage");

	}
	
	@Test
	void testRegisterError() throws ClientRegistrationException {

		when(messageSource.getMessage(eq(ClientRegistrationErrorCode.INVALID_CLIENT_METADATA.messageId()), any(), any(), any()))
			.thenReturn("testMessage");

		ClientRegistrationException ex = assertThrows(ClientRegistrationException.class, () -> {
			this.service.register(
					"clientName", 
					URI.create("http://localhost/logo"),
					Lists.list(URI.create("http://localhost/callback")),
					"client_secret_basic", 
					URI.create("http://localhost/"), 
					Lists.list(), 
					new Scope("hoge fuga test"));

		});
		
		assertThat(ex.errorCode()).isEqualTo(ClientRegistrationErrorCode.INVALID_CLIENT_METADATA);
		assertThat(ex.getMessage()).isEqualTo("testMessage");

	}
	
	@Test
	void testAuthenticateBasic() {
		OAuth2Client client = 
				OAuth2Client.createClient(
						"clientId", 
						"clientName", 
						URI.create("http://localhost"), 
						URI.create("http://localhost/logo"), 
						"password", 
						Lists.list(URI.create("http://localhost/callback")), 
						new Scope("hoge fuga"), 
						ClientAuthMethod.CLIENT_SECRET_BASIC, 
						Lists.list(GrantType.AUTHORIZATION_CODE), 
						passwordEncoder);
		
		repository.save(client);
		
		String basic = "BASIC " + Base64Utils.encodeToString("clientId:password".getBytes());
		assertDoesNotThrow(() -> {
			service.authenticate(Optional.of(basic), "", "");
		});
	}
	
	@Test
	void testAuthenticateBody() {
		OAuth2Client client = 
				OAuth2Client.createClient(
						"clientId", 
						"clientName", 
						URI.create("http://localhost"), 
						URI.create("http://localhost/logo"), 
						"password", 
						Lists.list(URI.create("http://localhost/callback")), 
						new Scope("hoge fuga"), 
						ClientAuthMethod.CLIENT_SECRET_POST, 
						Lists.list(GrantType.AUTHORIZATION_CODE), 
						passwordEncoder);
		
		repository.save(client);
		
		assertDoesNotThrow(() -> {
			service.authenticate(Optional.empty(), "clientId", "password");
		});
	}
	
	@Test
	void testAuthenticateAllEmpty() {
		when(messageSource.getMessage(eq(TokenErrorCode.INVALID_REQUEST.messageId()), any(), any(), any()))
			.thenReturn("testMessage");

		OAuth2TokenException ex = assertThrows(OAuth2TokenException.class, () -> {
			service.authenticate(Optional.empty(), null, null);
		});
		
		assertThat(ex.errorCode()).isEqualTo(TokenErrorCode.INVALID_REQUEST);
		assertThat(ex.getMessage()).isEqualTo("testMessage");
	}

	@Test
	void testAuthenticateClientNotFound() {
		when(messageSource.getMessage(eq(TokenErrorCode.INVALID_CLIENT.messageId()), any(), any(), any()))
			.thenReturn("testMessage");

		OAuth2TokenException ex = assertThrows(OAuth2TokenException.class, () -> {
			service.authenticate(Optional.empty(), "test", "password");
		});
		
		assertThat(ex.errorCode()).isEqualTo(TokenErrorCode.INVALID_CLIENT);
		assertThat(ex.getMessage()).isEqualTo("testMessage");
	}

	@Test
	void testAuthenticateAuthMethodNotMatch() {
		when(messageSource.getMessage(eq(TokenErrorCode.INVALID_CLIENT.messageId()), any(), any(), any()))
			.thenReturn("testMessage");

		OAuth2Client client = 
				OAuth2Client.createClient(
						"clientId", 
						"clientName", 
						URI.create("http://localhost"), 
						URI.create("http://localhost/logo"), 
						"password", 
						Lists.list(URI.create("http://localhost/callback")), 
						new Scope("hoge fuga"), 
						ClientAuthMethod.CLIENT_SECRET_BASIC, 
						Lists.list(GrantType.AUTHORIZATION_CODE), 
						passwordEncoder);
		
		repository.save(client);

		OAuth2TokenException ex = assertThrows(OAuth2TokenException.class, () -> {
			service.authenticate(Optional.empty(), "test", "password");
		});
		
		assertThat(ex.errorCode()).isEqualTo(TokenErrorCode.INVALID_CLIENT);
		assertThat(ex.getMessage()).isEqualTo("testMessage");
	}
	
	@Test
	void testAuthenticatePasswordNotMatch() {
		when(messageSource.getMessage(eq(TokenErrorCode.INVALID_CLIENT.messageId()), any(), any(), any()))
			.thenReturn("testMessage");

		OAuth2Client client = 
				OAuth2Client.createClient(
						"clientId", 
						"clientName", 
						URI.create("http://localhost"), 
						URI.create("http://localhost/logo"), 
						"password", 
						Lists.list(URI.create("http://localhost/callback")), 
						new Scope("hoge fuga"), 
						ClientAuthMethod.CLIENT_SECRET_POST, 
						Lists.list(GrantType.AUTHORIZATION_CODE), 
						passwordEncoder);
		
		repository.save(client);

		OAuth2TokenException ex = assertThrows(OAuth2TokenException.class, () -> {
			service.authenticate(Optional.empty(), "test", "rawPassword");
		});
		
		assertThat(ex.errorCode()).isEqualTo(TokenErrorCode.INVALID_CLIENT);
		assertThat(ex.getMessage()).isEqualTo("testMessage");
	}

}
