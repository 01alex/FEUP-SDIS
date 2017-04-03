import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.rmi.server.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;
import java.lang.IndexOutOfBoundsException;
import java.util.*;

public class Peer implements RMI{

    private static MulticastSocket socket;
    private static MC mcChanel;
    private static Disk disk;
    private static String ipv4_str;

    public static int serverID;
    public static String serviceAP; //nome obj remoto
    public static int protocol_v;

    public static HashMap<String, List<String>> FileChunk = new HashMap<String, List<String>>();  //fileID, List<ChunkNames>
    public static HashMap<String, FileC> FileFileC = new HashMap<String, FileC>(); //filepathname, fileC


    public static MC getMcChanel() {
        return mcChanel;
    }

    public static MulticastSocket getSocket() {
        return socket;
    }

    public static void sendToMC(byte[] buf) {
        DatagramPacket packet = new DatagramPacket(buf, buf.length,
                mcChanel.address, mcChanel.port);

        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String filePath) throws RemoteException {

        if(!FileFileC.containsKey(filePath)){
            System.out.println("This server didn't back up file " + filePath);
            return;
        }

        String fileID = FileFileC.get(filePath).getFileID();

        new Thread(new Delete(fileID)).start();
    }

    public void backup(String filePath, int replicationDegree) throws RemoteException {
        new Thread(new Backup(filePath, replicationDegree)).start();
    }

    public String state() throws RemoteException{

        String state = "";

        for (HashMap.Entry<String, FileC> entry : FileFileC.entrySet()) {

            String fileID = entry.getValue().getFileID();

            String file_info = entry.getKey() +
                                "\nID: " + fileID +
                                "\nDesired Replication Degree: " + entry.getValue().getRepDegree() + "\n";

            /*ArrayList<Chunk> chunks = FileFileC.get(fileID).getChunks();

            String chunk_info = "";

            for(int i=0; i<chunks.size(); i++){

                Chunk c = chunks.get(i);
                String cid = c.getChunkID().getFileID()+ c.getChunkID().getChunkNo();

                chunk_info = chunk_info + "\nChunk " + i + " ID " + cid +
                                    "\nPerceived Replication Degree: " + c.getRepDegree() + "\n";
            }

            state += file_info + chunk_info;*/
            state += file_info;
        }

        return state;

    }

    private static boolean procArgs(String[] args) throws UnknownHostException {

        InetAddress mcA, mdbA, mdrA;
        int mcP, mdbP, mdrP;

        if (args.length == 9) {

            serviceAP = args[2];

            try {
                mcA = InetAddress.getByName(args[3]);
                mdbA = InetAddress.getByName(args[5]);
                mdrA = InetAddress.getByName(args[7]);

            }catch(UnknownHostException e){
                System.out.println("Address not found.\n");
                return false;
            }

            try {
                protocol_v = Integer.parseInt(args[0]);
                serverID = Integer.parseInt(args[1]);
                mcP = Integer.parseInt(args[4]);
                mdbP = Integer.parseInt(args[6]);
                mdrP = Integer.parseInt(args[8]);

            } catch (NumberFormatException e) {
                System.out.println("Protocol Version/Port must be an integer.\n");
                return false;
            }

        } else {
            System.out.println("Usage: java Peer <protocolV> <serverID> <serviceAP>" +
                    " <mcAddress> <mcPort> <mdbAddress> <mdbPort> <mdrAddress> <mdrPort>\n");
            return false;
        }

        System.out.println("Protocol Version: " + protocol_v +
                "\nServerID: " + serverID +
                "\nServiceAP: " + serviceAP +
                "\nMulticast Address: " + mcA +
                "\nMulticast Port: " + mcP +
                "\nMulticast Backup Address: " + mdbA +
                "\nMulticast Backup Port: " + mdbP +
                "\nMulticast Restore Address: " + mdrA +
                "\nMulticast Restore Port: " + mdrP);

        mcChanel = new MC(mcA, mcP);

        return true;
    }

    public static void main(String[] args) throws IOException {

        if (!procArgs(args))
            return;

        ipv4_str = Utils.getIPv4().getHostAddress();
        System.setProperty("java.rmi.server.hostname", ipv4_str);
        System.out.println("\nIPv4 " + ipv4_str);

        socket = new MulticastSocket();

        disk = new Disk(2400);
        System.out.println("Disk with " + disk.getCapacityKB() + "kB.\n");

        //register peer in rmi
        try {

            RMI peer = new Peer();

            RMI stub = (RMI) UnicastRemoteObject.exportObject(peer, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(serviceAP, stub);

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

        new Thread(mcChanel).start();

        System.out.println("Peer Ready\n");
    }
}
