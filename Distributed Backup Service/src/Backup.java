import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Backup implements Runnable{

    private static File file;
    private static FileC fileDBS;
    private static Chunk chunk;
    private static int replicationDegree;

    public Backup(String filePath, int replicationDegree){
        file = new File(filePath);
        this.replicationDegree = replicationDegree;

        if(!file.exists()) {
            System.out.println("File doesn't exist\n");
            return;
        }

        String mix = file.getName() + file.length() + file.lastModified();
        String fileID = Utils.sha256(mix);

        fileDBS = new FileC(fileID, Peer.serviceAP);

        System.out.println("File ID: " + fileDBS.getFileID());

        Peer.FileFileC.put(filePath, fileDBS);
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

        byte[] fileData = Utils.loadFile(file.getPath());
        System.out.println("\nFile length " + fileData.length);

        int numChunks = fileData.length / Utils.PACKET_MAX_SIZE + 1;

        if(numChunks > Utils.MAX_CHUNKS_PER_FILE){
            System.out.println("File size limit 64GB\n");
            return;
        }

        else {

            List<ChunkID> chunks = new ArrayList<ChunkID>();

            for (int i = 0; i < numChunks; i++) {

                byte[] chunkData = null;
                int chunkLength;
                int index = i * Utils.PACKET_MAX_SIZE;

                if(i == numChunks-1)
                    chunkLength = fileData.length - index;
                else chunkLength = Utils.PACKET_MAX_SIZE;

                System.out.println("\nFile " + file.getName() + " Chunk # " + i);

                try {

                    chunkData = Arrays.copyOfRange(fileData, index,
                            index + chunkLength);

                } catch(IndexOutOfBoundsException e){
                    System.out.println("Error creating chunk\n");
                    e.printStackTrace();
                }

                chunk = new Chunk(fileDBS, replicationDegree, chunkData);
                chunk.setChunkNo(i);

                chunks.add(chunk.getChunkID());

                PUTCHUNK(chunk);
            }

        }
    }
}