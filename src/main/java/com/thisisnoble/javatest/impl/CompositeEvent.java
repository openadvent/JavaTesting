package com.thisisnoble.javatest.impl;

import com.thisisnoble.javatest.Event;

import java.util.HashMap;
import java.util.Map;

public class CompositeEvent implements Event {

    private final String id;
    private final Event parent;
    private final Map<String, Event> children = new HashMap<>();
    private String parentObjectID;
    private boolean parentFlag;
    int numOfChildren;

    public CompositeEvent(String id, Event parent,String parentObjectID) {
        this.id = id;
        this.parent = parent;
        this.parentObjectID = parentObjectID;
        this.parentFlag = true;
    }

    public String getId() {
        return id;
    }

    public Event getParent() {
        return parent;
    }

    public CompositeEvent addChild(Event child) {
        children.put(child.getId(), child);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <E extends Event> E getChildById(String id) {
        return (E) children.get(id);
    }

    public int size() {
        return children.size();
    }
    
    public String getParentObjectID() {
    	
    	return parentObjectID;
    }
    
    public boolean isParentEvent() {
    	
    	return parentFlag;
    }
    
    public void setParentObjectID(String objectID) {
    	this.parentObjectID = objectID;
    }
    
    public void setNumOfChildren(int number) {
    	this.numOfChildren = number;
    }
    
    public int getNumOfChildren() {
    	return numOfChildren;
    }
    
}
