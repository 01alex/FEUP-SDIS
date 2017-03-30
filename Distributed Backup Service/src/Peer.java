import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.rmi.server.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.IndexOutOfBoundsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Peer implements RMI{

    public static final int MAX_CHUNKS_PER_FILE = 1000000;
    public static final String CRLF = "\r\n";

    private static MulticastSocket socket;
    private static MC mcChanel;
    private static Disk disk;

    private static String serviceAP; //nome obj remoto
    private static int protocol_v;
    private static String ipv4_str;

    public static MC getMcChanel() {
        return mcChanel;
    }

    public static MulticastSocket getSocket() {
        return socket;
    }

    //http://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha256-in-java/11009612#11009612
    private static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void sendToMC(byte[] buf) {
        DatagramPacket packet = new DatagramPacket(buf, buf.length,
                mcChanel.address, mcChanel.port);

        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PUTCHUNK(Chunk chunk) {
        String header = "PUTCHUNK";
        header += " " + protocol_v;						//Version
        header += " " + chunk.getFile().getHome();		//Sender ID
        header += " " + chunk.getChunkID().getFileID(); //FileID
        header += " " + chunk.getChunkNo();				//Chunk No
        header += " " + chunk.getRepDegree();			//Rep Degree
        header += " " + CRLF + CRLF;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(header.getBytes());
            outputStream.write(chunk.getData());

            byte message[] = outputStream.toByteArray();

            sendToMC(message);

        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
    
    public void sendDELETE(FileC file) {
    	String header = "DELETE";
    	header += " " + protocol_v;       //Version
    	header += " " + file.getHome();   //Sender ID
    	header += " " + file.getFileID(); //File ID
    	header += " " + CRLF + CRLF;
    	
    	try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			outputStream.write(header.getBytes());
			
			byte message[] = outputStream.toByteArray();
			
			sendToMC(message);
			
		} catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void delete(String filepath) {
    	
    	File file = new File(filepath);
    	
    	if(!file.exists()) {
            System.out.println("File doesn't exist\n");
            return;
        }

    }

    public void backup(String filePath, int replicationDegree) throws RemoteException {

        File file = new File(filePath);

        if(!file.exists()) {
            System.out.println("File doesn't exist\n");
            return;
        }

        String mix = file.getName() + file.length() + file.lastModified();
        String fileID = sha256(mix);

        /*System.out.println("File " + f.getName() +
                "\nSize " + f.length() +
                "\nLast Modified " + f.lastModified() +
                "\nSHA256 FileID " + sha);*/


        FileC fileDBS = new FileC(fileID, serviceAP); 

        System.out.println("File found ID: " + fileDBS.getFileID());



            byte[] fileData = loadFile(filePath);
            System.out.println("\nFile data length " + fileData.length);

            int numChunks = fileData.length / mcChanel.PACKET_MAX_SIZE + 1;

            if(numChunks > MAX_CHUNKS_PER_FILE){
                System.out.println("File size limit 64GB\n");
                return;
            }

            else {

                ByteArrayInputStream stream = new ByteArrayInputStream(fileData);
                byte[] buf;

                for (int i = 0; i < numChunks; i++) {

                    byte[] data = null;

                    if(i == numChunks-1)
                       buf = new byte[fileData.length - (i * mcChanel.PACKET_MAX_SIZE)];
                    else buf =  new byte[mcChanel.PACKET_MAX_SIZE];

                    System.out.println("\nFile " + file.getName() + " Chunk # " + i);

                    try {

                        int numBytesRead = stream.read(buf, 0,
                                buf.length);

                        data = Arrays.copyOfRange(buf, 0,
                                numBytesRead);

                    } catch(IndexOutOfBoundsException e){
                        System.out.println("Error reading chunk\n" + "len: " + buf.length + "\nb.length: " + buf.length + "\noff: " + i*buf.length);
                        e.printStackTrace();
                    }

                    Chunk chunk = new Chunk(fileDBS, replicationDegree, data);
                    chunk.setChunkNo(i);

                    PUTCHUNK(chunk);
                }

            }

    }

    public static byte[] loadFile(String filePath) {

        byte[] data = null;

        try {
            data = Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            System.out.println("Error loading file\n");
            e.printStackTrace();
        }

        return data;

    }

    private static boolean procArgs(String[] args) throws UnknownHostException {

        InetAddress mcA, mdbA, mdrA;
        int mcP, mdbP, mdrP;

        if (args.length == 9) {

            serviceAP = args[2];

            mcA = InetAddress.getByName(args[3]);
            mdbA = InetAddress.getByName(args[5]);
            mdrA = InetAddress.getByName(args[7]);

            try {
                protocol_v = Integer.parseInt(args[0]);
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

        mcChanel = new MC(mcA, mcP);

        return true;
    }

    public static InetAddress getIPv4() throws IOException {
        MulticastSocket socket = new MulticastSocket();
        socket.setTimeToLive(0);

        InetAddress addr = InetAddress.getByName("225.0.0.0");
        socket.joinGroup(addr);

        byte[] bytes = new byte[0];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, addr,
                socket.getLocalPort());

        socket.send(packet);
        socket.receive(packet);

        socket.close();

        return packet.getAddress();
    }

    public static void main(String[] args) throws IOException {

        if (!procArgs(args))
            return;

        ipv4_str = getIPv4().getHostAddress();
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
