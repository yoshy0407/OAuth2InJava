package com.example.oauth2.authorization.oauth2.domain.authorize.value;

import org.springframework.core.convert.converter.Converter;

public class ResponseTypeConverter implements Converter<String, ResponseType> {

	@Override
	public ResponseType convert(String source) {
		return ResponseType.of(source);
	}

}
