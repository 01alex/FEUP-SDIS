import java.net.DatagramPacket;

public class Handler implements Runnable{

    private DatagramPacket packet;

    private byte[] header;
    private byte[] body;

    public Handler(DatagramPacket packet){
        this.packet=packet;

        header=null;
        body=null;
    }

    public void run(){

        //parse header

        System.out.printf("Packet handler\n");
        System.out.println("Message: " + new String(packet.getData(), 0, packet.getLength()));

    }
}