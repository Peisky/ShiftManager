package com.peisky.sm.dbclass;

import com.peisky.sm.dataclass.Worker;

public class SWorker {
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ID = " +id+ " Name = " + worker_name + " created = "+created + " ischeckde = " +isChecked();
 	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	int id; 
	String worker_name;
	String created;
	boolean checked;
	public SWorker() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWorker_name() {
		return worker_name;
	}
	public void setWorker_name(String worker_name) {
		this.worker_name = worker_name;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public Worker toWorker(){
		Worker w = new Worker(0, worker_name, 0, id);
		
		
		return w;
	}
}
