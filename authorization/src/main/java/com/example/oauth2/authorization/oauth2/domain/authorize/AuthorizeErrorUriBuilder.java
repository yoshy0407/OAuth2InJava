package com.example.oauth2.authorization.oauth2.domain.authorize;

import java.net.URI;

import org.springframework.web.util.UriComponentsBuilder;

public class AuthorizeErrorUriBuilder {

	private final UriComponentsBuilder builder;
	
	private AuthorizeErrorUriBuilder(URI redirectUri) {
		this.builder = UriComponentsBuilder.fromUri(redirectUri);
	}
	
	private AuthorizeErrorUriBuilder error(String errorCode) {
		this.builder.queryParam("error", errorCode);
		return this;
	}
	
	public AuthorizeErrorUriBuilder errorDescription(String msg) {
		this.builder.queryParam("error_description", msg);
		return this;
	}
	
	public AuthorizeErrorUriBuilder errorUri(URI uri) {
		this.builder.queryParam("error_uri", uri);
		return this;
	}

	public AuthorizeErrorUriBuilder errorUri(String uri) {
		this.builder.queryParam("error_uri", uri);
		return this;
	}
	
	public AuthorizeErrorUriBuilder state(String state) {
		this.builder.queryParam("state", state);
		return this;
	}
	
	public String toUriString() {
		return this.builder.toUriString();
	}
	
	public URI toUri() {
		return this.builder.build().toUri();
	}
	
	/**
	 * リクエストのパラメータが不正な場合
	 * @param redirectUri
	 * @return
	 */
	public static AuthorizeErrorUriBuilder invalidRequest(URI redirectUri) {
		AuthorizeErrorUriBuilder builder = new AuthorizeErrorUriBuilder(redirectUri);
		builder.error("invalid_request");
		return builder;
	}

	/**
	 * クライアントは現在の方法で認可コードを取得することを認可されていない
	 * @param redirectUri
	 * @return
	 */
	public static AuthorizeErrorUriBuilder unauthorizedClient(URI redirectUri) {
		AuthorizeErrorUriBuilder builder = new AuthorizeErrorUriBuilder(redirectUri);
		builder.error("unauthorized_client");
		return builder;
	}

	/**
	 * ソースオーナーまたは認可サーバーがリクエストを拒否した
	 * @param redirectUri
	 * @return
	 */
	public static AuthorizeErrorUriBuilder accessDenied(URI redirectUri) {
		AuthorizeErrorUriBuilder builder = new AuthorizeErrorUriBuilder(redirectUri);
		builder.error("access_denied");
		return builder;
	}

	/**
	 * 認可サーバーは現在の方法による認可コード取得をサポートしていない.
	 * @param redirectUri
	 * @return
	 */
	public static AuthorizeErrorUriBuilder unsupportedResponseType(URI redirectUri) {
		AuthorizeErrorUriBuilder builder = new AuthorizeErrorUriBuilder(redirectUri);
		builder.error("unsupported_response_type");
		return builder;
	}

	/**
	 * リクエストのパラメータが不正な場合
	 * @param redirectUri
	 * @return
	 */
	public static AuthorizeErrorUriBuilder invalidScope(URI redirectUri) {
		AuthorizeErrorUriBuilder builder = new AuthorizeErrorUriBuilder(redirectUri);
		builder.error("invalid_scope");
		return builder;
	}

	/**
	 * 認可サーバーがリクエストの処理ができないような予期しない状況に遭遇した
	 * (リダイレクトのため500が返せない)
	 * @param redirectUri
	 * @return
	 */
	public static AuthorizeErrorUriBuilder serverError(URI redirectUri) {
		AuthorizeErrorUriBuilder builder = new AuthorizeErrorUriBuilder(redirectUri);
		builder.error("server_error");
		return builder;
	}

	/**
	 * 認可サーバーが一時的な過負荷やメンテナンスによってリクエストを扱うことが出来ない
	 * (リダイレクトのため503が返せない)
	 * @param redirectUri
	 * @return
	 */
	public static AuthorizeErrorUriBuilder temporaryUnavailable(URI redirectUri) {
		AuthorizeErrorUriBuilder builder = new AuthorizeErrorUriBuilder(redirectUri);
		builder.error("temporary_unavailable");
		return builder;
	}
}
