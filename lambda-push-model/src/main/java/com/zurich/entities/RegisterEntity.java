package com.zurich.entities;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="REGISTER_DEVICES")
public class RegisterEntity {

	private String token;
	
	private String platform;
	
	private String identifier;
	
	private String endpointArn;
	
	private Date registryDate;

	public RegisterEntity() {
		super();
	}

	public RegisterEntity(String token, String platform, String identifier) {
		super();
		this.token = token;
		this.platform = platform;
		this.identifier = identifier;
		this.registryDate = new Date();
	}

	public RegisterEntity(String token, String platform, String identifier, String endpointArn) {
		super();
		this.token = token;
		this.platform = platform;
		this.identifier = identifier;
		this.endpointArn = endpointArn;
		this.registryDate = new Date();
	}

	/**
	 * @return the token
	 */
	@DynamoDBHashKey()
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the endpointArn
	 */
	public String getEndpointArn() {
		return endpointArn;
	}

	/**
	 * @param endpointArn the endpointArn to set
	 */
	public void setEndpointArn(String endpointArn) {
		this.endpointArn = endpointArn;
	}

	/**
	 * @return the registryDate
	 */
	public Date getRegistryDate() {
		return registryDate;
	}

	/**
	 * @param registryDate the registryDate to set
	 */
	public void setRegistryDate(Date registryDate) {
		this.registryDate = registryDate;
	}
}
