package com.preferenceclass;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.peisky.R;

public class ScorePreference extends Preference {




	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		
		return Double.parseDouble(a.getString(index));
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		// TODO Auto-generated method stub
		
		return super.onCreateView(parent);
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue,
			Object defaultValue) {
		// TODO Auto-generated method stub
		super.onSetInitialValue(restorePersistedValue, defaultValue);
		if(restorePersistedValue){
			
			mCurrentValue = this.getPersistedLong(Double.doubleToLongBits(1d));
		}else{
			mCurrentValue = Double.doubleToLongBits((Double) defaultValue);
	        persistLong(mCurrentValue);
		}
	}

	ImageButton btnplus;
	ImageButton btnminus;	
	EditText edtScore;
	TextView txtTitle;
	long mCurrentValue;
	Double DEFAULT_Value;
	double innerValue;
	@Override
	protected void onBindView(View view) {
		// TODO Auto-generated method stub
		super.onBindView(view);
		btnplus = (ImageButton)view.findViewById(R.id.btnplus);
		btnminus = (ImageButton)view.findViewById(R.id.btnminus);
		edtScore = (EditText)view.findViewById(R.id.edtScore);
		txtTitle = (TextView)view.findViewById(R.id.txtPref_Title);
		txtTitle.setText(getTitle());
		innerValue = Double.longBitsToDouble(mCurrentValue);
		edtScore.setText(String.valueOf(innerValue));
		
		setListenser();
		
	}

	public ScorePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setLayoutResource(R.layout.pref_score);
		
	}
	private void setListenser(){
		btnminus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(innerValue-0.5>=0){
					innerValue -=0.5;
				}
				updateValue();
				Log.d("XXXX", innerValue +"");
			}
		});
		btnplus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(innerValue+0.5<=10){
					innerValue+=0.5;
				}
				updateValue();
				
				Log.d("XXXX", innerValue +"");
			}
		});
	}
	public void updateValue(){
		mCurrentValue = Double.doubleToLongBits(innerValue);
		Log.d("XXXX", "Current " + mCurrentValue );
		persistLong(mCurrentValue);
		edtScore.setText(String.valueOf(innerValue));
	}


}
