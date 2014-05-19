package com.thisisnoble.javatest.impl;

import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import com.thisisnoble.javatest.Event;
import com.thisisnoble.javatest.Orchestrator;
import com.thisisnoble.javatest.Processor;
import com.thisisnoble.javatest.Publisher;


public class OrchestratorImpl implements Orchestrator {
	
	private Publisher publisher;	 
	//Queue to hold all the parent events
	private final BlockingQueue parentsEventsQueue = new LinkedBlockingQueue<Event>();
	//Container to hold all the processors that are registered.
	private final ConcurrentHashMap<String,Processor> processorList = new ConcurrentHashMap<String,Processor>();
	//Thread Pool to process parent events concurrently 
	private final ExecutorService parentEventsthreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	//Container to hold futures tasks. Each task is dedicated to handle parent and child requests that are tied together.
	private final ConcurrentHashMap<String,EventFutureTask<Long>> futureTaskList = new ConcurrentHashMap<String,EventFutureTask<Long>>();
	
	public void register(Processor processor) {
		
		//Get the className and add the processor to the list.
		String className = processor.getClass().getName();
		this.processorList.put(className, processor);
	}
	
	public void receive(Event event) {
		
		try {
			
			//This can be further streamlined by moving the event processing
			//out of this receive method.The receive method is not the right
			//place for processing and it should just receive the event and return
			//to calling method immediately for quick processing of all the incoming requests. 
			if(event.isParentEvent()) {
					//UUID is used to tie parent and it's corresponding child events together.
					String uniqueObjetID = UUID.randomUUID().toString();
					event.setParentObjectID(uniqueObjetID);
					parentsEventsQueue.put(event);
					HandleParentEvent handleEventTask = new HandleParentEvent();
					parentEventsthreadPool.submit(handleEventTask);
			}
			else {
				//Handle child events.
				EventFutureTask<Long> futureTask = futureTaskList.get(event.getParentObjectID());
				futureTask.getCallable().addChildElementToQueue(event);
				
			}
		} catch (InterruptedException e) {	
			// TODO Handle the exception.
			e.printStackTrace();
		}
	}
	
	public void setup(Publisher publisher) {
		
		this.publisher = publisher;
	}

	public void publish(Event event) {
		publisher.publish(event);
	}
	
	//Process all the events by invoking the processors.
	public void processEvent(Event event) {
			
			for (Entry<String,Processor> element : processorList.entrySet()) {
				
					Processor processor = element.getValue();
					if(processor.interestedIn(event)) {
						processor.process(event);
					}
					
			}	
		
	}
	
	//This Runnable class will handle all the parent and
	//it's child events in a separate thread.
	private class HandleParentEvent implements Runnable {
			
			@Override
	        public void run() {
				Event event;
				try {
					event = (Event)parentsEventsQueue.take();
					EventCallable eventCallable = new EventCallable(event.getNumOfChildren()+1);
					FutureResultHandler futureResultHandler = new FutureResultHandler();
					EventFutureTask<Long> eventFutureTask = new EventFutureTask<Long>(eventCallable,futureResultHandler);
					eventFutureTask.getCallable().addChildElementToQueue(event);
					futureTaskList.put(event.getParentObjectID(), eventFutureTask);				
					Thread childThread = new Thread(eventFutureTask);
					childThread.start();
					System.out.println("Waiting for latch");
					eventFutureTask.getCountDownlatch().await();
					System.out.println("Acquired latch");
					System.out.println("Remove FutureTask");
					futureTaskList.remove(event.getParentObjectID());
					
				} catch (InterruptedException e) {
					// TODO Handle exception.
					e.printStackTrace();
				}
	            
	        }
		}
	
	//Interface that's used to perform processing after receiving all the 
	//child events and Future task is completed.
	public interface taskResultHandler {  
	    public void taskCompleted(Future result, Object compositeEvent,CountDownLatch latch);  
	}
	
	public class FutureResultHandler implements taskResultHandler {
		
		public void taskCompleted(Future result,Object compositeEvent,CountDownLatch latch) {
			
			System.out.println("Task Completed");
			System.out.println("Publishing..");
			publish((CompositeEvent)compositeEvent);
			System.out.println("Decrement latch");
			latch.countDown();
						
		}
	}
	
	//This callable class is used to handle parent and it's
	//child events in a separate thread.This callable
	//class is passed to FutureTask.
	public class EventCallable implements Callable<Long> {
		
		private int numOfChilds;
		private final BlockingQueue eventsQueue = new LinkedBlockingQueue<Event>();
		private CompositeEvent compositeEvent = null;
		public EventCallable(int numOfChilds) { this.numOfChilds = numOfChilds; }
		public CompositeEvent getCompositeElement() { return compositeEvent; }
		
		public void addChildElementToQueue(Event event) {
			
			try {
				eventsQueue.put(event);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public Long call() {
			
			int i=numOfChilds;
			while(i>0) {
				
				try {
					
				   Event event = (Event)eventsQueue.take();
				   if(event.isParentEvent()) {
					   System.out.println("Parent ID - " + event.getId());
					   compositeEvent = new CompositeEvent(event.getId(),event,event.getParentObjectID());
				   }
				   else {
					   System.out.println("Child ID - " + event.getId());
					   compositeEvent.addChild(event);
				   }
				   
				   processEvent(event);
				   System.out.println("Value of i:" + i);
				   i--;
				}
				catch(InterruptedException e)  {
					
				}
			}
			
			//Thread ID can be used if and where needed.
			return Thread.currentThread().getId();
		}
		
	}
	
	//FutureTask to handle parent and Child events
	//together.
	public class EventFutureTask<V> extends FutureTask<V> {  
		  
	    private FutureResultHandler futureResultHandler;  
	    EventCallable callable;
	    final CountDownLatch latch = new CountDownLatch(1);
	    public EventFutureTask(Callable<V> callable, FutureResultHandler futureResultHandler) {  
	        super(callable); 
	        this.callable = (EventCallable)callable;
	        this.futureResultHandler = futureResultHandler;  
	    }
	    public CountDownLatch getCountDownlatch() { return latch; }
	    
	    public EventCallable getCallable() { return callable; }
	  
	    @Override  
	    protected void done() {  
	    	futureResultHandler.taskCompleted(this,getCallable().getCompositeElement(),latch);  
	    }
	    
	}  
	

}
