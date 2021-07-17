package com.wego.test.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="car_park_availability")
public class CarParkAvailability {

	@Id
	@Column(name = "car_park_no")
	protected String car_park_no;
	
	@Column(name="total_lots")
	private Integer total_lots;
	
	@Column(name="lots_available")
	private Integer lots_available;
	@Column(name = "lot_type") 
	private String lot_type;
	@Column(name = "update_datetime") 
	private Date update_datetime;
	
	public String getCar_park_no() {
		return car_park_no;
	}
	public void setCar_park_no(String car_park_no) {
		this.car_park_no = car_park_no;
	}
	public Integer getTotal_lots() {
		return total_lots;
	}
	public void setTotal_lots(Integer total_lots) {
		this.total_lots = total_lots;
	}
	public Integer getLots_available() {
		return lots_available;
	}
	public void setLots_available(Integer lots_available) {
		this.lots_available = lots_available;
	}
	public String getLot_type() {
		return lot_type;
	}
	public void setLot_type(String lot_type) {
		this.lot_type = lot_type;
	}
	public Date getUpdate_datetime() {
		return update_datetime;
	}
	public void setUpdate_datetime(Date update_datetime) {
		this.update_datetime = update_datetime;
	}
	
}
