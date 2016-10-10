package com.peisky.sm.shiftmanager;

import android.app.Activity;
import android.os.Bundle;

public class SettingActivity extends Activity{
	public final static String SUN = "SUN";
	public final static String MON = "MON";
	public final static String TUE = "TUE";
	public final static String WED = "WED";
	public final static String THU = "THU";
	public final static String FRI = "FRI";
	public final static String SAT = "SAT";
	public final static String isWeekeShift = "isWeekeShift";
	public final static String isWeekNoCont ="isWeekNoCont";
	public final static String NDayNoCont = "NDayNoCont";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new PreferenceAct())
        .commit();
	}

}
