package com.zurich.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "NOTIFICATIONS_STATUS")
public class NotificationStatusEntity {

	public final static int RECEIVED = 0;
	public final static int INVALID = 1;
	public final static int SEND = 2;
	public final static int ACK = 3;
	
	private long notificationId;
	
	private int notificationStatus;
	
	public NotificationStatusEntity(long notificationId, int notificationStatus) {
		super();
		this.notificationId = notificationId;
		this.notificationStatus = notificationStatus;
	}

	@DynamoDBHashKey
	public long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	/**
	 * @return the notification status
	 */
	public int getNotificationStatus() {
		return notificationStatus;
	}

	/**
	 * @param notificationStatus to set
	 */
	public void setNotificationStatus(int notificationStatus) {
		this.notificationStatus = notificationStatus;
	}
}
