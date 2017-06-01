
import java.io.IOException;
import java.net.InetAddress;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

import java.util.ArrayList;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class Chat {

    public ArrayList<Peer> peers;
    public Peer peer;

    public Chat(String chatIP) throws IOException{

        peer = new Peer(Utils.getIPv4().getHostAddress(), 8008, this);

        initPeerNetwork(chatIP);

    }

    private static boolean procArgs(String[] args) {

        return true;
    }

    private void initPeerNetwork(String chatIP) throws IOException{
        peers = new ArrayList<Peer>();

        String usersList = peer.getPeers(chatIP);

        new Thread(new PeerListener(this)).start();

        if(usersList.isEmpty())
            peer.joinPeer(chatIP);
        else
            sendJoin(chatIP);

    }

    private void createChat() throws IOException, MalformedURLException {

        String[] paramName = { "userip" };
        String[] paramVal = { Utils.getIPv4().getHostAddress() };

        String ret = new HttpRequest(Utils.getIPv4().getHostAddress(), "/joinUser").POST(paramName,
                paramVal);

    }

    public void sendJoin(String chatIP){

        try {

            String responseStr = peer.getPeers(chatIP);

            String[] users = responseStr.split("\\s+");

            String ip = users[0].split("\n")[0];

            System.out.println("Response: " + ip + "\n");

            peer.createNetworkData();

            peer.oos.writeObject(new Message("JOIN"));

            peers.add(peer);

            peer.joinPeer(ip);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("initPeerNetwork - IOException");
        }

    }

    public static void main(String[] args){

        try{
            String ip = args[0];

            Chat c = new Chat(ip);

        }
        catch(IOException e){

        }
    }
}

