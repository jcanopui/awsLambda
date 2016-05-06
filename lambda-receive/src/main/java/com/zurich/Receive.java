package com.zurich;

import java.util.Date;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.zurich.entities.NotificationEntity;
import com.zurich.entities.NotificationStatusEntity;
import com.zurich.entities.RequestClass;
import com.zurich.entities.ResponseClass;

public class Receive {
	
	static 	AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1);
	
	public static ResponseClass myHandler(RequestClass request, Context context) {
		
		LambdaLogger logger = context.getLogger();
		
		try{
			String message = request.getMessage();
			
			String topicName = request.getTopicName();
			
			boolean isTopic = request.isTopic();
			
			String token = getToken(topicName);
			
			saveToDynamoDB(token, message, isTopic);
			
		}catch(Exception e){
			logger.log("Exception: "+e.getLocalizedMessage());
		}
		
		return new ResponseClass(true);
	}
	
	private static String getToken(String topicName) {
		String token = "";
		
		if(topicName.equalsIgnoreCase("topic all")){
			
			DynamoDB dynamoDB = new DynamoDB(dynamoClient);
			
			Table table = dynamoDB.getTable("TOPICS");
			Item item = table.getItem("topicName",topicName);
			
			if(item!=null){
				token = item.getString("topicArn");
			}
		}
		
		return token;
	}
	
	private static void saveToDynamoDB(String token, String message, boolean isTopic) {
		
		long notificationId = getPrimaryKey();
		
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);
		
		NotificationStatusEntity notificationStatus = new NotificationStatusEntity(notificationId, NotificationStatusEntity.RECEIVED);
		mapper.save(notificationStatus);

		NotificationEntity notification = new NotificationEntity(notificationId, message, isTopic, token);
		mapper.save(notification);
	}
	
	private static long getPrimaryKey() {
		Date date = new Date();
		return date.getTime();
	}
}
