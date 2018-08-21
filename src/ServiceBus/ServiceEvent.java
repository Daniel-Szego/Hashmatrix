package ServiceBus;

import Utils.*;

// ancestor class of every service event
public class ServiceEvent {
	public final String message;
	public final Service source; 
	public final Severity severity;
	private byte[] signature; // messages must be signed by the node private key
	 	
    public ServiceEvent(String _message, Service _source, Severity _severity) {
        this.message = _message;
        this.source = _source;
        this.severity = _severity;
    }
   
    public ServiceEvent(String _message, Service _source) {
    	this(_message, _source, Severity.INFO);
    }

    public ServiceEvent(String _message) {
    	this(_message, null, Severity.INFO);
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
