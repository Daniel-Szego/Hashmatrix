package Explorer;

import java.security.PublicKey;

// class for aggregating transaction information for the explorer
public class TransactionInfo {
	public String transactionId;
	public String typeOfTransaction;
	public String fromAddress;
	public String toAddress;
	public float amount;
	public String newValue; 
	public byte[] signature; 	
	public int nonce;
}
