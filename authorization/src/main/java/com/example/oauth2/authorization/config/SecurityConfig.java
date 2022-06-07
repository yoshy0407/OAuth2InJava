package com.example.oauth2.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
			.antMatchers("/oauth2/**").permitAll();
		http.csrf().disable();
		return http.build();
	}
	
	@Bean
	public UserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {
		UserDetails user = User.withUsername("test")
				.authorities(new SimpleGrantedAuthority("ROLE"))
				.password("password")
				.passwordEncoder(password -> passwordEncoder.encode(password))
				.build();
		return new InMemoryUserDetailsManager(user);
	}
}
