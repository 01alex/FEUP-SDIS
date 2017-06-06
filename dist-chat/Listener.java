import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Boolean;

public class Listener extends Thread {

    private Socket peer;

    public Listener(Socket peer) throws IOException {

        this.peer = peer;
    }

    public void run(){

        Boolean done = false;
        while(!done) {

            try {

                ObjectInputStream ois = new ObjectInputStream(peer.getInputStream());

                Message msg = (Message) ois.readObject();


                if(msg.getHeader().equals("join")) {

                    Chat.sendConnectedPeers(msg.getOriginIP());

                    for(Peer p : Chat.peers){
                        if(p.getIP().equals(Chat.ip))
                            continue;
                        Chat.resendJoinPeer(p.getIP(), msg);
                    }

                    addPeer(msg.getOriginIP());
                }
                else if(msg.getHeader().equals("get_peers")) {

                    for(Peer p : msg.getPeers())
                        addPeer(p.getIP());

                }
                else if(msg.getHeader().equals("resend_join")) {

                    addPeer(msg.getOriginIP());
                }
                else if(msg.getHeader().equals("chat_msg"))
                    System.out.println("\n" + msg.getOriginIP() + ": " + msg.getBody());


            }catch(Exception e){

                Peer toRemove = null;

                for(Peer p : Chat.peers)
                    if(p.getIP().equals(peer.getInetAddress().getHostAddress()))
                        toRemove = p;

                System.out.println("Removed peer at " + toRemove.getIP());
                Chat.peers.remove(toRemove);

                done = true;

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
        for(Peer cp : Chat.peers){
            if(cp.getIP().equals(Chat.ip))
                continue;
            System.out.println(cp.getIP() + "\n");
        }

        return true;
    }
}
