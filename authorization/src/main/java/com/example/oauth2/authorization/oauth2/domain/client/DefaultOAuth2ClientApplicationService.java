package com.example.oauth2.authorization.oauth2.domain.client;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import com.example.oauth2.authorization.oauth2.domain.client.spi.OAuth2ClientApplicationService;
import com.example.oauth2.authorization.oauth2.domain.client.spi.OAuth2ClientRepository;
import com.example.oauth2.authorization.oauth2.domain.client.value.BasicAuthorization;
import com.example.oauth2.authorization.oauth2.domain.client.value.ClientAuthMethod;
import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.exception.client.ClientRegistrationErrorCode;
import com.example.oauth2.authorization.oauth2.exception.client.ClientRegistrationException;
import com.example.oauth2.authorization.oauth2.exception.token.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.exception.token.TokenErrorCode;
import com.example.oauth2.authorization.oauth2.value.Scope;

@Service
public class DefaultOAuth2ClientApplicationService implements OAuth2ClientApplicationService {

	private final OAuth2ClientRepository clientRepository;

	private final PasswordEncoder passwordEncoder;

	private final MessageSource messageSource;

	public DefaultOAuth2ClientApplicationService(
			OAuth2ClientRepository clientRepository, 
			PasswordEncoder passwordEncoder,
			MessageSource messageSource) {
		this.clientRepository = clientRepository;
		this.passwordEncoder = passwordEncoder;
		this.messageSource = messageSource;
	}

	@Override
	public RegisterResult register(
			String clientName,
			URI logoUri,
			List<URI> redirectUris,
			String tokenEndpointAuthMethod,
			URI clientUri,
			List<GrantType> grantTypes,
			Scope scope) throws ClientRegistrationException {
		OAuth2Client client = null;
		String clientId = UUID.randomUUID().toString();
		String clientSecret = Base64Utils.encodeToString(new byte[256]);
		ClientAuthMethod authMethod = ClientAuthMethod.of(tokenEndpointAuthMethod);
		try {
			client = OAuth2Client.createClient(clientId, clientName, clientUri, 
					logoUri, clientSecret, redirectUris, scope, authMethod, grantTypes, passwordEncoder);
		} catch(IllegalArgumentException ex) {
			if (ex.getMessage().equals("redirect_uris")) {
				throw new ClientRegistrationException(ClientRegistrationErrorCode.INVALID_REDIRECT_URI, 
						this.messageSource, ex.getMessage());				

			} else {
				throw new ClientRegistrationException(ClientRegistrationErrorCode.INVALID_CLIENT_METADATA, 
						this.messageSource, ex.getMessage());				
			}
		}
		this.clientRepository.save(client);
		return new RegisterResult(clientId, clientSecret, System.currentTimeMillis(), 0);
	}
	
	@Override
	public void authenticate(Optional<String> authorization, String bodyClientId, String bodyClientSecret) throws OAuth2TokenException {
		ClientAuthMethod authMethod = resolveAuthMethod(authorization, bodyClientId, bodyClientSecret);

		if (authMethod.equals(ClientAuthMethod.CLIENT_SECRET_BASIC)) {
			BasicAuthorization basicAuth = new BasicAuthorization(authorization.get());
			doAuthenticate(basicAuth.getUsername(), basicAuth.getPassword(), authMethod);
			
		} else if (authMethod.equals(ClientAuthMethod.CLIENT_SECRET_POST)) {
			doAuthenticate(bodyClientId, bodyClientSecret, authMethod);
			
		} else {
			throw new OAuth2TokenException(TokenErrorCode.INVALID_REQUEST, messageSource);
		
		}
	}
	
	private ClientAuthMethod resolveAuthMethod(Optional<String> authorization, String bodyClientId, String bodyClientSecret) throws OAuth2TokenException {
		if (authorization.isPresent()) {
			return ClientAuthMethod.CLIENT_SECRET_BASIC;
		} else if (StringUtils.hasLength(bodyClientId)
				&& StringUtils.hasLength(bodyClientSecret)) {
			return ClientAuthMethod.CLIENT_SECRET_POST;
		} else {
			throw new OAuth2TokenException(TokenErrorCode.INVALID_REQUEST, messageSource);
		}
			
	}

	private void doAuthenticate(String clientId, String clientSecret, ClientAuthMethod authMethod) throws OAuth2TokenException {
		Optional<OAuth2Client> client = this.clientRepository.get(clientId);
		if (client.isEmpty()) {
			throw new OAuth2TokenException(TokenErrorCode.INVALID_CLIENT, messageSource);
		}
		if (!client.get().equalAuthMethod(authMethod)) {
			throw new OAuth2TokenException(TokenErrorCode.INVALID_CLIENT, messageSource);
		}
		if (!client.get().matchSecret(clientSecret, passwordEncoder)) {
			throw new OAuth2TokenException(TokenErrorCode.INVALID_CLIENT, messageSource);
		}
		return;
	}

}
