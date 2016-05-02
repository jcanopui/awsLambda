package com.zurich;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.zurich.entities.NotificationStatusEntity;
import com.zurich.entities.RequestClass;
import com.zurich.entities.ResponseClass;

public class Confirm {

	static AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1);
	
	/*
	 * Handler Register
	 */
	public static ResponseClass myHandler(RequestClass request, Context context) {
		
		saveToDynamoDB(request.getNotificationId());
		
		return new ResponseClass(true);
	}
	
	private static void saveToDynamoDB(long notificationId) {

		DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);
		NotificationStatusEntity notificationStatus = 
				new NotificationStatusEntity(notificationId, NotificationStatusEntity.ACK);
		mapper.save(notificationStatus);
	}
}
