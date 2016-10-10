package com.peisky.sm.functionclass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import android.util.Log;

import com.peisky.sm.dataclass.AnalyticData;
import com.peisky.sm.dataclass.ShiftConf;
import com.peisky.sm.dataclass.ShiftTableContainer;
import com.peisky.sm.dataclass.Worker;

public class shifter {
	private Calendar cal;
	private TreeMap<Integer, Worker> n_workerlist; 
	private ShiftTableContainer container;
	private Map<Integer,Double> ScoreArray ;
	private Map<Integer, ArrayList<String>> n_offdatelist;
	private boolean Complete = false;
	private boolean weekendshift  = false;
	private boolean wk_no_cont = false;
	private int day_no_cont = 0;
	
	int year;
	int month;
	ArrayList<TreeMap<String, ArrayList<Integer>>> newshiftlist;
	ArrayList<AnalyticData> scoreData;
	
	
	SimpleDateFormat sdf;
	
	public shifter(Calendar cal, TreeMap<Integer, Worker> n_workerlist,
			Map<Integer, Double> scoreArray,
			Map<Integer, ArrayList<String>> n_offdatelist,
			boolean weekendshift, boolean wk_no_cont,int day_no_conts) {
		super();
		this.cal = Calendar.getInstance();
		this.cal.setTime(cal.getTime());
		this.n_workerlist = n_workerlist;
		ScoreArray = scoreArray;
		this.n_offdatelist = n_offdatelist;
		this.weekendshift = weekendshift;
		this.wk_no_cont = wk_no_cont;
		this.day_no_cont  = day_no_conts;
	}
	public shifter(Calendar cal, TreeMap<Integer, Worker> n_workerlist,Map<Integer, ArrayList<String>> n_offdatelist, ShiftConf conf){
		this.cal = Calendar.getInstance();
		this.cal.setTime(cal.getTime());
		this.n_workerlist = n_workerlist;
		ScoreArray = conf.getScoreArray();
		this.n_offdatelist = n_offdatelist;
		this.weekendshift = conf.isWeekendshift();
		this.wk_no_cont = conf.isWeekendshift();
		this.day_no_cont  = conf.getDay_no_cont();
	}

	public ShiftTableContainer new_shift_multi(){
		newshiftlist =  new ArrayList<TreeMap<String,ArrayList<Integer>>>();
		sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.TAIWAN);
		scoreData = new ArrayList<AnalyticData>();
		container = new ShiftTableContainer(newshiftlist, scoreData);
		
		int number = n_offdatelist.size();
		Log.d("XXXX", number + "");
		cal.set(Calendar.DAY_OF_MONTH,1);
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		Complete = false;
		
		List<Integer> array = new ArrayList<Integer>();
		for(int X = 1;X<=number;X++){
			array.add(X);
		}
		List<List<Integer>> allList =Permutation.perm(array); 
		Object[] keylist = n_workerlist.keySet().toArray();
		
		TreeMap<Integer, Worker> templist = new TreeMap<Integer, Worker>(n_workerlist);
		ArrayList<Doit> t_list = new ArrayList<Doit>();
		cal.set(Calendar.DAY_OF_MONTH,1);
		for(int J = 0;J<allList.size() ;J+=n_workerlist.size()){
			
			for(int X = 0;X<keylist.length;X++){
				templist.get(keylist[X]).setOrder(allList.get(J).get(X));
				templist.get(keylist[X]).setScore(0);
			}
			TreeMap<Integer, Worker> ct = new TreeMap<Integer, Worker>();
			Set<Integer> ks = templist.keySet();

			for (Integer integer : ks) {
				ct.put(integer, templist.get(integer).cloneD());
			}
			Log.d("XXXX", "Thread " + J);
//			Thread t = new Thread(new Doit(ct));
			Doit t =  new Doit(ct);
			t.start();
			t_list.add(t);
			
			
		}
		
		while(!Complete){
			int c_counter=0;
			for(int c = 0 ; c <t_list.size(); c++){
				if(t_list.get(c).getState() != Thread.State.TERMINATED)
					c_counter++;
			}
			if(c_counter==0)
				Complete = true;
		}
		
		
		return container;
		
		
	}
	public ShiftTableContainer new_shift(){
		newshiftlist =  new ArrayList<TreeMap<String,ArrayList<Integer>>>();
		sdf = new SimpleDateFormat("yyyy/MM/dd");
		scoreData = new ArrayList<AnalyticData>();
		ShiftTableContainer container = new ShiftTableContainer(newshiftlist, scoreData);
		int number = n_offdatelist.size();
		cal.set(Calendar.DAY_OF_MONTH,1);
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		
		
		List<Integer> array = new ArrayList<Integer>();
		for(int X = 1;X<=number;X++){
			array.add(X);
		}
		List<List<Integer>> allList =Permutation.perm(array); 
		Object[] keylist = n_workerlist.keySet().toArray();
		
		TreeMap<Integer, Worker> templist = new TreeMap<Integer, Worker>(n_workerlist);
		int LastDayofMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		
		
		
		for(int J = 0;J<allList.size();J+=n_workerlist.size()){
			for(int X = 0;X<keylist.length;X++){
				templist.get(keylist[X]).setOrder(allList.get(J).get(X));
				templist.get(keylist[X]).setScore(0);
			}
			cal.set(Calendar.DAY_OF_MONTH,1);
			TreeMap<String, ArrayList<Integer>> tempmap = new TreeMap<String, ArrayList<Integer>>();
			Map<Integer, ArrayList<String>> tempoffdate = new TreeMap<Integer, ArrayList<String>>();
			
//			TreeMap<Integer, Worker> ct = new TreeMap<Integer, Worker>();
//			Set<Integer> ks = templist.keySet();
//			for (Integer integer : ks) {
//				ct.put(integer, templist.get(integer).clone());
//			}
//			
//			Thread t = new Thread(new Doit(ct));
//			t.start();
			
			TreeMap<Integer, Integer> weekendcount = new TreeMap<Integer, Integer>();
			boolean notEnough = false;
			int weekmaxcount= 0;
			for (int i = 0; i < LastDayofMonth; i++) {
				cal.set(year,month,i+1);
				ArrayList<Integer> namelist = new ArrayList<Integer>();
				tempmap.put(sdf.format(cal.getTime()), namelist);
				
				int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
				boolean isSun = dayofweek == Calendar.SATURDAY;
				boolean isSat = dayofweek == Calendar.SUNDAY;
				
				if(!weekendshift && (isSun || isSat)){
					System.out.println(cal.getTime());
					continue;
				}
				if(isSun||isSat){
					
				}
				
				SortedSet<Map.Entry<Integer,Worker>> temp = entriesSortedByValues(templist);
				int id = temp.first().getKey();
				Iterator<Entry<Integer, Worker>> it = temp.iterator();
				
				int N = 1;
				int count = 0;
				
				while(count  < N & it.hasNext()){
					if(!tempoffdate.containsKey(id)){
						ArrayList<String> datemap = new ArrayList<String>(); 
						tempoffdate.put(id, datemap);
					}
					if(!weekendcount.containsKey(id)){
						weekendcount.put(id, 0);
					}
					
					if(!n_offdatelist.get(id).contains(sdf.format(cal.getTime())) && !tempoffdate.get(id).contains(sdf.format(cal.getTime()))){
						for(int daysnocont = 0;daysnocont < day_no_cont;daysnocont++){
							cal.set(Calendar.DAY_OF_MONTH,i+1+daysnocont);
							tempoffdate.get(id).add(sdf.format(cal.getTime()));
						}
						
						templist.get(id).addScore(ScoreArray.get(dayofweek));
						namelist.add(id);
						count++;
						if(isSat||isSun){
							Integer I = weekendcount.get(id);
							if(I>weekmaxcount){
								continue;
							}else{
								I++;
								weekmaxcount = I.intValue();
							}
						}
						if(wk_no_cont ){
							if(isSat){
								cal.set(year,month,i+7);
								tempoffdate.get(id).add(sdf.format(cal.getTime()));
								cal.set(year,month,i+8);
								tempoffdate.get(id).add(sdf.format(cal.getTime()));
								
							}else if(isSun){
								cal.set(year,month,i+8);
								tempoffdate.get(id).add(sdf.format(cal.getTime()));
								cal.set(year,month,i+9);
								tempoffdate.get(id).add(sdf.format(cal.getTime()));
							}
						}
					}
					id = it.next().getKey();
				}
				if( namelist.size()<N){
					notEnough = true;
				}
				
			}
			System.out.println("out"+notEnough);
			if (notEnough) {
				continue;
			}
			ArrayList<Double> data = new ArrayList<Double>();
			for(int X = 0;X<keylist.length;X++){
				data.add(templist.get(keylist[X]).getScore());
				
			}
			AnalyticData ana = new AnalyticData(data);
			
			ana.setWeekendScore(weekmaxcount);
				scoreData.add(new AnalyticData(data));
				newshiftlist.add(tempmap);
			
			if(scoreData.size()>10){
				return container;
			}
		}
		
		
		
		return container;
	}

	
	public  <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                return e1.getValue().compareTo(e2.getValue());
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}

	class Doit extends Thread {
		private TreeMap<Integer, Worker> tlist;
	
		public Doit(TreeMap<Integer, Worker> templist) {
			super();
			this.tlist = templist;
		}

		@Override
		public void run() {
			Calendar the_cal = Calendar.getInstance();
			the_cal.setTime(cal.getTime());

			SimpleDateFormat TSDF = new SimpleDateFormat("yyyy/MM/dd",Locale.TAIWAN);
			
			TreeMap<String, ArrayList<Integer>> tempmap = new TreeMap<String, ArrayList<Integer>>();
			Map<Integer, ArrayList<String>> tempoffdate = new TreeMap<Integer, ArrayList<String>>();
			boolean notEnough = false;
			Object[] keylist = n_workerlist.keySet().toArray();
				
			TreeMap<Integer, Integer> weekendcount = new TreeMap<Integer, Integer>();
			int LastDayofMonth = the_cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			int weekmaxcount= 0;
			
			for (int i = 0; i < LastDayofMonth; i++) {
				
				Log.d("LOGX", i + "  " +tlist.toString() );
				the_cal.set(year,month,i+1);
				ArrayList<Integer> namelist = new ArrayList<Integer>();
				tempmap.put(TSDF.format(the_cal.getTime()), namelist);
				
				int dayofweek = the_cal.get(Calendar.DAY_OF_WEEK);
				boolean isSun = dayofweek == Calendar.SATURDAY;
				boolean isSat = dayofweek == Calendar.SUNDAY;
				
				if(!weekendshift && (isSun || isSat)){
					System.out.println(the_cal.getTime());
					continue;
				}
				if(isSun||isSat){
					
				}
				
				SortedSet<Map.Entry<Integer,Worker>> temp = entriesSortedByValues(tlist);
				int id = temp.first().getKey();
				Iterator<Entry<Integer, Worker>> it = temp.iterator();
				
				int N = 1;
				int count = 0;
				
				while(count  < N & it.hasNext()){
					if(Complete){
						return;
					}
	
					if(!tempoffdate.containsKey(id)){
						ArrayList<String> datemap = new ArrayList<String>(); 
						tempoffdate.put(id, datemap);
					}
					if(!weekendcount.containsKey(id)){
						weekendcount.put(id, 0);
					}
					
					if(!n_offdatelist.get(id).contains(TSDF.format(the_cal.getTime())) && !tempoffdate.get(id).contains(TSDF.format(the_cal.getTime()))){
						for(int daysnocont = 0;daysnocont < day_no_cont;daysnocont++){
							the_cal.set(Calendar.DAY_OF_MONTH,i+1+daysnocont);
							tempoffdate.get(id).add(TSDF.format(the_cal.getTime()));
						}
						
						tlist.get(id).addScore(ScoreArray.get(dayofweek));
						namelist.add(id);
						count++;
						if(isSat||isSun){
							Integer I = weekendcount.get(id);
							if(I>weekmaxcount){
								continue;
							}else{
								I++;
								weekmaxcount = I.intValue();
							}
						}
						if(wk_no_cont ){
							if(isSat){
								the_cal.set(year,month,i+7);
								tempoffdate.get(id).add(TSDF.format(the_cal.getTime()));
								the_cal.set(year,month,i+8);
								tempoffdate.get(id).add(TSDF.format(the_cal.getTime()));
								
							}else if(isSun){
								the_cal.set(year,month,i+8);
								tempoffdate.get(id).add(TSDF.format(the_cal.getTime()));
								the_cal.set(year,month,i+9);
								tempoffdate.get(id).add(TSDF.format(the_cal.getTime()));
							}
						}
					}
					id = it.next().getKey();
				}
				if( namelist.size()<N){
					System.out.println("out"+notEnough);
					return;
				}
			}
			
	
			ArrayList<Double> data = new ArrayList<Double>();
			for(int X = 0;X<keylist.length;X++){
				data.add(tlist.get(keylist[X]).getScore());
				
			}
			Log.d("XXXX",tempmap.toString());
			if(Complete){
				return;
			}
			synchronized (container) {
				scoreData.add(new AnalyticData(data));
				newshiftlist.add(tempmap);
				if(newshiftlist.size()>20 ){
					Complete = true;
				}
			}
	
//				Complete = true;
			
			
		}
		
	}
	
}
