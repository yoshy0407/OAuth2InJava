package com.example.oauth2.authorization.oauth2.domain.token.generator;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import com.nimbusds.jose.Payload;

public class JwtPayloadBuilder {

	private final Map<String, Object> map = new HashMap<>();
	
	private String issuer;
	
	private JwtPayloadBuilder() {}
	
	/**
	 * JWTの発行者を意味する、文字列かURLを設定します
	 * @param issuer
	 * @return
	 */
	public JwtPayloadBuilder issuer(String issuer) {
		map.put("iss", issuer);
		this.issuer = issuer;
		return this;
	}

	/**
	 * JWTの用途を表します。
	 * 文字列かURLを設定し、同じIssuerの中でユニークな値を設定します
	 * @param issuer
	 * @return
	 */
	public JwtPayloadBuilder subject(String issuer) {
		map.put("sub", issuer);
		return this;
	}

	/**
	 * JWTの想定利用者を意味します。
	 * 文字列かURIを設定します
	 * @param issuer
	 * @return
	 */
	public JwtPayloadBuilder audience(String...audiences) {
		map.put("aud", issuer);
		return this;
	}

	/**
	 * JWTが失効する日時のUNIXTimeを設定します
	 * @param issuer
	 * @return
	 */
	public JwtPayloadBuilder expireTime(LocalDateTime dateTime) {
		return expireTime(dateTime.atZone(ZoneId.systemDefault()).toEpochSecond());
	}

	/**
	 * JWTが失効する日時のUNIXTimeを設定します
	 * @param issuer
	 * @return
	 */
	public JwtPayloadBuilder expireTime(Long unixTime) {
		map.put("exp", unixTime);
		return this;
	}

	/**
	 * JWTが有効になる日付を指定します
	 * @param dateTime
	 * @return
	 */
	public JwtPayloadBuilder notBefore(LocalDateTime dateTime) {
		return notBefore(dateTime.atZone(ZoneId.systemDefault()).toEpochSecond());
	}

	/**
	 * JWTが有効になる日付を指定します
	 * @param unixTime
	 * @return
	 */
	public JwtPayloadBuilder notBefore(Long unixTime) {
		map.put("nbf", unixTime);
		return this;
	}

	/**
	 * JWTが発行された日時を意味します
	 * @param dateTime
	 * @return
	 */
	public JwtPayloadBuilder issueAt(LocalDateTime dateTime) {
		return issueAt(dateTime.atZone(ZoneId.systemDefault()).toEpochSecond());
	}

	/**
	 * JWTが発行された日時を意味します
	 * @param unixTime
	 * @return
	 */
	public JwtPayloadBuilder issueAt(Long unixTime) {
		map.put("iat", unixTime);
		return this;
	}
	
	/**
	 * JWTのユニーク性を担保するID値を意味します。
	 * @param jwtId
	 * @return
	 */
	public JwtPayloadBuilder jwtId(String jwtId) {
		map.put("jti", jwtId);
		return this;
	}
	
	public JwtPayloadBuilder setPrivateClaim(String property, Object value) {
		return setClaim(String.format("%s/claim-types/%s", this.issuer, property), value);
	}
	
	public JwtPayloadBuilder setClaim(String property, Object value) {
		map.put(property, value);
		return this;
	}
	
	public Payload toPayload() {
		return new Payload(map);
	}
	
	public static JwtPayloadBuilder create() {
		return new JwtPayloadBuilder();
	}
}
