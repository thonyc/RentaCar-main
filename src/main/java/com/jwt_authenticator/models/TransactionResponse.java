package com.jwt_authenticator.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) 	//  ignore all null fields
public class TransactionResponse implements Serializable {
	/**
	 * This is a model class used to control the response
	 */
	private static final long serialVersionUID = 1L;
	private String responseCode;
    private String responseMessage;
	private int statusCode;
	private Object data;
	private Object data2;
    
    public TransactionResponse() {
    	
    }
    
    public TransactionResponse(String responseCode, String responseMessage) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.statusCode = 200;
	}
    
	public TransactionResponse(String responseCode, String responseMessage, int strSatusCode) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.statusCode = strSatusCode;
	}
	public TransactionResponse(String responseCode, String responseMessage, Object data) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.data = data;
		this.statusCode = 200;
	}
	
	public TransactionResponse(String responseCode, String responseMessage, Object data, Object data2) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.data = data;
		this.data2 = data2;
	}
	public String getresponseCode() {
		return responseCode;
	}
	public void setresponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getresponseMessage() {
		return responseMessage;
	}
	public void setresponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public int getstatusCode() {
		return statusCode;
	}

	public void setstatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "TransactionResponse [responseCode=" + responseCode + ", responseMessage=" + responseMessage
				+ ", statusCode=" + statusCode + ", data=" + data + "]";
	}

	public Object getData2() {
		return data2;
	}

	public void setData2(Object data2) {
		this.data2 = data2;
	}
    
    

}
