package Node;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// handling networks, connections and sockets
public class Network implements NetworkInterface  {

	// Constants
	public static int defaultport = 8425;
	public static String serverNameBase = "NetworkInterface";
	
	public int usedPort;
	public final Node node;
	public String clientVersion = "0.0.2";
	public boolean networkStarted = false;
	public Registry registry;
	
	public Network() {
		node = null;
	}
	
	public Network(Node _node) {
		this.node = _node;		
	}
	
	public String getClienVersion() {
	  return clientVersion;
	}
	
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
			
}
