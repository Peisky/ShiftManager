package com.peisky.sm.shiftmanager;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.peisky.R;


public class AdDialog extends DialogFragment{
	Dialog dialog;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 dialog = new Dialog(getActivity(),R.style.adDialog);
		 
		 
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 LayoutInflater inflater = getActivity().getLayoutInflater();
		 View v = inflater.inflate(R.layout.ad_dialog, null);
		 dialog.setContentView(v);
		 dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		 ImageButton ad_x = (ImageButton)v.findViewById(R.id.btnexit);
		 ad_x.setOnClickListener(new OnClickListener() {
			 @Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		 ImageButton adlink = (ImageButton)v.findViewById(R.id.imbtnadlink);
		 adlink.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.carebest.com.tw/"));
				startActivity(browserIntent);
				
				
			}
		});
		 
		 
		 
		 
		 
		 
		 return dialog;
	}

	
	

	
}
