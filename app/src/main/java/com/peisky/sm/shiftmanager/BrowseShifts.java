package com.peisky.sm.shiftmanager;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.peisky.R;
import com.peisky.sm.dbclass.DatabaseHelper;
import com.peisky.sm.dbclass.ShiftTable;

public class BrowseShifts extends ListActivity implements ShiftTableInputDialog.NoticeDialogListener {
	DatabaseHelper db;
	ArrayList<ShiftTable> tablelist;
	ActionMode mActionMode;
	ArrayList<Booleanholder> chklist;
	BrowseListAdapter myadapter;
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent intent = new Intent();
		
		intent.putExtra("TableID", tablelist.get(position).getID());
		intent.setAction(Intent.ACTION_VIEW);
		intent.setClass(BrowseShifts.this, ReadTable.class);
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablelist);
		
		registerForContextMenu(getListView());
		
		db  = new DatabaseHelper(getApplicationContext());
		tablelist = db.getAllShiftTable();
		chklist = new ArrayList<Booleanholder>();
		
		
		
		for(int i =0;i<tablelist.size();i++){
			Booleanholder b = new Booleanholder();
			b.ischecked=false;
			chklist.add(b);
		}
		myadapter = new BrowseListAdapter(this);
		
		setListAdapter(myadapter);
		refreshlist();
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
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		  switch (item.getItemId()) {
	        case R.id.item_del:
	        	alertdialog(info.position);
	            return true;
	        case R.id.item_edit:
	        	ShiftTable st = tablelist.get(info.position);
	        	DialogFragment dialog =  ShiftTableInputDialog.newInstance(st.getTable_Name(), st.getNote(),st.getID());
	        	dialog.show(getFragmentManager(), "編輯班表資訊");
	        	return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
		
	}

	@Override
	public void onDialogPositiveClick(ShiftTableInputDialog dialog) {
		ShiftTable st =db.getShiftTable(dialog.ID);
		st.setTable_Name(dialog.edtname.getText().toString());
		st.setNote(dialog.edtnote.getText().toString());
		db.updateTable(st);
		tablelist = db.getAllShiftTable();
		refreshlist();
	}

	@Override
	public void onDialogNegativeClick(ShiftTableInputDialog dialog) {
		// TODO Auto-generated method stub
		
	}
	private void alertdialog(final int position){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.del_confirmation)
		       .setTitle(R.string.del_alert_title);	
		builder
		.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ShiftTable st = tablelist.get(position);
				db.deletingShiftTable(st.getID());
				Toast.makeText(BrowseShifts.this,"已刪除班表" + st.getTable_Name() , Toast.LENGTH_LONG).show();
				refreshlist();
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	private void GroupDeleteTable(){
		int count = 0;
		for(int i=0;i<chklist.size();i++){
			if(chklist.get(i).ischecked){
				count++;
				ShiftTable st = tablelist.get(i);
				db.deletingShiftTable(st.getID());
			}
		}
		Toast.makeText(BrowseShifts.this,"已刪除" + count + "個班表" , Toast.LENGTH_LONG).show();
	}
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
	    // Called when the action mode is created; startActionMode() was called
	    @Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        // Inflate a menu resource providing context menu items
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.browser_actionbar, menu);
	        return true;
	    }

	    // Called each time the action mode is shown. Always called after onCreateActionMode, but
	    // may be called multiple times if the mode is invalidated.
	    @Override
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	        return false; // Return false if nothing is done
	    }
	    // Called when the user selects a contextual menu item
	    @Override
	    public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.action_Delete:
	            	AlertDialog.Builder builder = new AlertDialog.Builder(BrowseShifts.this);
	        		builder.setMessage(R.string.del_confirmation)
	        		       .setTitle(R.string.del_alert_title);	
	        		builder
	        		.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {	
	        			@Override
	        			public void onClick(DialogInterface dialog, int which) {
	        				GroupDeleteTable();
	    	            	refreshlist();
	    	                mode.finish();
	        			}
	        		});		
	        		AlertDialog dialog = builder.create();
	        		dialog.show();
	                return true;
	            case R.id.action_reverseletct:
	            	ReversSleect();
	            	return true;
	            default:
	            	mode.finish();
	                return false;
	        }
	    }

	    // Called when the user exits the action mode
	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	        mActionMode = null;
	        for(int i =0;i<chklist.size();i++)
	        	chklist.get(i).ischecked = false;
	        myadapter.notifyDataSetChanged();
	    }
	};

	class BrowseListAdapter extends BaseAdapter{
		LayoutInflater inflater;
		
		public BrowseListAdapter(Context context) {

			this.inflater = LayoutInflater.from(context);
			
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tablelist.size();
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
			// TODO Auto-generated method stub
			Holder holder = new Holder();
			if(null==v){
				v = inflater.inflate(R.layout.tablelist_content,null);
				holder.txtTableName = (TextView)v.findViewById(R.id.txtTable_Name);
				holder.txtTableCreated = (TextView)v.findViewById(R.id.txtTable_Created);
				holder.txtTableId = (TextView)v.findViewById(R.id.txtTable_id);
				holder.txtTableNote = (TextView)v.findViewById(R.id.txtTable_note);
				holder.txtMax =  (TextView)v.findViewById(R.id.txtTable_Max_score);
				holder.txtMin =  (TextView)v.findViewById(R.id.txtTable_Min_score);
				holder.txtAve =  (TextView)v.findViewById(R.id.txtTable_Ave_score);
				holder.cb = (CheckBox)v.findViewById(R.id.cbTablelist);
				holder.cb.setOnClickListener(new OnClickListener() {				
					@Override
					public void onClick(View v) {
						CheckBox cb = (CheckBox)v;
						Booleanholder b = (Booleanholder)cb.getTag();
						b.ischecked = cb.isChecked();
						
						int count =0;
						for(int i =0;i<chklist.size();i++){
							if(chklist.get(i).ischecked)
								count++;
						}
						
						if(count!=0){
							if (mActionMode == null) {
								mActionMode = startActionMode(mActionModeCallback);
					        }
							mActionMode.setTitle(count+"");
						}else{
							mActionMode.finish();
						}
						
					}
				});
				v.setTag(holder);
			}else{
				holder = (Holder)v.getTag();
			}
			holder.txtTableCreated.setText( tablelist.get(position).getCreated());
			holder.txtTableId.setText("ID:" + tablelist.get(position).getID());
			holder.txtTableName.setText(tablelist.get(position).getTable_Name());
			holder.txtTableNote.setText("note:"+tablelist.get(position).getNote());

			holder.txtMax.setText("" + tablelist.get(position).getMaxScore());
			holder.txtMin.setText("" + tablelist.get(position).getMinScore());
			holder.txtAve.setText("" + tablelist.get(position).getAveScore() );
			holder.cb.setChecked(chklist.get(position).ischecked);
			holder.cb.setTag(chklist.get(position));
			return v;
		}
		class Holder{
			TextView txtTableName;
			TextView txtTableCreated;
			TextView txtTableNote;
			TextView txtTableId;
			TextView txtMax;
			TextView txtMin;
			TextView txtAve;
			CheckBox cb;
		}
		
	}
	class Booleanholder{
		boolean ischecked;
	}
	private void refreshlist(){
		tablelist = db.getAllShiftTable();
		chklist = new ArrayList<Booleanholder>();
		for(int i =0;i<tablelist.size();i++){
			Booleanholder b = new Booleanholder();
			b.ischecked=false;
			chklist.add(b);
		}
		myadapter = new BrowseListAdapter(this);
		setListAdapter(myadapter);
	}
	private void ReversSleect(){
		for(int i=0;i<chklist.size();i++){
			chklist.get(i).ischecked = !chklist.get(i).ischecked;
		}
		int count =0;
		for(int i =0;i<chklist.size();i++){
			if(chklist.get(i).ischecked)
				count++;
		}
		mActionMode.setTitle(count+"");
		myadapter.notifyDataSetChanged();
	}


}
