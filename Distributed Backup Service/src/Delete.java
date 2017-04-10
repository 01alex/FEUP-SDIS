import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Delete implements Runnable{

    private static String filePath;
    private static String fileID;
    private static Message msg;

    public Delete(String filePath){
        this.filePath = filePath;

        if(!Peer.sharedFiles.containsKey(filePath)){
            System.out.println("Peer didn't back up file " + filePath);
            return;
        }

        fileID = Peer.sharedFiles.get(filePath).getFileID();
    }

    public void sendDELETE() {

        Chunk aux = new Chunk(fileID, 0, 0, null);  //hmm...

        msg = new Message("DELETE", aux);

        Peer.sendToMC(msg.getHeader().getBytes());
    }

    public void run(){

        /*try {
            Files.deleteIfExists(Paths.get(filePath));
        }
        catch(IOException e){
            e.printStackTrace();
        }*/

        Peer.sharedFiles.remove(filePath);
        sendDELETE();
    }
}