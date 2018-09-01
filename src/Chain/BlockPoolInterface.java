package Chain;

import java.util.ArrayList;

import Block.*;

// interface for pool of blocks that are still not in the blockchain
public interface BlockPoolInterface {

	// getting the blocks of the pool
	public ArrayList<BlockInterface> getBlocksFromPool();
	
	// adding block to the chain if already not exist in the pool
	public void addBlockToPool(BlockInterface _block);
	
	// deleting a block based on the block id
	public void deleteBlockByIdFromPool(String blockId); 
	
	// deleting the block from the block pool
	public void deleteBlockFromPool(BlockInterface _block);
	
}
