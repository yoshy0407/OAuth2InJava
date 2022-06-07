package com.example.oauth2.authorization.oauth2.domain.authorize.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CodeChallengeMethodTest {

	@Test
	void testToString() {
		assertThat(CodeChallengeMethod.PLAIN.toString()).isEqualTo("plain");
		assertThat(CodeChallengeMethod.S256.toString()).isEqualTo("S256");
	}

	@Test
	void testOf() {
		CodeChallengeMethod plain = CodeChallengeMethod.of("PLAIN");
		assertThat(plain).isEqualTo(CodeChallengeMethod.PLAIN);

		CodeChallengeMethod plain2 = CodeChallengeMethod.of("plain");
		assertThat(plain2).isEqualTo(CodeChallengeMethod.PLAIN);

		CodeChallengeMethod s256 = CodeChallengeMethod.of("s256");
		assertThat(s256).isEqualTo(CodeChallengeMethod.S256);

		CodeChallengeMethod s256_2 = CodeChallengeMethod.of("S256");
		assertThat(s256_2).isEqualTo(CodeChallengeMethod.S256);
}

}
