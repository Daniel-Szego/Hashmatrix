package ServiceBus;

import Block.*;
import Utils.*;

public class ServiceEventBlockReceived extends ServiceEvent {
	
	public final Block block;
	
	public ServiceEventBlockReceived(String _message, Service _source, Block _block) {
		super(_message,_source,Severity.INFO);
		this.block = _block;
	}			
}
