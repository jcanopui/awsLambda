package com.everis.push.services.entities;


public class DBRecord {	
	
//	@SerializedName("NewImage")
	private NotificationEntity notification;

	/**
	 * @return the notification
	 */
	public NotificationEntity getNotification() {
		return notification;
	}

	/**
	 * @param notification the notification to set
	 */
	public void setNotification(NotificationEntity notification) {
		this.notification = notification;
	}		

}
