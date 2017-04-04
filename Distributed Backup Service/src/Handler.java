import java.util.*;
import java.net.DatagramPacket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Handler implements Runnable{

    private DatagramPacket packet;

    private String header_str;
    private byte[] body;

    private int bodyIDX;

    private String oper;
    private int senderID;
    private String fileID;

    private int chunkNo;
    private int repDegree;

    public Handler(DatagramPacket packet){
        this.packet=packet;

        body=null;
    }

    public void run(){

        if(!parseHeader()){
            System.out.println("Invalid Header\n");
            return;
        }

        if(Peer.serverID == senderID){
            //System.out.println("Ignore own messages\n");
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
            senderID = Integer.parseInt(parts[2]);
            fileID = parts[3];

            if(oper.equals("PUTCHUNK")) {
                chunkNo = Integer.parseInt(parts[4]);
                repDegree = Integer.parseInt(parts[5]);
            }

        } catch (IOException | NumberFormatException e ) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void storeChunk(String chunkName) throws IOException{

        try {

            Files.write(Paths.get(chunkName), body);

            Chunk chunk = new Chunk(fileID, chunkNo, repDegree, body);

            if(!Peer.storedChunks.containsKey(fileID)){
                List <Chunk> chunks = new ArrayList<Chunk>();
                Peer.storedChunks.put(fileID, chunks);
            }

            Peer.storeChunk(chunk);

        }catch(IOException e){
            System.out.println("Error saving chunk\n");
            e.printStackTrace();
        }
    }

    private void handlePUTCHUNK(){

        body = Arrays.copyOfRange(packet.getData(), bodyIDX, packet.getLength());

        try{
            String chunkName = fileID + chunkNo;
            storeChunk(chunkName);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void deleteChunk(String chunkName) throws IOException{

        try{
            Files.deleteIfExists(Paths.get(chunkName));
        }catch(IOException e){
            System.out.println("Error deleting chunk\n");
            e.printStackTrace();
        }
    }

    private void handleDELETE(){

        List<Chunk> chunksList = Peer.storedChunks.get(fileID);

        if(chunksList == null){
            System.out.println("File doesn't exist\n");
            return;
        }

        for(int i=0; i<chunksList.size(); i++) {

            Chunk chunk = chunksList.get(i);
            String chunkName = chunk.getFileID() + chunk.getChunkNo();

            try{
                System.out.println("Deleting chunk #" + chunk.getChunkNo());
                deleteChunk(chunkName);
                Peer.deleteChunk(chunk);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        Peer.storedChunks.remove(fileID);
    }
}