package Node;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NetworkInterface extends Remote {
    String getClienVersion() throws RemoteException;
}

