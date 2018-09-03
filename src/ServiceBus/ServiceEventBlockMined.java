package ServiceBus;

import Block.*;
import Utils.Severity;

// event by the miner if a new block has been mined
public class ServiceEventBlockMined extends ServiceEvent {

	public final BlockInterface block;
	
	public ServiceEventBlockMined(String _message, ServiceBase _source, BlockInterface _block) {
		super(_message,_source,Severity.INFO);
		this.block = _block;
	}				
}
