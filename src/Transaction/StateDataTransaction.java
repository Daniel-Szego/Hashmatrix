package Transaction;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import Crypto.CryptoUtil;
import Crypto.StringUtil;

// state transaction setting the state
// parameters: address, newValue
public class StateDataTransaction extends StateTransaction {
	public final PublicKey address; // address of the variable to change which must be the public key of the owner
	public final String newValue; // new value to be stored
	
	// Constructor: 
	public StateDataTransaction(PublicKey _address, String _newValue) {
		this.address = _address;
		this.newValue = _newValue;
		setTransactionId(calulateHash());
	}
	
	// This Calculates the transaction hash (which will be used as its Id)
	private String calulateHash() {
		return CryptoUtil.applySha256(
				address +
				newValue
				);
	}
	
	public String GetAddressString() {
		return CryptoUtil.getStringFromKey(this.address);
	}

	//Signs all the data we dont wish to be tampered with.
	public void generateSignature(PrivateKey privateKey) {
		String data = CryptoUtil.getStringFromKey(address) + newValue + Integer.toString(this.getNonce());
		setSignature(CryptoUtil.applyECDSASig(privateKey,data));		
	}
	
	//Verifies the data we signed hasnt been tampered with
	public boolean verifiySignature() {
		String data = CryptoUtil.getStringFromKey(address) + newValue + Integer.toString(this.getNonce());
		return CryptoUtil.verifyECDSASig(address, data, getSignature());
	}	
	
	public String getRawTransaction(){
		return CryptoUtil.getStringFromKey(address) + newValue;
	}
	
}
