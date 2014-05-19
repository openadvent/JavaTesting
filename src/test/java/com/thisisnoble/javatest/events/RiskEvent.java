package com.thisisnoble.javatest.events;

import com.thisisnoble.javatest.Event;

public class RiskEvent implements Event {

    private final String id;
    private final String parentId;
    private final double riskValue;
    private boolean parentFlag;
    private String objectID = null;
    private int numOfChildren;

    public RiskEvent(String id, String parentId, double riskValue,String objectID) {
        this.id = id;
        this.parentId = parentId;
        this.riskValue = riskValue;
        this.parentFlag = false;
        this.objectID = objectID;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public double getRiskValue() {
        return riskValue;
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
