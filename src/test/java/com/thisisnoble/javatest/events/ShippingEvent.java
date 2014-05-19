package com.thisisnoble.javatest.events;

import com.thisisnoble.javatest.Event;

public class ShippingEvent implements Event {

    private final String id;
    private final String parentId;
    private final double shippingCost;
    private boolean parentFlag;
    private String objectID = null;
    private int numOfChildren;

    public ShippingEvent(String id, double shippingCost) {
        this(id, null, shippingCost,null);
    }

    public ShippingEvent(String id, String parentId, double shippingCost,String ObjectId) {
        this.id = id;
        this.parentId = parentId;
        this.shippingCost = shippingCost;
        this.objectID = ObjectId;
        this.parentFlag = false;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public double getShippingCost() {
        return shippingCost;
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
