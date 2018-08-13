package Chain;

import java.util.ArrayList;
import java.util.Date;

import Block.Block;

// handling blocks with further information
public class ExtendedBlock {
	
	public Block internBlock;
	public ExtendedBlock previousBlock;
	public ArrayList<ExtendedBlock> nextBlocks = new ArrayList<ExtendedBlock>();
	public int blockHeight;
	
	public ExtendedBlock(Block _block, ExtendedBlock _previousBlock, ArrayList<ExtendedBlock> _nextBlocks) {
		internBlock = _block;
		previousBlock = _previousBlock;	
		nextBlocks = _nextBlocks;
	}

	public ExtendedBlock(Block _block, ExtendedBlock _previousBlock) {
		this(_block, _previousBlock, null);
	}
	
	public ExtendedBlock(Block _block) {
		this(_block, null, null);
	}	
	
	public boolean nextBlockContains (ExtendedBlock _block) {
		if (nextBlocks.size() == 0)
			return false;	
		for (ExtendedBlock block: nextBlocks){
			if (block.internBlock.equals(_block))
				return true;			
		}
		return false;
	}
}
