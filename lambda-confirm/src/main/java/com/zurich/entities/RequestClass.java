package com.zurich.entities;

public class RequestClass {
	
	long notificationId;
	
	public long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	public RequestClass(long notificationId) {
		this.notificationId = notificationId;
	}

	public RequestClass() {}
}
