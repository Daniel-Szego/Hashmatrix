package Wallet;

import java.security.PrivateKey;

import Crypto.CryptoUtil;
import State.*;

// Simple wrapper for the account containing if it is added to the blockchin 
// or only to the wallet
public class AccountWallet {
	public static Account account;
	private static PrivateKey privateKey; // the blockchain does not store the private keys, but the wallet should!
	public boolean isAddedToBlockchain;	
	
	public AccountWallet(Account _account, PrivateKey _privateKey) {
		this.account = _account;
		this.privateKey = _privateKey;
		isAddedToBlockchain = false;
	}
	
	// how to make it more secure ??, encrypting private keys ?
	public PrivateKey getOwner(){
		return privateKey;
	}

	// how to make it more secure ??, encrypting private keys ?
	public String getOwnerString(){
		return CryptoUtil.getStringFromKey(privateKey);
	}

}
