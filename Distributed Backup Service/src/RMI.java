import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI extends Remote{
    void backup(String filePath, int replicationDegree) throws RemoteException;
}