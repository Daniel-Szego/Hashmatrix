package Wallet;

import java.security.*;

import Node.*;
import State.*;

// implementation of a random wallet
public class RandomWallet extends Wallet{

	public RandomWallet(Node _node) {
		super(_node);
	}
	
	public void importAccount(PrivateKey privateKey) {
		AccountWallet account = new AccountWallet();
		account.importAccount(privateKey);
		accounts.add(account);
	}
	
	public AccountWallet createNewAccount() {
		AccountWallet account = new AccountWallet();
		PrivateKey privateKey = account.generateAccount();
		accounts.add(account);
		return account;
	}
}
