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

	private String notificationId;
	
	private String message;

	private boolean topic;

	private String targetAWS;
	
	public void setValue(String property, AttributeValue v) {
		if ("notificationId".equals(property)) {
			notificationId = v.getS();
		} else if ("topic".equals(property)) {
			topic = v.getBOOL();
		} else {
			message = v.getS();
		}
	}
	
	@DynamoDBHashKey
	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
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
