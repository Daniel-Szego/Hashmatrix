package Transaction;

import Utils.LoggerConsole;
import Utils.Severity;

public interface TransactionInterface {

	// getting transaction Id
	public String getTransctionId();

	// transaction id can be set only once
	public void setTransactionId(String _transactionId);

	// getting signature
	public byte[] getSignature ();
	
	// signature can be set only once
	public void setSignature(byte[] _signature);	
	
	// getting nonce
	public int getNonce();
	
	//nonce can only be set once
	public void setNonce(int _nonce);
	
	// calculate hash of the transaction
	public String calculateHash();
		
	// transaction can be signed only once
	public void signTransaction(String privateKey);
	
	// verfying signature
	public boolean verifySignature();
	
	//raw transaction processing ?
	
}
