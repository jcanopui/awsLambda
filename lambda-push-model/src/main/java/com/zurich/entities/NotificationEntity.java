package com.zurich.entities;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

@DynamoDBTable(tableName = "NOTIFICATIONS")
public class NotificationEntity {

	private long notificationId;
	
	private String message;

	private boolean topic;

	private String targetAWS;
	
	private Date registerDate;

	public NotificationEntity() {
		super();
	}

	public NotificationEntity(long notificationId, String message, boolean topic, String targetAWS) {
		super();
		this.notificationId = notificationId;
		this.message = message;
		this.topic = topic;
		this.targetAWS = targetAWS;
		this.registerDate = new Date();
	}
	
	public void setValue(String property, AttributeValue v) {
		if ("notificationId".equals(property)) {
			//TODO this launch an exception
			notificationId = Long.parseLong(v.getN());
		} else if ("topic".equals(property)) {
			if ("1".equals(v.getN())) {
				topic = true;
			} else {
				topic = false;
			}

			StringBuilder buff = new StringBuilder("NotificationEntity Boolean process: " );
			buff.append(topic).append("; originalValue: ").append(v.getN());
			System.out.println(buff.toString());
		} else if ("message".equals(property)){
			message = v.getS();
		} else if ("targetAWS".equals(property))  {
			targetAWS = v.getS();
			//TODO
//		} else if ("registerDate".equals(property))  {
//			registerDate = 
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

	/**
	 * @return the registerDate
	 */
	public Date getRegisterDate() {
		return registerDate;
	}

	/**
	 * @param registerDate the registerDate to set
	 */
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}	
}
