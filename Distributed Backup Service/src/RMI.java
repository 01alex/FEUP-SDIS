import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI extends Remote {
    void backup(String filePath, int replicationDegree) throws RemoteException;

    void delete(String filePath) throws RemoteException;

    void restore(String filePath) throws RemoteException;

    String state() throws RemoteException;
}