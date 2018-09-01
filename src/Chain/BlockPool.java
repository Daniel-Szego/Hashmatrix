package Chain;

import java.util.ArrayList;

import Block.BlockInterface;
import Node.*;
import ServiceBus.ServiceBus;
import Utils.Severity;

// pool for storing blocks 
public class BlockPool {
	
	protected Node node;
	protected ArrayList<BlockInterface> blocks;
	
	public BlockPool(Node _node) {
		this.node = _node;
	}
	
	// getting the blocks of the pool
	public ArrayList<BlockInterface> getBlocks() {
		return this.blocks;
	}
	
	// adding block to the chain if already not exist in the pool
	public void addBlock(BlockInterface _block) {
		for (BlockInterface block:blocks) {
			if (block.getBlockId().equals(_block.getBlockId()))
				return;
		}
		blocks.add(_block);
	}
	
	// deleting a block based on the block id
	public void deleteBlockById(String blockId) {
		BlockInterface toDelete = null;
		for (BlockInterface block:blocks) {
			if (block.getBlockId().equals(blockId))
				toDelete = block;
		}
		if (toDelete != null)
			blocks.remove(toDelete);
		else
			ServiceBus.logger.log("Block to be deleted from the Block pool does not exisit, Id " + blockId, Severity.ERROR);		
	}	
	
	// deleting the block from the block pool
	public void deleteBlock(BlockInterface _block) {
		this.deleteBlockById(_block.getBlockId());
	}
}
