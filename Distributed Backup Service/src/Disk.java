public class Disk{

    private int capacityKB;
    private int usedKB;

    public Disk(int capacityKB){
        this.capacityKB = capacityKB;
        usedKB = 0;
    }

    public int getCapacityKB() {
        return capacityKB;
    }

    public int getUsedSpaceKB() {
        return usedKB;
    }

    public int getFreeSpace() {
        return capacityKB-usedKB;
    }

}