package com.peisky.sm.dbclass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper  {
	
	private static final String LOG = "DBHELPER";
	
	private static final int  DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "shiftmanager";
	
	
	private static final String TABLE_WORKERS = "workers";
	private static final String TABLE_SHIFTTABLELIST = "ShiftTableList";
	private static final String TABLE_OFFDATE = "offdate";
	private static final String TABLE_SHIFTABLE = "ShiftTable"; 
	
	private static final String KEY_ID = "ID";
	//Table Worker
	private static final String KEY_WORKER_NAME = "worker_name";
	private static final String KEY_CREATED = "created";
	private static final String KEY_CHECKED = "checked";
	private static final String KEY_ENABLED = "enabled";
	//Table offdate
	private static final String KEY_OFFDATE ="offdate";
	private static final String KEY_WORKERID = "worker_id";
	//Table ShiftTable
	private static final String KEY_SHIFT_TABLE_NAME = "TableName";
	private static final String KEY_SCORE_SUN = "SUN"; 
	private static final String KEY_SCORE_MON = "MON"; 
	private static final String KEY_SCORE_TUE = "TUE"; 
	private static final String KEY_SCORE_WED = "WED";
	private static final String KEY_SCORE_THU = "THU"; 
	private static final String KEY_SCORE_FRI = "FRI";
	private static final String KEY_SCORE_SAT = "SAT";
	private static final String KEY_NOTE = "Note";
	private static final String KEY_TABLE_DATE = "Table_Date";
	private static final String KEY_MAX_SCORE = "MAX";
	private static final String KEY_MIN_SCORE = "MIN";
	private static final String KEY_AVE_SCORE = "AVE";
	private static final String KEY_Temp = "Temp";
	
	//Table shift
	private static final String KEY_WORKINGDATE = "WorkingDay";
	private static final String KEY_TABLE_ID = "TableID";
	
//	Table Create Statements
//	Worker table create statements
	private static final String CREAT_TABLE_WORKERS = 
			"CREATE TABLE " + TABLE_WORKERS + "(" + KEY_ID +" INTEGER PRIMARY KEY,"+ KEY_WORKER_NAME + " TEXT," 
					+ KEY_CREATED + " TEXT," 
					+ KEY_CHECKED + " INTEGER," 
					+ KEY_ENABLED + " INTEGER)";
//  WorkerOffDate table create statements
	private static final String CREATE_TABLE_OFFDATE = "CREATE TABLE " + TABLE_OFFDATE +"(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_WORKERID +  " INTEGER," +  KEY_OFFDATE + " TEXT)" ;
	
	private static final String CREATE_TABLE_SHIFTTABLELIST =
			"CREATE TABLE " + TABLE_SHIFTTABLELIST + "(" + KEY_ID +" INTEGER PRIMARY KEY,"
			+ KEY_SHIFT_TABLE_NAME + " TEXT,"
			+ KEY_SCORE_SUN + "  REAL,"
			+ KEY_SCORE_MON + "  REAL,"
			+ KEY_SCORE_TUE + "  REAL,"
			+ KEY_SCORE_WED + "  REAL,"
			+ KEY_SCORE_THU + "  REAL,"
			+ KEY_SCORE_FRI + "  REAL,"
			+ KEY_SCORE_SAT + "  REAL,"
			+ KEY_NOTE + " TEXT,"
			+ KEY_TABLE_DATE + " TEXT,"
			+ KEY_MAX_SCORE + " REAL,"
			+ KEY_MIN_SCORE + " REAL,"
			+ KEY_AVE_SCORE + " REAL,"
			+ KEY_CREATED + " TEXT,"
			+ KEY_Temp +  " ITEGER)";
	
	private static final String CREATE_TABLE_SHIFTTABLE = 
			"CREATE TABLE " + TABLE_SHIFTABLE + "(" + KEY_ID +" INTEGER PRIMARY KEY,"
			+ KEY_TABLE_ID + " INTEGER,"
			+ KEY_WORKERID + " INTEGER," 
			+ KEY_WORKINGDATE + " TEXT)";

	DateFormat formatter = DateFormat.getDateTimeInstance(
            DateFormat.SHORT, 
            DateFormat.SHORT);
	public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
	
	@Override
	public void onCreate(SQLiteDatabase db) {
			Log.d("XXXX", CREAT_TABLE_WORKERS);
			db.execSQL(CREAT_TABLE_WORKERS);
			Log.d("XXXX", CREATE_TABLE_OFFDATE);
			db.execSQL(CREATE_TABLE_OFFDATE);
			Log.d("XXXX", CREATE_TABLE_SHIFTTABLE);
			db.execSQL(CREATE_TABLE_SHIFTTABLE);
			Log.d("XXXX", CREATE_TABLE_SHIFTTABLELIST);
			db.execSQL(CREATE_TABLE_SHIFTTABLELIST);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFDATE);	
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIFTABLE );
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIFTTABLELIST);	
	
		onCreate(db);
	}
	


	public long createWorker(SWorker sworker){;
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_WORKER_NAME,sworker.getWorker_name());
		values.put(KEY_CREATED, formatter.format(Calendar.getInstance().getTime()));
		values.put(KEY_CHECKED,sworker.isChecked()?1:0);
		values.put(KEY_ENABLED, 1);
		long sworker_id = db.insert(TABLE_WORKERS,null, values);
		return sworker_id;
	}
	
	
	public long createOffdate(long id , Date date){
;
		SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_WORKERID,id);
		values.put(KEY_OFFDATE, df.format(date));

		long offdate_id = db.insert(TABLE_OFFDATE,null, values);
		
		return offdate_id;
	}
	public long createOffdate(long id , String datestr){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_WORKERID,id);
		values.put(KEY_OFFDATE, datestr);

		long offdate_id = db.insert(TABLE_OFFDATE,null, values);
		
		return offdate_id;
	}
	public long createShiftTable(ShiftTable st){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_SHIFT_TABLE_NAME,st.getTable_Name());
		values.put(KEY_SCORE_SUN, st.getScore().get(1) );
		values.put(KEY_SCORE_MON, st.getScore().get(2) );
		values.put(KEY_SCORE_TUE, st.getScore().get(3) );
		values.put(KEY_SCORE_WED, st.getScore().get(4) );
		values.put(KEY_SCORE_THU, st.getScore().get(5) );
		values.put(KEY_SCORE_FRI, st.getScore().get(6) );
		values.put(KEY_SCORE_SAT, st.getScore().get(7) );
		
		values.put(KEY_CREATED, formatter.format(Calendar.getInstance().getTime()));
		values.put(KEY_MAX_SCORE, st.getMaxScore() );
		values.put(KEY_MIN_SCORE, st.getMinScore() );
		values.put(KEY_AVE_SCORE, st.getAveScore() );
		values.put(KEY_NOTE, st.getNote());
		values.put(KEY_TABLE_DATE, st.getDate());
		values.put(KEY_Temp, st.isTemp()?1:0);
		
		
		
		long tableid = db.insert(TABLE_SHIFTTABLELIST,null,values);
		
		return tableid;
	}
	public long createShift(Shift shift,long table_id){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TABLE_ID, table_id);
		values.put(KEY_WORKERID, shift.getId());
		values.put(KEY_WORKINGDATE, shift.getDate());
		
		long shiftid = db.insert(TABLE_SHIFTABLE,null,values);
		
		return shiftid;
	}
	public ArrayList<Shift> getShifts(long table_id){
		ArrayList<Shift> shifts = new ArrayList<Shift>();
		
		String sqlstr = " SELECT * FROM " + TABLE_SHIFTABLE + " WHERE " + KEY_TABLE_ID + " = " + table_id;
		Log.e(LOG, sqlstr);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(sqlstr, null);
		if (c.moveToFirst()) {
			do{

				String date = c.getString(c.getColumnIndex(KEY_WORKINGDATE));
				int id = c.getInt(c.getColumnIndex(KEY_WORKERID));
				Shift shift = new Shift(date, id);
				shifts.add(shift);
			}while(c.moveToNext());
		}
		
		
		return shifts;
	}
	
	public ShiftTable getShiftTable(long table_id){
		String sqlstr = "SELECT * FROM " + TABLE_SHIFTTABLELIST +" WHERE " + KEY_ID + " = " + table_id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(sqlstr, null);
		if(c!=null)
			c.moveToFirst();
		HashMap<Integer, Double> score = new HashMap<Integer, Double>();
		score.put(1,c.getDouble(c.getColumnIndex(KEY_SCORE_SUN)));
		score.put(2,c.getDouble(c.getColumnIndex(KEY_SCORE_MON)));
		score.put(3,c.getDouble(c.getColumnIndex(KEY_SCORE_TUE)));
		score.put(4,c.getDouble(c.getColumnIndex(KEY_SCORE_WED)));
		score.put(5,c.getDouble(c.getColumnIndex(KEY_SCORE_THU)));
		score.put(6,c.getDouble(c.getColumnIndex(KEY_SCORE_FRI)));
		score.put(7,c.getDouble(c.getColumnIndex(KEY_SCORE_SAT)));
		String name = c.getString(c.getColumnIndex(KEY_SHIFT_TABLE_NAME));
		String created = c.getString(c.getColumnIndex(KEY_CREATED));
		String note = c.getString(c.getColumnIndex(KEY_NOTE));
		Long id = c.getLong(c.getColumnIndex(KEY_ID));
		String Datestr = c.getString(c.getColumnIndex(KEY_TABLE_DATE));
		boolean istemp = c.getInt(c.getColumnIndex(KEY_Temp))==1;
		Double MAX = c.getDouble(c.getColumnIndex(KEY_MAX_SCORE));
		Double MIN = c.getDouble(c.getColumnIndex(KEY_MIN_SCORE));
		Double AVE = c.getDouble(c.getColumnIndex(KEY_AVE_SCORE));

		ShiftTable st = new ShiftTable(name, score, note, istemp, created,id,Datestr, MAX,MIN, AVE);
		
		
		return st;
	}
	public ArrayList<ShiftTable> getAllShiftTable(){
		String sqlstr = "SELECT * FROM " + TABLE_SHIFTTABLELIST ;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(sqlstr, null);
		 ArrayList<ShiftTable> allSiftTable = new ArrayList<ShiftTable>();
		if (c.moveToFirst()) {
			do{
				HashMap<Integer, Double> score = new HashMap<Integer, Double>();
				score.put(1,c.getDouble(c.getColumnIndex(KEY_SCORE_SUN)));
				score.put(2,c.getDouble(c.getColumnIndex(KEY_SCORE_MON)));
				score.put(3,c.getDouble(c.getColumnIndex(KEY_SCORE_TUE)));
				score.put(4,c.getDouble(c.getColumnIndex(KEY_SCORE_WED)));
				score.put(5,c.getDouble(c.getColumnIndex(KEY_SCORE_THU)));
				score.put(6,c.getDouble(c.getColumnIndex(KEY_SCORE_FRI)));
				score.put(7,c.getDouble(c.getColumnIndex(KEY_SCORE_SAT)));
				String name = c.getString(c.getColumnIndex(KEY_SHIFT_TABLE_NAME));
				String created = c.getString(c.getColumnIndex(KEY_CREATED));
				String note = c.getString(c.getColumnIndex(KEY_NOTE));
				Long id = c.getLong(c.getColumnIndex(KEY_ID));
				String Datestr = c.getString(c.getColumnIndex(KEY_TABLE_DATE));
				boolean istemp = c.getInt(c.getColumnIndex(KEY_Temp))==1;
				Double MAX = c.getDouble(c.getColumnIndex(KEY_MAX_SCORE));
				Double MIN = c.getDouble(c.getColumnIndex(KEY_MIN_SCORE));
				Double AVE = c.getDouble(c.getColumnIndex(KEY_AVE_SCORE));

				ShiftTable st = new ShiftTable(name, score, note, istemp, created,id,Datestr, MAX,MIN, AVE);
				allSiftTable.add(st);
			}while(c.moveToNext());
		}
		
		
		return allSiftTable;
	}
	
	public SWorker getWorker(long sworker_id){
		String sqlstr = "SELECT * FROM " + TABLE_WORKERS +" WHERE " + KEY_ID + " = " +sworker_id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(sqlstr, null);
		SWorker sworker = new SWorker();
		if(c!=null)
			c.moveToFirst();
		sworker.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		sworker.setWorker_name(c.getString(c.getColumnIndex(KEY_WORKER_NAME)));
		sworker.setCreated(c.getString(c.getColumnIndex(KEY_CREATED)));
		sworker.setChecked(c.getInt(c.getColumnIndex(KEY_CHECKED))==1);
		return sworker;
	}
	
	public List<SWorker> getAllActiveWorker(){
		String sqlstr = " SELECT * FROM " + TABLE_WORKERS + " WHERE " + KEY_ENABLED + " = 1" ;
		Log.e(LOG, sqlstr);
		SQLiteDatabase db = this.getReadableDatabase();
		List<SWorker> workerlist = new ArrayList<SWorker>();
		Cursor c = db.rawQuery(sqlstr, null);
		if (c.moveToFirst()) {
			do{
				SWorker sworker = new SWorker();
				sworker.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				sworker.setWorker_name(c.getString(c.getColumnIndex(KEY_WORKER_NAME)));
				sworker.setCreated(c.getString(c.getColumnIndex(KEY_CREATED)));
				sworker.setChecked(c.getInt(c.getColumnIndex(KEY_CHECKED))==1);
				workerlist.add(sworker);
				
			}while(c.moveToNext());
		}
		return workerlist;
		
	}
	
	

	public HashMap<Long ,String> getOffdate(long worker_id) {
		HashMap<Long ,String> offdate = new HashMap<Long ,String>();
		String sqlstr = "SELECT * FROM " + TABLE_OFFDATE + " WHERE " + KEY_WORKERID + " = " + worker_id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(sqlstr, null);
		Log.e(LOG, sqlstr);
		if(c.moveToFirst()){
			do{
				offdate.put(c.getLong(c.getColumnIndex(KEY_ID)),c.getString(c.getColumnIndex(KEY_OFFDATE)));
			}while(c.moveToNext());
		}
			
		return offdate;
	}
	public ArrayList<String> getOffdateList(long worker_id) {
		ArrayList<String> offdate = new ArrayList<String>();
		String sqlstr = "SELECT * FROM " + TABLE_OFFDATE + " WHERE " + KEY_WORKERID + " = " + worker_id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(sqlstr, null);
		Log.e(LOG, sqlstr);
		if(c.moveToFirst()){
			do{
				offdate.add(c.getString(c.getColumnIndex(KEY_OFFDATE)));
			}while(c.moveToNext());
		}
			
		return offdate;
	}
	public int updateTable(ShiftTable st){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_SHIFT_TABLE_NAME,st.getTable_Name());
		values.put(KEY_NOTE, st.getNote());
		values.put(KEY_Temp, st.isTemp()?1:0);
		return db.update(TABLE_SHIFTTABLELIST, values, KEY_ID + " =" + st.getID(), null);
		
	}
	
	public int updateWorker(SWorker sworker){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_WORKER_NAME,sworker.getWorker_name());
		values.put(KEY_CHECKED,sworker.isChecked()?1:0);
		values.put(KEY_CREATED, sworker.created);
		values.put(KEY_ENABLED, 1);
		Log.e(LOG, values.toString());
		return db.update(TABLE_WORKERS, values, KEY_ID + " =" + sworker.getId(), null);
	}
	
	public void deletingShiftTable(long table_id){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SHIFTTABLELIST, KEY_ID +  " = ?",new String[] {String.valueOf(table_id)} );
		db.delete(TABLE_SHIFTABLE, KEY_TABLE_ID + " = ?", new String[] {String.valueOf(table_id)} );
		
	}
	public void deletingWorker(long worker_id){
		SQLiteDatabase db = this.getWritableDatabase();
		SWorker sworker = getWorker(worker_id);
		ContentValues values = new ContentValues();
		values.put(KEY_WORKER_NAME,sworker.getWorker_name());
		values.put(KEY_CHECKED,sworker.isChecked()?1:0);
		values.put(KEY_CREATED, sworker.created);
		values.put(KEY_ENABLED, 0);
		Log.e(LOG, values.toString());
		db.update(TABLE_WORKERS, values, KEY_ID + " =" + sworker.getId(), null);
		
	}
	public void deletingOffdate(long offdate_id){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_OFFDATE, KEY_ID +  " = ?",new String[] {String.valueOf(offdate_id)} );
	}
	public void deletingOffdateByDateAndWorker(String datestr,long worker_id){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_OFFDATE, KEY_OFFDATE + " = ? AND " + KEY_WORKERID + " = ?" , new String[]{datestr,String.valueOf(worker_id)});
	}
	
	public void deletingTempTable(){
	
		String sqlstr = "SELECT * FROM " + TABLE_SHIFTTABLELIST +" WHERE " + KEY_Temp + " =  1 ";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(sqlstr, null);
		if(c!=null)
			c.moveToFirst();
		do{
			deletingShiftTable(c.getLong(c.getColumnIndex(KEY_ID)));
		}while(c.moveToNext());
		
		
		
	}
	
	
	
	public void closeDB(){
		SQLiteDatabase db = this.getReadableDatabase();
		
		if(db != null && db.isOpen())
			db.close();
	}
	

}
