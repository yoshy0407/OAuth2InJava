package com.example.oauth2.authorization.oauth2.domain.authorize;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.oauth2.authorization.oauth2.domain.authorize.processor.AuthorizationProcessor;
import com.example.oauth2.authorization.oauth2.domain.authorize.value.ResponseType;
import com.example.oauth2.authorization.oauth2.domain.client.OAuth2ClientDomainService;
import com.example.oauth2.authorization.oauth2.exception.OAuth2AuthorizationException;
import com.example.oauth2.authorization.oauth2.exception.UnsupportedResponseType;
import com.example.oauth2.authorization.oauth2.exception.client.ClientNotFoundException;
import com.example.oauth2.authorization.oauth2.exception.client.InvalidRedirectUriException;
import com.example.oauth2.authorization.oauth2.exception.client.InvalidScopeException;
import com.example.oauth2.authorization.oauth2.value.Scope;
import com.example.oauth2.authorization.oauth2.web.authorize.AuthorizeEndpointRequest;

class DefaultAuthorizeApplicationServiceTest {

	DefaultAuthorizeApplicationService service;
	
	OAuth2ClientDomainService clientDomainService;
	
	AuthorizationProcessor processor;
	
	@BeforeEach
	void setup() {
		clientDomainService = Mockito.mock(OAuth2ClientDomainService.class);
		processor = Mockito.mock(AuthorizationProcessor.class);
		service = new DefaultAuthorizeApplicationService(clientDomainService, Lists.list(processor));
	}
	
	@Test
	void testCheckClient() {
		
		AuthorizeEndpointRequest request = 
				new AuthorizeEndpointRequest(
						Lists.list(ResponseType.CODE), 
						"test-client", 
						URI.create("http://localhost/callback"), 
						Optional.of(new Scope("hoge fuga")), 
						"state", 
						"code-challenge", 
						"plain");
		assertDoesNotThrow(() -> {
			service.checkClient(request);			
		});			
	}

	@Test
	void testCheckClientError() throws InvalidScopeException, ClientNotFoundException, InvalidRedirectUriException {
		
		AuthorizeEndpointRequest request = 
				new AuthorizeEndpointRequest(
						Lists.list(ResponseType.CODE), 
						"test-client", 
						URI.create("http://localhost/callback"), 
						Optional.of(new Scope("hoge fuga")), 
						"state", 
						"code-challenge", 
						"plain");
		
		doThrow(new ClientNotFoundException("message", null)).when(clientDomainService).checkClient(anyString(), any(), any());
		
		assertThrows(OAuth2AuthorizationException.class, () -> {
			service.checkClient(request);
		});
	}

	@Test
	void testAuthorize() {

		AuthorizeEndpointRequest request = 
				new AuthorizeEndpointRequest(
						Lists.list(ResponseType.CODE), 
						"test-client", 
						URI.create("http://localhost/callback"), 
						Optional.of(new Scope("hoge fuga")), 
						"state", 
						"code-challenge", 
						"plain");
	
		when(processor.supports(anyList())).thenReturn(Boolean.TRUE);
		when(processor.authorize(any())).thenReturn(URI.create("http://localhost/callback"));
		
		URI uri = service.authorize(request);
		
		assertThat(uri).isEqualTo(URI.create("http://localhost/callback"));
	}
	
	@Test
	void testAuthorizeErrorClientNotFound() throws InvalidScopeException, ClientNotFoundException, InvalidRedirectUriException {

		AuthorizeEndpointRequest request = 
				new AuthorizeEndpointRequest(
						Lists.list(ResponseType.CODE), 
						"test-client", 
						URI.create("http://localhost/callback"), 
						Optional.of(new Scope("hoge fuga")), 
						"state", 
						"code-challenge", 
						"plain");
	
		doThrow(new ClientNotFoundException("message", null)).when(clientDomainService).checkClient(anyString(), any(), any());

		URI uri = service.authorize(request);
		
		assertThat(uri).isEqualTo(URI.create("http://localhost/callback?error=unauthorized_client&error_description=message&state=state"));
	}

	@Test
	void testAuthorizeErrorInvalidScope() throws InvalidScopeException, ClientNotFoundException, InvalidRedirectUriException {

		AuthorizeEndpointRequest request = 
				new AuthorizeEndpointRequest(
						Lists.list(ResponseType.CODE), 
						"test-client", 
						URI.create("http://localhost/callback"), 
						Optional.of(new Scope("hoge fuga")), 
						"", 
						"code-challenge", 
						"plain");
	
		doThrow(new InvalidScopeException("message", null)).when(clientDomainService).checkClient(anyString(), any(), any());

		URI uri = service.authorize(request);
		
		assertThat(uri).isEqualTo(URI.create("http://localhost/callback?error=invalid_scope&error_description=message"));
	}

	@Test
	void testAuthorizeErrorInvalidRequest() throws InvalidScopeException, ClientNotFoundException, InvalidRedirectUriException {

		AuthorizeEndpointRequest request = 
				new AuthorizeEndpointRequest(
						null, 
						"test-client", 
						URI.create("http://localhost/callback"), 
						Optional.of(new Scope("hoge fuga")), 
						"", 
						"code-challenge", 
						"plain");
	

		URI uri = service.authorize(request);
		
		assertThat(uri).isEqualTo(URI.create("http://localhost/callback?error=invalid_request&error_description=response_typeに値が設定されていません。"));
	}

	@Test
	void testAuthorizeErrorUnsupportedResponseType() throws InvalidScopeException, ClientNotFoundException, InvalidRedirectUriException {

		AuthorizeEndpointRequest request = 
				new AuthorizeEndpointRequest(
						Lists.list(ResponseType.CODE), 
						"test-client", 
						URI.create("http://localhost/callback"), 
						Optional.of(new Scope("hoge fuga")), 
						"", 
						"code-challenge", 
						"plain");
	
		when(processor.supports(anyList())).thenReturn(Boolean.FALSE);

		assertThrows(UnsupportedResponseType.class, () -> {
			service.authorize(request);			
		});
		
	}

}
