package com.zurich;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.everis.push.services.entities.NotificationStatusEntity;

public class Confirm {


	static AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1);
	
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
		//TODO what is the value that this method must be return?
		
		/*boolean result = */saveToDynamoDB(request.getNotificationId());
		
		return new ResponseClass(true/*result*/);
	}
	
	private static void saveToDynamoDB(long notificationId) {
		
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);
		NotificationStatusEntity notificationStatus = 
				new NotificationStatusEntity(notificationId, NotificationStatusEntity.ACK);
		mapper.save(notificationStatus);
/*		
		AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1);
		DynamoDB dynamoDB = new DynamoDB(dynamoClient);
		Table table = dynamoDB.getTable("NOTIFICATIONS_STATUS");

		Item item = new Item()
				.withPrimaryKey("notificationId",notificationId)
				.withBoolean("notificationStatus", true);
		
		PutItemOutcome outcome = table.putItem(item);
		
		return (outcome!=null);
*/
	}
}
