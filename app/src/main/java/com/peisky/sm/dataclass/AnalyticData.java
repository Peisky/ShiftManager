package com.peisky.sm.dataclass;

import java.util.ArrayList;

public class AnalyticData implements Comparable<AnalyticData> {
	public int getWeekendScore() {
		return WeekendScore;
	}
	public void setWeekendScore(int weekendScore) {
		WeekendScore = weekendScore;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Double getDif() {
		return Dif;
	}
	public void setDif(Double dif) {
		Dif = dif;
	}
	Double Max = 0d;
	Double Min = 0d;
	Double Ave;
	Double Dif = 0d;
	int id=0;
	int WeekendScore = 0;
	
	public AnalyticData(ArrayList<Double> data) {
		double sum = 0;
		Min = data.get(0);
		for(int i = 0;i<data.size();i++){
			sum +=data.get(i);
			Max = Max>data.get(i)?Max:data.get(i);
			Min = data.get(i)>Min?Min:data.get(i);
		}
		Ave = ((double) Math.round(10*sum/data.size()))/10;
		Dif = Max - Min;
	}
	public Double getMax() {
		return Max;
	}
	public void setMax(Double max) {
		Max = max;
	}
	public Double getMin() {
		return Min;
	}
	public void setMin(Double min) {
		Min = min;
	}
	public Double getAve() {
		return Ave;
	}
	public void setAve(Double ave) {
		Ave = ave;
	}
	@Override
	public int compareTo(AnalyticData o) {
		if(this.Dif < o.Dif){
			return -1;
		}else{
			if(this.WeekendScore < o.WeekendScore){
				return -1;
			}
		}
		
		return 1;
	}
	
	
}
