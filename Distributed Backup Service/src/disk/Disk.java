package disk;

public class Disk{

    /*
        All values in kB
    */

    private int capacity;
    private int used;

    public Disk(int capacity){
        this.capacity = capacity;
        used = 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getUsedSpace() {
        return used;
    }

    public int getFreeSpace() {
        return capacity-used;
    }

    public void storeData(int length) {
        used += length;
    }

    public void deleteData(int length) {
        used -= length;
    }

}