import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.Boolean;


public class Listener extends Thread {

    private Socket peer;

    public Listener(Socket peer) throws IOException {
 
        this.peer = peer;
        
    }


    /**************************************************
     * runs the threads to listen to the port and talk to the peer
     */
    public void run(){

        Boolean done = false;
        while(!done) {

            try {

                ObjectInputStream ois = new ObjectInputStream(peer.getInputStream());

                Message msg = (Message) ois.readObject();

                //System.out.println("MSG HEADER " + msg.getHeader());

                if (msg.getHeader().equals("join")) {

                    Chat.sendConnectedPeers(msg.getOriginIP());

                    addPeer(msg.getOriginIP());
                }
                else if (msg.getHeader().equals("get_peers")) {
                 
                    for (Peer p : msg.getPeers())
                        addPeer(p.getIP());                     
                    
                }
                else if (msg.getHeader().equals("chat_msg"))
                    System.out.println(msg.getOriginIP() + ": " + msg.getBody());

            }catch(Exception e){
                System.out.println("Exception " + e + " on listener run");
            }

        }

    }

    public Boolean addPeer(String ip) throws Exception{
        for(Peer p : Chat.peers)
            if(p.getIP().equals(ip))
                return false;

        Peer p = new Peer(ip, 8008);

        Chat.peers.add(p);

        System.out.println("Connected peers: \n");
        for(Peer cp : Chat.peers)
            System.out.println(cp.getIP() + "\n");

        return true;
    }
}
