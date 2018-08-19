package ServiceBus;

// ancestor class of every service event
public abstract class ServiceEvent {
	public final String message;
	private byte[] signature; // messages must be signed by the node private key
	 
    public ServiceEvent(String _message) {
        this.message = _message;
    }
    
    // signature should be set only once
    public void setSignature(byte[] _signature){
    	if (this.signature == null) {
    		this.signature = _signature;
    	}
    	else {
    		// error handling, signature should not be set more than once
    	}
    }
	
}
