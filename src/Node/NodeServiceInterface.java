package Node;

import Block.*;
import Chain.*;
import ServiceBus.*;
import Transaction.*;

// public interface for the services of a node
public interface NodeServiceInterface {
	
	// braodcasting a transaction to the network
	public void broadcastTransaction(TransactionInterface tr);

	// braodcasting a transaction to the network
	public void broadcastBlock(BlockInterface block);
	
	
}
