package Wallet;

import java.security.PrivateKey;

import Node.*;

// implementation of a Hierarchical deterministic wallet
public class HDWallet extends Wallet {
	
	public HDWallet(Node _node){
		super(_node);
	}

	public void importAccount(PrivateKey privateKey) {
		
	}
	
	public AccountWallet createNewAccount() {
		return null;
	}
}
