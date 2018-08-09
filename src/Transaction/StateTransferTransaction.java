package Transaction;

import java.security.PrivateKey;
import java.security.PublicKey;

import Crypto.CryptoUtil;

// state transaction transfering money
public class StateTransferTransaction  extends StateTransaction {
	public final PublicKey fromAddress; // address of the from transfer
	public final PublicKey toAddress; // address of the to transfer
	public final float amount; // address of the to transfer
	
	public StateTransferTransaction(PublicKey _fromAddress,PublicKey _toAddress,float _amount) {
		this.fromAddress = _fromAddress;
		this.toAddress = _toAddress;
		this.amount = _amount; 
		setTransactionId(calulateHash());
	}

	// This Calculates the transaction hash (which will be used as its Id)
	private String calulateHash() {
		return CryptoUtil.applySha256(
				CryptoUtil.getStringFromKey(fromAddress) +
				CryptoUtil.getStringFromKey(toAddress) + 
				Float.toString(amount)
				);
	}	
	
	//Signs all the data we dont wish to be tampered with.
	public void generateSignature(PrivateKey privateKey) {
		String data = CryptoUtil.getStringFromKey(fromAddress) + CryptoUtil.getStringFromKey(toAddress) + Float.toString(amount);
		setSignature(CryptoUtil.applyECDSASig(privateKey,data));		
	}
	
	//Verifies the data we signed hasnt been tampered with
	public boolean verifiySignature() {
		String data = CryptoUtil.getStringFromKey(fromAddress) + CryptoUtil.getStringFromKey(toAddress) + Float.toString(amount);
		return CryptoUtil.verifyECDSASig(fromAddress, data, getSignature());
	}		
}
