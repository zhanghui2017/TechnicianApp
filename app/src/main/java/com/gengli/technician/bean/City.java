package com.gengli.technician.bean;

import java.io.Serializable;

public class City implements Serializable{
	private String city;
	private String city_code;
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCity_code() {
		return city_code;
	}
	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}
	@Override
	public String toString() {
		return "City [city=" + city + ", city_code=" + city_code + "]";
	}
}
