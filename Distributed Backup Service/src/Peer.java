import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.io.File;

public class Peer {

    private static MulticastSocket socket;
    private static MC mcChanel;
    private static Disk disk;

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

    private static boolean procArgs(String[] args) throws UnknownHostException {

        InetAddress mcA, mdbA, mdrA;
        int mcP, mdbP, mdrP;

        if (args.length == 9) {
            mcA = InetAddress.getByName(args[3]);
            mdbA = InetAddress.getByName(args[5]);
            mdrA = InetAddress.getByName(args[7]);

            try {
                mcP = Integer.parseInt(args[4]);
                mdbP = Integer.parseInt(args[6]);
                mdrP = Integer.parseInt(args[8]);

            } catch (NumberFormatException e) {
                System.out.println("Port must be an integer.\n");
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

    public static void main(String[] args) throws IOException {

        if (!procArgs(args))
            return;

        socket = new MulticastSocket();

        /*
        //test FileID
        File f = new File("./MC.java");

        String mix = f.getName() + f.length() + f.lastModified();
        String sha = sha256(mix);

        System.out.println("File " + f.getName() + "\nSize " + f.length() + "\nLast Modified " + f.lastModified() + "\nSHA256 FileID " + sha);
        */

        disk = new Disk(2400);
        System.out.println("Disk with " + disk.getCapacityKB() + "kB.\n");

        System.out.println("Peer Ready\n");


        new Thread(mcChanel).start();
    }
}