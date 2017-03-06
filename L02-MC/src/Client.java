import java.io.IOException;
import java.net.*;
import java.net.InetAddress;
import java.util.*;
public class Client {

    public static void main(String[] args) throws IOException {

        if(args.length < 4) {
            System.out.println("java Client <multicastAddr>");
            return;
        }

        //which port should we listen to
        int port = 5000;
        //which address
        InetAddress mcaddr = InetAddress.getByName(args[0]);
        System.out.println("ADDR " + mcaddr);

        //create socket and bind it to port 'port'
        MulticastSocket s = new MulticastSocket(port);

        //join the multicast group
        s.joinGroup(mcaddr);

        //Now that the socket is set up and we are ready to receive packets
        //Create the DatagramPacket and do a receive

        byte buf[] = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        s.receive(packet);

        //Now let's make and ACK message

        System.out.println("Received data from: " + packet.getAddress().toString() + "\t" + packet.getPort() +
                "\t with length: " + packet.getLength());
        System.out.println();

        //And when we're finished receiving data, we leave the group and close the socket

        s.leaveGroup(mcaddr);
        s.close();

    }
}
