package Transaction;

import Crypto.CryptoUtil;

// ancestor class for state transition transactions
public abstract class StateTransaction {
	private String transactionId; // this is also the hash of the transaction.
	private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.	

	public String GetTransctionId () {
		return transactionId;
	}

	public byte[] GetSignature () {
		return signature;
	}

	// transaction id can be set only once
	public void SetTransactionId(String _transactionId) {
		if (this.transactionId == null) 
			this.transactionId = _transactionId;
		else 
			// Exception handling
			throw new RuntimeException(new Exception("Transaction id is already set"));
	}
	
	// signature can be set only once
	public void SetSignature(byte[] _signature) {
		if (this.signature == null) 
			this.signature = _signature;		
		else 
			// Exception handling
			throw new RuntimeException(new Exception("Signature id is already set"));
	}

}
