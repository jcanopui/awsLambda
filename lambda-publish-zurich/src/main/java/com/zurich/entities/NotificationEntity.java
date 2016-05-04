package com.zurich.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

@DynamoDBTable(tableName = "NOTIFICATIONS")
public class NotificationEntity {

	private long notificationId;
	
	private String message;

	private boolean topic;

	private String targetAWS;

	public void setValue(String property, AttributeValue v) {
		if ("notificationId".equals(property)) {
			//TODO this launch an exception
			notificationId = Long.parseLong(v.getN());
		} else if ("topic".equals(property)) {
			topic = Boolean.getBoolean(v.getN());
		} else if ("message".equals(property)){
			message = v.getS();
		} else {
			targetAWS = v.getS();
		}
	}
	
	@DynamoDBHashKey
	public long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isTopic() {
		return topic;
	}

	/**
	 * @param topic the topic to set
	 */
	public void setTopic(boolean topic) {
		this.topic = topic;
	}

	public String getTargetAWS() {
		return targetAWS;
	}

	/**
	 * @param targetAWS the targetAWS to set
	 */
	public void setTargetAWS(String targetAWS) {
		this.targetAWS = targetAWS;
	}	
}
