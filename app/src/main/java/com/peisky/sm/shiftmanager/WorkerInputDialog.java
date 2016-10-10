package com.peisky.sm.shiftmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.peisky.R;



public class WorkerInputDialog extends DialogFragment {
	
	static WorkerInputDialog newInstance(String name,boolean editmode){
		WorkerInputDialog f = new WorkerInputDialog();
		Bundle arg = new Bundle();
		arg.putString("name",name);
		arg.putBoolean("editmode", editmode);
		f.setArguments(arg);
		return f;
	}
	
	
	NoticeDialogListener mListener;
	public interface NoticeDialogListener {
        public void onDialogPositiveClick(WorkerInputDialog dialog);
        public void onDialogNegativeClick(WorkerInputDialog dialog);
    }
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
	}
	EditText edtname;
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 LayoutInflater inflater = getActivity().getLayoutInflater();
		 
		 View v = inflater.inflate(R.layout.shiftsetdialog, null);
		 edtname = (EditText)v.findViewById(R.id.DialogedtSetName);
		 
		 edtname.setText(this.getArguments().getString("name"));
		 boolean editmode = getArguments().getBoolean("editmode");
		 
		 builder.setView(v);
		 builder.setMessage(editmode?R.string.WorkerEditDialogTitle :R.string.WorkerInputDialogTitle)
		 	.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mListener.onDialogPositiveClick(WorkerInputDialog.this);
				}
			})
			.setNegativeButton(R.string.cancel, new Dialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mListener.onDialogNegativeClick(WorkerInputDialog.this); 
				}
			});	
		
		return builder.create();
	}
}
