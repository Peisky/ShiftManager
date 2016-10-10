package com.peisky.sm.functionclass;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

public class Myfunction {
	public static  TreeMap<Integer,String> getDayListMap(Date date){
		TreeMap<Integer,String> Daylist = new TreeMap<Integer, String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int firstDay = cal.get(Calendar.DAY_OF_WEEK);
		for(int i = 0;i<cal.getActualMaximum(Calendar.DAY_OF_MONTH);i++){
			Daylist.put(i+firstDay-1,"" +  (i+1));
			
		}
		return Daylist;
	}
}
