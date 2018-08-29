package Node;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import Block.BlockBase;
import Transaction.*;

public interface NetworkInterface extends Remote {
    
	// getting the vlient version
	String getClienVersion() throws RemoteException;
    
	// getting peer list
    ArrayList<Peer> getPeerList (Peer _callee) throws RemoteException;
    
    // checking if peer is alive
    boolean isPeerAlive() throws RemoteException;
  
    // broadcasting a transaction to the network
    void boradcastTransaction (StateTransaction tr) throws RemoteException;
    
    // broadcasting a block to the network
    void broadcastBlock(BlockBase block) throws RemoteException;
    
    // getting the height of the block of the connected peer
    int getMaxBlockHeight() throws RemoteException;
    
    // getting block header Id-s
    ArrayList<String> getInventar(int from, int to) throws RemoteException;
    
    // getting a block specified by the Id
    BlockBase getBlock(String blockId) throws RemoteException;

}

