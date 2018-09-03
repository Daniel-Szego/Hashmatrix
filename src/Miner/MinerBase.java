package Miner;

import Node.*;
import Block.*;
import Chain.*;
import Transaction.*;

// abstract base class for the Miner
public abstract class MinerBase implements ValidatorInterface {
	
	// creating the next valid block
	public abstract BlockInterface createNextBlock(BlockInterface previousBlock, TransactionPoolInterface pool);
	
	// strategy for increasing nonce
	public abstract int nonceIncreaseStrategy (int inputNonce);
	
	// transacton choice heuristic
	protected abstract boolean transactionHeuristic(TransactionInterface tr);
}
