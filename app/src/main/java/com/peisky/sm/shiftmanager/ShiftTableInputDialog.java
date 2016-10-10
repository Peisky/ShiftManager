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

public class ShiftTableInputDialog extends DialogFragment{
	static ShiftTableInputDialog newInstance(String name,String note,long id){
		ShiftTableInputDialog f = new ShiftTableInputDialog();
		Bundle arg = new Bundle();
		arg.putString("name",name);
		arg.putString("note", note);
		arg.putLong("ID", id);
		f.setArguments(arg);
		return f;
	}
	NoticeDialogListener mListener;
	public interface NoticeDialogListener {
        public void onDialogPositiveClick(ShiftTableInputDialog dialog);
        public void onDialogNegativeClick(ShiftTableInputDialog dialog);
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
	EditText edtnote;
	long ID;
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 LayoutInflater inflater = getActivity().getLayoutInflater();
		 
		 View v = inflater.inflate(R.layout.shifttabledialog, null);
		 edtname = (EditText)v.findViewById(R.id.DialogedtSetName);
		 edtnote = (EditText)v.findViewById(R.id.DialogedtSetNote);
		 ID = this.getArguments().getLong("ID");
		 edtname.setText(this.getArguments().getString("name"));
		 edtnote.setText(this.getArguments().getString("note"));
		 builder.setView(v);
		 builder.setMessage(R.string.DialogTableEditTitle)
		 	.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mListener.onDialogPositiveClick(ShiftTableInputDialog.this);
				}
			})
			.setNegativeButton(R.string.cancel, new Dialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mListener.onDialogNegativeClick(ShiftTableInputDialog.this); 
				}
			});	
		
		return builder.create();
	}
	
	
}
