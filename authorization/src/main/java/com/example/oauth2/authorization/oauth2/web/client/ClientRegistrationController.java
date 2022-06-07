package com.example.oauth2.authorization.oauth2.web.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth2.authorization.oauth2.domain.client.RegisterResult;
import com.example.oauth2.authorization.oauth2.domain.client.spi.OAuth2ClientApplicationService;
import com.example.oauth2.authorization.oauth2.exception.client.ClientRegistrationException;

@RestController
public class ClientRegistrationController {

	private final OAuth2ClientApplicationService clientService;
	
	public ClientRegistrationController(OAuth2ClientApplicationService clientService) {
		this.clientService = clientService;
	}
	
	@PostMapping("/oauth2/client")
	@ResponseStatus(HttpStatus.CREATED)
	public ClientRegistrationResponse registerClient(ClientRegisterRequest req) throws ClientRegistrationException {
		RegisterResult result = clientService.register(
				req.getClientName(), 
				req.getLogoUri(), 
				req.getRedirectUris(), 
				req.getTokenEndpointAuthMethod(), req.getClientUri(), req.getGrantTypes(), req.getScope());
		ClientRegistrationResponse res = new ClientRegistrationResponse();
		res.setClientId(result.getClientId());
		res.setClientSecret(result.getClientSecret());
		res.setClientIdIssuedAt(res.getClientIdIssuedAt());
		res.setClientSecretExpiresAt(result.getClientSecretExpiresAt());
		res.setClientName(req.getClientName());
		res.setLogoUri(req.getLogoUri());
		res.setRedirectUris(req.getRedirectUris());
		res.setTokenEndpointAuthMethod(req.getTokenEndpointAuthMethod());
		res.setClientUri(req.getClientUri());
		res.setGrantTypes(req.getGrantTypes());
		res.setScope(req.getScope());
		return res;
	}
 	
	@ExceptionHandler({ClientRegistrationException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ClientRegistrationErrorResponse handleError(ClientRegistrationException ex) {
		ClientRegistrationErrorResponse res = 
				new ClientRegistrationErrorResponse(ex);
		return res;
	}
}
