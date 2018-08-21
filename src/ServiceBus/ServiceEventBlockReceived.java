package ServiceBus;

import Block.*;
import Utils.*;

// event if a new block has been received on the network
public class ServiceEventBlockReceived extends ServiceEvent {
	
	public final Block block;
	
	public ServiceEventBlockReceived(String _message, Service _source, Block _block) {
		super(_message,_source,Severity.INFO);
		this.block = _block;
	}			
}
