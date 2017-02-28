import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Client {

    private static String hostName;
    private static int port = 4445;
    private static String plate, owner, oper;

    public static void main(String[] args) throws IOException{

        if(!procArgs(args))
            return;

        DatagramSocket socket = new DatagramSocket();

        String req;

        if(oper.equals("REGISTER"))
            req = oper + "-" + plate + "-" + owner;
        else if(oper.equals("LOOKUP"))
            req = oper + "-" + plate;
        else req = null;

        byte[] buf = req.getBytes();
        InetAddress address = InetAddress.getByName(hostName);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address,
                port);
        socket.send(packet);
        System.out.println("SENT: " + req);


        socket.close();
    }

    private static boolean procArgs(String[] args){

        if(args.length == 4){
            hostName = args[0];
            oper = args[1];
            plate = args[2];
            owner = args[3];

            if(!oper.equals("REGISTER")){
                System.out.println("Usage <hostname> <oper> <plate> <owner>");
                return false;
            }

        }

        else if(args.length == 3){
            hostName = args[0];
            oper = args[1];
            plate = args[2];

            if(!oper.equals("LOOKUP")){
                System.out.println("Usage <hostname> <oper> <plate>");
                return false;
            }

        }

        else {
            System.out.println("Usage for REGISTER: <hostname> REGISTER <plate> <owner>\n" +
                    "Usage for LOOKUP: <hostname> LOOKUP <plate>\n");
            return false;
        }

        return true;
    }
}
