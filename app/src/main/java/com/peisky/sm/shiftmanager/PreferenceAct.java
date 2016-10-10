package com.peisky.sm.shiftmanager;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.peisky.R;
import com.preferenceclass.NumberPickerPreference;
import com.preferenceclass.ScorePreference;

public class PreferenceAct extends PreferenceFragment implements OnSharedPreferenceChangeListener{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefer);
		
		
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Preference pref = findPreference(key);
		Log.d("XXXX","KEY " + key);
		
		if(pref instanceof NumberPickerPreference){
			NumberPickerPreference p = (NumberPickerPreference)pref;
			Log.d("XXXX", "SharedPreferences " + sharedPreferences.getInt(key,0));
			p.setSummary("最少間隔" + sharedPreferences.getInt(key,0) + "天");
		}else if(pref instanceof ScorePreference){
			
		}else if(pref instanceof CheckBoxPreference){
			
		}
		
	}
	@Override
	public void onResume() {
	    super.onResume();
	    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

	}

	@Override
	public void onPause() {
	    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	    super.onPause();
	}
}
