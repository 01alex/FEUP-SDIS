public class Chunk{

    private String fileID;
    private int chunkNo;
    private int repDegree;
    private byte[] data;

    public Chunk(String fileID, int chunkNo, int repDegree, byte[] data){
        this.fileID = fileID;
        this.chunkNo = chunkNo;
        this.repDegree = repDegree;
        this.data = data;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    public void setChunkNo(int chunkNo) {
        this.chunkNo = chunkNo;
    }

    public int getRepDegree() {
        return repDegree;
    }

    public void setRepDegree(int repDegree) {
        this.repDegree = repDegree;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getLength() {
        return data.length;
    }

}