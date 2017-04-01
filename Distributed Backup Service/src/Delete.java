import java.io.File;

public class Delete implements Runnable{

    private static File file;

    public Delete(String filePath){
        file = new File(filePath);

        if(!file.exists()) {
            System.out.println("File doesn't exist\n");
            return;
        }
    }

    public void sendDELETE() {
        String header = "DELETE";
        header += " " + Peer.protocol_v;        //Version
        header += " " + Peer.serverID;          //Sender ID
        header += " " + "FileID";               //File ID
        header += " " + Utils.CRLF + Utils.CRLF;

        Peer.sendToMC(header.getBytes());
    }

    public void run(){
        sendDELETE();
    }
}