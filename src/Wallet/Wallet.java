package Wallet;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.*;

import Chain.Hashmatrix;
import Crypto.CryptoUtil;
import Node.*;
import Transaction.*;

// Ancestor class for wallet
public abstract class Wallet {
	
	protected ArrayList<AccountWallet> accounts = new ArrayList<AccountWallet>(); 
	private Node node;	
	
	public Wallet(Node _node) {
		this.node = _node;
	}
	
	// READ OUT FUNCTIONALITIES
	
	// getting all the accounts
	public ArrayList<AccountWallet> getAccounts() {
		return accounts;
	}

	// getting a certain account by transacton Id
	public AccountWallet getAccountbyId(String transactionId) {
		AccountWallet toReturn = null;
		for (AccountWallet account : accounts) {
			if (account.account.accountId.equals(transactionId))
				toReturn = account;
		}
		// error handling if not found
		return toReturn;
	}
	
	// getting a certain account by public key
	public AccountWallet getAccountbyPublicKey(PublicKey address) {
		AccountWallet toReturn = null;
		for (AccountWallet account : accounts) {
			if (account.account.getAddress().equals(address))
				toReturn = account;
		}
		// error handling if not found
		return toReturn;
	}
	
	// getting a certain account by public key string
	public AccountWallet getAccountbyPublicKey(String address) {
		return this.getAccountbyPublicKey(CryptoUtil.getPublicKeyFromString(address));
	}

	// getting a certain account by private key
	public AccountWallet getAccountbyPrivateKey(PrivateKey owner) {
		AccountWallet toReturn = null;
		for (AccountWallet account : accounts) {
			if (account.getOwner().equals(owner))
				toReturn = account;
		}
		// error handling if not found
		return toReturn;
	}

	// getting a certain account by private key string
	public AccountWallet getAccountbyPrivateKey(String owner) {
		return this.getAccountbyPrivateKey(CryptoUtil.getPrivateKeyFromString(owner));		
	}

	// getting the account information for a wallet
	public String getAccountInformation(PublicKey address) {
		AccountWallet aWallet = getAccountbyPublicKey(address);
		return aWallet.account.accountData;
	}
	
	// Strig as public key
	public String getAccountInformation(String address) {
		return this.getAccountInformation(CryptoUtil.getPublicKeyFromString(address));
	}

	// getting the balance for one account
	public float getAccountBalance(PublicKey address) {
		AccountWallet aWallet = getAccountbyPublicKey(address);
		return aWallet.account.accountBalance;		
	}

	// string as public key
	public float getAccountBalance(String address) {
		return this.getAccountBalance(CryptoUtil.getPublicKeyFromString(address));		
	}

	// getting the balance for the whole wallet
	public float getWalletBalance() {
		float toReturn = 0;
		for (AccountWallet account : accounts) {
			toReturn += account.account.accountBalance;
		}
		// error handling if not found
		return toReturn;
	}

	// TRANSACTIONS
		
	public void createDataTransaction(AccountWallet account, String newValue){
		StateDataTransaction tr = new StateDataTransaction(account.account.getAddress(), newValue);
		tr.generateSignature(account.getOwner());
		node.broadcastTransaction(tr);
	}
	
	public void createTransferTransaction(AccountWallet account, PublicKey toAddress, float amount){
		StateTransferTransaction tr = new StateTransferTransaction(account.account.getAddress(), toAddress, amount);
		tr.generateSignature(account.getOwner());
		node.broadcastTransaction(tr);		
	}

	
	public void createTransferTransaction(AccountWallet account, String toAddress, float amount){
		PublicKey toPublicKey = CryptoUtil.getPublicKeyFromString(toAddress);
		this.createTransferTransaction(account,toPublicKey,amount);
	}

	
	// BACKUP RESTORE
	
	// backing up a wallet	
	public void backupWallet() {
		
	}
	
	// restoring a wallet
	public void restoreWallet() {
		
	}

	public abstract void importAccount(PrivateKey privateKey);
	
	public abstract AccountWallet createNewAccount();
	
}
