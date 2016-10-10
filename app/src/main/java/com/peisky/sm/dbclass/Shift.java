package com.peisky.sm.dbclass;

public class Shift {
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String date;
	private int id;
	public Shift(String date, int id) {
		super();
		this.date = date;
		this.id = id;
	}
	
}
