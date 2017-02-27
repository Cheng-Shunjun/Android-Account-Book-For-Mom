package com.example.acountformom;

public class ListItem {
	
    private String name;
    private String sumWeight;
    private String time;
    
    public ListItem(String name, String sumWeight, String time) {
        this.name   = name;
        this.sumWeight = sumWeight;
        this.time = time;
    }
    public void setName(String name) {
        this.name = name;
    }   
    public String getName() {
        return this.name;
    }
    
    public void setSumweight(String sumWeight) {
        this.sumWeight  = sumWeight;
    } 
    public String getSumWeight() {
        return this.sumWeight;
    }
    
    public void setTime(String time) {
        this.time = time;
    } 
    public String getTime() {
        return this.time;
    }
}
