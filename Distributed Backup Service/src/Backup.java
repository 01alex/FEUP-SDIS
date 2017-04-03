import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Backup implements Runnable{

    private static File file;
    private static FileC fileDBS;
    private static Chunk chunk;
    private static int replicationDegree;
    private String filePath;

    public Backup(String filePath, int replicationDegree){

        this.filePath = filePath;
        this.file = new File(filePath);
        this.replicationDegree = replicationDegree;

        if(!procFile())
            System.out.println("Error processing file\n");

    }

    public boolean procFile(){

        if(!file.exists()) {
            System.out.println("File doesn't exist\n");
            return false;
        }

        byte[] fileData = Utils.loadFile(filePath);

        if(fileData == null){
            System.out.println("Null File Data\n");
            return false;
        }

        System.out.println("\nFile length " + fileData.length);

        String mix = file.getName() + file.length() + file.lastModified();
        String fileID = Utils.sha256(mix);

        fileDBS = new FileC(fileID, Peer.serviceAP, replicationDegree, fileData);

        System.out.println("File ID: " + fileDBS.getFileID());

        return true;

    }

    public void PUTCHUNK(Chunk chunk) {
        String header = "PUTCHUNK";
        header += " " + Peer.protocol_v;				//Version
        header += " " + Peer.serverID;          		//Sender ID
        header += " " + chunk.getChunkID().getFileID(); //FileID
        header += " " + chunk.getChunkNo();				//Chunk No
        header += " " + chunk.getRepDegree();			//Rep Degree
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

    public void run(){

        int numChunks = fileDBS.getData().length / Utils.PACKET_MAX_SIZE + 1;

        if(numChunks > Utils.MAX_CHUNKS_PER_FILE){
            System.out.println("File size limit 64GB\n");
            return;
        }

        for (int i = 0; i < numChunks; i++) {

            byte[] chunkData = null;
            int chunkLength;
            int index = i * Utils.PACKET_MAX_SIZE;

            if(i == numChunks-1)
                chunkLength = fileDBS.getData().length - index;
            else chunkLength = Utils.PACKET_MAX_SIZE;

            System.out.println("\nFile " + file.getName() + " Chunk # " + i);

            try {

                chunkData = Arrays.copyOfRange(fileDBS.getData(), index,
                        index + chunkLength);

            } catch(IndexOutOfBoundsException e){
                System.out.println("Error creating chunk data\n");
                e.printStackTrace();
            }

            chunk = new Chunk(fileDBS, replicationDegree, chunkData);
            chunk.setChunkNo(i);

            fileDBS.addChunk(chunk);

            PUTCHUNK(chunk);
        }

        Peer.FileFileC.put(file.getPath(), fileDBS);

    }
}