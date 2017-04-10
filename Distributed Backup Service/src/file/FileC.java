package file;

import java.util.*;

public class FileC{

    private String fileID;
    private int homeID;
    private int repDegree;
    private ArrayList <Chunk> chunks;
    private byte[] data;

    public FileC(String fileID, int homeID, int repDegree){
        this.fileID = fileID;
        this.homeID = homeID;
        this.repDegree = repDegree;
        this.data = null;
        this.chunks = new ArrayList<Chunk>();
    }

    public String getFileID(){
        return fileID;
    }

    public void setFileID(String fileID){
        this.fileID = fileID;
    }

    public int getHomeID() {
        return homeID;
    }

    public void setHomeID(int homeID) {
        this.homeID = homeID;
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

    public void setData(byte[] data){this.data = data;}
}