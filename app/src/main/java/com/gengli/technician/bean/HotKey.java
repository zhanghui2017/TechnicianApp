package com.gengli.technician.bean;

import java.io.Serializable;

/**
 * 热搜词
 * */

public class HotKey implements Serializable{
	private int id;
	private String word;
	private int hots;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public int getHots() {
		return hots;
	}
	public void setHots(int hots) {
		this.hots = hots;
	}
	
}
