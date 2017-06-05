import java.io.*;

public class Peer implements Serializable{

    private String ip;
    private int port;
    private static final long serialVersionUID = 1L;

    public Peer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIP(){
        return ip;
    }

    public int getPort(){
        return port;
    }

}
