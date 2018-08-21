package ServiceBus;

import Transaction.StateDataTransaction;
import Utils.Severity;

public class ServiceEventTransactionInitiated extends ServiceEvent {
	
	public final StateDataTransaction transaction;

	public ServiceEventTransactionInitiated(String _message, Service _source, StateDataTransaction _transaction) {
		super(_message, _source, Severity.INFO);
		this.transaction = _transaction;
	}	
}
