package com.example.oauth2.authorization.oauth2.web.introspect;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenIntrospectController {

	@PostMapping("/oauth2/introspect")
	public String introspect() {
		return "";
	}
}
