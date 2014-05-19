package com.thisisnoble.javatest.events;

import com.thisisnoble.javatest.Event;

public class TradeEvent implements Event {

    private final String id;
    private final double notional;
    private boolean parentFlag;
    private String objectID = null;
    private int numOfChildren;

    public TradeEvent(String id, double notional) {
        this.id = id;
        this.notional = notional;
        this.parentFlag = false;
        
    }
    
    public String getId() {
        return id;
    }

    public double getNotional() {
        return notional;
    }
    
    public void setParentFlag(boolean parentFlag) { 
    	this.parentFlag = parentFlag;
    }
    
    public boolean isParentEvent() {
    	
    	return this.parentFlag;
    }
    
    public void setParentObjectID(String objectID) {
    	
    	this.objectID = objectID;
    }
    
    public String getParentObjectID() {
    	
    	return objectID;
    }
    
    public void setNumOfChildren(int number) {
    	this.numOfChildren = number;
    }
    
    public int getNumOfChildren() {
    	return numOfChildren;
    }
    
}
