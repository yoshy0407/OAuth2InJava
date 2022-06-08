package com.example.oauth2.authorization.oauth2.domain.client;

import java.net.URI;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.example.oauth2.authorization.oauth2.domain.client.value.ClientAuthMethod;
import com.example.oauth2.authorization.oauth2.domain.client.value.ClientType;
import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.value.Scope;

import lombok.Getter;
import lombok.ToString;

/**
 * OAuthのクライアントを表すエンティティクラスです
 * @author yoshiokahiroshi
 *
 */
@ToString
public class OAuth2Client {

	@Getter
	private String clientId;
	
	private String clientName;
	
	private ClientType clientType;
	
	private URI clientUri;
	
	private URI logoUri;
	
	private String encodedClientSecret;
	
	private List<URI> redirectUris;
	
	private Scope scope;

	private ClientAuthMethod authMethod;
	
	private List<GrantType> grantTypes;
	
	public boolean isClient() {
		return clientType.equals(ClientType.AUTH_CLIENT);
	}

	public boolean isResource() {
		return clientType.equals(ClientType.RESOURCE);
	}

	public boolean matchSecret(String rawSecret, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(rawSecret, this.encodedClientSecret);
	}
	
	public boolean verifyRedirectUri(URI redirectUri) {
		return this.redirectUris.contains(redirectUri);
	}
	
	public boolean containScope(Scope scope) {
		return this.scope.contains(scope);
	}
	
	public boolean verifyGrantType(GrantType grantType) {
		return this.grantTypes.contains(grantType);
	}
	
	public boolean equalAuthMethod(ClientAuthMethod authMethod ) {
		return this.authMethod.equals(authMethod);
	}

	public static OAuth2Client of(
			String clientId,
			String clientName,
			ClientType clientType,
			URI clientUri,
			URI logoUri,
			String encodedClientSecret,
			List<URI> redirectUris,
			Scope scope,
			ClientAuthMethod authMethod,
			List<GrantType> grantTypes) throws IllegalArgumentException {
		OAuth2Client client = new OAuth2Client();
		if (!StringUtils.hasLength(clientId)) {
			throw new IllegalArgumentException("client_id");
		}
		client.clientId = clientId;
		client.clientName = clientName;
		Assert.notNull(clientType, "client_type");
		client.clientType = clientType;
		client.clientUri = clientUri;
		client.logoUri = logoUri;
		if (!StringUtils.hasLength(clientId)) {
			throw new IllegalArgumentException("client_secret");
		}
		client.encodedClientSecret = encodedClientSecret;
		Assert.notEmpty(redirectUris, "redirect_uris");
		client.redirectUris = redirectUris;
		client.scope = scope;
		Assert.notNull(authMethod, "auth_method");
		client.authMethod = authMethod;
		Assert.notEmpty(grantTypes, "grant_types");
		client.grantTypes  = grantTypes;
		return client;
	}
	
	public static OAuth2Client createClient(
			String clientId,
			String clientName,
			URI clientUri,
			URI logoUri,
			String rawClientSecret,
			List<URI> redirectUris,
			Scope scope,
			ClientAuthMethod authMethod,
			List<GrantType> grantTypes,
			PasswordEncoder passwordEncoder) throws IllegalArgumentException {
		return of(clientId, clientName, ClientType.AUTH_CLIENT, 
				clientUri, logoUri, passwordEncoder.encode(rawClientSecret), redirectUris, scope, authMethod, grantTypes);
	}

	public static OAuth2Client createResource(
			String clientId,
			String clientName,
			URI clientUri,
			URI logoUri,
			String rawClientSecret,
			List<URI> redirectUris,
			Scope scope,
			ClientAuthMethod authMethod,
			List<GrantType> grantTypes,
			PasswordEncoder passwordEncoder) throws IllegalArgumentException {
		return of(clientId, clientName, ClientType.RESOURCE, 
				clientUri, logoUri, passwordEncoder.encode(rawClientSecret), redirectUris, scope, authMethod, grantTypes);
	}
}
