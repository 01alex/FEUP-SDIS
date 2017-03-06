/**
 * Created by luiscosta on 3/6/17.
 */

import java.io.IOException;
import java.net.*;
import java.net.InetAddress;
import java.util.*;

public class Server {

    public static void main(String[] args) throws IOException {

        int port = 5000;
        InetAddress mcaddr = InetAddress.getByName(args[0]);

        //which ttl
        int ttl = 1;

        //Create socket but we don't bind it as we are only going to send data

        MulticastSocket s = new MulticastSocket();

        //Note that we don't have to join the multicast group since we are only sending and not receiving

        //Fill the buffer with some data
        byte buf[] = new byte[1024];
        for(int i = 0; i < buf.length; i++)
            buf[i] = (byte)i;
        //Create a Datagram Packet
        DatagramPacket packet = new DatagramPacket(buf, buf.length, mcaddr, port);

        //Send.
        //Note that the send takes a byte for the ttl and not for an int
        //s.send(packet, (byte)ttl);
        System.out.printf("Sending multicast...");
        s.send(packet);

        //and then we close the socket
        s.close();

    }
}
