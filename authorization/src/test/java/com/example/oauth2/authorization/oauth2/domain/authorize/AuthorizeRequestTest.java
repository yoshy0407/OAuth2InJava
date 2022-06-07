package com.example.oauth2.authorization.oauth2.domain.authorize;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;

import com.example.oauth2.authorization.oauth2.domain.authorize.value.CodeChallengeMethod;
import com.example.oauth2.authorization.oauth2.domain.authorize.value.ResponseType;
import com.example.oauth2.authorization.oauth2.exception.ValidateException;
import com.example.oauth2.authorization.oauth2.value.Scope;

class AuthorizeRequestTest {

	AuthorizeRequest request = AuthorizeRequest.of(
			"code", 
			Lists.list(ResponseType.CODE, ResponseType.TOKEN), 
			URI.create("http://localhost/callback"), 
			"client-id", 
			new Scope("hoge fuga"), "state");

	@Test
	void testCheckClientId() {
		assertThat(request.checkClientId("client-id")).isTrue();
		assertThat(request.checkClientId("client-id-test")).isFalse();
	}

	@Test
	void testHasRedirectUri() {
		assertThat(request.hasRedirectUri()).isTrue();

		AuthorizeRequest request2 = AuthorizeRequest.of(
				"code", 
				Lists.list(ResponseType.CODE, ResponseType.TOKEN), 
				null, 
				"client-id", 
				new Scope("hoge fuga"), "state");

		assertThat(request2.hasRedirectUri()).isFalse();
}

	@Test
	void testEqualsRedirectUri() throws ValidateException {
		assertThat(request.equalsRedirectUri(URI.create("http://localhost/callback"))).isTrue();
		assertThat(request.equalsRedirectUri(URI.create("http://localhost/callback/hello"))).isFalse();
	}

	@Test
	void testHasCodeChallenge() {
		assertThat(request.hasCodeChallenge()).isFalse();
		request.setCodeChallenges("challenge_code", CodeChallengeMethod.S256);
		assertThat(request.hasCodeChallenge()).isTrue();
	}

	@Test
	void testVerifyCodeChallenge() throws NoSuchAlgorithmException {
		AuthorizeRequest request2 = AuthorizeRequest.of(
				"code", 
				Lists.list(ResponseType.CODE, ResponseType.TOKEN), 
				URI.create("http://localhost/callback"), 
				"client-id", 
				new Scope("hoge fuga"), "state");
		
		request2.setCodeChallenges("code", CodeChallengeMethod.PLAIN);
		assertThat(request2.verifyCodeChallenge("code")).isTrue();
		assertThat(request2.verifyCodeChallenge("code1")).isFalse();

		String challenge = Base64Utils.encodeToUrlSafeString(MessageDigest.getInstance("SHA-256").digest("code".getBytes()));
		request2.setCodeChallenges(challenge, CodeChallengeMethod.S256);
		assertThat(request2.verifyCodeChallenge("code")).isTrue();
		assertThat(request2.verifyCodeChallenge("code1")).isFalse();
	
	}

	@Test
	void testOf() {
		AuthorizeRequest request = AuthorizeRequest.of(
				"code", 
				Lists.list(ResponseType.CODE, ResponseType.TOKEN), 
				URI.create("http://localhost/callback"), 
				"client-id", 
				new Scope("hoge fuga"), "state");
		assertThat(request.getCode()).isEqualTo("code");
	}

}
