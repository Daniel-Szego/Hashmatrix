package Miner;

import Block.*;
import Transaction.*;

// interface for the validators
public interface ValidatorInterface {

	// creating the next valid block
	public BlockInterface createNextBlock(BlockInterface previousBlock, TransactionPoolInterface pool);
	
}
