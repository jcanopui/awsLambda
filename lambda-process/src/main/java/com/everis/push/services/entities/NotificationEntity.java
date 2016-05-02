/**
 * 
 */
package com.everis.push.services.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

/**
 * @author jcanopui
 *
 */
@DynamoDBTable(tableName = "NOTIFICATION_TEST")
public class NotificationEntity {

	private long notificationId;
	
	private String message;

	private boolean topic;

	private String targetAWS;
	
	
	public NotificationEntity(long notificationId, String message,
			boolean topic, String targetAWS) {
		super();
		this.notificationId = notificationId;
		this.message = message;
		this.topic = topic;
		this.targetAWS = targetAWS;
	}

	public void setValue(String property, AttributeValue v) {
		if ("notificationId".equals(property)) {
			//TODO this launch an exception
			notificationId = Long.parseLong(v.getN());
		} else if ("topic".equals(property)) {
			topic = v.getBOOL();
		} else {
			message = v.getS();
		}
	}
	
	@DynamoDBHashKey
	public long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	@DynamoDBAttribute
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the topic
	 */
	public boolean isTopic() {
		return topic;
	}

	/**
	 * @param topic the topic to set
	 */
	public void setTopic(boolean topic) {
		this.topic = topic;
	}

	/**
	 * @return the targetAWS
	 */
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
