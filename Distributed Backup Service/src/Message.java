public class Message{

    private static String header;
    private static Chunk chunk;

    public Message(String type, Chunk chunk){

        this.chunk = chunk;

        switch(type){
            case "PUTCHUNK": PUTCHUNK(); break;

            case "STORED": STORED(); break;

            case "DELETE": DELETE(); break;

            case "GETCHUNK": GETCHUNK(); break;

            case "CHUNK": CHUNK(); break;

            case "REMOVED": REMOVED(); break;
        }

    }

    public void PUTCHUNK() {
        header = "PUTCHUNK";
        header += " " + Peer.protocol_v;
        header += " " + Peer.serverID;
        header += " " + chunk.getFileID();
        header += " " + chunk.getChunkNo();
        header += " " + chunk.getRepDegree();
        header += " " + Utils.CRLF + Utils.CRLF;
    }

    public void STORED(){
        header = "STORED";
        header += " " + Peer.protocol_v;
        header += " " + Peer.serverID;
        header += " " + chunk.getFileID();
        header += " " + chunk.getChunkNo();
        header += " " + Utils.CRLF + Utils.CRLF;
    }

    public void DELETE(){
        header = "DELETE";
        header += " " + Peer.protocol_v;
        header += " " + Peer.serverID;
        header += " " + chunk.getFileID();
        header += " " + Utils.CRLF + Utils.CRLF;
    }

    public void GETCHUNK(){
        header = "GETCHUNK";
        header += " " + Peer.protocol_v;
        header += " " + Peer.serverID;
        header += " " + chunk.getFileID();
        header += " " + chunk.getChunkNo();
        header += " " + Utils.CRLF + Utils.CRLF;
    }

    public void CHUNK(){
        header = "CHUNK";
        header += " " + Peer.protocol_v;
        header += " " + Peer.serverID;
        header += " " + chunk.getFileID();
        header += " " + chunk.getChunkNo();
        header += " " + Utils.CRLF + Utils.CRLF;
    }

    public void REMOVED(){
        header = "REMOVED";
        header += " " + Peer.protocol_v;
        header += " " + Peer.serverID;
        header += " " + chunk.getFileID();
        header += " " + chunk.getChunkNo();
        header += " " + Utils.CRLF + Utils.CRLF;
    }

    public String getHeader(){
        return header;
    }

    public Chunk getChunk(){
        return chunk;
    }

}