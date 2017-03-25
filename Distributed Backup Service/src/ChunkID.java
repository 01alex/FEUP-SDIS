public class ChunkID{

    private String fileID;
    private int chunkNo;

    public ChunkID(String fileID, int chunkNo){
        this.fileID = fileID;
        this.chunkNo = chunkNo;
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

}