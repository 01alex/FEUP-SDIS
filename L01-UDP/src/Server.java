import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

public class Server {

    private static int port = 4445;

    public static void main(String[] args) throws IOException{

        DatagramSocket socket = new DatagramSocket(port);

        HashMap <String, String> po = new HashMap<String, String>();

        boolean loopH = true;

        while(loopH){
            System.out.println("Waiting");

            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            String request = new String(packet.getData(), 0, packet.getLength());

            System.out.println("Request: " + request);

            String[] plateOwner = request.split("-");

            String oper = plateOwner[0];

            if(oper.equals("REGISTER")){
                String plate = plateOwner[1];
                String owner = plateOwner[2];

                if(po.get(plate) == null)
                    po.put(plate, owner);
                else System.out.println("Plate " + plate + " already registered with owner " + po.get(plate));

                System.out.println("Plate-Owner " + plate + "-" + po.get(plate));
            }
            else if(oper.equals("LOOKUP")) {
                String plate = plateOwner[1];
                String owner = po.get(plate);

                System.out.println("Lookup for " + plate + " is " + owner);

            }

        }

        socket.close();
    }

}
