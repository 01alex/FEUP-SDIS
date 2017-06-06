import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{

    private static final long serialVersionUID = 1L;

    private String originIP;
    private String header;
    private String body;
    private ArrayList<Peer> peers;

    public Message(){
        this.header = "join";

        addIP();
    }

    public Message(String body) {
        this.header = "chat_msg";
        this.body = body + " (" + Utils.getTime() + ")";

        addIP();
    }

    public Message(ArrayList<Peer> peers){
        this.header = "get_peers";
        this.peers = peers;

        addIP();
    }

    private void addIP() {
        try {
            originIP = Utils.getIPv4().getHostAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getOriginIP() {
        return originIP;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public ArrayList<Peer> getPeers() {
        return peers;
    }

    public void setHeader(String header){
        this.header = header;
    }

    public void setBody(String body){
        this.body = body;
    }

}
