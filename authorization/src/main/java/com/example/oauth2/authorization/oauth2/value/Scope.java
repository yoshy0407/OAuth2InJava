package com.example.oauth2.authorization.oauth2.value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scope {

	private final List<String> scopeList;
	
	public Scope(String scope) {
		this.scopeList = Arrays.asList(scope.split(" "));
	}
	
	private Scope(List<String> scope) {
		this.scopeList = scope;
	}
	
	public boolean contains(Scope scope) {
		return contains(scope.toList());
	}
	
	public boolean contains(List<String> scope) {
		boolean result = true;
		for (String str : scope) {
			if (!scopeList.contains(str)) {
				result = false;
			}
		}
		return result;
		
	}
		
	public List<String> toList(){
		List<String> list = new ArrayList<>();
		for (String str : this.scopeList) {
			list.add(str);
		}
		return list;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String str : this.scopeList) {
			sb.append(str);
			sb.append(" ");
		}
		return sb.toString().trim();
	}
	
	public static Scope fromList(List<String> scope) {
		return new Scope(scope);
	}
}
