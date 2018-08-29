package Block;
import java.io.Serializable;
import Utils.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import ServiceBus.*;

import Crypto.CryptoUtil;
import Crypto.StringUtil;
import State.AccountBase;
import State.AccountInterface;
import State.State;
import Transaction.*;

// basic block implmementation
public class BlockBase implements Serializable, BlockInterface {

	protected String blockId;
	// base implementation will be only a simple hash
	protected BlockInterface previousBlock;
	protected final TransactionPool transactions;
	protected final State state;
	
	protected String stateRoot;
	protected String transactionRoot;
		
	//Creating a blank Block 
	public BlockBase() {
		state = new State();
		transactions = new TransactionPool(null);
	}
	
	// getting previous block 
	public BlockInterface getPreviousBlock() {
		return this.previousBlock;
	}
		
	// previous block might be not seted at stale blocks
	// but previous block should be seted only once
	// if a block arrives on the network, the block is still not seted
	// Used by: Network, 
	public void setPreviousBlock(BlockInterface block) {
		if (this.previousBlock == null) {
			this.previousBlock = block;
		}
		else{
			ServiceBus.logger.log("Previousblock can be set only once", Severity.ERROR);
		}
	}

	// based on the hash structure indentifies if block one is 
	public boolean isPreviousBlockHash(BlockInterface block) {
		
	}
		
	// adding transaction to the block
	// only if valid 
	// and transfer the state
	// Used by: miner
	public void addTransaction (TransactionInterface tr) {
		if (state.isTransactionValidEx(tr)) {
			if(state.applyTransaction(tr)) {
				transactions.addTransaction(tr);
			}	
		}
	}
	
	// block id is the block hash
	// or in case of a multi hashing, the hash of the different block headers
	public String getBlockId() {
		return blockId;
	}

	// validating the whole block
	public boolean validateBlock() {
		// validate transactions against the state
		for(TransactionInterface tr :transactions.getTransactions()) {
			if(!state.isTransactionValid(tr))
				return false;
		}
		
		// validate state root
		
		// validate transaction root
		
		// validate block id
		
		// validate previous block 
		
	}
	
	// setting nonce for mining
	// in a multihash blockchain, there can be different nonces at different positions
	public void setNonce(int nonce, int position);
	
	// if block matches with the difficulty
	public boolean hashMatchesDifficulty(int difficulty, int position);	
	
	// getting the accounts
	public ArrayList<AccountInterface> getState();
	
	// getting the transactions
	public ArrayList<TransactionInterface> getTransactions();

	
	
	// calculating state root
	public String calculateStateRoot() {
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
					LoggerConsole.Log("non valid transaction in the proposed block, TrID : " + tr.getTransctionId());
				}
			}
			else if(tr instanceof StateTransferTransaction) {
				if(TransactionValidator.validateTransferTransaction((StateTransferTransaction)tr, accounts)){
					StateTransformer.applyTransferTransactionToState((StateTransferTransaction)tr, accounts);	
				}
				else {
					// non valid transaction in the proposed transactions ? error ?
					LoggerConsole.Log("non valid transaction in the proposed block, TrID : " + tr.getTransctionId());
				}				
			}
			else if(tr instanceof StateRuleTransaction) {
				if(TransactionValidator.validateRuleTransaction((StateRuleTransaction)tr, accounts)){
					StateTransformer.applyRuleTransactionToState((StateRuleTransaction)tr, accounts);	
				}
				else {
					// non valid transaction in the proposed transactions ? error ?
					LoggerConsole.Log("non valid transaction in the proposed block, TrID : " + tr.getTransctionId());
				}				
			}
			else {
				// unknown transaction -> error handing
				LoggerConsole.Log("unknown transaction");
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


