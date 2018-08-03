package Transaction;

import java.security.PublicKey;
import java.util.ArrayList;

import Crypto.CryptoUtil;
import Crypto.StringUtil;

public class StateTransaction {
	public String transactionId; // this is also the hash of the transaction.
	public String address; // address of the variable to change
	public String newValue;
	public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
	
	// Constructor: 
	public StateTransaction(String _address, String _newValue) {
		this.address = _address;
		this.newValue = _newValue;
	}
	
	// This Calculates the transaction hash (which will be used as its Id)
	private String calulateHash() {
		return CryptoUtil.applySha256(
				address +
				newValue
				);
	}
	
}
