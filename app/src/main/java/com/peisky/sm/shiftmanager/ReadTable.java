package com.peisky.sm.shiftmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.peisky.R;
import com.peisky.sm.dbclass.DatabaseHelper;
import com.peisky.sm.dbclass.SWorker;
import com.peisky.sm.dbclass.Shift;
import com.peisky.sm.dbclass.ShiftTable;
import com.peisky.sm.functionclass.Myfunction;

public class ReadTable extends Activity{
	ArrayList<SWorker> Workerlist;
	HashMap<Integer,SWorker> workermap;
	DatabaseHelper db;
	TreeMap<String, ArrayList<Integer>> AdapterShifts;
	ArrayList<Shift> Shiftlist;
	Calendar cal;
	GridView gridview;
	ShiftGrivewAdapter gridvewadapter;
	Spinner WorkerSpinnger;
	ShiftTable shifttable;
	int MaxHeight=0;
	int currentWorkerID;
	TreeMap<Integer, TreeMap<Integer, Double>> score_map;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View v = LayoutInflater.from(this).inflate(R.layout.arraged_v2, null);
		setContentView(v);
		
		
		db  = new DatabaseHelper(getApplicationContext());
		Bundle b = getIntent().getExtras();
		WorkerSpinnger = (Spinner)findViewById(R.id.spinner1);
		gridview =(GridView)findViewById(R.id.gridView1);
		
		shifttable = db.getShiftTable(b.getLong("TableID"));
		
		Log.d("XXXX","Table Date " +shifttable.getDate());
		
		Shiftlist = db.getShifts(shifttable.getID());
		
		cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		try {
			cal.setTime(sdf.parse(shifttable.getDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("XXXX","shiftlist " + Shiftlist.size());
		AdapterShifts = converList(Shiftlist);
		

		TreeMap<Integer,String> daylist = Myfunction.getDayListMap(cal.getTime());
		score_map = new TreeMap<Integer, TreeMap<Integer,Double>>();
		Calendar tempcal = Calendar.getInstance();
		tempcal.setTime(cal.getTime());
		
		setWorkerList();
		
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd");
		for(SWorker w : Workerlist){
			TreeMap<Integer, Double> temp = new TreeMap<Integer, Double>();
			score_map.put(w.getId(), temp);
			double score = 0;
			for(int i = daylist.firstKey() ;i<=daylist.lastKey();i++){
				int dayofweek = i%7+1;
				tempcal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(daylist.get(i)));
				if(AdapterShifts.containsKey(SDF.format(tempcal.getTime()))){
					ArrayList<Integer> wlist = AdapterShifts.get(SDF.format(tempcal.getTime()));
					
					for(int j =0;j<wlist.size();j++){
						if(wlist.get(j)==w.getId()){
							score+=shifttable.getScore().get(dayofweek);
							temp.put(i, score);
							Log.d("XXXX","ID: "+w.getId()+" Date:"+daylist.get(i)+ " Score:" +score  );
						}
					}
				}
			}
		
		}
		
		
		setTitle(shifttable.getTable_Name());
		gridvewadapter = new ShiftGrivewAdapter(this);
		Log.d("XXXX", cal.getTime().toString());
		
		currentWorkerID = Workerlist.get(0).getId();
		Log.d("XXXX", "Workerlist Set");
		gridview.setAdapter(gridvewadapter);
		gridvewadapter = new ShiftGrivewAdapter(this);
		gridview.setAdapter(gridvewadapter);
		Log.d("XXXX", "GridView Set");
		String [] str = new String[Workerlist.size()];
		
		
		
		
		
		
		
		
		
		
		
		
		for(int i =0;i<Workerlist.size();i++){
			str[i] = Workerlist.get(i).getWorker_name();
		}
		WorkerSpinnger.setAdapter(new TableListViewAdapter(this,R.layout.listcontent , str));
		WorkerSpinnger.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				currentWorkerID = Workerlist.get(position).getId();
				Log.d("XXXX", "currentWorkerID = " +currentWorkerID );
				refreshgridview();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	

	
	
	class ShiftGrivewAdapter extends BaseAdapter{
		LayoutInflater inflater;
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd");
		TreeMap<Integer,String> daylist;
		HashSet<Holder> allholder;
		Double score = 0d;
		public ShiftGrivewAdapter(Context context) {
			allholder = new HashSet<ReadTable.ShiftGrivewAdapter.Holder>();
			this.inflater=LayoutInflater.from(context);
			daylist = Myfunction.getDayListMap(cal.getTime());
			
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return daylist.lastKey()<35?35:42;
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
			
			View v = convertView;
			Holder holder = null;
			Calendar tempcal = Calendar.getInstance();
			tempcal.setTime(cal.getTime());
			
			if(null == v){
				holder = new Holder();
				v = inflater.inflate(R.layout.gridcontent, null);
				holder.txtName = (TextView)v.findViewById(R.id.txtname);
				holder.txtDay = (TextView)v.findViewById(R.id.txtday);
				holder.content = (RelativeLayout)v.findViewById(R.id.content);
				holder.background = (RelativeLayout)v.findViewById(R.id.contentbackground);
				v.setTag(holder);
			}else{
				holder = (Holder)v.getTag();
			}
			
			if(holder.background.getMeasuredHeight() > MaxHeight)
				MaxHeight = holder.background.getMeasuredHeight();
			
			holder.background.setMinimumHeight(MaxHeight);
			
			if(daylist.containsKey(position)){
				holder.txtDay.setText(daylist.get(position));	
				tempcal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(daylist.get(position)));
				holder.txtDay.setTag(SDF.format(tempcal.getTime()));
				
				if(AdapterShifts.containsKey(holder.txtDay.getTag())){
					ArrayList<Integer> wlist = AdapterShifts.get(holder.txtDay.getTag());
					String str = "";
					boolean flag = false;
					
					for(int i = 0;i<wlist.size();i++){
						
						str = str +workermap.get(wlist.get(i)).getWorker_name() + "\n";
						
						if(wlist.get(i)==currentWorkerID){
						
							
							str += "("+score_map.get(currentWorkerID).get(position)+")\n";

							flag = true;
						}else{
							str+="\n";
						}
					}
					holder.txtName.setText(str);
					holder.txtName.setTag(wlist);
					holder.switchColor(flag);
				}else{
					holder.txtName.setText("");
					holder.txtName.setTag("");
				}
				
			}else{
	
				holder.txtDay.setText("");
				holder.txtName.setText("");
			}
			allholder.add(holder);
			return v;
		}
		class Holder{
			TextView txtDay;
			TextView txtName;
			RelativeLayout background;
			RelativeLayout content;
			public void switchColor(boolean enable){
				if(enable){
					background.setBackgroundResource(R.color.bg_blue);
					txtDay.setTextColor(getResources().getColor(R.color.White) );
					txtName.setTextColor(getResources().getColor(R.color.White) );
				}else{
					background.setBackgroundResource(R.color.White);
					txtDay.setTextColor(getResources().getColor(R.color.dark_gray) );
					txtName.setTextColor(getResources().getColor(R.color.dark_gray) );
				}
			}
		}
		
	}
	class TableListViewAdapter extends ArrayAdapter<String>{
		
		String[] list;
		LayoutInflater inflater;
		
		public TableListViewAdapter(Context context, int resource,
				String[] objects) {
			super(context, resource,objects);
			inflater = LayoutInflater.from(context);
			list = objects;
		}


		@Override
		public View getView(int position, View v, ViewGroup parent) {
			v = inflater.inflate(R.layout.spinner_top, null);
		
			TextView txtName = (TextView)v.findViewById(R.id.txtWorkerName);
			txtName.setText(list[position]);
			
			return v;
		}


		@Override
		public View getDropDownView(int position, View v,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			v = inflater.inflate(R.layout.listcontent, null);
			TextView txtName = (TextView)v.findViewById(R.id.txtWorkerName);
			txtName.setText(list[position]);
			
			return v;
		}
	
	}
	
	private TreeMap<String, ArrayList<Integer>> converList(ArrayList<Shift> slist){
		TreeMap<String, ArrayList<Integer>> templist = new TreeMap<String, ArrayList<Integer>>();
		for(int i =0;i<slist.size();i++){
			Shift shift = slist.get(i);
			String datestr = shift.getDate();
			if (templist.containsKey(datestr)) {
				templist.get(datestr).add(shift.getId());
			}else{
				ArrayList<Integer> idlist = new ArrayList<Integer>();
				idlist.add(shift.getId());
				templist.put(datestr, idlist);
			}
		}
		return templist;
	}
	
	private void setWorkerList(){
		TreeSet<Integer> t = new TreeSet<Integer>();
		for (Shift s : Shiftlist) {
			t.add(s.getId());
		}
		Workerlist = new ArrayList<SWorker>();
		workermap = new HashMap<Integer, SWorker>();
		Iterator<Integer> it = t.iterator();
		while(it.hasNext()){
			SWorker sw = db.getWorker(it.next());
			Workerlist.add(sw);
			workermap.put(sw.getId(),sw );
		}
	}
	private void refreshgridview(){
		gridvewadapter = new ShiftGrivewAdapter(this);
		gridview.setAdapter(gridvewadapter);
	}
}
