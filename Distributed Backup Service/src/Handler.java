import java.net.DatagramPacket;
import java.util.Arrays;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Handler implements Runnable{

    private DatagramPacket packet;

    private byte[] header;
    private byte[] body;
    private String header_str;
    private String oper;

    public Handler(DatagramPacket packet){
        this.packet=packet;

        header=null;
        body=null;
    }

    public void run(){

        if(!parseHeader()){
            System.out.println("Invalid Header\n");
            return;
        }

        System.out.println("Header: " + header_str);

        switch(oper){
            case "PUTCHUNK": handlePUTCHUNK(); break;

            case "DELETE": handleDELETE(); break;
        }

    }

    private boolean parseHeader(){
        ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        try {
            header_str = reader.readLine();
            String[] parts = header_str.split(" ");

            oper = parts[0];

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private String constrChunkName(){

        String[] parts = header_str.split(" ");

        String fileID = parts[3];
        String chunkNo = parts[4];

        String chunkName = fileID + chunkNo;

        return chunkName;
    }

    public void saveChunk(String chunkName) throws IOException{

        try {
            Files.write(Paths.get(chunkName), body);
        }catch(IOException e){
            System.out.println("Error saving chunk\n");
            e.printStackTrace();
        }
    }

    private void handlePUTCHUNK(){
        body = Arrays.copyOfRange(packet.getData(), header_str.getBytes().length, packet.getData().length);

        try{
            saveChunk(constrChunkName());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void handleDELETE(){
        System.out.println("Handle delete\n");
    }
}