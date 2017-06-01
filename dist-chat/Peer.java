
import java.io.IOException;
import java.net.InetAddress;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.InetSocketAddress;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Peer{

    private String ip;
    private int port;
    public static String joinIP;
    private Socket socket;
    public ObjectOutputStream oos;
    public ObjectInputStream ois;
    public Chat chat;

    public Peer(String ip, int port, Chat chat){
        this.ip = ip;
        this.port = port;
        this.chat = chat;

        System.out.println("New Peer at: " + ip);
    }

    public Peer(String ip, Chat chat){
        this.ip = ip;
        this.port = 8008;
        this.chat = chat;

        try {
            createNetworkData();
        }catch(IOException e){

        }

        System.out.println("New Peer at: " + ip);
    }

    //HTTP REQUESTS

    public String getPeers(String ip) throws IOException, MalformedURLException{

        String response = new HttpRequest(ip, "/getUsers").GET("UTF-8");

        return response;
    }

    public String joinPeer(String joinIP) throws IOException, MalformedURLException{
        String[] paramName = { "userip" };
        String[] paramVal = { ip };

        String response = new HttpRequest(joinIP, "/joinUser").POST(paramName, paramVal);

        return response;
    }

    //P2P TCP

    public void createNetworkData() throws IOException {
        socket = new Socket();
        socket.setReuseAddress(true);
        socket.connect(new InetSocketAddress(ip, port));
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    public void setNetworkData(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) throws IOException {
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
    }

    public String getIP() {
        return ip;
    }

    public Socket getSocket() {
        return socket;
    }

}
