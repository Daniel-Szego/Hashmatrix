package ServiceBus;

public class ServiceListenerInfo {
	public ServiceListenerInterface serviceListener;
	public Class typeOfEvent;	
	
	public ServiceListenerInfo(ServiceListenerInterface _serviceListener,  Class _typeOfEvent) {
		this.serviceListener = _serviceListener;
		this.typeOfEvent = _typeOfEvent;
	}
	
}
