package Node;

import java.util.ArrayList;

import Block.BlockBase;
import Crypto.CryptoUtil;
import Transaction.*;
import Utils.LoggerConsole;
import Utils.Severity;
import ServiceBus.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// class for representing a peer -> implicitely as a client
public class Peer implements NetworkRemoteInterface, Serializable  {
	  
	// peerId -> known peer implementation
	public String peerId;
	public final String peerHost;
	public final int peerPort;
	public boolean active;
	
	public Peer(String _peerHost, int _peerPort){
		peerHost = _peerHost;
		peerPort = _peerPort;
		peerId = ServiceBus.crypto.applyHash(
				peerHost +
				peerPort
				);
	}
	
	// REMOTE FUNCTION client wrappers
	//getting the client version
	public String getClienVersion() {
			try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkRemoteInterface stub = (NetworkRemoteInterface)registry.lookup(Constants.ServerNameBase + peerPort);
				String clientVersion = stub.getClienVersion();
				return clientVersion;
			} catch (Exception e) {
				ServiceBus.logger.log(e,Severity.ERROR);
			}
			return null;
		}
		
		//getting peer list
	    public ArrayList<Peer> getPeerList (Peer _callee) 
	    {
			try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkRemoteInterface stub = (NetworkRemoteInterface)registry.lookup(Constants.ServerNameBase + peerPort);
				ArrayList<Peer> peerList = stub.getPeerList(_callee);
				return peerList;
			} catch (Exception e) {
				ServiceBus.logger.log(e,Severity.ERROR);
			}
			return null;
	    }

	    //checking if peer is alive
	    public boolean isPeerAlive() {
			try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkRemoteInterface stub = (NetworkRemoteInterface)registry.lookup(Constants.ServerNameBase + peerPort);
				boolean isPeerAlive = stub.isPeerAlive();
				return isPeerAlive;
			} catch (Exception e) {
				ServiceBus.logger.log(e,Severity.ERROR);
			}
			return false;
	    }
	
	    //broadcasting a transaction to the network
	    public void boradcastTransaction (StateTransaction tr) {
			try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkRemoteInterface stub = (NetworkRemoteInterface)registry.lookup(Constants.ServerNameBase + peerPort);
				stub.boradcastTransaction(tr);
			} catch (Exception e) {
				ServiceBus.logger.log(e,Severity.ERROR);
			}	    	
	    }
	    
	    //broadcasting a block to the network
	    public void broadcastBlock(BlockBase block) {
			try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkRemoteInterface stub = (NetworkRemoteInterface)registry.lookup(Constants.ServerNameBase + peerPort);
				stub.broadcastBlock(block);
			} catch (Exception e) {
				ServiceBus.logger.log(e,Severity.ERROR);
			}	    		    	
	    }
	    
	    //getting the height of the block of the connected peer
	    public int getMaxBlockHeight () {
	    	try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkRemoteInterface stub = (NetworkRemoteInterface)registry.lookup(Constants.ServerNameBase + peerPort);
				return stub.getMaxBlockHeight();
			} catch (Exception e) {
				ServiceBus.logger.log(e,Severity.ERROR);
			}
	    	return -1;
	    }
	    
	    // getting block header Id-s
	    public ArrayList<String> getInventar(int from, int to) {
	    	try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkRemoteInterface stub = (NetworkRemoteInterface)registry.lookup(Constants.ServerNameBase + peerPort);
				return stub.getInventar(from, to);
			} catch (Exception e) {
				ServiceBus.logger.log(e,Severity.ERROR);
			}
	    	return null;	    	
	    }
	    
	    // getting a block specified by the Id
	    public BlockBase getBlock(String blockId) {
	    	try {
				Registry registry = LocateRegistry.getRegistry(peerHost,peerPort);
				NetworkRemoteInterface stub = (NetworkRemoteInterface)registry.lookup(Constants.ServerNameBase + peerPort);
				return stub.getBlock(blockId);
			} catch (Exception e) {
				ServiceBus.logger.log(e,Severity.ERROR);
			}
	    	return null;	    		    	
	    }
}
