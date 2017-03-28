public class Chunk{

    private FileC file;
    private int chunkNo;
    private ChunkID chunkID;
    private int repDegree;
    private int sizeKB;
    private byte[] data;

    public Chunk(FileC file, int repDegree, byte[] data){
        this.file = file;
        this.repDegree = repDegree;

        //generate chunk no
        chunkNo = 99;

        chunkID = new ChunkID(this.file.getFileID(), chunkNo);

        this.data = data;
    }

    public FileC getFile() {
        return file;
    }

    public void setFile(FileC file) {
        this.file = file;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    public void setChunkNo(int chunkNo) {
        this.chunkNo = chunkNo;
    }

    public ChunkID getChunkID() {
        return chunkID;
    }

    public void setChunkID(ChunkID chunkID) {
        this.chunkID = chunkID;
    }

    public int getRepDegree() {
        return repDegree;
    }

    public void setRepDegree(int repDegree) {
        this.repDegree = repDegree;
    }

    public int getSizeKB() {
        return sizeKB;
    }

    public void setSizeKB(int sizeKB) {
        this.sizeKB = sizeKB;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}