package State;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

import Crypto.CryptoUtil;
import Crypto.StringUtil;

public class Account {
	public String address; 
	private PrivateKey owner;
	public int sequence = 0;
	public String data;
	public String accountId;
	
	public Account() {
		generateAccount();	
	}
	
	// generating an account
	public void generateAccount() {
		try {
			PublicKey publicKey;
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
	        KeyPair keyPair = keyGen.generateKeyPair();
	        	// Set the public and private keys from the keyPair
	        owner = keyPair.getPrivate();
	        publicKey = keyPair.getPublic();
	        
	        // address is the Sha256 encryption of the public key
	        address = CryptoUtil.applySha256(CryptoUtil.getStringFromKey(publicKey));
	        
	        // account id is the hash of the address, owner, sequence and data
	        accountId = calulateAccountHash();	  
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private String calulateAccountHash() {
		return CryptoUtil.applySha256(				
				address +
				CryptoUtil.getStringFromKey(owner) +
				sequence +
				data
		);
	}

	
	public void setData(){
		
	}
}
