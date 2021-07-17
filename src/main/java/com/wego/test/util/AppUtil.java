package com.wego.test.util;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AppUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static ApiAppResponse callAPI(String url, HttpMethod method, String token, Object body){
	    RestTemplate restTemplate = new RestTemplate();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<?> entity = new HttpEntity<Object>(body, headers);
	    ResponseEntity<?> response = restTemplate.exchange(url, method, entity, Object.class);
	    return new ApiAppResponse(response, true);
	}
		
}
