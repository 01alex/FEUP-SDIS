import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{

    private static final long serialVersionUID = 1L;

    private String type;

    private String originIP;

    private ArrayList<String> peersIP;

    public Message(String type) {
        this.type = type;

        addIP();
    }

    public Message(ArrayList<String> peersIP) {
        this.type = type;
        this.peersIP = peersIP;

        addIP();
    }

    private void addIP() {
        try {
            originIP = Utils.getIPv4().getHostAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getType() {
        return type;
    }

    public String getOriginIP() {
        return originIP;
    }

    public ArrayList<String> getPeersIP() {
        return peersIP;
    }

}
