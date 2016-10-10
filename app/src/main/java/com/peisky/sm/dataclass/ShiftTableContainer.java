package com.peisky.sm.dataclass;

import java.util.ArrayList;
import java.util.TreeMap;

public class ShiftTableContainer {
	private ArrayList<TreeMap<String, ArrayList<Integer>>> newshiftlist ;
	private ArrayList<AnalyticData> scoreData ;
	public ShiftTableContainer(
			ArrayList<TreeMap<String, ArrayList<Integer>>> newshiftlist,
			ArrayList<AnalyticData> scoreData) {
		super();
		this.newshiftlist = newshiftlist;
		this.scoreData = scoreData;
	}
	public ArrayList<TreeMap<String, ArrayList<Integer>>> getNewshiftlist() {
		return newshiftlist;
	}
	public void setNewshiftlist(
			ArrayList<TreeMap<String, ArrayList<Integer>>> newshiftlist) {
		this.newshiftlist = newshiftlist;
	}
	public ArrayList<AnalyticData> getScoreData() {
		return scoreData;
	}
	public void setScoreData(ArrayList<AnalyticData> scoreData) {
		this.scoreData = scoreData;
	}
}
