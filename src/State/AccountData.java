package State;

import ServiceBus.ServiceBus;
import Utils.Severity;

// Account handling only data
public class AccountData extends AccountBase{

	public AccountData(String _address) {
		super(_address);
	}
	
	// getting the balance 
	public double getBalance() {
		ServiceBus.logger.log("Balance is not available in a data account", Severity.WARNING);
		return 0;
	}
	
	// increasing balance
	public void increaseBalance(double amount) {
		ServiceBus.logger.log("Balance is not available in a data account", Severity.WARNING);
	}

	// decreasing balance
	public void decreaseBalance(double amount) {
		ServiceBus.logger.log("Balance is not available in a data account", Severity.WARNING);
	}
	
	
	
}
