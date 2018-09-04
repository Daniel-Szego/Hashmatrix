package Node;

// configuration class for simmarizing the network / client config
public class ClientConfiguration {
	
	// which port is used locally
	public int usedPort;

	// is the network started
	public boolean networkStarted = false;

	// host information of the peer
	public String peerHost;
	
	// the variable shows if this is the local peer information
	public boolean isLocal;
	
	// boolean indicating if the peer is alive
	public boolean isAlive;
	
}
