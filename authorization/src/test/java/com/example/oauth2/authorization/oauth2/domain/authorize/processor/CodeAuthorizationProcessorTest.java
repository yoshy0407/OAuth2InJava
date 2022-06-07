package com.example.oauth2.authorization.oauth2.domain.authorize.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.oauth2.authorization.oauth2.domain.authorize.AuthorizeRequest;
import com.example.oauth2.authorization.oauth2.domain.authorize.InMemoryAuthorizeRequestRepository;
import com.example.oauth2.authorization.oauth2.domain.authorize.spi.AuthorizeTokenGenerator;
import com.example.oauth2.authorization.oauth2.domain.authorize.value.ResponseType;
import com.example.oauth2.authorization.oauth2.value.Scope;
import com.example.oauth2.authorization.oauth2.web.authorize.AuthorizeEndpointRequest;

class CodeAuthorizationProcessorTest {

	AuthorizeTokenGenerator authTokenGenerator;
	
	InMemoryAuthorizeRequestRepository repository;
	
	CodeAuthorizationProcessor processor;
	
	@BeforeEach
	void setup() {
		authTokenGenerator = Mockito.mock(AuthorizeTokenGenerator.class);
		repository = new InMemoryAuthorizeRequestRepository();
		processor = new CodeAuthorizationProcessor(repository, authTokenGenerator);
	}
			
	@Test
	void testCodeAuthorizationProcessor() {
		assertThat(processor.supports(Lists.list(ResponseType.CODE))).isTrue();
		assertThat(processor.supports(Lists.list(ResponseType.ID_TOKEN))).isFalse();
	}

	@Test
	void testAuthorize() {
		AuthorizeEndpointRequest request = 
				new AuthorizeEndpointRequest(
						Lists.list(ResponseType.CODE), 
						"test-client", 
						URI.create("http://localhost/callback"), 
						Optional.of(new Scope("hoge fuga")), 
						"stateCode", 
						"code-challenge", 
						"plain");

		when(authTokenGenerator.generate()).thenReturn("authorizeToken");
		
		URI uri = processor.authorize(request);
		
		assertThat(uri).isEqualTo(URI.create("http://localhost/callback?code=authorizeToken&state=stateCode"));
		
		Optional<AuthorizeRequest> req = repository.get("authorizeToken");
		assertThat(req.isPresent()).isTrue();
	}

}
