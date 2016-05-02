/**
 * 
 */
package com.everis.push.services.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * @author jcanopui
 *
 */
@DynamoDBTable(tableName = "NOTIFICATION_STATUS")
public class NotificationStatusEntity {

	public final static int RECEIVED = 0;
	public final static int INVALID = 1;
	public final static int SEND = 2;
	public final static int ACK = 3;
	
	private long notificationId;
	
	private int status;
	
	public NotificationStatusEntity(long notificationId, int status) {
		super();
		this.notificationId = notificationId;
		this.status = status;
	}

	@DynamoDBHashKey
	public long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

}
