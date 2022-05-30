package com.example.oauth2.authorization.oauth2.domain.authorize;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import com.example.oauth2.authorization.oauth2.domain.authorize.value.CodeChallengeMethod;
import com.example.oauth2.authorization.oauth2.domain.authorize.value.ResponseType;
import com.example.oauth2.authorization.oauth2.exception.ValidateException;
import com.example.oauth2.authorization.oauth2.value.Message;
import com.example.oauth2.authorization.oauth2.value.Scope;

import lombok.Getter;
import lombok.ToString;

@ToString
public class AuthorizeRequest {

	@Getter
	private String code;
	
	private List<ResponseType> responseType;

	private URI redirectUri;
	
	private String clientId;
	
	private Scope scope;
	
	private String state;
	
	private String codeChallenge;
	
	private CodeChallengeMethod codeChallengeMethod;
	
	public boolean checkClientId(String clientId) {
		return this.clientId.equals(clientId);
	}
	
	public boolean hasRedirectUri() {
		return this.redirectUri != null;
	}
	
	public boolean equalsRedirectUri(URI redirectUri) throws ValidateException {
		if (redirectUri != null) {
			return this.redirectUri.equals(redirectUri);
		} else {
			throw new ValidateException(Message.MSG1002.resolveMessage("redirect_uri"));
		}
	}
	
	public void setCodeChallenges(String codeChallenge, CodeChallengeMethod method) {
		this.codeChallenge = codeChallenge;
		this.codeChallengeMethod = method;
	}
	
	public boolean hasCodeChallenge() {
		return StringUtils.hasLength(this.codeChallenge);
	}
	
	public boolean verifyCodeChallenge(String codeVerifier) {
		if (this.codeChallengeMethod.equals(CodeChallengeMethod.PLAIN)) {
			return this.codeChallenge.equals(codeVerifier);
		}
		if (this.codeChallengeMethod.equals(CodeChallengeMethod.S256)) {
			MessageDigest sha256 = null;
			try {
				sha256 = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("想定外のエラーです", e);
			}
			String computedValue = Base64Utils.encodeToUrlSafeString(sha256.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII)));
			return this.codeChallenge.equals(computedValue);
		}
		throw new IllegalStateException("CODE_CHALLENGE_METHODが不正です。");
	}
	
	public static AuthorizeRequest of(
			String code,
			List<ResponseType> responseType,
			URI redirectUri,
			String clientId,
			Scope scope,
			String state) {
		AuthorizeRequest req = new AuthorizeRequest();
		req.code = code;
		req.responseType = responseType;
		req.redirectUri = redirectUri;
		req.clientId = clientId;
		req.scope = scope;
		req.state = state;
		return req;		
	}
}
