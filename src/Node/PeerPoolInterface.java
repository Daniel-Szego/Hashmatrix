package Node;

// interface for managing a set of peers
public interface PeerPoolInterface {
		
	// adding peer to the pool
	public void addPeer(String host, int port, boolean isLocal);
	
	// adding peer to the pool
	public void addPeer(Peer peer);
	
	// deleting peer by id
	public void deletePeer(String peerId);

	// deleting peer by object
	public void deletePeer(Peer peer);
	
	// checking if the peers are alive
	public void checkPeersAlive();	
	
}
