package ServiceBus;

import java.util.ArrayList;

import Node.*;
import Utils.Severity;

// experimental implementation of the service bus
public class ServiceBus {
	
	public final Node node;
	ArrayList<ServiceEvent> events;
	ArrayList<ServiceListener> listeners;
	
	public ServiceBus (Node _node) {
		this.node = _node;
		listeners = new ArrayList<ServiceListener>();
		events = new ArrayList<ServiceEvent>();
	}	

	public void addEvent(String _message, Service _source, Severity _severity, boolean async)  {
		ServiceEvent event = new ServiceEvent(_message,_source, _severity);
		events.add(event);
		
		// calling listeners
		for(ServiceListener listener: listeners) {			
			if (!async) 
				listener.EventRaised(event);
			else {
				new Thread(new Runnable() {
				     public void run() {
				    	 listener.EventRaised(event);
				     }
				}).start();	
			}				
		}	
	}

	public void addEvent(String _message, Service _source, Severity _severity){
		addEvent(_message, _source, _severity, true);
	}
	
	public void addEvent(String _message, Service _source)  {
		addEvent(_message, _source, Severity.INFO, true);
	}
	
	public void addEvent(String _message)  {
		addEvent(_message, null, Severity.INFO, true);
	}

	public void addServiceListener(ServiceListener _listener) {
		listeners.add(_listener);
	}
	
}
