package com.zurich;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.apigateway.model.NotFoundException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.GetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.GetEndpointAttributesResult;
import com.amazonaws.services.sns.model.SetEndpointAttributesRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;

public class Register {
	
	static AmazonSNS client = new AmazonSNSClient();
	static String topicArn = "arn:aws:sns:us-east-1:688943189407:AMEU8";

	/*
	 * Request Class 
	 */
	public static class RequestClass {
		String platform;
		String token;
		String identifier;
		
		public String getPlatform() {
			return platform;
		}
		
		public void setPlatform(String platform) {
			this.platform = platform;
		}
		
		public String getToken() {
			return token;
		}
		
		public void setToken(String token) {
			this.token = token;
		}
		
		public String getIdentifier() {
			return identifier;
		}
		
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}
		
		public RequestClass(String platform, String token, String identifier) {
			this.platform = platform;
			this.token = token;
			this.identifier = identifier;
		}
		
		public RequestClass() {
		}
	}
	
	/*
	 * Response Class
	 */
	public static class ResponseClass {
		String subscriptionArn;
		
		public String getSubscriptionArn() {
			return subscriptionArn;
		}
		
		public void setSubcriptionArn(String subscriptionArn) {
			this.subscriptionArn = subscriptionArn;
		}
		
		public ResponseClass(String subscriptionArn) {
			this.subscriptionArn = subscriptionArn;
		}
		
		public ResponseClass() {
		}
	}
	
	/*
	 * Handler Register
	 */
	public static ResponseClass myHandler(RequestClass request, Context context) {
		
		boolean updateNeeded = false;
		boolean createNeeded = false;
		
		String token = request.getToken();

		String platformArn = getPlatformArn(request.getPlatform());
		
		String endpointArn = createEndpoint(platformArn, token);
		
		try{
			GetEndpointAttributesRequest geaReq = new GetEndpointAttributesRequest().withEndpointArn(endpointArn);
		
			GetEndpointAttributesResult geaRes = client.getEndpointAttributes(geaReq);
		
			updateNeeded = !geaRes.getAttributes().get("Token").equals(token) || !geaRes.getAttributes().get("Enabled").equalsIgnoreCase("true");
		}catch(NotFoundException nfe){
			createNeeded = true;
		}
		
		if(createNeeded) {
			endpointArn = createEndpoint(platformArn, token);
		}
		
		if(updateNeeded) {
			//The platform endpoint is out of sync with the current data, update the token and enable it
			updateEndpoint(endpointArn, token);
		}
		
		String subscriptionArn = subscribeToTopic(endpointArn);
		
		//Save to database
		saveToDynamoDB(token, request.getPlatform(), request.getIdentifier());
		
		return new ResponseClass(subscriptionArn);
	}
	
	/*
	 * Get PlatformArn
	 */
	private static String getPlatformArn(String platformType) {
		String platformArn = null;
		if(platformType.equalsIgnoreCase("android")){
			platformArn = "arn:aws:sns:us-east-1:688943189407:app/GCM/AMEU8_GCM";
		}else if(platformType.equalsIgnoreCase("iosProd")){
			platformArn = "arn:aws:sns:us-east-1:688943189407:app/APNS/AMEU8_APNS_PROD";
		}else if(platformType.equalsIgnoreCase("iosDev")){
			platformArn = "arn:aws:sns:us-east-1:688943189407:app/APNS_SANDBOX/AMEU8_APNS_DEV";
		}
		return platformArn;
	}
	
	/*
	 * Create Endpoint with this platformArn and this token
	 */
	private static String createEndpoint(String platformArn, String token){
		String endpointArn = null;
		CreatePlatformEndpointRequest cpeReq =  new CreatePlatformEndpointRequest().withPlatformApplicationArn(platformArn).withToken(token);
		CreatePlatformEndpointResult cpeRes = client.createPlatformEndpoint(cpeReq);
		endpointArn = cpeRes.getEndpointArn();
		return endpointArn;
	}
	
	/*
	 * Update Endpoint with this endpointArn and this token
	 */
	private static void updateEndpoint(String endpointArn, String token) {
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("Token", token);
		attributes.put("Enabled", "true");
		SetEndpointAttributesRequest saeReq = new SetEndpointAttributesRequest().withEndpointArn(endpointArn)
.withAttributes(attributes);	
		client.setEndpointAttributes(saeReq);
	}
	
	/*
	 * Subscribe to topic with this endpointarn
	 */
	private static String subscribeToTopic(String endpointArn) {
		SubscribeRequest subscribeReq = new SubscribeRequest(topicArn, "application", endpointArn);
		SubscribeResult subscribeRes = client.subscribe(subscribeReq);
		return subscribeRes.getSubscriptionArn();
	}
	
	private static void saveToDynamoDB(String token, String platform, String identifier) {
		AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1);
		DynamoDB dynamoDB = new DynamoDB(dynamoClient);
		Table table = dynamoDB.getTable("REGISTER_DEVICES");
		//DynamoMapper doesn't allow to add empty strings
		Item item = null;
		if(identifier.length()>0){
			item = new Item()
					.withPrimaryKey("TOKEN_DEVICE", token)
					.withString("PLATFORM", platform)
					.withString("USERID", identifier);
		}else {
			item = new Item()
					.withPrimaryKey("TOKEN_DEVICE", token)
					.withString("PLATFORM", platform);
		}
		table.putItem(item);
	}
}
