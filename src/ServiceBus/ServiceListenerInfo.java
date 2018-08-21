package ServiceBus;

public class ServiceListenerInfo {
	public ServiceListener serviceListener;
	public Class typeOfEvent;	
	
	public ServiceListenerInfo(ServiceListener _serviceListener,  Class _typeOfEvent) {
		this.serviceListener = _serviceListener;
		this.typeOfEvent = _typeOfEvent;
	}
	
}
