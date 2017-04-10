public class Chunk{

    private String fileID;
    private int chunkNo;
    private String chunkID;
    private int repDegree;
    private byte[] data;

    public Chunk(String fileID, int chunkNo, int repDegree, byte[] data){
        this.fileID = fileID;
        this.chunkNo = chunkNo;
        this.repDegree = repDegree;
        this.data = data;

        this.chunkID = fileID + chunkNo;
    }

    public String getFileID() {
        return fileID;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    public String getChunkID() { return chunkID; }

    public int getRepDegree() {
        return repDegree;
    }

    public byte[] getData() {
        return data;
    }

    public int getLength() {
        return data.length;
    }

}