public class FileC{

    private String fileID;
    private String home;
    private int repDegree;

    public FileC(String fileID, String home){
        this.fileID = fileID;
        this.home = home;
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
}