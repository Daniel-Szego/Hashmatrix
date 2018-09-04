package Node;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Collections;

import Utils.*;


import Block.BlockBase;
import Chain.*;
import Transaction.*;

// handling networks, connections and sockets and server functionalities
public class Network implements NetworkRemoteInterface  {

	// network constants Constants
		
	public final NodeServiceBase node;
	public Registry registry;
	public Peer selfPeer;
	
	public Network(Peer masterPeer) {
		node = null;
		this.peers.add(masterPeer);
		selfPeer = masterPeer;
	}
	
	public Network(NodeServiceBase _node) {
		this.node = _node;		
	}
	
	public void addPeer(Peer peer) {
		peer.active = true;
		peers.add(peer);	
	}

	// REMOTE FUNCTIONS
	// Called remotely by other peers
	// getting the remote client version -> compatibility check
	public String getClienVersion() {
	  node.serviceBus.addEvent("getPeerList called");
	  return clientVersion;
	}
	
	// returning the known peers
    public ArrayList<Peer> getPeerList (Peer _callee) 
    {
    	// if  peer is not contained, add to the list
    	node.serviceBus.addEvent("getPeerList called, host :" + _callee.peerHost + " port : " + _callee.peerPort);
    	boolean contains = false;
    	for(Peer peer: peers) {
    		if (peer.peerId.equals(_callee.peerId))
    			contains = true;
    	} 
    	if (!contains)
    		peers.add(_callee);
    	return peers;
    }

    // if it gets back some information, it means, the peer is still alive
    public boolean isPeerAlive() {
    	return true;
    }
    
    // getting a bradcasted transaction from another peer
    public void boradcastTransaction (StateTransaction tr) {
		// SINGLE HOP -> transactions are not propagated further
    	// can be used in fully connected networks
    	
    	node.serviceBus.addEvent("bradcast transaction called, transaction Id :" + tr.getTransctionId());
    	node.serviceBus.addEventTransactionReceived("Transaction received", null, tr, true);
    	node.pool.addTransaction(tr);
    }
    
    // getting a broadcasted block from another peer
    public void broadcastBlock(BlockBase block) {
		// SINGLE HOP -> transactions are not propagated further
    	// can be used in fully connected networks    	
    	node.serviceBus.addEvent("bradcast block called, transaction Id :" + block.blockId);
    	node.serviceBus.addEventBlockReceived("block received",null,block,true);
    	node.blockchain.addBlock(block);	
    }
   
    // giving back the max block height
    public int getMaxBlockHeight () {
    	int blockHeight = node.blockchain.getBlockchinHeight();
    	node.serviceBus.addEvent("getMaxBlockHeigh has been called : " + blockHeight);
    	return blockHeight;
    }
    
    // getting block header Id-s
    public ArrayList<String> getInventar(int from, int to) {
    	node.serviceBus.addEvent("getInventar has been called, from : " + from + " to : " + to);
    	ArrayList<String> inventar = new ArrayList<String>();
    	for(ExtendedBlock block: node.blockchain.getBlockchain()) {
    		if ((from <= block.blockHeight) && (to >= block.blockHeight)) {
    			inventar.add(block.internBlock.blockId);
    		}
    	}
    	Collections.reverse(inventar);
    	return inventar;
    }
    
    // getting a block specified by the Id
    public BlockBase getBlock(String blockId) {
    	node.serviceBus.addEvent("getBloock has been called, Id : " + blockId);
    	for(ExtendedBlock block: node.blockchain.getBlockchain()) {
    		if (block.internBlock.blockId.equals(blockId)) {
    			return block.internBlock;
    		}
    	}
    	return null;
    }
    
    // CLI FUNCTIONS
	// starting the rmiregistry
	public void startNetwork(int port) {
        
        try {
        	//java.rmi.server.hostname = "localhost";
        	NetworkRemoteInterface stub = (NetworkRemoteInterface) UnicastRemoteObject.exportObject(this, 0);

            // Bind the remote object's stub in the registry
            //Registry registry = LocateRegistry.getRegistry();
        	if (port <= 1)
        		usedPort = defaultport;
        	else
        		usedPort = port;
        	registry = LocateRegistry.createRegistry(usedPort);
            registry.bind(serverNameBase + usedPort, stub);

            networkStarted = true;
            Peer newPeer = new Peer("localhost", usedPort);
            selfPeer = newPeer;
            this.addPeer(newPeer);
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
	
	// stopping and deleting rmiregistry 
	public void stopNetwork() {
		try {

		 registry.unbind(serverNameBase + usedPort);
         UnicastRemoteObject.unexportObject(registry, true);
         
 		 System.out.println("server stopped");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Syncing the peers 
	public void syncPeers(String host, int port){
		// if peer not contained
		Peer peer = this.getPeer(host, port);
		if (peer == null) {
			peer = new Peer(host, port);
			this.addPeer(peer);
		}
				
		ArrayList<Peer> remotePeers = peer.getPeerList(selfPeer);
		
		for(Peer remotePeer: remotePeers) {
			Peer localPeer = this.getPeer(remotePeer.peerHost, remotePeer.peerPort);
			if (localPeer == null) {
				this.addPeer(remotePeer);
			}
		}	
	}
		
	// HELP FUNCTIONS
	protected Peer getPeer(String host, int port){
		for(Peer peer: peers) {
			if ((peer.peerHost.equals(host)) && (peer.peerPort == port)) {
				return peer;
			}
		}
		return null;		
	}				
}

