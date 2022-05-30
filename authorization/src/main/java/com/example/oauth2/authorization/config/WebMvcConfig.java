package com.example.oauth2.authorization.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.oauth2.authorization.oauth2.domain.authorize.value.ResponseTypeConverter;
import com.example.oauth2.authorization.oauth2.domain.token.value.GrantTypeConverter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new ResponseTypeConverter());
		registry.addConverter(new GrantTypeConverter());
	}

}
