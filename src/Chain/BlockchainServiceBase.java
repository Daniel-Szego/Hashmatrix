package Chain;

import java.util.ArrayList;

import Block.BlockInterface;
import Block.GenesisBlockInterface;
import Node.*;
import ServiceBus.*;

// implements and agregates services related to the blockchain and block pool
public class BlockchainServiceBase extends ServiceBase  implements BockchainServiceInterface, BlockchainInterface, BlockPoolInterface {

	protected BlockchainInterface blockchain;
	protected BlockPoolInterface pool;
	protected boolean isSynced;
	
	public BlockchainServiceBase(ServiceBus _bus) {
		super(ServiceBus.crypto.getRandomString(), _bus);
		this.isSynced = false;
	}
	
	// BLOCKCHAIN SERVICES
	
	// adding the genesis block to the blockchain
	public ExtendedBlockInterface addGenesisBlock(GenesisBlockInterface _block) {
		return blockchain.addGenesisBlock( _block);
	}
	
	// adding block to the blockchain
	// the wrapper is created and added to the chain
	public ExtendedBlockInterface addBlock(BlockInterface _block) {
		return blockchain.addBlock(_block);
	}
	
	// getting block height
	public int getBlockchinHeight() {
		return blockchain.getBlockchinHeight();
	}
	
	// getting top blocks 
	public ArrayList<ExtendedBlockInterface> getTopBlocks() {
		return blockchain.getTopBlocks();
	}
	
	// getting the top block after fork resolution
	public ExtendedBlockInterface getTopBlock() {
		return blockchain.getTopBlock();
	}
	
	// getting the top stable block
	// depending on the stable strategy
	public ExtendedBlockInterface getTopStableBlock() {
		return blockchain.getTopStableBlock();
	}
	
	// getting the whole blockchain
	public ArrayList<ExtendedBlockInterface> getBlockchain() {
		return blockchain.getBlockchain();
	}
		
	// validating the whole chains
	public boolean validateChain() {
		return blockchain.validateChain();
	}

	// POOL SERVICES
	
	// getting the blocks of the pool
	public ArrayList<BlockInterface> getBlocksFromPool() {
		return pool.getBlocksFromPool();
	}
	
	// adding block to the chain if already not exist in the pool
	public void addBlockToPool(BlockInterface _block) {
		pool.addBlockToPool(_block);
	}
	
	// deleting a block based on the block id
	public void deleteBlockByIdFromPool(String blockId) {
		pool.deleteBlockByIdFromPool(blockId);
	}
	
	// deleting the block from the block pool
	public void deleteBlockFromPool(BlockInterface _block) {
		 pool.deleteBlockFromPool(_block);
	}
	
	// SERVICE BUS SERVICES
	// getting the service id
	public String getServiceId() {
		return serviceId;
	}
	
	
	// getting the blockchain
	public BlockchainInterface getInternalBlockchain() {
		return this.blockchain;
	}
	
	// getting the block pool
	public BlockPoolInterface getInternalBlockPool() {
		return this.pool;
	}

	
}
