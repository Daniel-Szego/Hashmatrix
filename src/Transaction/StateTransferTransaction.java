package Transaction;

import java.security.PrivateKey;
import java.security.PublicKey;

import Crypto.CryptoUtil;
import ServiceBus.*;

// state transaction transfering money
public class StateTransferTransaction  extends StateTransaction {
	public final String from; // address of the from transfer
	public final String to; // address of the to transfer
	public final double amount; // address of the to transfer
	
	public StateTransferTransaction(String _from,String  _to, double _amount) {
		this.from = _from;
		this.to = _to;
		this.amount = _amount; 
		setTransactionId(calculateHash());
	}
	
	// This Calculates the transaction hash (which will be used as its Id)
	public String calculateHash() {
		return ServiceBus.crypto.applyHash((
				from +
				to + 
				Double.toString(amount)
				));
	}	
	
	// transaction can be signed only once
	public void signTransaction(String privateKey) {
		String data = ServiceBus.crypto.applyHash(this.from + this.to + Double.toString(amount));
		this.setSignature(ServiceBus.crypto.applySignature(privateKey, data));	
	}
	
	// verfying signature
	public boolean verifySignature() {
		String data = ServiceBus.crypto.applyHash(this.from + this.to + Double.toString(amount));
		return ServiceBus.crypto.verifySignature(from, data, this.signature);
	}

}
