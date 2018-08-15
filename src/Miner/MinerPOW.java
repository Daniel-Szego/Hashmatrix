package Miner;

import Node.*;
import Block.*;
import Chain.*;
import Transaction.*;

// simple Proof of Work Miner
public class MinerPOW extends Miner{
	
	// CONSTANTS -> question if it should be a constant
	public int MaxTransactionInABlock = 100;
	
	public MinerPOW(Node _node) {
		super(_node);
	}	
	
	public Block mineNextBlock(Block previousBlock, TransactionPool pool) {
		// blank block
		Block newBlock = new Block();
		
		// copy old state -> copying the accounts
		StateTransformer.copyState(previousBlock, newBlock);		
		
		int addedTransactions = 0;
		// getting transactions from pool and adding to the new pool
		for(StateTransaction tr : pool.transactions) {
			if (tr instanceof StateDataTransaction) {
				if(TransactionValidator.validateDataTransaction((StateDataTransaction)tr, newBlock.accounts)){
					if (transactionHeuristic()) {
						newBlock.transactions.add(tr);
						addedTransactions++;
					}
				}
			}
			else if(tr instanceof StateTransferTransaction) {
				if(TransactionValidator.validateTransferTransaction((StateTransferTransaction)tr, newBlock.accounts)){
					if (transactionHeuristic()) {
						newBlock.transactions.add(tr);
						addedTransactions++;
					}
				}
			}
			// limiting transactions in a block
			if (addedTransactions >= MaxTransactionInABlock)
				break;
		}
				
		// applying them to the state 
		newBlock.calculateNewState();
		
		// computing merkle roots of state and transaction pool
		newBlock.calculateStateRoot();
		newBlock.calculateTransactionRoot();
			
		// doing proof of work -> calculating nonces
		// copying previous hash parameters
		for(HashLink previousHash:previousBlock.matrix) {
			String previousHashOne =  previousHash.hashOne;
			String previousHashTwo = previousHash.hashTwo;
			int resetPolicy = previousHash.resetPolicy;
			int resetCount = previousHash.resetCount; 
			boolean lastResetedHash = previousHash.lastResetedHash;
			boolean singleHash = previousHash.singleHash;
			int difficulty = previousHash.difficulty;

			// setting resetcount and last reseted hash
			if (resetPolicy >= resetCount +1) {
				resetCount = 0;
				lastResetedHash = !lastResetedHash;
			}
			else
				resetCount++;
						
			// copying hashlink
			HashLink newHashLink = new HashLink("", "", resetPolicy, resetCount, lastResetedHash, singleHash, difficulty);
			
			// mining hash one
			while(!newHashLink.validateHashOne()) {
				newHashLink.nonceOne ++;
				newHashLink.setHashOne(newBlock.stateRoot, newBlock.transactionRoot,previousHashOne);
			}
			
			// mining hash two
			while(!newHashLink.validateHashTwo()) {
				newHashLink.nonceTwo ++;
				newHashLink.setHashTwo(newBlock.stateRoot, newBlock.transactionRoot,previousHashTwo);
			}	
			
			newBlock.matrix.add(newHashLink);
		}
		
		//computing blockId
		//blockId can be attached previously as well, however than it is only temporary
		newBlock.setBlockId();
		return newBlock;
	}

	// simple overridable implementaion for transaction heuristics
	protected boolean transactionHeuristic(){
		return true;
	}
	
	
}
