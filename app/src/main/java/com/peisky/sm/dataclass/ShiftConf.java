package com.peisky.sm.dataclass;

import java.util.HashMap;
import java.util.Map;

public class ShiftConf {
	public HashMap<Integer, Double> getScoreArray() {
		return ScoreArray;
	}
	public void setScoreArray(HashMap<Integer, Double> scoreArray) {
		ScoreArray = scoreArray;
	}
	public boolean isWeekendshift() {
		return weekendshift;
	}
	public void setWeekendshift(boolean weekendshift) {
		this.weekendshift = weekendshift;
	}
	public boolean isWk_no_cont() {
		return wk_no_cont;
	}
	public void setWk_no_cont(boolean wk_no_cont) {
		this.wk_no_cont = wk_no_cont;
	}
	public int getDay_no_cont() {
		return day_no_cont;
	}
	public void setDay_no_cont(int day_no_cont) {
		this.day_no_cont = day_no_cont;
	}
	public ShiftConf(boolean weekendshift, boolean wk_no_cont, int day_no_cont,
			HashMap<Integer, Double> scoreArray) {
		super();
		this.weekendshift = weekendshift;
		this.wk_no_cont = wk_no_cont;
		this.day_no_cont = day_no_cont;
		ScoreArray = scoreArray;
	}
	private boolean weekendshift  = false;
	private boolean wk_no_cont = false;
	private int day_no_cont = 0;
	private HashMap<Integer,Double> ScoreArray ;
}
