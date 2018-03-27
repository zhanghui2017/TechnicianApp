package com.gengli.technician.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 行程
 * @author Administrator
 *
 */
public class Routing implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String time;
	private String address;
	private String jobContent;
	private String machine;
	private String model;
	private String phone;
	private String chargeName;
	/**
	 * 培训
	 * 巡检
	 * 维修
	 * 调试
	 * 
	 */
	private String type;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getJobContent() {
		return jobContent;
	}
	public void setJobContent(String jobContent) {
		this.jobContent = jobContent;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getMachine() {
		return machine;
	}
	public void setMachine(String machine) {
		this.machine = machine;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getChargeName() {
		return chargeName;
	}
	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	@Override
	public String toString() {
		return "Routing{" +
				"time='" + time + '\'' +
				", address='" + address + '\'' +
				", jobContent='" + jobContent + '\'' +
				", machine='" + machine + '\'' +
				", model='" + model + '\'' +
				", phone='" + phone + '\'' +
				", chargeName='" + chargeName + '\'' +
				", type='" + type + '\'' +
				'}';
	}
}
