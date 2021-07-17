package com.wego.test.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="car_park_information")
public class CarParkInformation {

	@Id
	@Column(name = "car_park_no")
	protected String car_park_no;
	
	@Column(name="address")
	private String address;
	
	@Column(name="x_coord")
	private Double x_coord;
	@Column(name="y_coord")
	private Double y_coord;
	@Column(name="latitude")
	private Double latitude;
	@Column(name="longitude")
	private Double longitude;

	@Column(name = "car_park_type") 
	private String car_park_type;
	@Column(name = "type_of_parking_system") 
	private String type_of_parking_system;
	@Column(name = "short_term_parking") 
	private String short_term_parking;
	@Column(name = "free_parking") 
	private String free_parking;
	@Column(name = "night_parking") 
	private String night_parking;
	@Column(name = "car_park_decks") 
	private Integer car_park_decks;
	@Column(name = "gantry_height") 
	private Double gantry_height;
	@Column(name = "car_park_basement") 
	private String car_park_basement;
	
	public String getCar_park_no() {
		return car_park_no;
	}
	public void setCar_park_no(String car_park_no) {
		this.car_park_no = car_park_no;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Double getX_coord() {
		return x_coord;
	}
	public void setX_coord(Double x_coord) {
		this.x_coord = x_coord;
	}
	public Double getY_coord() {
		return y_coord;
	}
	public void setY_coord(Double y_coord) {
		this.y_coord = y_coord;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getCar_park_type() {
		return car_park_type;
	}
	public void setCar_park_type(String car_park_type) {
		this.car_park_type = car_park_type;
	}
	public String getType_of_parking_system() {
		return type_of_parking_system;
	}
	public void setType_of_parking_system(String type_of_parking_system) {
		this.type_of_parking_system = type_of_parking_system;
	}
	public String getShort_term_parking() {
		return short_term_parking;
	}
	public void setShort_term_parking(String short_term_parking) {
		this.short_term_parking = short_term_parking;
	}
	public String getFree_parking() {
		return free_parking;
	}
	public void setFree_parking(String free_parking) {
		this.free_parking = free_parking;
	}
	public String getNight_parking() {
		return night_parking;
	}
	public void setNight_parking(String night_parking) {
		this.night_parking = night_parking;
	}
	public Integer getCar_park_decks() {
		return car_park_decks;
	}
	public void setCar_park_decks(Integer car_park_decks) {
		this.car_park_decks = car_park_decks;
	}
	public Double getGantry_height() {
		return gantry_height;
	}
	public void setGantry_height(Double gantry_height) {
		this.gantry_height = gantry_height;
	}
	public String getCar_park_basement() {
		return car_park_basement;
	}
	public void setCar_park_basement(String car_park_basement) {
		this.car_park_basement = car_park_basement;
	}
	
	
}
