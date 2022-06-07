package com.example.oauth2.authorization.oauth2.domain.authorize.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.oauth2.authorization.oauth2.domain.authorize.value.ResponseType;
import com.example.oauth2.authorization.oauth2.domain.token.TokenDomainService;
import com.example.oauth2.authorization.oauth2.domain.token.generator.AccessToken;
import com.example.oauth2.authorization.oauth2.value.Scope;
import com.example.oauth2.authorization.oauth2.web.authorize.AuthorizeEndpointRequest;

class TokenAuthorizationProcessorTest {

	TokenAuthorizationProcessor tokenAuthProcessor;
	
	TokenDomainService tokenDomainService;
	
	@BeforeEach
	void setup() {
		this.tokenDomainService = Mockito.mock(TokenDomainService.class);
		this.tokenAuthProcessor = new TokenAuthorizationProcessor(tokenDomainService);
	}
	@Test
	void testAuthorize() {
		UserDetails user = User.withUsername("test")
				.authorities(new SimpleGrantedAuthority("ROLE"))
				.password("password")
				.build();
		
		SecurityContextImpl sc = new SecurityContextImpl();
		sc.setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
		SecurityContextHolder.setContext(sc);
		
		AuthorizeEndpointRequest request = 
				new AuthorizeEndpointRequest(
						Lists.list(ResponseType.CODE), 
						"test-client", 
						URI.create("http://localhost/callback"), 
						Optional.of(new Scope("hoge fuga")), 
						"stateCode", 
						"code-challenge", 
						"plain");
		
		AccessToken accessToken = new AccessToken();
		accessToken.setAccessToken("test-token");
		accessToken.setTokenType("Bearer");
		accessToken.setTokenLifeTime(1800);
		when(tokenDomainService.generateAccessToken(eq("test-client"), any(), any())).thenReturn(accessToken);
		
		URI uri = tokenAuthProcessor.authorize(request);
		
		assertThat(uri).isEqualTo(URI.create("http://localhost/callback?access_token=test-token&token_type=Bearer&expires_in=1800&state=stateCode&scope=hoge%20fuga"));
	}

	@Test
	void testSupports() {
		assertThat(tokenAuthProcessor.supports(Lists.list(ResponseType.TOKEN))).isTrue();
		assertThat(tokenAuthProcessor.supports(Lists.list(ResponseType.NONE))).isFalse();
	}

}
