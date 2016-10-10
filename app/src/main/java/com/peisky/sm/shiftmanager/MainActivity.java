package com.peisky.sm.shiftmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import com.peisky.R;
import com.peisky.sm.dbclass.DatabaseHelper;

public class MainActivity extends Activity {
	DatabaseHelper db;
	SharedPreferences sharedPref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_act);
		
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		db = new DatabaseHelper(getApplicationContext()); 
		findviews();
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.jump);
		shake.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.jump);
				shake.setAnimationListener(this);
				btnAd.startAnimation(shake);
			}
		});
		
		btnAd.startAnimation(shake);
		
		setlistener();

	}
	

	Button btnShiftSet;
	Button btnBrowsShift;
	ImageButton btnAd;
	
	private void findviews(){
		btnBrowsShift = (Button)findViewById(R.id.btn_BrowsShift);
		btnShiftSet = (Button)findViewById(R.id.btnshiftset);
		btnAd = (ImageButton)findViewById(R.id.imageButton1);
	}
	private void setlistener(){
		btnShiftSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);

				intent.setClass(MainActivity.this, Workerlist.class);
				startActivity(intent);
				
			}
		});
		btnBrowsShift.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
			
				intent.setClass(MainActivity.this, BrowseShifts.class);
				startActivity(intent);
			}
		});
		
		btnAd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AdDialog ad_dialog = new AdDialog();
				
				ad_dialog.show(getFragmentManager(), "AD");
			}
		});
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		return true;
	}

}
