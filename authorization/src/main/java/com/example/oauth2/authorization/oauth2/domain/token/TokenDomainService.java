package com.example.oauth2.authorization.oauth2.domain.token;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import com.example.oauth2.authorization.oauth2.domain.authorize.AuthorizeRequest;
import com.example.oauth2.authorization.oauth2.domain.authorize.AuthorizeRequestRepository;
import com.example.oauth2.authorization.oauth2.domain.token.generator.AccessToken;
import com.example.oauth2.authorization.oauth2.domain.token.generator.AccessTokenGenerator;
import com.example.oauth2.authorization.oauth2.domain.token.value.TokenType;
import com.example.oauth2.authorization.oauth2.exception.ValidateException;
import com.example.oauth2.authorization.oauth2.exception.token.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.exception.token.TokenErrorCode;
import com.example.oauth2.authorization.oauth2.value.Scope;
import com.example.oauth2.authorization.oauth2.web.token.TokenEndpointRequest;

@Component
public class TokenDomainService {

	private final AuthorizeRequestRepository authReqRepository;
	
	private final TokenRepository tokenRepository;
	
	private final AccessTokenGenerator accessTokenGenerator;
	
	private final MessageSource messageSource;
	
	public TokenDomainService(
			AuthorizeRequestRepository authReqRepository,
			TokenRepository tokenRepository,
			AccessTokenGenerator accessTokenGenerator,
			MessageSource messageSource) {
		this.authReqRepository = authReqRepository;
		this.tokenRepository = tokenRepository;
		this.accessTokenGenerator = accessTokenGenerator;
		this.messageSource = messageSource;
	}
	
	public void authorizationCodeClientCheck(TokenEndpointRequest req) throws OAuth2TokenException {

		Optional<AuthorizeRequest> optAuthReq = authReqRepository.get(req.getCode());
		if (optAuthReq.isEmpty()) {
			throw new OAuth2TokenException(TokenErrorCode.INVALID_GRANT, this.messageSource);
		}
		
		AuthorizeRequest authReq = optAuthReq.get();
		
		//認可エンドポイントのリクエストにredirect_uriが踏まれていた場合、チェック
		if (authReq.hasRedirectUri()) {
			try {
				authReq.equalsRedirectUri(req.getRedirectUri());
			} catch (ValidateException e) {
				throw new OAuth2TokenException(TokenErrorCode.INVALID_GRANT, this.messageSource);
			}
		}

		//認可エンドポイントのリクエストにcode_challengeが踏まれていた場合、チェック
		if (authReq.hasCodeChallenge()) {
			if (!authReq.verifyCodeChallenge(req.getCodeVerifier())) {
				throw new OAuth2TokenException(TokenErrorCode.INVALID_GRANT, this.messageSource);
			}
		}

		//急いで削除する（トランザクション制御されていたら意味ないのでは？）
		this.authReqRepository.remove(authReq);
		if (authReq.checkClientId(req.getClientId())) {
			return;
		} else {
			//:TODO
			throw new OAuth2TokenException(TokenErrorCode.INVALID_GRANT, this.messageSource);
		}
	}
	
	public AccessToken generateAccessToken(String clientId, UserDetails user, Optional<Scope> optScope) {
		AccessToken accessToken = this.accessTokenGenerator.generate(user);

		Scope scope = null;
		if (optScope.isPresent()) {
			scope = optScope.get();
		}
		Token token = Token.of(
				accessToken.getAccessToken(), 
				TokenType.ACCESS_TOKEN, 
				scope, 
				user.getUsername(),
				LocalDateTime.now().plusSeconds(accessToken.getTokenLifeTime()));
		
		this.tokenRepository.save(token);

		return accessToken;
	}
	
	public String generateRefreshToken(String clientId, UserDetails user, Optional<Scope> optScope) {
		String refreshTokenStr = Base64Utils.encodeToString(UUID.randomUUID().toString().getBytes());
		
		Scope scope = null;
		if (optScope.isPresent()) {
			scope = optScope.get();
		}
		Token refreshToken = Token.of(
				refreshTokenStr, 
				TokenType.REFRESH_TOKEN, 
				scope, 
				user.getUsername(),
				null);
		
		this.tokenRepository.save(refreshToken);
		
		return refreshTokenStr;
	}
}
