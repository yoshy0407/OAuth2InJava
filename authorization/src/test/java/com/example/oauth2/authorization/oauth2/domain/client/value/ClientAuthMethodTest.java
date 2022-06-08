package com.example.oauth2.authorization.oauth2.domain.client.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ClientAuthMethodTest {

	@Test
	void testToString() {
		assertThat(ClientAuthMethod.CLIENT_SECRET_BASIC.toString()).isEqualTo("client_secret_basic");
		assertThat(ClientAuthMethod.CLIENT_SECRET_POST.toString()).isEqualTo("client_secret_post");
	}

	@Test
	void testOf() {
		ClientAuthMethod basic = ClientAuthMethod.of("client_secret_basic");
		assertThat(basic).isEqualTo(ClientAuthMethod.CLIENT_SECRET_BASIC);
		
		ClientAuthMethod basic2 = ClientAuthMethod.of("CLIENT_SECRET_BASIC");
		assertThat(basic2).isEqualTo(ClientAuthMethod.CLIENT_SECRET_BASIC);

		ClientAuthMethod post = ClientAuthMethod.of("client_secret_post");
		assertThat(post).isEqualTo(ClientAuthMethod.CLIENT_SECRET_POST);

		ClientAuthMethod post2 = ClientAuthMethod.of("CLIENT_SECRET_POST");
		assertThat(post2).isEqualTo(ClientAuthMethod.CLIENT_SECRET_POST);
}

}
