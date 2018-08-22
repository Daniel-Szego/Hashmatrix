package State;
import java.io.Serializable;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import Crypto.CryptoUtil;
import Crypto.StringUtil;
import Utils.LoggerConsole;
import Utils.Severity;

public class Account implements Serializable {
	private PublicKey address = null; 
	public int nonce = 0;
	public String accountData;
	public float accountBalance = 0;
	public final char[] assetType = new char[3];
		
	// getting the address which is the public key
	public PublicKey getAddress() {
		if (address == null)
			throw new RuntimeException(new Exception("Account still not generated"));
		else
			return address;
	}

	// getting the address as a string
	public String getAddressString() {
		return CryptoUtil.getStringFromKey(this.getAddress());
	}

	// Public key and address can be set only once, otherwise it is a new account
	public void setAddress(PublicKey _publicKey) {
		if (this.address == null) 
			this.address = _publicKey;
		else 	
			LoggerConsole.Log("Address is already set",Severity.CRITICAL);
	}
	
	// Id is calculated based on the existing data, but it is not stored
	public String getId() {
		return CryptoUtil.applySha256(
				CryptoUtil.getStringFromKey(address) +
				Integer.toString(nonce) +
				accountData +
				Float.toString(accountBalance)
				);		
	}
	
	// security pattern to set data ?
	public void setData(){
		
	}
}
