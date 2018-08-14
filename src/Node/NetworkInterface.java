package Node;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import Block.Block;
import Transaction.*;

public interface NetworkInterface extends Remote {
    
	String getClienVersion() throws RemoteException;
    
    ArrayList<Peer> getPeerList (Peer _callee) throws RemoteException;
    
    boolean isPeerAlive() throws RemoteException;
  
    void boradcastTransaction (StateTransaction tr) throws RemoteException;
    
    void broadcastBlock(Block block) throws RemoteException;
    
}

