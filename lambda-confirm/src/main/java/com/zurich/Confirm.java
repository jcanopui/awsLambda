package com.zurich;

import java.util.Date;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.zurich.entities.NotificationStatusEntity;
import com.zurich.entities.RequestClass;
import com.zurich.entities.ResponseClass;

public class Confirm {

	static AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1);

	static DynamoDBMapper mapper = null;

	/*
	 * Handler Register
	 */
	public static ResponseClass myHandler(RequestClass request, Context context) {

		NotificationStatusEntity notificationStatusEntity = 
				getNotificationStatus(request.getNotificationId());

		if (notificationStatusEntity != null) {
			notificationStatusEntity.setAckDate(new Date());
			notificationStatusEntity.setNotificationStatus(NotificationStatusEntity.ACK);

			saveToDynamoDB(notificationStatusEntity);
		} else {
			StringBuilder buff = new StringBuilder("Confirm: [ Notification not found with Id: ");
			buff.append(request.getNotificationId()).append(" ] ");
			System.out.println(buff.toString());
		}

		return new ResponseClass(true);
	}

	private static DynamoDBMapper getDynamoDBMapper() {
		if (mapper == null) {
			mapper = new DynamoDBMapper(dynamoClient);
		}
		return mapper;
	}

	private static NotificationStatusEntity getNotificationStatus(long notificationId) {

		return getDynamoDBMapper().load(NotificationStatusEntity.class, notificationId);

	}

	private static void saveToDynamoDB(NotificationStatusEntity notificationStatusEntity) {

		getDynamoDBMapper().save(notificationStatusEntity);
	}
}
