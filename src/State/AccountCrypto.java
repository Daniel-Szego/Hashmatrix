package State;

import java.io.Serializable;

import ServiceBus.ServiceBus;
import Utils.Severity;

// account handling only crypto balances
public class AccountCrypto extends AccountBase{

	public AccountCrypto(String _address) {
		super(_address);
	}
	
	// getting data associated with the account
	public String getData() {
		ServiceBus.logger.log("Data is not available in a crypto account", Severity.WARNING);
		return null;		
	}

	// setting new data value
	public void setData(String data) {
		ServiceBus.logger.log("Data is not available in a crypto account", Severity.WARNING);
	}
	
}
