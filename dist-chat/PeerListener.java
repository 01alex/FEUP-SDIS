import java.io.IOException;
import java.net.ServerSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.util.ArrayList;


public class PeerListener implements Runnable {

    private final Chat chat;

    private ServerSocket serverSocket;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private int listenerPort = 8008;

    public PeerListener(final Chat chat) {
        this.chat = chat;
    }

    @Override
    public void run() {
        try {
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1){

        }
    }

    private synchronized void listen() throws IOException, ClassNotFoundException {
        try {
            serverSocket = new ServerSocket(listenerPort);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Listener.listen");
        }

        boolean done = false;
        while (!done) {

            System.out.println("DEBUG1\n");
            Socket s = null;

            try {
                s = serverSocket.accept();
                ois = new ObjectInputStream(s.getInputStream());
                oos = new ObjectOutputStream(s.getOutputStream());
            } catch (IOException e) {
                System.err.println("Server Socket accept failed");
                System.exit(1);
            }

            Message msg = (Message) ois.readObject();

            if(msg.getOriginIP().equals(Utils.getIPv4().getHostAddress()))
                continue;

            System.out.println("DEBUG2 " + msg.getType() + "\n");

            if(msg.getType().equals("JOIN"))
                handleJoin(msg, s);
            else if(msg.getType().equals("GET_PEERS"))
                handleGetPeers();
            else if(msg.getType().equals("PEERS"))
                handlePeers(msg);
            else  System.out.println("DEBUG3");


        }

        serverSocket.close();

        ois.close();
        oos.close();

        System.out.println("Server terminated.");
    }

    private void handleJoin(Message msg, Socket s) throws IOException{
        System.out.println("HANDLE JOIN 1 msgOriginIP " + msg.getOriginIP() + "\n");

        Peer peer = new Peer(msg.getOriginIP(), 8008, chat);
        System.out.println("HANDLE JOIN 2\n");

        peer.setNetworkData(s, ois, oos);
        System.out.println("HANDLE JOIN 3\n");

        chat.peers.add(peer);

        System.out.println("Peer " + peer + " added");
    }

    private void handleGetPeers() throws IOException{
        ArrayList<String> peersIP = new ArrayList<String>();

        for (Peer peer : chat.peers)
            peersIP.add(peer.getIP());

        oos.writeObject(new Message(peersIP));

    }

    private void handlePeers(Message msg) throws IOException{
        for (String peerIP : msg.getPeersIP()) {
            if (peerIP.equals(Utils.getIPv4().getHostAddress()))
                continue;

            Peer peer = new Peer(peerIP, chat);
            peer.createNetworkData();

            // add to peers array
            chat.peers.add(peer);

            // send JOIN
            chat.sendJoin(peerIP);
        }

    }

}
