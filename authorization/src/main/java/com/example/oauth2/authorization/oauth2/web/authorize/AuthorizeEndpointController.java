package com.example.oauth2.authorization.oauth2.web.authorize;

import java.net.URI;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import com.example.oauth2.authorization.oauth2.domain.authorize.AuthorizeErrorUriBuilder;
import com.example.oauth2.authorization.oauth2.domain.authorize.DefaultAuthorizeApplicationService;
import com.example.oauth2.authorization.oauth2.domain.client.OAuth2ClientDomainService;
import com.example.oauth2.authorization.oauth2.exception.OAuth2AuthorizationException;
import com.example.oauth2.authorization.oauth2.web.authorize.model.ClientInfo;

@Controller
public class AuthorizeEndpointController {

	private static final String REQ_ATTR = "authReq";

	private final DefaultAuthorizeApplicationService authorizeService;

	public AuthorizeEndpointController(
			OAuth2ClientDomainService clientService, 
			DefaultAuthorizeApplicationService authorizeService) {
		this.authorizeService = authorizeService;
	}

	@GetMapping("/oauth2/authorize")
	public ModelAndView authorize(AuthorizeEndpointRequest req, ModelAndView mav, HttpSession session) {

		try {
			this.authorizeService.checkClient(req);
		} catch (OAuth2AuthorizationException ex) {
			mav.addObject("error", ex.getMessage());
			mav.setViewName("error");
			return mav;			
		}

		session.setAttribute(REQ_ATTR, req);

		ClientInfo client = new ClientInfo();
		client.setClientId(req.getClientId());
		client.setClientUri(req.getRedirectUri());
		if (req.getScope().isPresent()) {
			client.setScopes(req.getScope().get().toList());			
		}
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

			URI redirectUri = this.authorizeService.authorize(req);
			mav.setViewName(String.format("redirect:%s", redirectUri));
			return mav;
		} else {
			mav.setViewName(buildErrorRedirectUri(req, "access_denied"));
			return mav;
		}
	}

	private String buildErrorRedirectUri(AuthorizeEndpointRequest req, String error) {
		AuthorizeErrorUriBuilder builder =AuthorizeErrorUriBuilder.accessDenied(req.getRedirectUri())
				.errorDescription("拒否されました。");
		if (StringUtils.hasLength(req.getState())) {
			builder.state(req.getState());
		}
		
		return String.format("redirect:%s", builder.toUriString());
	}
}
