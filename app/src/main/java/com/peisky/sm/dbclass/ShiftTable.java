package com.peisky.sm.dbclass;

import java.util.HashMap;

public class ShiftTable {
	
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public String getCreated() {
		return Created;
	}
	public void setCreated(String created) {
		Created = created;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
	private String Table_Name;
	private HashMap<Integer, Double> Score;
	private String Note;
	private boolean temp = false;
	private String Created;
	private Long ID;
	private String date;
	private double MaxScore;
	private double MinScore;
	private double AveScore;
//	public ShiftTable(String table_Name, HashMap<Integer, Double> score,
//			String note, String created, Long iD, String date) {
//		super();
//		Table_Name = table_Name;
//		Score = score;
//		Note = note;
//		Created = created;
//		ID = iD;
//		this.date = date;
//	}

	public ShiftTable(String table_Name, HashMap<Integer, Double> score,
			String note, boolean temp, String created, Long iD, String date,
			double maxScore, double minScore, double aveScore) {
		super();
		Table_Name = table_Name;
		Score = score;
		Note = note;
		this.temp = temp;
		Created = created;
		ID = iD;
		this.date = date;
		MaxScore = maxScore;
		MinScore = minScore;
		AveScore = aveScore;
	}
	public ShiftTable() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ShiftTable(String table_Name, HashMap<Integer, Double> score,
			String note) {
		super();
		Table_Name = table_Name;
		Score = score;
		Note = note;
	}
	public String getTable_Name() {
		return Table_Name;
	}
	public void setTable_Name(String table_Name) {
		Table_Name = table_Name;
	}
	public HashMap<Integer, Double> getScore() {
		return Score;
	}
	public void setScore(HashMap<Integer, Double> score) {
		Score = score;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public boolean isTemp() {
		return temp;
	}
	public void setTemp(boolean temp) {
		this.temp = temp;
	}
	public double getMaxScore() {
		return MaxScore;
	}
	public void setMaxScore(double maxScore) {
		MaxScore = maxScore;
	}
	public double getMinScore() {
		return MinScore;
	}
	public void setMinScore(double minScore) {
		MinScore = minScore;
	}
	public double getAveScore() {
		return AveScore;
	}
	public void setAveScore(double aveScore) {
		AveScore = aveScore;
	}
}
