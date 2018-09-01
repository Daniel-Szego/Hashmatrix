package Chain;

import java.util.ArrayList;

import Block.*;

// interface for pool of blocks that are still not in the blockchain
public interface BlockPoolInterface {

	// getting the blocks of the pool
	public ArrayList<BlockInterface> getBlocks();
	
	// adding block to the chain if already not exist in the pool
	public void addBlock(BlockInterface _block);
	
	// deleting a block based on the block id
	public void deleteBlockById(String blockId); 
	
	// deleting the block from the block pool
	public void deleteBlock(BlockInterface _block);
	
}
