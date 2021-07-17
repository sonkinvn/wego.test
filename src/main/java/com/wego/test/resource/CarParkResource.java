package com.wego.test.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wego.test.service.CarParkService;

@RestController
@RequestMapping("/carpark")
public class CarParkResource {

	public static final Logger logger = LoggerFactory.getLogger(CarParkResource.class);
	
	@Autowired 
	private CarParkService service;
	
	@GetMapping(value="/nearest")
	public @ResponseBody ResponseEntity<Object>  getNearestLocation(
			@RequestParam(value="latitude", required = true) Double latitude,
			@RequestParam(value="longitude", required = true) Double longitude,
			@RequestParam(value="page", required = false) Integer page,
			@RequestParam(value="per_page", required = false) Integer per_page
			) {
		try {
			return new ResponseEntity<Object>(service.getNearestLocation(latitude, longitude, page, per_page), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value="/load-data")
	public @ResponseBody ResponseEntity<Object> initDefaultData() {
		return new ResponseEntity<Object>(service.initDefaultData(), HttpStatus.OK);
	}
	
}