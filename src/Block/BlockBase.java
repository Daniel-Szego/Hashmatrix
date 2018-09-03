package Block;
import java.io.Serializable;
import Utils.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import ServiceBus.*;

import Crypto.CryptoUtil;
import Crypto.StringUtil;
import State.*;
import Transaction.*;

// basic block implmementation
// nonce is a simple integer
// blockid is the hash of the block header: 
// H(previousblockId, stateRoot, TransactionRoot, nonce)
public class BlockBase implements Serializable, BlockInterface {

	// BLOCK HEADER
	protected String blockId;
	// base implementation will be only a simple hash
	protected BlockInterface previousBlock;
	protected final TransactionPoolInterface transactions;
	protected StateInterface state;
	
	// simple blockbase, only simple nonce
	protected int nonce;
	
	// BLOCK BODY
	protected String stateRoot;
	protected String transactionRoot;
		
	//Creating a blank Block 
	public BlockBase() {
		state = new State();
		transactions = new TransactionPool();
	}
	
	// getting previous block 
	public BlockInterface getPreviousBlock() {
		if (this.previousBlock == null) {
			ServiceBus.logger.log("Previousblock has been queried but it is null", Severity.ERROR);
			return null;
		}
		else
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
		// recalculating hashes
	}

	// based on the hash structure indentifies if block one is 
	public boolean isPreviousBlockHash(BlockInterface block) {
		
		String calculatedId = ServiceBus.crypto.applyHash(this.stateRoot +
				this.transactionRoot +
				nonce +
				block.getBlockId());
		
		if (calculatedId.equals(this.blockId))
			return true;
		
		return false;
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
		// recalculating hashes
		setHashes();
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
		if (!this.stateRoot.equals(state.getStateRoot()))
			return false;
		
		// validate transaction root
		if (!this.transactionRoot.equals(transactions.getTransactionRoot()))
			return false;
		
		// validate block id
		String calculatedId = ServiceBus.crypto.applyHash(this.stateRoot +
				this.transactionRoot +
				nonce +
				previousBlock.getBlockId());
		
		if (!calculatedId.equals(this.blockId))
			return false;
		
		return true;
	}
	
	public int getNonce() {
		return this.nonce;
	}
	
	// setting nonce for mining
	// in a multihash blockchain, there can be different nonces at different positions
	// recalculates the hashes as well
	public void setNonce(int nonce, int position) {
		if (position != 0)
			ServiceBus.logger.log("Blockbase contains an only one hashlink scheme", Severity.ERROR);
		this.nonce = nonce;
		setHashes();
	}
	
	// if block matches with the difficulty
	public boolean matchesDifficulty(int difficulty, int position) {
		String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0" 
		if(blockId.substring( 0, difficulty).equals(target))
			return true;
		return false;
	}
	
	// getting the accounts
	public StateInterface getState() {
		return this.state;
	}
	
	// getting the transactions
	public TransactionPoolInterface getTransactions() {
		return this.transactions;
	}	
			
	// calculates and sets the hashes of the block
	// used in the miner for building up the chain
	protected String setHashes () {
		this.stateRoot = state.getStateRoot();
		this.transactionRoot = transactions.getTransactionRoot();
		String relevantData = this.stateRoot +
				this.transactionRoot +
				nonce +
				previousBlock.getBlockId();
		this.blockId = ServiceBus.crypto.applyHash(relevantData);
		return this.blockId;	
	}	
	
	
	// mining functionality at creating a new block the old one has to copied
 	public void copyState(BlockInterface previousBlock) {
 		if ((state == null) || (state.getAccountsSize() == 0)) {
 			this.state = previousBlock.getState().copyState();	
 		}
 		else
 			ServiceBus.logger.log("The state for a block cannot be copied if it is non-zero", Severity.ERROR);
 	} 
}


