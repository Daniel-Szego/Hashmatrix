package Wallet;

import java.security.PrivateKey;

import Node.*;

// simplified implementation of a Hierarchical deterministic wallet
public class HDWallet extends Wallet {
	
	private PrivateKey seedKey;
	
	public HDWallet(Node _node){
		super(_node);
	}

	public void importAccount(PrivateKey privateKey) {
		
	}
	
	public void createNewSeedKey(){
		
	}
	
	public void createNewKeyFromSeed(int nonce){
		
	}
	
	public AccountWallet createNewAccount() {
		return null;
	}
}
