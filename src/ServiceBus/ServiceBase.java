package ServiceBus;

// ancestor class for a service
public abstract class ServiceBase implements ServiceInterface {
	protected final String serviceId;
	
	public ServiceBase(String _serviceId) {
		this.serviceId = _serviceId;
	}	
	
	public String getServiceId() {
		return serviceId;
	}

}
