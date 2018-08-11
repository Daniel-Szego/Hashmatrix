package Node;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface NetworkInterface extends Remote {
    
	String getClienVersion() throws RemoteException;
    
    ArrayList<Peer> getPeerList () throws RemoteException;
    
    boolean isPeerAlive() throws RemoteException;
  
    
}

