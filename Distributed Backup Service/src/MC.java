import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MC implements Runnable{

    public static final int PACKET_MAX_SIZE = 64000;

    public MulticastSocket socket;
    public InetAddress address;
    public int port;

    public MC(InetAddress a, int p){
        this.address = a;
        this.port = p;
    }

    public void run() {

        //open socket
        try {
            socket = new MulticastSocket(port);

            socket.setTimeToLive(1);

            socket.joinGroup(address);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //listener
        boolean done = false;
        while (!done) {

            byte[] buf = new byte[PACKET_MAX_SIZE];

            try {

                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                socket.receive(packet);

                //packet handler
                new Thread(new Handler(packet)).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        socket.close();
    }
}