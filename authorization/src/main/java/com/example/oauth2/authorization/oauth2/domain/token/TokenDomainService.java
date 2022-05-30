package com.example.oauth2.authorization.oauth2.domain.token;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import com.example.oauth2.authorization.oauth2.domain.authorize.AuthorizeRequest;
import com.example.oauth2.authorization.oauth2.domain.authorize.AuthorizeRequestRepository;
import com.example.oauth2.authorization.oauth2.domain.token.generator.AccessTokenGenerator;
import com.example.oauth2.authorization.oauth2.domain.token.value.TokenType;
import com.example.oauth2.authorization.oauth2.exception.OAuth2TokenException;
import com.example.oauth2.authorization.oauth2.exception.ValidateException;
import com.example.oauth2.authorization.oauth2.value.Message;
import com.example.oauth2.authorization.oauth2.value.Scope;
import com.example.oauth2.authorization.oauth2.web.token.TokenEndpointRequest;

@Component
public class TokenDomainService {

	private final AuthorizeRequestRepository authReqRepository;
	
	private final TokenRepository tokenRepository;
	
	private final AccessTokenGenerator accessTokenGenerator;
	
	public TokenDomainService(
			AuthorizeRequestRepository authReqRepository,
			TokenRepository tokenRepository,
			AccessTokenGenerator accessTokenGenerator) {
		this.authReqRepository = authReqRepository;
		this.tokenRepository = tokenRepository;
		this.accessTokenGenerator = accessTokenGenerator;
	}
	
	public void authorizationCodeClientCheck(TokenEndpointRequest req) throws OAuth2TokenException {
		if (req.getGrantType() == null) {
			throw new OAuth2TokenException(HttpStatus.BAD_REQUEST, Message.MSG1001.resolveMessage("grant_type"));
		}
		if (!StringUtils.hasLength(req.getCode())) {
			throw new OAuth2TokenException(HttpStatus.BAD_REQUEST, Message.MSG1001.resolveMessage("code"));
		}		

		Optional<AuthorizeRequest> optAuthReq = authReqRepository.get(req.getCode());
		if (optAuthReq.isEmpty()) {
			throw new OAuth2TokenException(HttpStatus.BAD_REQUEST , Message.MSG1010.resolveMessage());
		}
		
		AuthorizeRequest authReq = optAuthReq.get();
		
		//認可エンドポイントのリクエストにredirect_uriが踏まれていた場合、チェック
		if (authReq.hasRedirectUri()) {
			try {
				authReq.equalsRedirectUri(req.getRedirectUri());
			} catch (ValidateException e) {
				throw new OAuth2TokenException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
		}

		//認可エンドポイントのリクエストにcode_challengeが踏まれていた場合、チェック
		if (authReq.hasCodeChallenge()) {
			if (!authReq.verifyCodeChallenge(req.getCodeVerifier())) {
				throw new OAuth2TokenException(HttpStatus.BAD_REQUEST, Message.MSG1010.resolveMessage());
			}
		}

		//急いで削除する（トランザクション制御されていたら意味ないのでは？）
		this.authReqRepository.remove(authReq);
		if (authReq.checkClientId(req.getClientId())) {
			return;
		} else {
			//:TODO
			throw new OAuth2TokenException(HttpStatus.BAD_REQUEST, "invalid_grant");			
		}
	}
	
	public String generateAccessToken(String clientId, UserDetails user, Optional<Scope> optScope) {
		String accessTokenStr = this.accessTokenGenerator.generate(user);

		Scope scope = null;
		if (optScope.isPresent()) {
			scope = optScope.get();
		}
		Token accessToken = Token.of(
				accessTokenStr, 
				TokenType.ACCESS_TOKEN, 
				scope, 
				user.getUsername());
		
		this.tokenRepository.save(accessToken);

		return accessTokenStr;
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
				user.getUsername());
		
		this.tokenRepository.save(refreshToken);
		
		return refreshTokenStr;
	}
}
