package ServiceBus;

// abstract implementation for a listener
public interface ServiceListener {
	
	// reaction procedure for an event
	public abstract void EventRaised (ServiceEvent event); 

}
