package com.zurich;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class Process implements RequestHandler<S3Event, String>{

	public String handleRequest(S3Event event, Context context) {
		
		LambdaLogger logger = context.getLogger();
		
		try{
		
			S3EventNotificationRecord record = event.getRecords().get(0);
	
	        String srcBucket = record.getS3().getBucket().getName();
	        // Object key may have spaces or unicode non-ASCII characters. 
	        String srcKey = URLDecoder.decode(record.getS3().getObject().getKey().replace('+', ' '), "UTF-8");
	
	        
	        AmazonS3 s3Client = new AmazonS3Client();
	        S3Object s3Object = s3Client.getObject(new GetObjectRequest(srcBucket, srcKey));
	        
	        InputStream objectData = s3Object.getObjectContent();
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(objectData,"UTF-8"));
	        
	        //Process SCV file
	        
	        String line = "";
	        List<String> tokens = new ArrayList<String>();
	        List<String> messages = new ArrayList<String>();
	        while((line = br.readLine()) !=null){
	        	String[] info = line.split(",");
	        	tokens.add(info[0]);
	        	messages.add(info[1]);
	        }
	        
	        //Store in DynamoDB
	        
	        int index = 0;
	        for(String token : tokens) {
	        	String message = messages.get(index);
	        	saveToDynamoDB(token, message);
	        	index++;
	        }
		}catch(IOException e){
			logger.log("Exception: "+e.getLocalizedMessage());
		}
        
		return "Ok";
	}
	
	private static void saveToDynamoDB(String token, String message) {
		AmazonDynamoDBClient dynamoClient = new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1);
		DynamoDB dynamoDB = new DynamoDB(dynamoClient);
		Table table = dynamoDB.getTable("NOTIFICATIONS");
		Item item = new Item()
					.withPrimaryKey("notificationId", getPrimaryKey())
					.withString("tokenId", token)
					.withString("message", message);
		table.putItem(item);
	}
	
	private static long getPrimaryKey() {
		Date date = new Date();
		return date.getTime();
	}
}
