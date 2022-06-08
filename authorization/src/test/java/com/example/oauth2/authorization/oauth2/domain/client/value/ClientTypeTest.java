package com.example.oauth2.authorization.oauth2.domain.client.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ClientTypeTest {

	@Test
	void testGetValue() {
		assertThat(ClientType.AUTH_CLIENT.getValue()).isEqualTo(1);
		assertThat(ClientType.RESOURCE.getValue()).isEqualTo(2);
	}

	@Test
	void testOf() {
		assertThat(ClientType.of(1)).isEqualTo(ClientType.AUTH_CLIENT);
		assertThat(ClientType.of(2)).isEqualTo(ClientType.RESOURCE);
	}

}
