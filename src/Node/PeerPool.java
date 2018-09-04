package Node;

import java.util.ArrayList;

// service for managing a pool of peers
public class PeerPool implements PeerPoolInterface {

	protected ArrayList<Peer> peers;
	
	public PeerPool() {
		peers = new ArrayList<Peer>();
	}
	
	// adding peer to the pool
	public void addPeer(String host, int port, boolean isLocal) {
		Peer newPeer = new Peer(host, port, isLocal);
		this.peers.add(newPeer);		
	}
	
	// adding peer to the pool
	public void addPeer(Peer peer) {
		this.peers.add(peer);
	}
	
	// deleting peer by id
	public void deletePeer(String peerId) {
		Peer toDelete = null;
		for(Peer peer: peers) {
			if (peer.peerId.equals(peerId))
				toDelete = peer;
		}
		if (toDelete != null)
			peers.remove(toDelete);
	}

	// deleting peer by object
	public void deletePeer(Peer peer) {
		this.deletePeer(peer);
	}
	
	// checking if the peers are alive
	public void checkPeersAlive() {
		for(Peer peer: peers) {
			if (!peer.clientConfig.isLocal) {
				try	
				{
					peer.isPeerAlive();
					peer.clientConfig.isAlive = true;
				}
				catch (Exception ex) {
					peer.clientConfig.isAlive = false;					
				}
			}	
		}
	}	
}
