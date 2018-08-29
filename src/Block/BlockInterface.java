package Block;

import java.util.ArrayList;

import State.*;
import Transaction.*;

// general interface for the block
// including both simple and multi-hash systems
public interface BlockInterface {
	
	// getting previous block 
	public BlockInterface getPreviousBlock();
		
	// previous block might be not seted at stale blocks
	// but previous block should be seted only once
	// if a block arrives on the network, the block is still not seted
	// Used by: Network, 
	public void setPreviousBlock(BlockInterface block);

	// based on the hash structure indentifies if block one is 
	public boolean isPreviousBlockHash(BlockInterface block);
		
	// adding transaction to the block
	// Used by: miner
	public void addTransaction (TransactionInterface tr);
	
	// block id is the block hash
	// or in case of a multi hashing, the hash of the different block headers
	public String getBlockId();

	// validating the whole block
	public boolean validateBlock();
	
	// setting nonce for mining
	// in a multihash blockchain, there can be different nonces at different positions
	public void setNonce(int nonce, int position);
	
	// if block matches with the difficulty
	public boolean hashMatchesDifficulty(int difficulty, int position);	
	
	// getting the accounts
	public ArrayList<AccountInterface> getState();
	
	// getting the transactions
	public ArrayList<TransactionInterface> getTransactions();
}
