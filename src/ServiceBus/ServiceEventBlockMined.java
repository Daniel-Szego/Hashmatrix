package ServiceBus;

import Block.Block;
import Utils.Severity;

// event by the miner if a new block has been mined
public class ServiceEventBlockMined extends ServiceEvent {

	public final Block block;
	
	public ServiceEventBlockMined(String _message, Service _source, Block _block) {
		super(_message,_source,Severity.INFO);
		this.block = _block;
	}				
}
