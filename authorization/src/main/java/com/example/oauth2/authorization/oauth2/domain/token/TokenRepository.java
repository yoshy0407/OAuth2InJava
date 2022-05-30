package com.example.oauth2.authorization.oauth2.domain.token;

import java.util.Optional;

public interface TokenRepository {

	public Optional<Token> get(String accessToken);

	public void save(Token token);

	public void update(Token token);

	public void remove(Token token);
}
