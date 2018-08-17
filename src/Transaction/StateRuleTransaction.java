package Transaction;

import java.security.PrivateKey;
import java.security.PublicKey;

import Crypto.CryptoUtil;

// simple transaction, executing one rule
public class StateRuleTransaction extends  StateTransaction{
	public final PublicKey effectedAddress; // address of the variable that will be changedr
	public final String ruleCode; // code of the rule

	public StateRuleTransaction(PublicKey _effectedAddress, String _ruleCode ) {
		this.effectedAddress = _effectedAddress;
		this.ruleCode = _ruleCode;
		setTransactionId(calulateHash());
	}
	
	
	// This Calculates the transaction hash (which will be used as its Id)
	private String calulateHash() {
		return CryptoUtil.applySha256(
				ruleCode
				);
	}
	
	public String GetEffectedAddressString() {
		return CryptoUtil.getStringFromKey(this.effectedAddress);
	}

	//Signs all the data we dont wish to be tampered with.
	public void generateSignature(PrivateKey privateKey) {
		String data = CryptoUtil.getStringFromKey(effectedAddress) + ruleCode + Integer.toString(this.getNonce());
		setSignature(CryptoUtil.applyECDSASig(privateKey,data));		
	}
	
	//Verifies the data we signed hasnt been tampered with
	public boolean verifiySignature() {
		String data = CryptoUtil.getStringFromKey(effectedAddress) + ruleCode + Integer.toString(this.getNonce());
		return CryptoUtil.verifyECDSASig(effectedAddress, data, getSignature());
	}
			
	
}
