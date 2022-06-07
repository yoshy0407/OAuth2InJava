package com.example.oauth2.authorization.oauth2.domain.authorize;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;

class AuthorizeErrorUriBuilderTest {

	@Test
	void testInvalidRequest() {
		URI uri = AuthorizeErrorUriBuilder.invalidRequest(URI.create("http://localhost"))
			.errorDescription("errorMessage")
			.errorUri(URI.create("http://localhost"))
			.state("state")
			.toUri();
		assertThat(uri).isEqualTo(URI.create("http://localhost?error=invalid_request&error_description=errorMessage&error_uri=http://localhost&state=state"));
	}

	@Test
	void testUnauthorizedClient() {
		String uri = AuthorizeErrorUriBuilder.unauthorizedClient(URI.create("http://localhost"))
				.errorUri("http://localhost")
				.state("state")
				.toUriString();
		
		assertThat(uri).isEqualTo("http://localhost?error=unauthorized_client&error_uri=http://localhost&state=state");
	}

	@Test
	void testAccessDenied() {
		URI uri = AuthorizeErrorUriBuilder.accessDenied(URI.create("http://localhost"))
				.state("state")
				.toUri();
		
		assertThat(uri).isEqualTo(URI.create("http://localhost?error=access_denied&state=state"));
	}

	@Test
	void testUnsupportedResponseType() {
		URI uri = AuthorizeErrorUriBuilder.unsupportedResponseType(URI.create("http://localhost"))
				.toUri();
		
		assertThat(uri).isEqualTo(URI.create("http://localhost?error=unsupported_response_type"));
	}

	@Test
	void testInvalidScope() {
		URI uri = AuthorizeErrorUriBuilder.invalidScope(URI.create("http://localhost"))
				.toUri();
		
		assertThat(uri).isEqualTo(URI.create("http://localhost?error=invalid_scope"));
	}

	@Test
	void testServerError() {
		URI uri = AuthorizeErrorUriBuilder.serverError(URI.create("http://localhost"))
				.toUri();
		
		assertThat(uri).isEqualTo(URI.create("http://localhost?error=server_error"));
	}

	@Test
	void testTemporarilyUnavailable() {
		URI uri = AuthorizeErrorUriBuilder.temporaryUnavailable(URI.create("http://localhost"))
				.toUri();
		
		assertThat(uri).isEqualTo(URI.create("http://localhost?error=temporary_unavailable"));
	}

}
