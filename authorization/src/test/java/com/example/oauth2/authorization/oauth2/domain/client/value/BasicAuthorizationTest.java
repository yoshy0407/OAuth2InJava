package com.example.oauth2.authorization.oauth2.domain.client.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;

class BasicAuthorizationTest {

	@Test
	void test() {
		String str = "Basic " + Base64Utils.encodeToString("username:password".getBytes());
		BasicAuthorization basicAuthorization = new BasicAuthorization(str);
		
		assertThat(basicAuthorization.getUsername()).isEqualTo("username");
		assertThat(basicAuthorization.getPassword()).isEqualTo("password");
	}

}
