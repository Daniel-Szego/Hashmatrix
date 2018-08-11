package Node;

import java.util.ArrayList;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


// class for representing a peer -> implicitely as a client
public class Peer implements NetworkInterface {
	
	// peerId -> known peer implementation
	public String peerId;
	public String peerHost;
	public int peerPort;
	public boolean active;
	
	public Peer(String _peerHost, int _peerPort){
		peerHost = _peerHost;
		peerPort = _peerPort;
	}
	
	// REMOTE FUNCTION client wrapper 
	public String getClienVersion() {
			try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				Network stub = (Network)registry.lookup(Network.serverNameBase+peerPort);
				String clientVersion = stub.getClienVersion();
				return clientVersion;
			} catch (Exception e) {
				System.err.println("Client exception: " + e.toString());
				e.printStackTrace();
			}
			return null;
		}
		
		// returning the known peers
	    public ArrayList<Peer> getPeerList () 
	    {
			try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				Network stub = (Network)registry.lookup(Network.serverNameBase+peerPort);
				ArrayList<Peer> peerList = stub.getPeerList();
				return peerList;
			} catch (Exception e) {
				System.err.println("Client exception: " + e.toString());
				e.printStackTrace();
			}
			return null;
	    }

	    // if it gets back some information, it means, the peer is still alive
	    public boolean isPeerAlive() {
			try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				Network stub = (Network)registry.lookup(Network.serverNameBase+peerPort);
				boolean isPeerAlive = stub.isPeerAlive();
				return isPeerAlive;
			} catch (Exception e) {
				System.err.println("Client exception: " + e.toString());
				e.printStackTrace();
			}
			return false;
	    }

	    
	
}