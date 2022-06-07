package com.example.oauth2.authorization.oauth2.domain.authorize.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ResponseTypeTest {

	@Test
	void testOf() {
		assertThat(ResponseType.of("none")).isEqualTo(ResponseType.NONE);
		assertThat(ResponseType.of("NONE")).isEqualTo(ResponseType.NONE);
		assertThat(ResponseType.of("code")).isEqualTo(ResponseType.CODE);
		assertThat(ResponseType.of("CODE")).isEqualTo(ResponseType.CODE);
		assertThat(ResponseType.of("token")).isEqualTo(ResponseType.TOKEN);
		assertThat(ResponseType.of("TOKEN")).isEqualTo(ResponseType.TOKEN);
		assertThat(ResponseType.of("id_token")).isEqualTo(ResponseType.ID_TOKEN);
		assertThat(ResponseType.of("ID_TOKEN")).isEqualTo(ResponseType.ID_TOKEN);
	}

}
