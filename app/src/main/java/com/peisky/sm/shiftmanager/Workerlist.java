package com.peisky.sm.shiftmanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.peisky.R;
import com.peisky.sm.dbclass.DatabaseHelper;
import com.peisky.sm.dbclass.SWorker;

public class Workerlist extends ListActivity implements OnDateSetListener, WorkerInputDialog.NoticeDialogListener{
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		toWorkerEdit(position);
	}

	@Override
	protected void onResume() {
		refreshlist();
		super.onResume();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		  switch (item.getItemId()) {
	        case R.id.item_del:
	        	alertdialog(info.position);
	            return true;
	        case R.id.item_edit:
	        	toWorkerEdit(info.position);
	        	return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
		
	}
	
	private void toWorkerEdit(int position){
		Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setClass(Workerlist.this, Gridact.class);
    	intent.putExtra("Date", edtDate.getText().toString());
    	intent.putExtra("ID", adapter.workerlist.get(position).getId());
    	startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.workercontextmenu, menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addworker:
			DialogFragment dialog =  WorkerInputDialog.newInstance("",false);
			dialog.show(getFragmentManager(), "Add worker");
			
			break;
		case R.id.setting:
			Intent intent = new Intent(Intent.ACTION_VIEW);
	    	intent.setClass(Workerlist.this, SettingActivity.class);
	    	startActivity(intent);
			break;
		default:
			
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.workerlistmenu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workerlist_v2);
		registerForContextMenu(getListView());
		iniObj();
		setting();
		
		setListAdapter(adapter);

	}
	
	DatabaseHelper db;
	workerlistadapter adapter;
	Button btn;
	DatePickerDialog dpd;
	Calendar cal ;
	EditText edtDate;
	SimpleDateFormat sdf;
	private void iniObj(){
		btn = (Button)findViewById(R.id.btnToArrange);
		db = new DatabaseHelper(getApplicationContext());
		adapter = new workerlistadapter(this);
		btn = (Button)findViewById(R.id.btnToArrange);
		cal= Calendar.getInstance();
		dpd = new DatePickerDialog(this, this,cal.get(Calendar.YEAR) , cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
		edtDate = (EditText)findViewById(R.id.edtDate);
		sdf = new SimpleDateFormat("yyyy/MM");
	}
	private void setting(){
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<Integer>  checkedWorker =new ArrayList<Integer>();
				if(adapter.getCount() ==0){
					Toast.makeText(Workerlist.this, "點選右上+號新增人員" , Toast.LENGTH_LONG).show();
					return;
				}
				for(int i =0;i<adapter.getCount();i++){
					if(adapter.workerlist.get(i).isChecked())
						checkedWorker.add(adapter.workerlist.get(i).getId());
				}
				if(checkedWorker.size()==0){
					Toast.makeText(Workerlist.this, "請選擇值班人員" , Toast.LENGTH_LONG).show();
					return;
				}
				Intent intent = new Intent();
				intent.putIntegerArrayListExtra("List",checkedWorker );
				intent.putExtra("Date", sdf.format(cal.getTime()));
				intent.setAction(Intent.ACTION_VIEW);
				intent.setClass(Workerlist.this, ArrangementSetUp.class);
				startActivity(intent);
				
			}
		});
		
		edtDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dpd.show();
			}
		});
		
		
		dpd.setTitle("");
		((ViewGroup) dpd.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);	
		edtDate.setText(sdf.format(cal.getTime()));
		
		getListView().setEmptyView(findViewById(R.id.txtEmpty));
		
	}
	
	
	class workerlistadapter extends BaseAdapter{
		List<SWorker> workerlist ;
		private LayoutInflater mLayoutInflater;
		
		public workerlistadapter(Context mContext){
            super();
            mLayoutInflater = LayoutInflater.from(mContext);
            workerlist = db.getAllActiveWorker();
		}

		@Override	
		public int getCount() {
			// TODO Auto-generated method stub
			return workerlist.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			Holder holder =null;
            if(null == view){
            	view = mLayoutInflater.inflate(R.layout.workerlistcontent, null);
            	holder = new Holder();
            	holder.workercheckbox = (CheckBox)view.findViewById(R.id.chxWorker);
            	holder.workername =(TextView)view.findViewById(R.id.txtWorkerName);
            	holder.workercheckbox.setOnClickListener(new OnClickListener() {	
					@Override
					public void onClick(View v) {
						CheckBox cb = (CheckBox)v;
						SWorker worker = (SWorker)cb.getTag();
						worker.setChecked(cb.isChecked());

						db.updateWorker(worker);
						
					}
				});
            	view.setTag(holder);
            } else{
            	holder = (Holder) view.getTag();
            }
            
            holder.workercheckbox.setChecked(workerlist.get(position).isChecked());
            holder.workername.setText(workerlist.get(position).getWorker_name());
            holder.workercheckbox.setTag(workerlist.get(position));
            return view;
		}
		
		
	}

	@Override
	public void onDialogPositiveClick(WorkerInputDialog dialog) {
			SWorker worker = new SWorker();
			worker.setChecked(true);
			worker.setWorker_name(dialog.edtname.getText().toString());
			db.createWorker(worker);
			refreshlist();
	}

	@Override
	public void onDialogNegativeClick(WorkerInputDialog dialog) {
		// TODO Auto-generated method stub
		
	}
	private void alertdialog(final int position){

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		
		builder.setMessage(R.string.del_confirmation)
		       .setTitle(R.string.del_alert_title);
		
		
		builder
		.setNegativeButton(R.string.cancel, new Dialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {
				
			@Override
			public void onClick(DialogInterface dialog, int which) {
				db.deletingWorker(adapter.workerlist.get(position).getId());
				refreshlist();
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	private void refreshlist(){
		adapter = new workerlistadapter(this);
		setListAdapter(adapter);
	}
	class Holder{
		CheckBox workercheckbox;
		TextView workername;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		edtDate.setText(sdf.format(cal.getTime()));
	}
	
}
