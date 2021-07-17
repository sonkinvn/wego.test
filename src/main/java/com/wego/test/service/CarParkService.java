package com.wego.test.service;

import java.util.List;

import com.wego.test.repository.CarParkAvailabilityRepository.CarParkAvailabilityDTO;

public interface CarParkService {

	String initDefaultData();

	List<CarParkAvailabilityDTO> getNearestLocation(Double latitude, Double longitude, Integer per_page, Integer page);

}
