package com.zurich.entities;

public class RequestClass {
	
	String message;
	
	String topicName;
	
	boolean isTopic;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public boolean isTopic() {
		return isTopic;
	}

	public void setTopic(boolean isTopic) {
		this.isTopic = isTopic;
	}
	
	public RequestClass(String message, String topicName, boolean isTopic) {
		this.message = message;
		this.topicName = topicName;
		this.isTopic = isTopic;
	}
	
	public RequestClass(){}
}
