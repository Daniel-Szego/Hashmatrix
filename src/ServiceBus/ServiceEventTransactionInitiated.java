package ServiceBus;

import Transaction.*;
import Utils.Severity;

public class ServiceEventTransactionInitiated extends ServiceEvent {
	
	public final StateTransaction transaction;

	public ServiceEventTransactionInitiated(String _message, Service _source, StateTransaction _transaction) {
		super(_message, _source, Severity.INFO);
		this.transaction = _transaction;
	}	
}
