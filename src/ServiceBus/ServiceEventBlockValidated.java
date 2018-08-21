package ServiceBus;

import Block.Block;
import Utils.Severity;

// event if the new block has been validated
public class ServiceEventBlockValidated extends ServiceEvent {

	public final Block block;
	
	public ServiceEventBlockValidated(String _message, Service _source, Block _block) {
		super(_message,_source,Severity.INFO);
		this.block = _block;
	}				

}
