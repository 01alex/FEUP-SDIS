import java.net.DatagramPacket;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Handler implements Runnable{

    private DatagramPacket packet;

    private byte[] header;
    private byte[] body;

    private String header_str;
    private int bodyIDX;

    private String oper;
    private int sourceID;
    private String fileID;

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

        if(Peer.serverID == sourceID){
            System.out.println("Source server doesn't store chunks of its own file\n");
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
            bodyIDX = header_str.length() + (2*Utils.CRLF.length());

            String[] parts = header_str.split(" ");

            oper = parts[0];
            sourceID = Integer.parseInt(parts[2]);
            fileID = parts[3];

        } catch (IOException | NumberFormatException e ) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private String constrChunkName(){

        String[] parts = header_str.split(" ");

        String chunkNo = parts[4];

        String chunkName = fileID + chunkNo;

        return chunkName;
    }

    public void saveChunk(String chunkName) throws IOException{

        try {
            Files.write(Paths.get(chunkName), body);

            if (Peer.FileChunk.get(fileID) == null) {
                List <String> chunkNames = new ArrayList<String>();
                Peer.FileChunk.put(fileID, chunkNames);
            }

            Peer.FileChunk.get(fileID).add(chunkName);

        }catch(IOException e){
            System.out.println("Error saving chunk\n");
            e.printStackTrace();
        }
    }

    private void handlePUTCHUNK(){
        body = Arrays.copyOfRange(packet.getData(), bodyIDX, packet.getLength());

        try{
            saveChunk(constrChunkName());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void deleteChunk(String chunkName) throws IOException{

        try{
            Files.deleteIfExists(Paths.get(chunkName));
        }catch(IOException e){
            System.out.println("Error saving chunk\n");
            e.printStackTrace();
        }
    }

    private void handleDELETE(){
        System.out.println("Handle delete\n");

        List<String> chunksList = Peer.FileChunk.get(fileID);

        for(int i=0; i<chunksList.size(); i++) {
            String aux = chunksList.get(i);

            try{
                deleteChunk(aux);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}