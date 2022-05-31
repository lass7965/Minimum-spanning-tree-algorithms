package FT;

public abstract class Heap {
    int size;
    abstract boolean isEmpty();
    abstract void insert(int vertexID, int weight,int parentID, int edgeID);
    abstract int[] popMin();
    abstract void markTakenOut(int id);
    abstract void decreaseIfContains(int id, int weight,int parentID, int edgeID);
    abstract void decreaseKey(int id, int weight,int parentID, int edgeID);
    abstract void reset();
}
