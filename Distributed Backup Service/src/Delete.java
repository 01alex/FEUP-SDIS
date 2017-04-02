import java.io.File;

public class Delete implements Runnable{

    private static String fileID;

    public Delete(String fileID){
        this.fileID = fileID;
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

            sendDELETE();
    }
}