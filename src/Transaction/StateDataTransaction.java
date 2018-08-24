package Transaction;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import Crypto.*;
import ServiceBus.*;

// state transaction setting the state
// parameters: address, newValue
public class StateDataTransaction extends StateTransaction {
	public final String address; // address of the variable to change which must be the public key of the owner
	public final String newValue; // new value to be stored
	
	// Constructor: 
	public StateDataTransaction(String _address, String _newValue) {
		this.address = _address;
		this.newValue = _newValue;
		setTransactionId(calculateHash());
	}
	
	// This Calculates the transaction hash (which will be used as its Id)
	public String calculateHash() {
		return ServiceBus.crypto.applyHash(
				address +
				newValue
				);
	}
	
	// transaction can be signed only once
	public void signTransaction(String privateKey) {
		String data = ServiceBus.crypto.applyHash(this.address + this.newValue + Integer.toString(this.getNonce()));
		this.setSignature(ServiceBus.crypto.applySignature(privateKey, data));
	} 
	
	// verfying signature
	public boolean verifySignature() {
		String data = ServiceBus.crypto.applyHash(this.address + this.newValue + Integer.toString(this.getNonce()));
		return ServiceBus.crypto.verifySignature(this.address, data, this.getSignature());
	}

}
