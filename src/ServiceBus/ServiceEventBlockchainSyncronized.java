package ServiceBus;

import Block.Block;
import Utils.Severity;

// event if the blockchain gets syncronized
public class ServiceEventBlockchainSyncronized extends ServiceEvent {
	
	public ServiceEventBlockchainSyncronized(String _message, Service _source) {
		super(_message,_source,Severity.INFO);
	}	
}
