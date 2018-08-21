package ServiceBus;

// abstract implementation for a listener
public interface ServiceListenerInterface {
	
	// reaction procedure for an event
	public abstract void EventRaised (ServiceEvent event); 

}
