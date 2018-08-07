package Wallet;

import java.security.*;

import Node.*;
import State.*;

// implementation of a random wallet
public class RandomWallet extends Wallet{

	public RandomWallet(Node _node) {
		super(_node);
	}
	
	public void  importAccount(PrivateKey privateKey) {
		Account account = new Account();
		account.importAccount(privateKey);
		accounts.add(new AccountWallet(account, privateKey));
	}
	
	public AccountWallet createNewAccount() {
		Account account = new Account();
		PrivateKey privateKey = account.generateAccount();
		AccountWallet newAccountWallet = new AccountWallet(account, privateKey);
		accounts.add(newAccountWallet);
		return newAccountWallet;
	}
}
