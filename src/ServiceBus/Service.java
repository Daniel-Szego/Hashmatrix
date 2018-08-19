package ServiceBus;

// ancestor class for a service
public abstract class Service {
	public final String serviceId;
	
	public Service(String _serviceId) {
		this.serviceId = _serviceId;
	}
	
}
