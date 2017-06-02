import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{

    private static final long serialVersionUID = 1L;

    private String originIP;

    private String body;

    public Message(String body) {
        this.body = body;

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

    public String getBody() {
        return body;
    }

}
