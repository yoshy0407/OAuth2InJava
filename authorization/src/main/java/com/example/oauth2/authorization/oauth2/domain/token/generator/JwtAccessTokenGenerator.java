package com.example.oauth2.authorization.oauth2.domain.token.generator;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;

public abstract class JwtAccessTokenGenerator implements AccessTokenGenerator {

	private final Algorithm algorithm;
	
	private final String issuer;
	
	
	
	public JwtAccessTokenGenerator(Algorithm algorithm, String issuer) {
		this.algorithm = algorithm;
		this.issuer = issuer;
	}
	
	@Override
	public String generate(UserDetails userDetails) {
		Builder jwtBuilder = JWT.create();
		//発行者 アプリケーション名やドメイン名が入る
		jwtBuilder.withIssuer("");
		//JWTの用途を設定 認可サーバでユニークである必要があるので、ユーザIDを設定する
		jwtBuilder.withSubject("");
		//想定利用者の名前かURI
		jwtBuilder.withAudience("");
		//JWTが失効する時間を設定する
		jwtBuilder.withExpiresAt(null);
		//JWTが有効になる日時
		jwtBuilder.withNotBefore(null);
		//JWTが発行された日をあらわします
		jwtBuilder.withIssuedAt(null);		
		//JWT ID 
		jwtBuilder.withJWTId(UUID.randomUUID().toString());
		return null;
	}
	
	protected abstract Map<String, Object> getClaims(UserDetails userDetails);

}
