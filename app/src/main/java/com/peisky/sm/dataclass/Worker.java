package com.peisky.sm.dataclass;


public class Worker implements Comparable<Worker> {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private double Score = 0;
	private String Name;
	private int Order;
	private int id;
	public Worker(Double score, String name) {
		super();
		this.Score = score;
		this.Name = name;
		
		
	}
	public Worker(double score, String name, int order, int id) {
		super();
		Score = score;
		Name = name;
		Order = order;
		this.id = id;
	}
	public void addScore(double s){
		this.Score = this.Score+s;
	}
	public Worker() {
		super();
		
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return Name;
	}
	public double getScore() {
		return Score;
	}
	public Worker(double score, String name) {
		super();
		Score = score;
		Name = name;
	}
	public void setScore(double score) {
		Score = score;
	}
	public void setName(String Name) {
		this.Name = Name;
	}

	public String toString(){
		return Double.toString(this.Score);
		
	}
	
	public int compareTo(Worker o) {
		if (o.Score>Score) {
			return -1;	
		}else if(o.Score < Score){
			return 1;	
		}else{
			if(o.Order > Order){
				return -1;
			}else{
				return 1;
			}
			
		}
		
	}
	public int getOrder() {
		return Order;
	}
	public void setOrder(int order) {
		Order = order;
	}
	public Worker cloneD(){
		return new Worker(Score, Name, Order, id);
	}

}

