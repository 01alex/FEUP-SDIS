import java.util.*;
import java.rmi.server.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import java.net.UnknownHostException;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;

public class Peer implements RMI{

    private static MulticastSocket socket;

    private static MC mcChanel;
    private static MDB mdbChanel;
    private static MDB mdrChanel;

    private static Disk disk;
    private static String ipv4_str;

    public static int serverID;
    public static String serviceAP; //nome obj remoto
    public static int protocol_v;

    public static HashMap<String, FileC> sharedFiles = new HashMap<String, FileC>(); //path, FileC
    public static HashMap<String, List<Chunk>> storedChunks = new HashMap<String, List<Chunk>>(); //fileID, chunks

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

    public static void sendToMDB(byte[] buf) {
        DatagramPacket packet = new DatagramPacket(buf, buf.length,
                mdbChanel.address, mdbChanel.port);

        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendToMDR(byte[] buf) {
        DatagramPacket packet = new DatagramPacket(buf, buf.length,
                mdrChanel.address, mdrChanel.port);

        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void storeChunk(Chunk chunk){
        storedChunks.get(chunk.getFileID()).add(chunk);
        disk.storeData(chunk.getLength()/1000);     //convert to kB
    }

    public void backup(String filePath, int replicationDegree) throws RemoteException {
        Thread t = new Thread(new Backup(filePath, replicationDegree));
        t.start();

        try{
            t.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void deleteChunk(Chunk chunk){
        if(!storedChunks.get(chunk.getFileID()).remove(chunk)){
            System.out.println("Error deleting chunk from hashmap\n");
            return;
        }

        disk.deleteData(chunk.getLength()/1000);        //convert to kB
    }

    public void delete(String filePath) throws RemoteException {
        Thread t = new Thread(new Delete(filePath));
        t.start();

        try{
            t.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void restore(String filePath){
        Thread t = new Thread(new Restore(filePath));
        t.start();

        try{
            t.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public String state() throws RemoteException{

        String state = "\n========== SHARED FILES ==========\n";

        for (HashMap.Entry<String, FileC> entry : sharedFiles.entrySet()) {

            FileC file = entry.getValue();

            String fileID = file.getFileID();

            String file_info = "\nFile path name: " + entry.getKey() +
                    "\nID: " + fileID +
                    "\nDesired Replication Degree: " + file.getRepDegree() + "\n";

            String chunk_info = "";

            for(int i=0; i<file.getChunks().size(); i++){
                Chunk c = file.getChunks().get(i);
                String chunkID = c.getFileID() + c.getChunkNo();

                chunk_info += "\nChunk ID: " + chunkID +
                        "\nPerceived Replication Degree: " + c.getRepDegree() + "\n";

            }

            state += file_info + chunk_info;
        }

        state += "\n========== STORED CHUNKS ==========\n";

        for(HashMap.Entry<String, List<Chunk>> entry : storedChunks.entrySet()){

            String chunk_info = "";

            for(int i=0; i<entry.getValue().size(); i++){
                Chunk c = entry.getValue().get(i);

                chunk_info += "\nChunk ID: " + c.getFileID() + c.getChunkNo() +
                        "\nChunk size: " + c.getLength() + "B" +
                        "\nPerceived Replication Degree: " + c.getRepDegree() + "\n";

            }
            state += chunk_info;
        }

        state += "\n========== STORAGE INFO ==========\n";

        state += "\nDisk total capacity: " + disk.getCapacity() + "kB\n" +
                "\nUsed space: " + disk.getUsedSpace() + "kB\n" +
                "\nFree space: " + disk.getFreeSpace() + "kB\n";

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
        mdbChanel = new MDB(mdbA, mdbP);
        mdrChanel = new MDB(mdrA, mdrP);

        return true;
    }

    public static void main(String[] args) throws IOException {

        if (!procArgs(args))
            return;

        ipv4_str = Utils.getIPv4().getHostAddress();
        System.setProperty("java.rmi.server.hostname", ipv4_str);
        System.out.println("\nIPv4 " + ipv4_str);

        socket = new MulticastSocket();

        disk = new Disk(Utils.DISK_SIZE);

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
        new Thread(mdbChanel).start();
        new Thread(mdrChanel).start();

        System.out.println("Peer Ready\n");
    }
}
