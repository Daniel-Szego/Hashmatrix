package ServiceBus;

import Block.BlockBase;
import Utils.Severity;

// event by the miner if a new block has been mined
public class ServiceEventBlockMined extends ServiceEvent {

	public final BlockBase block;
	
	public ServiceEventBlockMined(String _message, Service _source, BlockBase _block) {
		super(_message,_source,Severity.INFO);
		this.block = _block;
	}				
}
