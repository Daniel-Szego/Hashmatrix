package Transaction;

import java.io.Serializable;

import Crypto.CryptoUtil;
import Utils.*;

// ancestor class for state transition transactions
public abstract class StateTransaction  implements Serializable{
	private String transactionId; // this is also the hash of the transaction.
	private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.	
	private int nonce = -1; // can be only set once
	
	public String getTransctionId () {
		return transactionId;
	}

	
	public byte[] getSignature () {
		return signature;
	}
	
	public int getNonce() {
		return nonce;
	}
	
	//nonce can only be set once
	public void setNonce(int _nonce) {
		if (nonce == -1) {
			nonce = _nonce;
		}
		else {
			// error -> nonce of the account already set
			
		}
	}

	// transaction id can be set only once
	public void setTransactionId(String _transactionId) {
		if (this.transactionId == null) 
			this.transactionId = _transactionId;
		else 
			// Exception handling
			LoggerConsole.Log("Transaction id is already set", Severity.CRITICAL);
	}
	
	// signature can be set only once
	public void setSignature(byte[] _signature) {
		if (this.signature == null) 
			this.signature = _signature;		
		else 
			// Exception handling
			LoggerConsole.Log("Signature id is already set", Severity.CRITICAL);
	}	
}
