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

