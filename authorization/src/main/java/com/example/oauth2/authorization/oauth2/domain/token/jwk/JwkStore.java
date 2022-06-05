package com.example.oauth2.authorization.oauth2.domain.token.jwk;

import java.util.List;
import java.util.Optional;


/**
 * RSAの鍵を保存するインタフェースです
 * @author yoshiokahiroshi
 *
 */
public interface JwkStore {

	public Optional<Jwk> get(Optional<String> keyId);
	
	public List<Jwk> getAll();
	
	public void save(Jwk jwk);
}
