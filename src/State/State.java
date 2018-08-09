package State;

import java.util.ArrayList;

import Crypto.StringUtil;
import Crypto.CryptoUtil;


public class State {
	public ArrayList<Account> accounts = new ArrayList<Account>();
	public String stateId;
	
	public State () {
		stateId = getStateMerkleRoot();
	}
	
	public void addAccont(Account _newAccount) {
		accounts.add(_newAccount);
		stateId = getStateMerkleRoot();
	}
		
	public String getStateMerkleRoot() {
		int count = accounts.size();
		ArrayList<String> previousTreeLayer = new ArrayList<String>();
		for(Account account : accounts) {
			previousTreeLayer.add(account.getId());
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
}
