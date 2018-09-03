package ServiceBus;

import Block.*;
import Utils.Severity;

// event if the new block has been validated
public class ServiceEventBlockValidated extends ServiceEvent {

	public final BlockInterface block;
	
	public ServiceEventBlockValidated(String _message, ServiceBase _source, BlockInterface _block) {
		super(_message,_source,Severity.INFO);
		this.block = _block;
	}				

}
