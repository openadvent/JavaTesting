package com.thisisnoble.javatest.events;

import com.thisisnoble.javatest.Event;

public class MarginEvent implements Event {

    private final String id;
    private final String parentId;
    private final double margin;
    private boolean parentFlag;
    private String parentObjectID = null;
    private int numOfChildren;

    public MarginEvent(String id, String parentId, double margin,String objectID) {
        this.id = id;
        this.parentId = parentId;
        this.margin = margin;
        this.parentFlag = false;
        this.parentObjectID = objectID;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public double getMargin() {
        return margin;
    }
    
    public void setParentFlag(boolean parentFlag) { 
    	this.parentFlag = parentFlag;
    }
    
    public boolean isParentEvent() {
    	
    	return this.parentFlag;
    }
    
    public void setParentObjectID(String objectID) {
    	
    	this.parentObjectID = objectID;
    }
    	
    public String getParentObjectID() {
    	
    	return parentObjectID;
    }
    public void setNumOfChildren(int number) {
    	this.numOfChildren = number;
    }
    
    public int getNumOfChildren() {
    	return numOfChildren;
    }
    
}
