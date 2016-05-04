package com.zurich.entities;

public class ResponseClass {

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
	
	public ResponseClass(){}
}
