package Miner;

import Node.*;
import ServiceBus.Constants;
import Block.*;
import Chain.*;
import Transaction.*;

// simple Proof of Work Miner
public class MinerPOWSimple extends MinerBase{
		
	public MinerPOWSimple() {
	}	
	
	public BlockInterface createNextBlock(BlockInterface previousBlock, TransactionPoolInterface pool) {
		// blank block
		BlockInterface newBlock = new BlockBase();
		
		// setting the previous block
		newBlock.setPreviousBlock(previousBlock);
		
		// copying the state
		newBlock.copyState(previousBlock);
			
		int addedTransactions = 0;
		// getting transactions from pool and adding to the new pool
		for(TransactionInterface tr : pool.getTransactions()) {
			// adding the transaction validates and transforms the state
			newBlock.addTransaction(tr);
			// limiting transactions in a block
			if (addedTransactions >= Constants.MaxTransactionInABlock)
				break;
		}

		newBlock.setNonce(0, 1);
				
		// mining hash one
		
		while(!newBlock.matchesDifficulty(Constants.Difficulty, 0)) {
			int newNonce = nonceIncreaseStrategy(newBlock.getNonce());
			newBlock.setNonce(newNonce, 0);
		} 
				
		return newBlock;
	}

	// simple increase strategy: input nonce increased by one
	public int nonceIncreaseStrategy (int inputNonce) {
		return inputNonce + 1;
	}

	// simple overridable implementaion for transaction heuristics
	protected boolean transactionHeuristic(TransactionInterface tr){
		return true;
	}
		
}
