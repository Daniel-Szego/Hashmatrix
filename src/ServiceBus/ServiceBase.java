package ServiceBus;

// ancestor class for a service
public abstract class ServiceBase implements ServiceInterface {
	protected final String serviceId;
	protected final ServiceBus bus;
	
	public ServiceBase(String _serviceId, ServiceBus _bus) {
		this.serviceId = _serviceId;
		this.bus = _bus;
	}	
	
	public String getServiceId() {
		return serviceId;
	}

	// getting a service bus reference
	public ServiceBus getServiceBus() {
		return bus;
	}
	
}
