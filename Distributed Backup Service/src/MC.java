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
        try {
            socket = new MulticastSocket(port);

            socket.setTimeToLive(1);

            socket.joinGroup(address);

        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buf = new byte[PACKET_MAX_SIZE];

        boolean done = false;
        while (!done) {
            try {

                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                socket.receive(packet);

                //packet handler
                String request = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received data from: " + packet.getAddress().toString() + "\t" + packet.getPort() +
                        "\t with length: " + packet.getLength() + "\t data: " + request);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        socket.close();
    }
}