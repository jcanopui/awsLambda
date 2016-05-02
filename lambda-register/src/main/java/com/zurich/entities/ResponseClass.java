package com.zurich.entities;

public class ResponseClass {

	String subscriptionArn;
	
	public String getSubscriptionArn() {
		return subscriptionArn;
	}
	
	public void setSubcriptionArn(String subscriptionArn) {
		this.subscriptionArn = subscriptionArn;
	}
	
	public ResponseClass(String subscriptionArn) {
		this.subscriptionArn = subscriptionArn;
	}
	
	public ResponseClass() {
	}
}
