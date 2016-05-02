/**
 * 
 */
package com.everis.push.services.entities;

/**
 * @author jcanopui
 *
 */
public class RequestClass {
	
	private String message;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public RequestClass(String message) {
		this.message = message;
	}
}
