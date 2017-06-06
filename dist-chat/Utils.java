import java.security.MessageDigest;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Date;
import java.text.*;

public class Utils{

    //get LAN IPv4
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

    //get hh:mm:ss timestamp
    public static String getTime(){
        DateFormat df = new SimpleDateFormat("hh:mm:ss");
        Date dateobj = new Date();
        return df.format(dateobj);
    }

}