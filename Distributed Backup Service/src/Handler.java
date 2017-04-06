import java.util.*;
import java.net.DatagramPacket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

        System.out.println(header_str);     //print header

        switch(oper){
            case "PUTCHUNK": handlePUTCHUNK(); break;

            case "DELETE": handleDELETE(); break;

            case "GETCHUNK": handleGETCHUNK(); break;

            case "CHUNK": handleCHUNK(); break;
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

            if(!oper.equals("DELETE")) {

                chunkNo = Integer.parseInt(parts[4]);

                if(oper.equals("PUTCHUNK")) {

                    repDegree = Integer.parseInt(parts[5]);

                    if (!Peer.storedChunks.containsKey(fileID)) {
                        List<Chunk> chunks = new ArrayList<Chunk>();
                        Peer.storedChunks.put(fileID, chunks);
                    }
                }
            }

        } catch (IOException | NumberFormatException e ) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    //HANDLE BACKUP

    public void STORED(){
        String header = "STORED";
        header += " " + Peer.protocol_v;        //Version
        header += " " + Peer.serverID;          //Sender ID
        header += " " + fileID;               //File ID
        header += " " + chunkNo;               //Chunk Number
        header += " " + Utils.CRLF + Utils.CRLF;

        Peer.sendToMC(header.getBytes());
    }


    public void storeChunk(String chunkName) throws IOException{

        try {
            Files.write(Paths.get(chunkName), body);

            Chunk chunk = new Chunk(fileID, chunkNo, repDegree, body);

            Peer.storeChunk(chunk);

            STORED();

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


    //HANDLE DELETE

    public void deleteChunk(String chunkName) throws IOException{

        try{
            Files.deleteIfExists(Paths.get(chunkName));
        }catch(IOException e){
            System.out.println("Error deleting chunk\n");
            e.printStackTrace();
        }
    }

    private void handleDELETE(){

        List<Chunk> chunksList = new ArrayList<Chunk>(Peer.storedChunks.get(fileID));

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


    //HANDLE RESTORE

    public void CHUNK(Chunk chunk) {
        String header = "CHUNK";
        header += " " + Peer.protocol_v;				//Version
        header += " " + Peer.serverID;          		//Sender ID
        header += " " + chunk.getFileID();              //FileID
        header += " " + chunk.getChunkNo();				//Chunk No
        header += " " + Utils.CRLF + Utils.CRLF;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(header.getBytes());
            outputStream.write(chunk.getData());

            byte message[] = outputStream.toByteArray();

            Peer.sendToMC(message);

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void handleGETCHUNK(){

        List<Chunk> chunksList = Peer.storedChunks.get(fileID);

        if(chunksList == null){
            System.out.println("File doesn't exist\n");
            return;
        }

        for(int i=0; i<chunksList.size(); i++){
            Chunk c = chunksList.get(i);

            CHUNK(c);
        }

    }

    public void handleCHUNK(){

        body = Arrays.copyOfRange(packet.getData(), bodyIDX, packet.getLength());

        try{
            String chunkName = fileID + chunkNo;

            Files.write(Paths.get(chunkName), body);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}