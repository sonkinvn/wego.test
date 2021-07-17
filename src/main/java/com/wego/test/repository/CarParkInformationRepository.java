package com.wego.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wego.test.model.CarParkInformation;

@Repository
public interface CarParkInformationRepository extends JpaRepository<CarParkInformation, String> {


}
