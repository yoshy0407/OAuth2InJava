package com.example.oauth2.authorization.oauth2.constant;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.example.oauth2.authorization.oauth2.value.Scope;

class ScopeTest {

	@Test
	void testToString() {
		Scope scope = new Scope("foo bar hoge");
		assertThat(scope.toString()).isEqualTo("foo bar hoge");
	}

	@Test
	void testContains() {
		Scope scope1 = new Scope("foo bar");
		Scope scope2 = new Scope("foo bar hoge");
		
		assertThat(scope2.contains(scope1)).isTrue();
		
		assertThat(scope1.contains(scope2)).isFalse();
		
		Scope scope3 = new Scope("test");
		
		assertThat(scope2.contains(scope3)).isFalse();
	}
}
