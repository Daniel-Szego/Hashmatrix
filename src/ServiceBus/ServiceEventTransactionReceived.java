package ServiceBus;

import Block.*;
import Transaction.*;
import Utils.Severity;

public class ServiceEventTransactionReceived extends ServiceEvent {
	
	public final StateTransaction transaction;
	
	public ServiceEventTransactionReceived(String _message, Service _source, StateTransaction _transaction) {
		super(_message, _source, Severity.INFO);
		this.transaction = _transaction;
	}
}
