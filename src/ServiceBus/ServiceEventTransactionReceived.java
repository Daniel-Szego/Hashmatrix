package ServiceBus;

import Block.*;
import Transaction.*;
import Utils.Severity;

// event if a transaction has been received on the network
public class ServiceEventTransactionReceived extends ServiceEvent {
	
	public final StateTransaction transaction;
	
	public ServiceEventTransactionReceived(String _message, ServiceBase _source, StateTransaction _transaction) {
		super(_message, _source, Severity.INFO);
		this.transaction = _transaction;
	}
}
