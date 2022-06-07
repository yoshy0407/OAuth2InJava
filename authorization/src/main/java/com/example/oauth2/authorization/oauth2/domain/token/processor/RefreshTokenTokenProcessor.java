package com.example.oauth2.authorization.oauth2.domain.token.processor;

import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import com.example.oauth2.authorization.oauth2.domain.token.Token;
import com.example.oauth2.authorization.oauth2.domain.token.TokenDomainService;
import com.example.oauth2.authorization.oauth2.domain.token.generator.AccessToken;
import com.example.oauth2.authorization.oauth2.domain.token.spi.TokenRepository;
import com.example.oauth2.authorization.oauth2.domain.token.util.TokenResponseBuilder;
import com.example.oauth2.authorization.oauth2.domain.token.value.GrantType;
import com.example.oauth2.authorization.oauth2.exception.NoRollbackException;
import com.example.oauth2.authorization.oauth2.exception.token.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.exception.token.TokenErrorCode;
import com.example.oauth2.authorization.oauth2.web.token.TokenEndpointRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RefreshTokenTokenProcessor extends AbstractTokenProcessor  {

	private final TokenRepository tokenRepository;

	private final TokenDomainService tokenDomainService;
	
	private final UserDetailsManager userDetailsManager;
	
	private final ObjectMapper objectMapper;
	
	public RefreshTokenTokenProcessor(
			TokenRepository tokenRepository,
			TokenDomainService tokenDomainService,
			UserDetailsManager userDetailsManager,
			ObjectMapper objectMapper,
			MessageSource messageSource) {
		super(GrantType.REFRESH_TOKEN, messageSource);
		this.tokenRepository = tokenRepository;
		this.tokenDomainService = tokenDomainService;
		this.userDetailsManager = userDetailsManager;
		this.objectMapper = objectMapper;
	}

	@Override
	public JsonNode process(TokenEndpointRequest req) throws OAuth2TokenException, NoRollbackException {
		requestParamNotEmpty(req.getRefreshToken(), "refresh_token");
		
		Optional<Token> optToken = this.tokenRepository.get(req.getRefreshToken());

		if (optToken.isEmpty()) {
			throw new OAuth2TokenException(TokenErrorCode.INVALID_GRANT, getMessageSource());
		}

		Token token = optToken.get();

		//リフレッシュトークンは正しいのに、クライアントIDが間違っているのは、
		//リフレッシュトークンが漏洩したからと考えて、トークンの削除を行う
		if (!token.equalsClientId(req.getClientId())) {
			this.tokenRepository.remove(token);
			//これトランザクションで、ロールバックされないように
			throw new NoRollbackException(HttpStatus.BAD_REQUEST, "invalid_grant");
		}

		//このリフレッシュトークンに紐づくアクセストークンを無効化しなくて良いのか？
		//本では、無効化については記載されず、リフレッシュトークンを送ってくる時は、
		//アクセストークンが何かしら無効になっている前提で考えられている
		UserDetails userDetails = token.resolveTokenUser(userDetailsManager);
		AccessToken accessToken  = this.tokenDomainService.generateAccessToken(req.getClientId(), userDetails, Optional.empty());

		return TokenResponseBuilder.refreshToken(objectMapper)
				.accessToken(accessToken.getAccessToken())
				.tokenType(accessToken.getTokenType())
				.expiresIn(accessToken.getTokenLifeTime())
				.refreshToken(req.getRefreshToken())
				.build();
	}

}
