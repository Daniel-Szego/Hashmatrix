package Transaction;

import java.security.PrivateKey;
import java.security.PublicKey;

import Crypto.CryptoUtil;
import ServiceBus.*;

// simple transaction, executing one rule
public class StateRuleTransaction extends  StateTransaction {
	public final String address; // address of the variable that will be changedr
	public final String code; // code of the rule

	public StateRuleTransaction(String _address, String _code ) {
		this.address = _address;
		this.code = _code;
		setTransactionId(calculateHash());
	}
	
	// This Calculates the transaction hash (which will be used as its Id)
	public String calculateHash() {
		return ServiceBus.crypto.applyHash((
				address +
				code
				));
	}	
		
	// transaction can be signed only once
	public void signTransaction(String privateKey) {
		String data = ServiceBus.crypto.applyHash(this.address + this.code);
		this.setSignature(ServiceBus.crypto.applySignature(privateKey, data));		
	}
	
	// verfying signature
	public boolean verifySignature() {
		String data = ServiceBus.crypto.applyHash(this.address + this.code);
		return ServiceBus.crypto.verifySignature(this.address, data, this.getSignature());
	}	
}
