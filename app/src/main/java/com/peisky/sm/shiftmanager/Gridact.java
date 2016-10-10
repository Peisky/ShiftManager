package com.peisky.sm.shiftmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peisky.R;
import com.peisky.sm.dbclass.DatabaseHelper;
import com.peisky.sm.dbclass.SWorker;
import com.peisky.sm.functionclass.Myfunction;

public class Gridact extends Activity implements OnDateSetListener , WorkerInputDialog.NoticeDialogListener{
	
	GridView gridview;
	long workerid;
	DatePickerDialog datePickerDialog;
	EditText edtDate;
	EditText edtName;
	SWorker worker;
	DatabaseHelper db;
	private String[] weekn = new String[]{"日","一","二","三","四","五","六"};
	SimpleDateFormat SDF;
	Calendar cal;
	HashMap<Long ,String> offdaylist;
	HashMap<Integer, Boolean> offdaycheck;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.grid_offdate);
		db = new DatabaseHelper(getApplicationContext());
		Bundle b  = getIntent().getExtras();
		workerid = b.getInt("ID");
		worker = db.getWorker(workerid);
		cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		try {
			cal.setTime(sdf.parse(b.getString("Date")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setview();
		
		gridview = (GridView)findViewById(R.id.gridView1);

		gridview.setAdapter(new Gridadapter(this,cal.getTime()));
		
	}
	
	private void setview(){
		
		
		datePickerDialog = new DatePickerDialog(this, this,cal.get(Calendar.YEAR) , cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
		datePickerDialog.setTitle("");
		((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
		edtDate = (EditText)findViewById(R.id.edtDate);
		edtName = (EditText)findViewById(R.id.edtName);
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		
		edtDate.setText(sdf.format(cal.getTime()));
		edtName.setText(worker.getWorker_name());
		
		offdaylist = db.getOffdate(worker.getId());

		
		edtDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				datePickerDialog.show();
			}
		});
		edtName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment dialog =  WorkerInputDialog.newInstance(worker.getWorker_name(),true);
				dialog.show(getFragmentManager(), "Edit Name");
			}
		});
		
		
		SDF = new SimpleDateFormat("yyyy/MM/dd");
		
	}


	class Gridadapter extends BaseAdapter{
		private LayoutInflater mLayoutInflater;
		private TreeMap<Integer, String> daylist;
		
		public Gridadapter(Context mContext,Date date){
            super();
            mLayoutInflater = LayoutInflater.from(mContext);
            this.daylist = Myfunction.getDayListMap(date);

		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 42;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			View view = convertView;
			Calendar tempcal = Calendar.getInstance();
			tempcal.setTime(cal.getTime());
			if(view==null){
				view = mLayoutInflater.inflate(R.layout.gridcontent, null);
				holder = new ViewHolder();
				holder.txtday = (TextView)view.findViewById(R.id.txtday);
				holder.txtName = (TextView)view.findViewById(R.id.txtname);
				
				holder.background = (RelativeLayout)view.findViewById(R.id.contentbackground);
				
				view.setOnClickListener(new OnClickListener() {	
				@Override
					public void onClick(View v) {
					
						ViewHolder holder = (ViewHolder)v.getTag();
						String Datestr = (String)holder.background.getTag();
						if(offdaylist.containsValue(Datestr)){
							Long id = null;
							for(java.util.Map.Entry<Long, String> entry :offdaylist.entrySet()){
								if(entry.getValue().equals(Datestr))
									id = entry.getKey();
							}
							db.deletingOffdate(id);
							
		
							holder.background.setBackgroundResource(R.color.White);
							holder.txtday.setTextColor(getResources().getColor(R.color.dark_gray));
							
//							v.setBackgroundResource(R.color.White);
						}else{
							db.createOffdate(worker.getId(), Datestr);
//							v.setBackgroundResource(R.color.NAE);
	
							holder.background.setBackgroundResource(R.color.bg_blue);	
							holder.txtday.setTextColor(getResources().getColor(R.color.White));
						}
						offdaylist = db.getOffdate(worker.getId());
					}
				});
			
				view.setTag(holder);
					
			}else{
				
				holder = (ViewHolder) view.getTag();
			}
			if(daylist.containsKey(position)){
				holder.txtday.setText(daylist.get(position));
				
				tempcal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(daylist.get(position)));
				view.setClickable(true);
				
				if(offdaylist.containsValue(SDF.format(tempcal.getTime()))){
					holder.background.setBackgroundResource(R.color.bg_blue);	
					holder.txtday.setTextColor(getResources().getColor(R.color.White));
				}else{
					holder.background.setBackgroundResource(R.color.White);
					holder.txtday.setTextColor(getResources().getColor(R.color.dark_gray));
				}
				holder.background.setTag(SDF.format(tempcal.getTime()));
			}else{
				holder.background.setBackgroundResource(R.color.White);
				view.setClickable(false);
				holder.txtday.setText("");
			}
			
			return view;
		}
		
	}
	class ViewHolder{
		TextView txtday;
		TextView txtName;
		RelativeLayout background;
	}
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		edtDate.setText(sdf.format(cal.getTime()));
		refreshlist();
		
	}
	@Override
	public void onDialogPositiveClick(WorkerInputDialog dialog) {
		worker.setWorker_name(dialog.edtname.getText().toString());
		edtName.setText(worker.getWorker_name());
		db.updateWorker(worker);
		
	}
	@Override
	public void onDialogNegativeClick(WorkerInputDialog dialog) {
		// TODO Auto-generated method stub
		
	}
	private void refreshlist(){
		gridview.setAdapter(new Gridadapter(this,cal.getTime()));
	}

}
