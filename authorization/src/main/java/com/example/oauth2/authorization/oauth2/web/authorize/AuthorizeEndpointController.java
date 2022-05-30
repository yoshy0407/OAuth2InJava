package com.example.oauth2.authorization.oauth2.web.authorize;

import java.net.URI;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oauth2.authorization.oauth2.domain.authorize.AuthorizeEndpointService;
import com.example.oauth2.authorization.oauth2.domain.client.OAuth2ClientApplicationService;
import com.example.oauth2.authorization.oauth2.exception.OAuth2AuthorizationException;
import com.example.oauth2.authorization.oauth2.exception.OAuth2ClientException;
import com.example.oauth2.authorization.oauth2.web.authorize.model.ClientInfo;

@Controller
public class AuthorizeEndpointController {

	private static final String REQ_ATTR = "authReq";

	private final OAuth2ClientApplicationService clientService;

	private final AuthorizeEndpointService authorizeService;

	public AuthorizeEndpointController(
			OAuth2ClientApplicationService clientService, 
			AuthorizeEndpointService authorizeService) {
		this.clientService = clientService;
		this.authorizeService = authorizeService;
	}

	@GetMapping("/oauth2/authorize")
	public ModelAndView authorize(AuthorizeEndpointRequest req, ModelAndView mav, HttpSession session) {

		try {
			this.clientService.checkClient(req.getClientId(), req.getRedirectUri(), req.getScope());
		} catch (OAuth2ClientException ex) {
			mav.addObject("error", ex.getMessage());
			mav.setViewName("error");
			return mav;			
		}

		session.setAttribute(REQ_ATTR, req);

		ClientInfo client = new ClientInfo();
		client.setClientId(req.getClientId());
		client.setClientUri(req.getRedirectUri());
		client.setScopes(req.getScope().toList());
		mav.addObject("client", client);

		mav.setViewName("approve");
		return mav;
	}

	//scopeの確認の実装をすべき
	@PostMapping("/oauth2/approve")
	public ModelAndView approval(
			AuthorizeApprove model, 
			ModelAndView mav, 
			HttpSession session) {
		AuthorizeEndpointRequest req = (AuthorizeEndpointRequest) session.getAttribute(REQ_ATTR);
		session.removeAttribute(REQ_ATTR);

		if (req == null ) {
			mav.addObject("error", "no matching authorization request");
			mav.setViewName("error");
			return mav;						
		}

		if (model.getApprove().equalsIgnoreCase("Approve")) {

			URI redirectUri = null;
			try {
				this.authorizeService.authorize(req, model.getScope());
				mav.setViewName(String.format("redirect:%s", redirectUri));
				return mav;
			} catch (OAuth2AuthorizationException ex) {
				mav.setViewName(buildErrorRedirectUri(req, ex.getMessage()));
				return mav;
			}
		} else {
			mav.setViewName(buildErrorRedirectUri(req, "access_denied"));
			return mav;
		}
	}

	private String buildErrorRedirectUri(AuthorizeEndpointRequest req, String error) {
		String retUri = UriComponentsBuilder.fromUri(req.getRedirectUri())
				.queryParam("error", "access_denied")
				.toUriString();
		return String.format("redirect:%s", retUri);
	}
}
