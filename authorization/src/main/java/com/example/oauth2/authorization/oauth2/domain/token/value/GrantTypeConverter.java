package com.example.oauth2.authorization.oauth2.domain.token.value;

import org.springframework.core.convert.converter.Converter;

public class GrantTypeConverter implements Converter<String, GrantType>{

	@Override
	public GrantType convert(String source) {
		return GrantType.of(source);
	}

}
