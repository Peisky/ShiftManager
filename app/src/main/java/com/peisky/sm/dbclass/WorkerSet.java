package com.peisky.sm.dbclass;

public class WorkerSet {
	int id;
	int Set_id ;
	int Worker_id;
	String Offdate;
	public String getOffdate() {
		return Offdate;
	}
	public void setOffdate(String offdate) {
		Offdate = offdate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSet_id() {
		return Set_id;
	}
	public void setSet_id(int set_id) {
		Set_id = set_id;
	}
	public int getWorker_id() {
		return Worker_id;
	}
	public void setWorker_id(int worker_id) {
		Worker_id = worker_id;
	}
	public WorkerSet() {
		super();
	}
	public WorkerSet(int id, int set_id, int worker_id) {
		super();
		this.id = id;
		Set_id = set_id;
		Worker_id = worker_id;
	}
}
