package com.wego.test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wego.test.model.CarParkAvailability;

@Repository
public interface CarParkAvailabilityRepository extends JpaRepository<CarParkAvailability, String> {
	

	@Query(value = "select a.total_lots, a.lots_available as available_lots, b.latitude, b.longitude, b.address, "
			+ "ST_Distance_Sphere( "
			+ "    point(?, ?), "
			+ "    point(b.longitude, b.latitude)) as distance "
			+ "from car_park_availability a "
			+ "inner join car_park_information b on a.car_park_no=b.car_park_no "
			+ "where a.lots_available>0 "
			+ "order by distance LIMIT ? OFFSET ? ", nativeQuery = true)
	List<CarParkAvailabilityDTO> getCarParkAvailabilities(Double longitude, Double latitude, Integer per_page, Integer page);
	
	public interface CarParkAvailabilityDTO{
		String getAddress();
		Double getLatitude();
		Double getlongitude();
		Integer getAvailable_lots();
		Integer getTotal_lots();
	}
	
}
