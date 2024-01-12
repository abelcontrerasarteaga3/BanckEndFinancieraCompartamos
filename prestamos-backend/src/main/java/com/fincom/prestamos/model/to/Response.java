package com.fincom.prestamos.model.to;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {
	private Integer data;
	@JsonProperty("isSuccess")
	private boolean isSuccess;
	private String message;
	private boolean isWarning;
	public Integer getData() {
		return data;
	}
	public void setData(Integer data) {
		this.data = data;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public boolean isWarning() {
		return isWarning;
	}
	public void setWarning(boolean isWarning) {
		this.isWarning = isWarning;
	}
	
}
