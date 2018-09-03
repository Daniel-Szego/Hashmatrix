package Chain;

import java.util.ArrayList;

import Block.*;
import ServiceBus.ServiceInterface;

// implements blockchain services
public interface BockchainServiceInterface extends ServiceInterface {
	
	// getting the blockchain
	public BlockchainInterface getInternalBlockchain();
	
	// getting the block pool
	public BlockPoolInterface getInternalBlockPool();
	
}
