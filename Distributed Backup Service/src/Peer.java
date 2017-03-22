import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Peer{

    private static MulticastSocket socket;

    private static InetAddress mcAddr;
    private static int mcPort;
    private static MC mcChanel;

    private static boolean procArgs(String[] args) throws UnknownHostException {

        if(args.length == 2){
            mcAddr = InetAddress.getByName(args[0]);

            try{
                mcPort = Integer.parseInt(args[1]);
            }catch(NumberFormatException e){
                System.out.println("Port must be an integer.\n");
                return false;
            }

        }
        else {
            System.out.println("Usage: java Peer <mcAddress> <mcPort>\n");
            return false;
        }

        return true;
    }

    public static void main(String[] args) throws IOException {

        if(!procArgs(args))
            return;

        mcChanel = new MC(mcAddr, mcPort);

        new Thread(mcChanel).start();

    }
}