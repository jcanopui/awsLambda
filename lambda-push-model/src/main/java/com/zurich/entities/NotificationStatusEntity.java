package com.zurich.entities;

import java.util.Date;

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
	
	private Date sendDate;
	
	private Date ackDate;
	
	private Date receiveDate;

	public NotificationStatusEntity() {
		super();
	}
	
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

	/**
	 * @return the ackDate
	 */
	public Date getAckDate() {
		return ackDate;
	}

	/**
	 * @param ackDate the ackDate to set
	 */
	public void setAckDate(Date ackDate) {
		this.ackDate = ackDate;
	}

	/**
	 * @return the sendDate
	 */
	public Date getSendDate() {
		return sendDate;
	}

	/**
	 * @param sendDate the sendDate to set
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * @return the receiveDate
	 */
	public Date getReceiveDate() {
		return receiveDate;
	}

	/**
	 * @param receiveDate the receiveDate to set
	 */
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
}
