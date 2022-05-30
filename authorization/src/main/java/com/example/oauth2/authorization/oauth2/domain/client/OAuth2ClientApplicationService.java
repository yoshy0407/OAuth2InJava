package com.example.oauth2.authorization.oauth2.domain.client;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.example.oauth2.authorization.oauth2.exception.OAuth2ClientException;
import com.example.oauth2.authorization.oauth2.value.Scope;

@Service
public class OAuth2ClientApplicationService {

	private final OAuth2ClientRepository clientRepository;

	private final PasswordEncoder passwordEncoder;

	public OAuth2ClientApplicationService(OAuth2ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
		this.clientRepository = clientRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public void checkClient(String clientId, URI redirectUri, Scope scope) throws OAuth2ClientException {
		Optional<OAuth2Client> optClient = this.clientRepository.get(clientId);
		if (optClient.isPresent()) {
			OAuth2Client client = optClient.get();
			if (client.containsRedirectUri(redirectUri)) {
				if (client.containScope(scope)) {
					return;					
				} else {
					throw new OAuth2ClientException(HttpStatus.BAD_REQUEST, "invalid_scope");
				}
			} else {
				throw new OAuth2ClientException(HttpStatus.BAD_REQUEST, "Invalid redirect URI");
			}			
		} else {
			throw new OAuth2ClientException(HttpStatus.BAD_REQUEST, "Unknown client");
		}
	}

	public void authenticateBasic(String authorization) throws OAuth2ClientException {
		byte[] base64Token = authorization.substring(6).getBytes(StandardCharsets.UTF_8);
		byte[] decoded = Base64Utils.decode(base64Token);
		String token = new String(decoded, StandardCharsets.UTF_8);
		int delim = token.indexOf(":");
		if (delim == -1) {
			throw new IllegalStateException("Invalid basic token");
		}
		authenticate(token.substring(0, delim), token.substring(delim + 1));
	}

	public void authenticate(String clientId, String clientSecret) throws OAuth2ClientException {
		Optional<OAuth2Client> client = this.clientRepository.get(clientId);
		if (client.isEmpty()) {
			throw new OAuth2ClientException(HttpStatus.UNAUTHORIZED, "Invalid_client");
		}
		if (!client.get().matchSecret(clientSecret, passwordEncoder)) {
			throw new OAuth2ClientException(HttpStatus.UNAUTHORIZED, "Invalid_client");
		}
	}
	
	public void checkScope(String clientId, Scope scope) throws OAuth2ClientException {
		Optional<OAuth2Client> optClient = this.clientRepository.get(clientId);
		if (optClient.isPresent()) {
			OAuth2Client client = optClient.get();
			if (!client.containScope(scope)) {
				throw new OAuth2ClientException(HttpStatus.BAD_REQUEST, "Unknown client");				
			}	
		} else {
			throw new OAuth2ClientException(HttpStatus.BAD_REQUEST, "Unknown client");
		}
	}

}
