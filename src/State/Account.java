package State;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

import Crypto.CryptoUtil;
import Crypto.StringUtil;

public class Account {
	public String accountId;
	private PublicKey address; 
	//private PrivateKey owner;
	public int sequence = 0;
	public String accountData;
	public float accountBalance;
	
	public Account() {
		//generateAccount();
	}
	
	// public and private keys can be generated only once
	public PublicKey getAddress() {
		if (address == null)
			throw new RuntimeException(new Exception("Account still not generated"));
		else
			return address;
	}
	
/*	public PrivateKey getOwner() {
		if (owner == null)
			throw new RuntimeException(new Exception("Account still not generated"));
		else
			return owner;
	}
*/	
	public void setAddress(PublicKey _publicKey) {
		if (this.address == null) 
			this.address = _publicKey;
		else 				
			throw new RuntimeException(new Exception("Address is already set"));
	}
	
/*	public void setOwner(PrivateKey _owner) {
		if (this.owner == null) 
			this.owner = _owner;
		else 				
			throw new RuntimeException(new Exception("Owner is already set"));		
	}
*/		
	// generates the account
	public PrivateKey generateAccount() {
		try {
			PublicKey publicKey;
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
	        KeyPair keyPair = keyGen.generateKeyPair();
	        	// Set the public and private keys from the keyPair
//	        setOwner(keyPair.getPrivate());
	        publicKey = keyPair.getPublic();
	        
	        // address is the Sha256 encryption of the public key
	       // address = CryptoUtil.applySha256(CryptoUtil.getStringFromKey(publicKey));
	        // simplified implementation: address is the public key
	        setAddress(publicKey);
	        
	        // account id is the hash of the address, owner, sequence and data
	        accountId = calulateAccountHash();	
	        return keyPair.getPrivate();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private String calulateAccountHash() {
		return CryptoUtil.applySha256(				
				CryptoUtil.getStringFromKey(address) +
//				CryptoUtil.getStringFromKey(owner) +
				Integer.toString(sequence) +
				accountData + 				
				Float.toString(accountBalance)
		);
	}
	
	public void setData(){
		
	}
}
