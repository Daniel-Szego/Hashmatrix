package ServiceBus;

import Block.BlockBase;
import Utils.Severity;

// event if the new block has been validated
public class ServiceEventBlockValidated extends ServiceEvent {

	public final BlockBase block;
	
	public ServiceEventBlockValidated(String _message, Service _source, BlockBase _block) {
		super(_message,_source,Severity.INFO);
		this.block = _block;
	}				

}
