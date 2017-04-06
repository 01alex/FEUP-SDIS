import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Restore implements Runnable{

    private static String filePath;
    private static FileC fileDBS;
    private static Chunk chunk;

    public Restore(String filePath){

        this.filePath = filePath;

        if((fileDBS = Peer.sharedFiles.get(filePath)) == null){
            System.out.println("Peer didn't back up file " + filePath);
            return;
        }

    }

    public void GETCHUNK() {
        String header = "GETCHUNK";
        header += " " + Peer.protocol_v;				//Version
        header += " " + Peer.serverID;          		//Sender ID
        header += " " + chunk.getFileID();              //FileID
        header += " " + chunk.getChunkNo();				//Chunk No
        header += " " + Utils.CRLF + Utils.CRLF;

        Peer.sendToMC(header.getBytes());
    }

    public void run() {

        int numChunks = fileDBS.getChunks().size();

        for(int i=0; i<numChunks; i++){

            chunk = fileDBS.getChunks().get(i);

            GETCHUNK();
        }

    }
}