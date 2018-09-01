package Chain;

import java.util.ArrayList;
import java.util.Date;

import Block.*;

// handling blocks with further information
// used only in blockchain
public class ExtendedBlock implements ExtendedBlockInterface {
	
	protected BlockInterface internBlock;
	protected ArrayList<ExtendedBlockInterface> nextBlocks;
	protected int blockHeight;
	
	// constructors
	public ExtendedBlock(BlockInterface _block, ExtendedBlockInterface _previousBlock, ArrayList<ExtendedBlockInterface> _nextBlocks) {
		internBlock = _block;
		internBlock.setPreviousBlock(_previousBlock.getInternBlock());	
		nextBlocks = _nextBlocks;
		if (_previousBlock == null)
			blockHeight = -1;
		else
			blockHeight = _previousBlock.getBlockHeight() + 1;
	}

	
	public ExtendedBlock(BlockInterface _block, ExtendedBlockInterface _previousBlock) {
		this(_block, _previousBlock, null);
	}
	
	public ExtendedBlock(BlockInterface _block) {
		this(_block, null, null);
	}	
	
	
	// getting the internal block
	public BlockInterface getInternBlock() {
		return internBlock;
	}
	
	// adding the next block if it still does not exist
	public void addNextBlock(ExtendedBlockInterface nextBlock) {
		if(!this.nextBlockContains(nextBlock.getInternBlock().getBlockId()))
			nextBlocks.add(nextBlock);
	}
	
	// getting the next blocks
	public ArrayList<ExtendedBlockInterface> getNextBlocks() {
		return nextBlocks;
	}
	
	// getting the next blocks
	public boolean nextBlockContains(String blockId) {
		for (ExtendedBlockInterface nBlock: nextBlocks) {
			if(nBlock.getInternBlock().getBlockId().equals(blockId))
				return true;
		}
		return false;
	}
	
	// getting the block height
	public int getBlockHeight() {
		return this.blockHeight;
	}
	
	// getting the next blocks
	public boolean nextBlockContains (ExtendedBlock _block) {
		if (nextBlocks.size() == 0)
			return false;	
		for (ExtendedBlockInterface block: nextBlocks){
			if (block.getInternBlock().equals(_block))
				return true;			
		}
		return false;
	}
}
