package Node;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import Block.Block;
import Transaction.*;

public interface NetworkInterface extends Remote {
    
	String getClienVersion() throws RemoteException;
    
    ArrayList<Peer> getPeerList () throws RemoteException;
    
    boolean isPeerAlive() throws RemoteException;
  
    void boradcastTransaction (StateTransaction tr);
    
    void broadcastBlock(Block block);
    
}

