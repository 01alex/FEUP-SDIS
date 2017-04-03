import java.util.*;

public class FileC{

    private String fileID;
    private String home;
    private int repDegree;
    private ArrayList <Chunk> chunks;
    private byte[] data;

    public FileC(String fileID, String home, int repDegree, byte[] data){
        this.fileID = fileID;
        this.home = home;
        this.repDegree = repDegree;
        this.data = data;
        this.chunks = new ArrayList<Chunk>();
    }

    public String getFileID(){
        return fileID;
    }

    public void setFileID(String fileID){
        this.fileID = fileID;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public int getRepDegree() {
        return repDegree;
    }

    public void setRepDegree(int repDegree) {
        this.repDegree = repDegree;
    }

    public void addChunk(Chunk chunk){ chunks.add(chunk); return;}

    public ArrayList<Chunk> getChunks(){return chunks;}

    public byte[] getData(){return data;}
}