package Node;

import java.util.ArrayList;

import Block.Block;
import Transaction.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


// class for representing a peer -> implicitely as a client
public class Peer implements NetworkInterface, Serializable  {
	  
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
				NetworkInterface stub = (NetworkInterface)registry.lookup(Network.serverNameBase+peerPort);
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
				NetworkInterface stub = (NetworkInterface)registry.lookup(Network.serverNameBase+peerPort);
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
				NetworkInterface stub = (NetworkInterface)registry.lookup(Network.serverNameBase+peerPort);
				boolean isPeerAlive = stub.isPeerAlive();
				return isPeerAlive;
			} catch (Exception e) {
				System.err.println("Client exception: " + e.toString());
				e.printStackTrace();
			}
			return false;
	    }
	
	    
	    public void boradcastTransaction (StateTransaction tr) {
			try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkInterface stub = (NetworkInterface)registry.lookup(Network.serverNameBase+peerPort);
				stub.boradcastTransaction(tr);
			} catch (Exception e) {
				System.err.println("Client exception: " + e.toString());
				e.printStackTrace();
			}	    	
	    }
	    
	    public void broadcastBlock(Block block) {
			try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkInterface stub = (NetworkInterface)registry.lookup(Network.serverNameBase+peerPort);
				stub.broadcastBlock(block);
			} catch (Exception e) {
				System.err.println("Client exception: " + e.toString());
				e.printStackTrace();
			}	    		    	
	    }
}
