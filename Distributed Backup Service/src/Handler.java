import java.net.DatagramPacket;
import java.util.Arrays;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

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
        header = Arrays.copyOfRange(packet.getData(), 0, 32);
        body = Arrays.copyOfRange(packet.getData(), 32, 64000);
        System.out.println("Message: " + new String(header, 0, header.length));

        try{
            saveChunk();
        }catch (IOException e){

        }
    }

    public void saveChunk() throws IOException{
        FileOutputStream out = new FileOutputStream("MC_2.txt", true);
        out.write(body);
        out.close();
    }
}