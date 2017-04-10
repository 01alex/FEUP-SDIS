package protocols;

import file.Chunk;
import file.FileC;
import peer.Peer;

import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Restore implements Runnable{

    private static String filePath;
    private static FileC fileDBS;
    private static Chunk chunk;
    private static Message msg;

    public Restore(String filePath){

        this.filePath = filePath;

        if((fileDBS = Peer.sharedFiles.get(filePath)) == null){
            System.out.println("Peer didn't back up file " + filePath);
            return;
        }

    }

    public void sendGETCHUNK() {

        msg = new Message("GETCHUNK", chunk);

        Peer.sendToMC(msg.getHeader().getBytes());
    }

    public void run() {

        int numChunks = fileDBS.getChunks().size();

        for(int i=0; i<numChunks; i++){

            chunk = fileDBS.getChunks().get(i);

            sendGETCHUNK();
        }

    }
}