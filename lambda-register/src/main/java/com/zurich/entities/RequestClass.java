package com.zurich.entities;

public class RequestClass {

	String platform;
	
	String token;
	
	String identifier;
	
	public String getPlatform() {
		return platform;
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public RequestClass(String platform, String token, String identifier) {
		this.platform = platform;
		this.token = token;
		this.identifier = identifier;
	}
	
	public RequestClass() {
	}
}
