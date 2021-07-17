package com.wego.test.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiAppResponse implements Serializable {

	private static final long serialVersionUID = 1L;
			
	private int code;
	private List<String> messages;
	private Object data;
	
	public ApiAppResponse() {
		super();
	}
	
	public ApiAppResponse(int code, List<String> messages, Object data) {
		super();
		
		this.code = code;
		this.messages = messages;
		this.data = data;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public void addMessage(String msg){
		if(messages == null){
			messages = new ArrayList<String>();
		}		
		messages.add(msg);
	}
	
	public boolean success(){
		if(code == 0) {
			return false;
		}
		return HttpStatus.valueOf(code).is2xxSuccessful();
	}
	
	public <T> ApiAppResponse(ResponseEntity<T> response, Boolean isWrapped) {
		super();
		setCode(response.getStatusCode().value());
		setData(response.getBody());
	}
}
