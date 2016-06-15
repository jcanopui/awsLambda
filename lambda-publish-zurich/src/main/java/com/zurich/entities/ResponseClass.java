package com.zurich.entities;

public class ResponseClass {
	
	String messageId;
	
	public String getMessageId() {
		return messageId;
	}
	
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	public ResponseClass(String messageId) {
		this.messageId = messageId;
	}
}
