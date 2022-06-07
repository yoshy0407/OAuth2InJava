package com.example.oauth2.authorization.oauth2.web.token;

import java.beans.ConstructorProperties;
import java.net.URI;
import java.util.Optional;

import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.value.Scope;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TokenEndpointRequest {

	@ConstructorProperties({
		"client_id",
		"client_secret",
		"grant_type",
		"code",
		"redirect_uri",
		"code_verifier",
		"username",
		"password",
		"refresh_token",
		"scope"
	})
	public TokenEndpointRequest(
			String clientId,
			String clientSecret,
			GrantType grantType,
			String code,
			URI redirectUri,
			String codeVerifier,
			String username,
			String password,
			String refreshToken,
			Optional<Scope> scope) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.grantType = grantType;
		this.code = code;
		this.redirectUri = redirectUri;
		this.codeVerifier = codeVerifier;
		this.username = username;
		this.password = password;
		this.refreshToken = refreshToken;
		this.scope = scope;		
	}
	
	//クライアントの認証をボディで実施する場合
	private String clientId;

	//クライアントの認証をボディで実施する場合
	private String clientSecret;

	//必須
	//リソースオーナー・パスワード・クレデンシャルズフロー 必須
	//クライアントクレデンシャルズフロー 必須
	//リフレッシュトークンフロー 必須
	private GrantType grantType;
	
	// 必須 認可エンドポイントのレスポンスに含まれる値を指定
	private String code;
	
	// 認可リクエストに redirect_uri が含まれていれば必須
	private URI redirectUri;
	
	// 認可リクエストに code_challenge が含まれていれば必須
	private String codeVerifier;
	
	//リソースオーナー・パスワード・クレデンシャルズフロー 必須
	private String username;
	
	//リソースオーナー・パスワード・クレデンシャルズフロー 必須
	private String password;
	
	//リフレッシュトークンフロー 必須
	private String refreshToken;
	
	//リソースオーナー・パスワード・クレデンシャルズフロー 任意
	//クライアントクレデンシャルズフロー 任意
	//リフレッシュトークンフロー 任意
	private Optional<Scope> scope;

	
}
