package com.gengli.technician.bean;

import java.io.Serializable;
import java.util.List;

public class Province implements Serializable{
	private String province;
	private String province_code;
	private List<City> list;
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getProvince_code() {
		return province_code;
	}
	public void setProvince_code(String province_code) {
		this.province_code = province_code;
	}
	public List<City> getList() {
		return list;
	}
	public void setList(List<City> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "Province [province=" + province + ", province_code="
				+ province_code + ", list=" + list + "]";
	}
	
}
