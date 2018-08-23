package State;

import java.util.ArrayList;

import Crypto.StringUtil;
import Crypto.CryptoUtil;
import java.util.Random;
import ServiceBus.*;


public class State implements StateInterface {

	protected ArrayList<AccountBase> accounts = new ArrayList<AccountBase>();
	public final String stateId;

	// default constructor, state id is initialized
	public State () {
		this.stateId = generateStateId();
	}
	
	public State (String _stateId) {
		this.stateId = _stateId;
	}
	
	// adding an account to the state
	public void addAccount(AccountBase _account) {
		if (!isAccountContained(_account)) {
			accounts.add(_account);
		}
	}
	
	// querying if an account is in the state 
	public boolean isAccountContained(AccountBase _account) {
		return isAccountContained(_account.address);
	}
	
	// querying if an account is in the state 
	public boolean isAccountContained(String _address) {
		for (AccountBase account: accounts) {
			if (account.address.equals(_address))
				return true;
		}
		return false;
	}

	// getting an account based on the address
	public AccountBase getAccount(String _address) {
		for (AccountBase account: accounts) {
			if (account.address.equals(_address))
				return account;
		}
		return null;		
	}
	
	// gettint merkle root of the state
	public String getMerkleRoot() {
		int count = accounts.size();
		ArrayList<String> previousTreeLayer = new ArrayList<String>();
		for(AccountBase account : accounts) {
			previousTreeLayer.add(account.getAddress());
		}
		
		ArrayList<String> treeLayer = previousTreeLayer;
		while(count > 1) {
			treeLayer = new ArrayList<String>();
			for(int i=1; i < previousTreeLayer.size(); i++) {
				treeLayer.add(CryptoUtil.applySha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
			}
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}
		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		return merkleRoot;		
	}

	// return all the accounts
	public ArrayList<AccountBase> getAccounts() {
		return accounts;
	}
	
	// hard copy a state with all of the accounts
	public State copyState() {
		State newState = new State();
		for (AccountBase account: accounts) {
			AccountBase newAccount = account.copyAccount();
			newState.addAccount(newAccount);
		}
		return newState;
	}
	
	// generating a state id string
	// first implementation, random and hash
	protected String generateStateId() {
		Random rand = new Random();
		int  n = rand.nextInt(60000);
		String Id = ServiceBus.crypto.applyHash(String.valueOf(n));
		return Id;
	}
	
	// getting the number of accounts
	public int getAccounsSize() {
		return accounts.size();
	}
}
