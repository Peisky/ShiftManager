package com.peisky.sm.shiftmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Interpolator.Result;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.peisky.R;
import com.peisky.sm.dataclass.AnalyticData;
import com.peisky.sm.dataclass.ShiftConf;
import com.peisky.sm.dataclass.ShiftTableContainer;
import com.peisky.sm.dataclass.Worker;
import com.peisky.sm.dbclass.DatabaseHelper;
import com.peisky.sm.dbclass.Shift;
import com.peisky.sm.dbclass.ShiftTable;
import com.peisky.sm.functionclass.shifter;

public class ArrangementSetUp extends ListActivity implements ShiftTableInputDialog.NoticeDialogListener{
	
	

	ArrayList<Integer> idlist;
	DatabaseHelper db;
	TreeMap<Integer, Worker> workerlist;
	Map<Integer, ArrayList<String>> n_offdatelist;
	Calendar cal;
	ArrayList<TreeMap<String, ArrayList<Integer>>> shiftlist;
	TreeMap<String, ArrayList<Integer>> adapter_shiftlist;
	ArrayList<ShiftTable> adapter_shifttablelist;
	SharedPreferences shp;
	String txtDate;
	ShiftTableContainer container;
	ShiftConf conf;
	TableListAdapter adapter;
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.workercontextmenu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		  switch (item.getItemId()) {
	        case R.id.item_del:
	        	
	            return true;
	        case R.id.item_edit:
	        	ShiftTable st = adapter_shifttablelist.get(info.position);
	        	DialogFragment dialog =  ShiftTableInputDialog.newInstance(st.getTable_Name(), st.getNote(),info.position);
	        	dialog.show(getFragmentManager(), "編輯班表資訊");
	        	return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablelist);

		db = new DatabaseHelper(getApplicationContext());
		ListView view = getListView();
		view.setEmptyView(findViewById(R.id.empty_list_item));
		
		setview();
	
		
		new DoItOnBackground().execute();
	
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent intent = new Intent();
		
		intent.putExtra("TableID", adapter_shifttablelist.get(position).getID());
		intent.setAction(Intent.ACTION_VIEW);
		intent.setClass(ArrangementSetUp.this, ReadTable.class);
		startActivity(intent);
	}
	
	class DoItOnBackground extends AsyncTask<String, Void, Result>{
		ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = ProgressDialog.show(ArrangementSetUp.this, "", 
	                "計算中...", true);
			dialog.show();
		}

		@Override
		protected void onPostExecute(Result result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		
			setListAdapter(new TableListAdapter(ArrangementSetUp.this));
			
			dialog.cancel();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected Result doInBackground(String... params) {		
			for (int i : idlist) {
				Worker w = db.getWorker(i).toWorker();
				workerlist.put(w.getId(), w);
				n_offdatelist.put(i, db.getOffdateList(i));
			}
		
			Log.d("XXXX", "before Calculate Shift ");
			
			ShiftConf conf = getConf();

			shifter sht = new shifter(cal, workerlist, n_offdatelist, conf);
			
			container = sht.new_shift_multi();
//			container = sht.new_shift();
			shiftlist  = container.getNewshiftlist();
			Log.d("XXXX", "Calculate Shift");
			TreeSet<AnalyticData> sort = new TreeSet<AnalyticData>();
			for(int i=0;i<container.getScoreData().size();i++){
				AnalyticData ana = container.getScoreData().get(i);
				ana.setId(i);
				sort.add(ana);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
			for (AnalyticData analyticData : sort) {
				int i = analyticData.getId();
				String namestr = "[" + i + "]" + sdf.format(cal.getTime());
				AnalyticData data = container.getScoreData().get(i);
				ShiftTable st = new ShiftTable(namestr, conf.getScoreArray() , "", true, "" , null 
						, txtDate, data.getMax(), data.getMin(),data.getAve());
				
				long table_id = db.createShiftTable(st);
				
				st.setID(table_id);
				adapter_shifttablelist.add(st);
				for (String s : shiftlist.get(i).keySet()) {
					for(int k = 0;k<shiftlist.get(i).get(s).size();k++){
						Shift shift = new Shift(s, shiftlist.get(i).get(s).get(k));
						db.createShift(shift, table_id);
					}
				}
				if(adapter_shifttablelist.size()>10){
					return null;
				}
				
			}
			
			return null;
		}
			
	}
	
	private void setview(){
		
		Log.d("XXXX", "Set View");
		
		workerlist = new TreeMap<Integer, Worker>();
		n_offdatelist = new TreeMap<Integer, ArrayList<String>>();
		adapter_shifttablelist = new ArrayList<ShiftTable>();
		
		Log.d("XXXX", "Get View");
		
		Bundle b = getIntent().getExtras();
		
		idlist = b.getIntegerArrayList("List");
		cal = Calendar.getInstance();
		
		Log.d("XXXX", idlist.size()+"");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		txtDate = b.getString("Date");
		try {
			cal.setTime(sdf.parse(txtDate));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setTitle(txtDate + "值班表");
		
		
		
		
		Log.d("XXXX", "Setting");
	}
	class TableListAdapter extends BaseAdapter{
		LayoutInflater inflater;
		public TableListAdapter(Context context){
			inflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return adapter_shifttablelist.size();
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
		public View getView(int position, View v, ViewGroup parent) {
			if(getCount()==0){
				v = inflater.inflate(R.layout.empty, null);
				return v;
			}
					
			Holder holder;
			if(v==null){
				v = inflater.inflate(R.layout.tablelist_content, null);
				holder = new Holder();
				holder.cbTempCheck = (CheckBox)v.findViewById(R.id.cbTablelist);
				holder.cbTempCheck.setBackgroundResource(R.drawable.del_save_checkbox);
				holder.cbTempCheck.setButtonDrawable(R.drawable.del_save_checkbox);
				
				holder.cbTempCheck.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ShiftTable st = (ShiftTable)v.getTag();
						CheckBox cb = (CheckBox)v;
						st.setTemp(!st.isTemp());
						cb.setChecked(!st.isTemp());
						db.updateTable(st);
					}
				});
				holder.txtName = (TextView)v.findViewById(R.id.txtTable_Name);
				holder.txtNote =  (TextView)v.findViewById(R.id.txtTable_note);
				holder.txtID =  (TextView)v.findViewById(R.id.txtTable_id);
				holder.txtMax =  (TextView)v.findViewById(R.id.txtTable_Max_score);
				holder.txtMin =  (TextView)v.findViewById(R.id.txtTable_Min_score);
				holder.txtAve =  (TextView)v.findViewById(R.id.txtTable_Ave_score);
				holder.txtCreated =  (TextView)v.findViewById(R.id.txtTable_Created);
				v.setTag(holder);
			}else{
				holder = (Holder)v.getTag();
			}
			ShiftTable st = adapter_shifttablelist.get(position);
			holder.txtName.setText(st.getTable_Name());
			holder.txtNote.setText(st.getNote());
			holder.txtID.setText("ID:" + st.getID());
			holder.txtMax.setText("" + st.getMaxScore());
			holder.txtMin.setText("" + st.getMinScore());
			holder.txtAve.setText("" + st.getAveScore() );
			holder.txtCreated.setText(st.getCreated());
			holder.cbTempCheck.setChecked(!st.isTemp());
			holder.cbTempCheck.setTag(st);
			
			return v;
		}
		class Holder{
			TextView txtName;
			TextView txtID;
			TextView txtNote;
			TextView txtMax;
			TextView txtMin;
			TextView txtAve;
			TextView txtCreated;
			CheckBox cbTempCheck;
		}
	}

	@Override
	public void onBackPressed() {
		
		boolean checknumber = true;
		for(int i = 0;i<adapter_shifttablelist.size();i++){
			if(adapter_shifttablelist.get(i).isTemp()){
				checknumber = false;
				break;
			}
		}
		if(checknumber){
			ArrangementSetUp.this.finish();
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.del_confirmation)
		       .setTitle(R.string.del_alert_title);	
		builder
		.setNegativeButton(R.string.cancel, new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {
				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				db.deletingTempTable();
				ArrangementSetUp.this.finish();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
		
	}

	@Override
	public void onDialogPositiveClick(ShiftTableInputDialog dialog) {
		ShiftTable st = adapter_shifttablelist.get(dialog.getId());
		st.setTable_Name(dialog.edtname.getText().toString());
		st.setNote(dialog.edtnote.getText().toString());
		db.updateTable(st);
		
		
	}

	@Override
	public void onDialogNegativeClick(ShiftTableInputDialog dialog) {
		// TODO Auto-generated method stub
		
	}
	public ShiftConf getConf(){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		HashMap<Integer,Double> Score = new HashMap<Integer,Double>();
		Score.put(1, Double.longBitsToDouble(sp.getLong(SettingActivity.SUN, 0)));
		Score.put(2, Double.longBitsToDouble(sp.getLong(SettingActivity.MON, 0)));
		Score.put(3, Double.longBitsToDouble(sp.getLong(SettingActivity.TUE, 0)));
		Score.put(4, Double.longBitsToDouble(sp.getLong(SettingActivity.WED, 0)));
		Score.put(5, Double.longBitsToDouble(sp.getLong(SettingActivity.THU, 0)));
		Score.put(6, Double.longBitsToDouble(sp.getLong(SettingActivity.FRI, 0)));
		Score.put(7, Double.longBitsToDouble(sp.getLong(SettingActivity.SAT, 0)));
		boolean weekendshift = sp.getBoolean(SettingActivity.isWeekeShift, true);
		boolean wk_no_cont = sp.getBoolean(SettingActivity.isWeekNoCont, true);
		int day_no_cont = sp.getInt(SettingActivity.NDayNoCont, 1);
		return new ShiftConf(weekendshift, wk_no_cont, day_no_cont, Score);
	}
}
