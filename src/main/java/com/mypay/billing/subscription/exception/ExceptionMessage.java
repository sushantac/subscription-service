package com.mypay.billing.subscription.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

public class ExceptionMessage {

	private HttpStatus status;
	private Date timestamp;
    private String message;
    private String details;
    
    
	public ExceptionMessage(HttpStatus status, Date timestamp, String message, String details) {
		super();
		this.status = status;
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}
	
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
    
      
 
   
}