package com.zurich;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;

public class Confirm {

	/*
	 * Request Class
	 */
	public static class RequestClass {
		
		long notificationId;
		
		public long getNotificationId() {
			return notificationId;
		}

		public void setNotificationId(long notificationId) {
			this.notificationId = notificationId;
		}

		public RequestClass(long notificationId) {
			this.notificationId = notificationId;
		}

		public RequestClass() {}
	}
	
	/*
	 * Response Class
	 */
	public static class ResponseClass {
		
		boolean result;
		
		public boolean isResult() {
			return result;
		}

		public void setResult(boolean result) {
			this.result = result;
		}

		public ResponseClass(boolean result) {
			this.result = result;
		}

		public ResponseClass() {}
	}
	
	/*
	 * Handler Register
	 */
	public static ResponseClass myHandler(RequestClass request, Context context) {
		
		boolean result = saveToDynamoDB(request.getNotificationId());
		
		return new ResponseClass(result);
	}
	
	private static boolean saveToDynamoDB(long notificationId) {

		AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1);
		DynamoDB dynamoDB = new DynamoDB(dynamoClient);
		Table table = dynamoDB.getTable("NOTIFICATIONS_STATUS");

		Item item = new Item()
				.withPrimaryKey("notificationId",notificationId)
				.withBoolean("notificationStatus", true);
		
		PutItemOutcome outcome = table.putItem(item);
		
		return (outcome!=null);
	}
}
