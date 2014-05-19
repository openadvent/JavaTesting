package com.thisisnoble.javatest;

//marker interface, add as many interface methods as required
public interface Event {

    String getId();
    boolean isParentEvent();
    void setParentObjectID(String uniqueID);
    String getParentObjectID();
    int getNumOfChildren();
    void setNumOfChildren(int number);
    
}
