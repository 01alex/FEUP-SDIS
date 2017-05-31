
import java.security.MessageDigest;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.net.DatagramPacket;

public class Utils{

    //stuff to be used across classes

    public static final int PACKET_MAX_SIZE = 64000;
    public static final int MAX_CHUNKS_PER_FILE = 1000000;
    public static final int DISK_SIZE = 2560;
    public static final String CRLF = "\r\n";

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

    //http://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha256-in-java/11009612#11009612
    public static String sha256(String base) {
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
}