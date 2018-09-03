package Chain;

import Block.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.*;

import Node.*;
import ServiceBus.*;
import Transaction.*;
import Utils.LoggerConsole;
import Wallet.Wallet;

import java.security.*;
import org.bouncycastle.*;

// blockchain base
// blockchain as a data structure and services
public class BlockchainBase implements Chain.BlockchainInterface {
	
	// all blocks that can be associated to another one is here
	// tree structure -> chain has to be derived
	public static ArrayList<ExtendedBlockInterface> blocklist = new ArrayList<ExtendedBlockInterface>();
	
	public BlockchainBase() {
	}
	
	// adding the genesis block to the blockchain
	public ExtendedBlockInterface addGenesisBlock(GenesisBlockInterface _block){
		ExtendedBlock newExtendedblock = new ExtendedBlock(_block);
		newExtendedblock.blockHeight = 0;
		blocklist.add(newExtendedblock);	
		return newExtendedblock;
	}

	// adding a block to the chain, only if it can be put to the chain
	// if does not match, return null
	public ExtendedBlockInterface addBlock(BlockInterface _block) {
		// if it is the  genesis block
		if ((blocklist.size() < 1) && (_block instanceof GenesisBlock))
			this.addGenesisBlock((GenesisBlock)_block);
	
		ExtendedBlock newExtendedblock = new ExtendedBlock(_block);

		// if match adding to the blockhcain finding previous and next blocks
		boolean isStaleBlock = true;
		for (ExtendedBlockInterface prevBlock: blocklist) {
			if (newExtendedblock.getInternBlock().isPreviousBlockHash(prevBlock.getInternBlock())) {
				// match
				isStaleBlock = false;
				blocklist.add(newExtendedblock);
				newExtendedblock.getInternBlock().setPreviousBlock(prevBlock.getInternBlock());
				prevBlock.addNextBlock(newExtendedblock);
			}
		}
		if (isStaleBlock)
			return null;
		else
			return newExtendedblock;	
	}
		
	// getting block height
	public int getBlockchinHeight() {
		int chainMaxHeight = 0;
		// get the longest chain length
		for(ExtendedBlockInterface eBlock: blocklist) {
			if ((eBlock.getInternBlock().getPreviousBlock() != null ) && (eBlock.getNextBlocks().size() == 0)) {
				if (chainMaxHeight < eBlock.getBlockHeight())
					chainMaxHeight = eBlock.getBlockHeight();
			}
		}
		return chainMaxHeight;
	}
	
	// stale blocks are in the block pool
	public ArrayList<ExtendedBlockInterface> getTopBlocks() {
		ArrayList<ExtendedBlockInterface> topBlocks = new ArrayList<ExtendedBlockInterface>();
				
		int chainMaxHeight = this.getBlockchinHeight();

		for(ExtendedBlockInterface eBlock: blocklist) {
			if (eBlock.getNextBlocks().size() == 0) {
				if (chainMaxHeight == eBlock.getBlockHeight())
					topBlocks.add(eBlock);
			}
		}
		//if empty, the genesis block 
		return topBlocks;
	}
	
	// blockchain resolution strategy, pick the first from the longest
	public ExtendedBlockInterface getTopBlock() {
		if (getTopBlocks() == null)
			return null;
		else if(getTopBlocks().size() == 0)
			return null;
		else 
			return getTopBlocks().get(0);
	}
	
	// latest block is not necessarily the same as latest stable block
	// improvement: the last block should be considered that does not fork much
	public ExtendedBlockInterface getTopStableBlock() {
		
		return getTopBlock();
	}

	// getting the blockchain for the longest chain 
	// forking resolution the first block is condiered from the longest ones
	public ArrayList<ExtendedBlockInterface> getBlockchain() {
		ArrayList<ExtendedBlockInterface> blockchain = new ArrayList<ExtendedBlockInterface>();
		ExtendedBlockInterface topBlock = this.getTopBlock();
		if (topBlock == null)
			return blockchain;
		while (topBlock.getInternBlock().getPreviousBlock() != null){
			blockchain.add(topBlock);
			for (ExtendedBlockInterface eBlock:blocklist) {
				if (eBlock.getInternBlock() == topBlock.getInternBlock().getPreviousBlock()) {
					topBlock = eBlock;
				}
			}
		}		
		blockchain.add(topBlock);

		return blockchain;
	}
	
	// validating the whole chains
	public boolean validateChain() {
		for (ExtendedBlockInterface block: blocklist) {
			if(!block.getInternBlock().validateBlock()) {
				return false;
			}
		}
		return true;
	}	
}
