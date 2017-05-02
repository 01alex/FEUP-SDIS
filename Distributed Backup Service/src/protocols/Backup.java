package protocols;

import file.Chunk;
import file.FileC;
import peer.Peer;
import utils.Utils;

import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Backup implements Runnable{

    private static File file;
    private static FileC fileDBS;
    private static Chunk chunk;
    private static Message msg;
    private static int replicationDegree;
    private String filePath;
    private  TreeSet<Integer> peerIDs;

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

        fileDBS = new FileC(fileID, Peer.serverID, replicationDegree);

        fileDBS.setData(fileData);

        System.out.println("File ID: " + fileDBS.getFileID());

        return true;
    }

    public void sendPUTCHUNK() {

        msg = new Message("PUTCHUNK", chunk);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(msg.getHeader().getBytes());
            outputStream.write(msg.getChunk().getData());

            byte message[] = outputStream.toByteArray();
            for(int i=0; i <= 5; i++) {
                Peer.sendToMDB(message);
                replicationCounter(i);

            }
 //for calling replication counter (if repDegPerceived >= RepDegree -> break)
            Peer.sendToMDB(message);
        }
        catch(IOException e){
            e.printStackTrace();
        } //esperar os storedChunks e conta-los e guardar em memória nao volatil
        //se os storedChunks < DesiredReplicationDegree -> esperar x ms e voltar a enviar PUTCHUNK (5 vezes e aumenta numero de ms)
    }

    public void run() {

        if(Peer.sharedFiles.containsKey(filePath)){
            System.out.println("Same version of file already backed up\n");
            return;
        }

        int numChunks = fileDBS.getData().length / Utils.PACKET_MAX_SIZE + 1;

        if(numChunks > Utils.MAX_CHUNKS_PER_FILE){
            System.out.println("File size limit 64GB\n");
            return;
        }

        System.out.println("\nFile " + file.getName() + " splited into " + numChunks + " chunks of " + Utils.PACKET_MAX_SIZE/1000 + "kB each.\n");

        for (int i = 0; i < numChunks; i++) {

            byte[] chunkData = null;
            int chunkLength;
            int index = i * Utils.PACKET_MAX_SIZE;

            if(i == numChunks-1)
                chunkLength = fileDBS.getData().length - index;
            else chunkLength = Utils.PACKET_MAX_SIZE;

            try {

                chunkData = Arrays.copyOfRange(fileDBS.getData(), index,
                        index + chunkLength);

            } catch(IndexOutOfBoundsException e){
                System.out.println("Error creating chunk data\n");
                e.printStackTrace();
            }

            chunk = new Chunk(fileDBS.getFileID(), i, replicationDegree, chunkData);

            fileDBS.addChunk(chunk);

            sendPUTCHUNK();
        }

        Peer.sharedFiles.put(file.getPath(), fileDBS);
    }

    /*private void replicationCounter(int iteration) {
        byte[] storeData = new byte[Constants.ARRAY_SIZE];
        long endTime = (long) (System.currentTimeMillis() + Constants.HALF_A_SECOND*Math.pow(2, iteration));

        try {
            mcSocket.setSoTimeout((int) (Constants.HALF_A_SECOND*Math.pow(2, iteration)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        replicationCounter = 0;

        while(System.currentTimeMillis() < endTime) {
            try {
                DatagramPacket storePacket = new DatagramPacket(storeData, storeData.length);
                mcSocket.receive(storePacket);
                if(System.currentTimeMillis() < endTime && correctChunk(new String(storePacket.getData(), Constants.ENCODING).trim())) {
                    replicationCounter++;
                }
            } catch (Exception e) {
            }
        }
    }*/
}
