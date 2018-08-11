package Node;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

// handling networks, connections and sockets and server functionalities
public class Network implements NetworkInterface  {

	// network constants Constants
	public static int defaultport = 8425;
	public static String serverNameBase = "NetworkInterface";
	public static int peerLimit = 1;
		
	public int usedPort;
	public final Node node;
	public String clientVersion = "0.0.2";
	public boolean networkStarted = false;
	public Registry registry;
	public ArrayList<Peer> peers = new ArrayList<Peer>();
	
	public Network(Peer masterPeer) {
		node = null;
		this.peers.add(masterPeer);
	}
	
	public Network(Node _node) {
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
	  return clientVersion;
	}
	
	// returning the known peers
    public ArrayList<Peer> getPeerList () 
    {
    	return peers;
    }

    // if it gets back some information, it means, the peer is still alive
    public boolean isPeerAlive() {
    	return true;
    }
    
    
    
    // CLI FUNCTIONS
	// starting the rmiregistry
	public void startNetwork(int port) {
        
        try {
        	NetworkInterface stub = (NetworkInterface) UnicastRemoteObject.exportObject(this, 0);

            // Bind the remote object's stub in the registry
            //Registry registry = LocateRegistry.getRegistry();
        	if (port <= 1)
        		usedPort = defaultport;
        	else
        		usedPort = port;
        	registry = LocateRegistry.createRegistry(usedPort);
            registry.bind(serverNameBase + usedPort, stub);

            networkStarted = true;
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
				
		ArrayList<Peer> remotePeers = peer.getPeerList();
		
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
