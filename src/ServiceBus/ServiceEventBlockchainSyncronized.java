package ServiceBus;

import Block.BlockBase;
import Utils.Severity;

// event if the blockchain gets syncronized
public class ServiceEventBlockchainSyncronized extends ServiceEvent {
	
	public ServiceEventBlockchainSyncronized(String _message, ServiceBase _source) {
		super(_message,_source,Severity.INFO);
	}	
}
