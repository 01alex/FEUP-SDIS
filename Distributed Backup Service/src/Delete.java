import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Delete implements Runnable{

    private static String filePath;
    private static String fileID;

    public Delete(String filePath){
        this.filePath = filePath;

        if(!Peer.sharedFiles.containsKey(filePath)){
            System.out.println("Peer didn't back up file " + filePath);
            return;
        }

        fileID = Peer.sharedFiles.get(filePath).getFileID();
    }

    public void sendDELETE() {
        String header = "DELETE";
        header += " " + Peer.protocol_v;        //Version
        header += " " + Peer.serverID;          //Sender ID
        header += " " + fileID;               //File ID
        header += " " + Utils.CRLF + Utils.CRLF;

        Peer.sendToMC(header.getBytes());
    }

    public void run(){

        /*try {
            Files.deleteIfExists(Paths.get(filePath));
        }
        catch(IOException e){
            e.printStackTrace();
        }*/

        sendDELETE();

        Peer.sharedFiles.remove(filePath);
    }
}