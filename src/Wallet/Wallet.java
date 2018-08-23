package Wallet;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.*;

import Chain.*;
import Crypto.CryptoUtil;
import Node.*;
import SmartContract.*;
import State.*;
import Transaction.*;
import Utils.LoggerConsole;

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
		return aWallet.account.getData();
	}
	
	// Strig as public key
	public String getAccountInformation(String address) {
		return this.getAccountInformation(CryptoUtil.getPublicKeyFromString(address));
	}

	// getting the balance for one account
	public double getAccountBalance(PublicKey address) {
		AccountWallet aWallet = getAccountbyPublicKey(address);
		return aWallet.account.getBalance();		
	}

	// string as public key
	public double getAccountBalance(String address) {
		return this.getAccountBalance(CryptoUtil.getPublicKeyFromString(address));		
	}

	// getting the balance for the whole wallet
	public double getWalletBalance() {
		float toReturn = 0;
		for (AccountWallet account : accounts) {
			toReturn += account.account.getBalance();
		}
		// error handling if not found
		return toReturn;
	}

	// TRANSACTIONS
		
	public StateDataTransaction createDataTransaction(AccountWallet account, String newValue){
		StateDataTransaction tr = new StateDataTransaction(account.account.getAddress().get, newValue);
		tr.setNonce(account.account.nonce + 1);
		tr.generateSignature(account.getOwner());
		node.broadcastTransaction(tr);
		return tr;
	}
	
	public StateTransferTransaction createTransferTransaction(AccountWallet account, PublicKey toAddress, float amount){
		StateTransferTransaction tr = new StateTransferTransaction(account.account.getAddress(), toAddress, amount);
		tr.setNonce(account.account.nonce + 1);
		tr.generateSignature(account.getOwner());
		node.broadcastTransaction(tr);
		return tr;
	}

	public StateRuleTransaction createRuleTransaction(String ruleString){
		SimpleRule rule = new SimpleRule(ruleString);		
		AccountWallet effectedAccount = node.wallet.getAccountbyPublicKey(rule.account_effect);				
		if (effectedAccount == null)
			LoggerConsole.Log("Error at create rule transaction: the effected account must be in wallet");
		StateRuleTransaction tr = new StateRuleTransaction(effectedAccount.account.getAddress(), ruleString);	
		tr.setNonce(effectedAccount.account.nonce + 1);
		tr.generateSignature(effectedAccount.getOwner());
		node.broadcastTransaction(tr);
		return tr;
	}

	
	public StateTransferTransaction createTransferTransaction(AccountWallet account, String toAddress, float amount){
		PublicKey toPublicKey = CryptoUtil.getPublicKeyFromString(toAddress);
		return this.createTransferTransaction(account,toPublicKey,amount);
	}

	// syncing the accounts with the last block
	// naive implementation
	public void syncAccounts(){
		ExtendedBlock eBlock = node.blockchain.getTopBlock();
		for (AccountBase a: eBlock.internBlock.accounts) {
			 AccountWallet aWallet = getAccountWallet(a.getAddressString());
			 if (aWallet != null) {
				 aWallet.account.accountBalance = a.accountBalance;
				 aWallet.account.accountData = a.accountData;
				 aWallet.account.nonce = a.nonce;
			 }
		}
	}
	
	// getting the account wallet
	public AccountWallet getAccountWallet(String address) {
		for (AccountWallet a: accounts) {
			if(a.account.getAddressString().equals(address)){
				return a;
			}
		}
		return null;
	}
	
	// BACKUP RESTORE
	
	// backing up a wallet	
	public void BackupWallet() {
		
	}
	
	// restoring a wallet
	public void RestoreWallet() {
		
	}

	public abstract void importAccount(PrivateKey privateKey);
	
	public abstract AccountWallet createNewAccount();
	
	
	
}
