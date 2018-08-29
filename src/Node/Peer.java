package Node;

import java.util.ArrayList;

import Block.BlockBase;
import Crypto.CryptoUtil;
import Transaction.*;
import Utils.LoggerConsole;
import Utils.Severity;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


// class for representing a peer -> implicitely as a client
public class Peer implements NetworkInterface, Serializable  {
	  
	// peerId -> known peer implementation
	public String peerId;
	public final String peerHost;
	public final int peerPort;
	public boolean active;
	
	public Peer(String _peerHost, int _peerPort){
		peerHost = _peerHost;
		peerPort = _peerPort;
		peerId = CryptoUtil.applySha256(
				peerHost +
				peerPort
				);
	}
	
	// REMOTE FUNCTION client wrapper 
	public String getClienVersion() {
			try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkInterface stub = (NetworkInterface)registry.lookup(Network.serverNameBase+peerPort);
				String clientVersion = stub.getClienVersion();
				return clientVersion;
			} catch (Exception e) {
				LoggerConsole.Log(e,Severity.CRITICAL);
			}
			return null;
		}
		
		// returning the known peers
	    public ArrayList<Peer> getPeerList (Peer _callee) 
	    {
			try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkInterface stub = (NetworkInterface)registry.lookup(Network.serverNameBase+peerPort);
				ArrayList<Peer> peerList = stub.getPeerList(_callee);
				return peerList;
			} catch (Exception e) {
				LoggerConsole.Log(e,Severity.CRITICAL);
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
				LoggerConsole.Log(e,Severity.CRITICAL);
			}
			return false;
	    }
	
	    
	    public void boradcastTransaction (StateTransaction tr) {
			try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkInterface stub = (NetworkInterface)registry.lookup(Network.serverNameBase+peerPort);
				stub.boradcastTransaction(tr);
			} catch (Exception e) {
				LoggerConsole.Log(e,Severity.CRITICAL);
			}	    	
	    }
	    
	    public void broadcastBlock(BlockBase block) {
			try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkInterface stub = (NetworkInterface)registry.lookup(Network.serverNameBase+peerPort);
				stub.broadcastBlock(block);
			} catch (Exception e) {
				LoggerConsole.Log(e,Severity.CRITICAL);
			}	    		    	
	    }
	    
	    public int getMaxBlockHeight () {
	    	try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkInterface stub = (NetworkInterface)registry.lookup(Network.serverNameBase+peerPort);
				return stub.getMaxBlockHeight();
			} catch (Exception e) {
				LoggerConsole.Log(e,Severity.CRITICAL);
			}
	    	return -1;
	    }
	    
	    // getting block header Id-s
	    public ArrayList<String> getInventar(int from, int to) {
	    	try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkInterface stub = (NetworkInterface)registry.lookup(Network.serverNameBase+peerPort);
				return stub.getInventar(from, to);
			} catch (Exception e) {
				LoggerConsole.Log(e,Severity.CRITICAL);
			}
	    	return null;	    	
	    }
	    
	    // getting a block specified by the Id
	    public BlockBase getBlock(String blockId) {
	    	try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkInterface stub = (NetworkInterface)registry.lookup(Network.serverNameBase+peerPort);
				return stub.getBlock(blockId);
			} catch (Exception e) {
				LoggerConsole.Log(e,Severity.CRITICAL);
			}
	    	return null;	    		    	
	    }
}
