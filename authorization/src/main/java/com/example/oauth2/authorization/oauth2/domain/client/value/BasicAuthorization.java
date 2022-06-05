package com.example.oauth2.authorization.oauth2.domain.client.value;

import java.nio.charset.StandardCharsets;

import org.springframework.util.Base64Utils;

public class BasicAuthorization {

	private final String username;
	
	private final String password;
	
	public BasicAuthorization(String basic) {
		byte[] base64Token = basic.substring(6).getBytes(StandardCharsets.UTF_8);
		byte[] decoded = Base64Utils.decode(base64Token);
		String token = new String(decoded, StandardCharsets.UTF_8);
		int delim = token.indexOf(":");
		if (delim == -1) {
			throw new IllegalStateException("Invalid basic token");
		}
		this.username = token.substring(0, delim);
		this.password = token.substring(delim + 1);	
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

}
