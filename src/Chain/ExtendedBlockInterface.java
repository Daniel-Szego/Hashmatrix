package Chain;

import java.util.*;

import Block.*;

// wrapper block for handling additional parameters of the block
public interface ExtendedBlockInterface {
	
	// getting the internal block
	public BlockInterface getInternBlock();
	
	// adding the next block if it still does not exist
	public void addNextBlock(ExtendedBlockInterface nextBlock);
	
	// getting the next blocks
	public ArrayList<ExtendedBlockInterface> getNextBlocks();
	
	// getting the next blocks
	public boolean nextBlockContains(String blockId);
	
	// getting the block height
	public int getBlockHeight();
		
}
