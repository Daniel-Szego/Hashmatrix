package Node;

// summarizing functionalities of the network
public interface NetworkInterface {

	// starting the network on the given port at the localhost
	public void startNetwork(int port);

	// stopping the local network
	public void stopNetwork();
	
	
}
