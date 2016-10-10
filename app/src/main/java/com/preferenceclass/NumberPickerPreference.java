package com.preferenceclass;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import com.peisky.R;

public class NumberPickerPreference extends DialogPreference {
	int DEFAULT_VALUE;
	
	@Override
	protected void onBindDialogView(View view) {
		// TODO Auto-generated method stub
		super.onBindDialogView(view);
		np = (NumberPicker)view.findViewById(R.id.npicker);
		np.setMaxValue(7);
		np.setMinValue(0);
		np.setValue(this.getPersistedInt(DEFAULT_VALUE));
		
	}
	int mCurrentValue;
	@Override
	protected void onSetInitialValue(boolean restorePersistedValue,
			Object defaultValue) {
		// TODO Auto-generated method stub
		super.onSetInitialValue(restorePersistedValue, defaultValue);
		if (restorePersistedValue) {
	        // Restore existing state
	        mCurrentValue = this.getPersistedInt(DEFAULT_VALUE);
	    } else {
	        // Set default state from the XML attribute
	        mCurrentValue = (Integer) defaultValue;
	        persistInt(mCurrentValue);
	    }
		setSummary("最少間隔" +mCurrentValue + "天");
		
		
	}
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		// TODO Auto-generated method stub
		super.onDialogClosed(positiveResult);
		if (positiveResult) {
			int mNewValue = np.getValue();
			persistInt(mNewValue);
		}
		
		
	}
	NumberPicker np;
	public NumberPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		DEFAULT_VALUE = 2;
		setDialogLayoutResource( R.layout.numberpickerdialog);
		
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        
        setDialogIcon(null);
	}

}
