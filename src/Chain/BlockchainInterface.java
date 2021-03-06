package Chain;

import java.util.ArrayList;

import Block.*;

// services related only to the blockchain
public interface BlockchainInterface {

	// adding the genesis block to the blockchain
	public ExtendedBlockInterface addGenesisBlock(GenesisBlockInterface _block);
	
	// adding block to the blockchain
	// the wrapper is created and added to the chain
	public ExtendedBlockInterface addBlock(BlockInterface _block);
	
	// getting block height
	public int getBlockchinHeight();
	
	// getting top blocks 
	public ArrayList<ExtendedBlockInterface> getTopBlocks();
	
	// getting the top block after fork resolution
	public ExtendedBlockInterface getTopBlock();
	
	// getting the top stable block
	// depending on the stable strategy
	public ExtendedBlockInterface getTopStableBlock();
	
	// getting the whole blockchain
	public ArrayList<ExtendedBlockInterface> getBlockchain();
		
	// validating the whole chains
	public boolean validateChain();
	
	
}
