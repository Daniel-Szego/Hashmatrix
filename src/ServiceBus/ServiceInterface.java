package ServiceBus;

// ancestor interface for the services
public interface ServiceInterface {
	
	// getting the service id
	public String getServiceId(); 
	
	// getting a service bus reference
	public ServiceBus getServiceBus();
}
