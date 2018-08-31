package Block;

import ServiceBus.*;
import State.*;
import Transaction.*;

// special block for the genesis block
public class GenesisBlock extends BlockBase implements GenesisBlockInterface {
	
	// calculates and sets the hashes of the block
	// used in the miner for building up the chain
	// there is no previous block at a genesis block
	// actually nonce is not so important either
	// genesis block contains null transactions, only the accounts are initialized
	protected String setHashes () {
		this.setNonce(0, 0);
		this.stateRoot = state.getStateRoot();
		this.transactionRoot = transactions.getTransactionRoot();
		String relevantData = this.stateRoot +
				this.transactionRoot +
				nonce;
		this.blockId = ServiceBus.crypto.applyHash(relevantData);
		return this.blockId;	
	}
	
	// validating the whole block
	// no previous block
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
				nonce);
		
		if (!calculatedId.equals(this.blockId))
			return false;
		
		return true;
	}
	
	// based on the hash structure indentifies if block one is 
	public boolean isPreviousBlockHash(BlockInterface block) {
		return false;
	}
	
	// adding account to the state
	// directly accessible only in the genesis block
	public void addAccount(AccountInterface account) {
		this.state.addAccount(account);
		this.setHashes();
	}
}
