package Block;
import java.io.Serializable;
import Utils.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;

import Crypto.CryptoUtil;
import Crypto.StringUtil;
import State.Account;
import State.State;
import Transaction.*;

public class Block implements Serializable {

	public String blockId;
	public ArrayList<HashLink> matrix = new ArrayList<HashLink>();
 	public ArrayList<StateTransaction> transactions = new ArrayList<StateTransaction>();
 	public ArrayList<Account> accounts = new ArrayList<Account>();
	public String stateRoot;
	public String transactionRoot;
		
	//Creating a blanck Block 
	public Block() {
	}
	
	// calculating state root
	public String calculateStateRoot() {
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
		stateRoot = merkleRoot;
		return merkleRoot;
	}
	
	// calculating transaction root
	public String calculateTransactionRoot() {
		int count = accounts.size();
		ArrayList<String> previousTreeLayer = new ArrayList<String>();
		for(StateTransaction tr : transactions) {
			previousTreeLayer.add(tr.getTransctionId());
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
		transactionRoot = merkleRoot;
		return merkleRoot;
	} 

		
	// aplying transactions to the state if they have a valid sginature
	// supposing that state is copied from the previous block
	public void calculateNewState(){
		for(StateTransaction tr : transactions) {
			if (tr instanceof StateDataTransaction) {
				if(TransactionValidator.validateDataTransaction((StateDataTransaction)tr, accounts)){
					StateTransformer.applyDataTransactionToState((StateDataTransaction)tr, accounts);	
				}
				else {
					// non valid transaction in the proposed transactions ? error ?
					Logger.Log("non valid transaction in the proposed block, TrID : " + tr.getTransctionId());
				}
			}
			else if(tr instanceof StateTransferTransaction) {
				if(TransactionValidator.validateTransferTransaction((StateTransferTransaction)tr, accounts)){
					StateTransformer.applyTransferTransactionToState((StateTransferTransaction)tr, accounts);	
				}
				else {
					// non valid transaction in the proposed transactions ? error ?
					Logger.Log("non valid transaction in the proposed block, TrID : " + tr.getTransctionId());
				}				
			}
			else {
				// unknown transaction -> error handing
				Logger.Log("unknown transaction");
			}
		}	
	}	
	
	public void setBlockId(){
		blockId = calculateBlockId();
	}
	
	public String calculateBlockId () {
		String hashLinkStrings = null;
		for(HashLink link: matrix) {
			hashLinkStrings += link.hashOne;
			hashLinkStrings += link.hashTwo;			
		}				
	return CryptoUtil.applySha256(hashLinkStrings);
	}
}


