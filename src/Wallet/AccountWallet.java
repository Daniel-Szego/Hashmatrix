package Wallet;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import Crypto.CryptoUtil;
import State.*;

// Simple wrapper for the account containing if it is added to the blockchin 
// or only to the wallet
public class AccountWallet {
	public final Account account;
	private PrivateKey privateKey; // the blockchain does not store the private keys, but the wallet should!
	public boolean isSyncedWithTheBlockchain;	
	
	// creating a brand new account
	public AccountWallet() {
		account = new Account();
		this.isSyncedWithTheBlockchain = false;
	}
	
	public AccountWallet(Account _account, PrivateKey _privateKey) {
		this.account = _account;
		this.privateKey = _privateKey;
		this.isSyncedWithTheBlockchain = false;
	}
	
	// how to make it more secure ??, encrypting private keys ?
	public PrivateKey getOwner(){
		return privateKey;
	}

	public void setPrivateKey(PrivateKey _privateKey){
		if (privateKey == null) {
			this.privateKey = _privateKey;
		}
	}
	
	// how to make it more secure ??, encrypting private keys ?
	public String getOwnerString(){
		return CryptoUtil.getStringFromKey(privateKey);
	}
	
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
	        account.setAddress(publicKey);
	        this.setPrivateKey(keyPair.getPrivate());	        
	        return keyPair.getPrivate();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void importAccount(PrivateKey _privateKey) {
		try {
			PublicKey publicKey;
			
			KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "BC");
		    ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

		    ECPoint Q = ecSpec.getG().multiply(((org.bouncycastle.jce.interfaces.ECPrivateKey) _privateKey).getD());

		    ECPublicKeySpec pubSpec = new ECPublicKeySpec(Q, ecSpec);
		    PublicKey publicKeyGenerated = keyFactory.generatePublic(pubSpec);
		    publicKey = publicKeyGenerated;
		    account.setAddress(publicKey);
	        this.setPrivateKey(_privateKey);	
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// syncing account from the blockchain
	public void syncAccount(Account _account) {
		if(this.isSyncedWithTheBlockchain)
		{
			// already syncronized -> error handling
			return;
		}
		
		if (account.getAddressString() == null) {
			
			// the internal account is still not initialized, error handling ?
		}
		// standard syncronisation
		else if (account.getAddressString() == _account.getAddressString()) {
			account.accountData = _account.accountData;
			account.accountBalance = _account.accountBalance;
			this.isSyncedWithTheBlockchain = true;
		}
		else {

			// wrong account is being syncronized, what to do?
		}
		
	}

}
