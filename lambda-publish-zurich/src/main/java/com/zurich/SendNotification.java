package com.zurich;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zurich.entities.NotificationEntity;
import com.zurich.entities.NotificationStatusEntity;
import com.zurich.entities.ResponseClass;

/**
 * The Class SendNotification.
 *
 * @author jcanopui
 */
public class SendNotification implements RequestHandler<DynamodbEvent, String> {

	/** The client. */
	static AmazonSNS client = new AmazonSNSClient();
	
	static 	AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1);
	
	/** The topic arn. */
	static String topicArn = "arn:aws:sns:us-east-1:688943189407:AMEU8";
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	/* (non-Javadoc)
	 * @see com.amazonaws.services.lambda.runtime.RequestHandler#handleRequest(java.lang.Object, com.amazonaws.services.lambda.runtime.Context)
	 */
	public String handleRequest(DynamodbEvent event, Context context) {
		
		ResponseClass response = null;
		
        for (DynamodbStreamRecord record : event.getRecords()){
        	
        	NotificationEntity notificationEntity = new NotificationEntity();
        	
        	Map<String, AttributeValue> items = record.getDynamodb().getNewImage();
        	
        	if (items!=null) {
        	
        		items.forEach( (k,v)->System.out.println("Item : " + k + " Count : " + v) );
        		items.forEach( (k,v)->notificationEntity.setValue(k, v)); 
        	
        		if (notificationEntity.isTopic()) {
        			response = sendTopic(notificationEntity, context);
        		} else {
        			response = sendPush(notificationEntity, context);
        		}
        	
        		saveNotificationStatus(notificationEntity.getNotificationId());
        		
        	} else {
        		System.out.println("new image was null");
        		response = new ResponseClass("KO");
        	}
        }
		return response.getMessageId();
	}
	
	/**
	 * Send push.
	 *
	 * @param notificationEntity the notification entity
	 * @param context the context
	 * @return the response class
	 */
	public ResponseClass sendPush(NotificationEntity notificationEntity, Context context) {
		
		ResponseClass response = null;
		
		try{
			PublishRequest publishRequest = new PublishRequest();
		
			String targetAWS = notificationEntity.getTargetAWS();
			String fixedTargetAWS = targetAWS;

			publishRequest.setTargetArn(fixedTargetAWS);
			
			PublishResult publishResult = publish(publishRequest, notificationEntity);
		
			response = new ResponseClass(publishResult.getMessageId());
		
		}catch(Exception e){
			System.out.println("Exception: "+e.getLocalizedMessage());
			response = new ResponseClass("KO");
		}
		
		return response;			
	}
	
	/**
	 * Send topic.
	 *
	 * @param notificationEntity the notification entity
	 * @param context the context
	 * @return the response class
	 */
	public ResponseClass sendTopic(NotificationEntity notificationEntity, Context context) {
		
		PublishRequest publishRequest = new PublishRequest();
		
		publishRequest.setTopicArn(topicArn);
		
		PublishResult publishResult = publish(publishRequest, notificationEntity);
		
		return new ResponseClass(publishResult.getMessageId());		
	}
	
	/**
	 * Update status to send to notification with specified notificationId 
	 * @param notificationId notification identifier
	 */
	private void saveNotificationStatus(long notificationId) {
		
		DynamoDBMapper mapper = new DynamoDBMapper(dynamoClient);
		
		NotificationStatusEntity notificationStatus = new NotificationStatusEntity(notificationId, NotificationStatusEntity.SEND);
		mapper.save(notificationStatus);
	}
	
	private PublishResult publish(PublishRequest publishRequest, NotificationEntity notificationEntity) {
		
		publishRequest.setMessageStructure("json");
		
		String androidMessage = getAndroidMessage(notificationEntity);
		String appleMessage = getAppleMessage(notificationEntity);
		
		Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put("default", notificationEntity.getMessage());
		messageMap.put("APNS", appleMessage);
		messageMap.put("APNS_SANDBOX", appleMessage);
		messageMap.put("GCM", androidMessage);
		
		String message = jsonify(messageMap);
		
		// Display the message that will be sent to the endpoint/
		System.out.println("{Message Body: " + message + "}");
		
		publishRequest.setMessage(message);
		
		PublishResult result = client.publish(publishRequest);
		
		return result;
	}
	
	private String getAppleMessage(NotificationEntity notificationEntity) {
		Map<String, Object> appleMessageMap = new HashMap<String, Object>();
		Map<String, Object> appMessageMap = new HashMap<String, Object>();
		appMessageMap.put("alert", notificationEntity.getMessage());
		appMessageMap.put("messageId", notificationEntity.getNotificationId());
		appleMessageMap.put("aps", appMessageMap);
		return jsonify(appleMessageMap);
	}
	
	private String getAndroidMessage(NotificationEntity notificationEntity) {
		Map<String, Object> androidMessageMap = new HashMap<String, Object>();
		Map<String, Object> appMessageMap = new HashMap<String, Object>();
		appMessageMap.put("message", notificationEntity.getMessage());
		appMessageMap.put("messageId", notificationEntity.getNotificationId());
		androidMessageMap.put("data", appMessageMap);
		return jsonify(androidMessageMap);
	}
	
	public static String jsonify(Object message) {
		try {
			return objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw (RuntimeException) e;
		}
	}
}
