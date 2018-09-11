package Node;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import Block.BlockBase;
import Block.BlockInterface;
import ServiceBus.ServiceInterface;
import Transaction.*;

// abstract services for the network interface
// reachable from inside
public interface NetworkServiceInterface extends ServiceInterface, Remote {
	
	// braodcasting a transaction to the network
	public void broadcastTransaction(TransactionInterface tr);

	// braodcasting a transaction to the network
	public void broadcastBlock(BlockInterface block);

	
	
	
}
