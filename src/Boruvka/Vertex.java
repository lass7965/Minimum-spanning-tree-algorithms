package Boruvka;

public class Vertex {
    private final int[] Neighbors;
    private final int[] EdgeID;
    private int nextWrite = 0;
    public Vertex(int Size){
        this.Neighbors = new int[Size];
        this.EdgeID = new int[Size];
    }

    public void addEdge(int target, int edgeID){
        this.Neighbors[nextWrite] = target;
        this.EdgeID[nextWrite++] = edgeID;
    }

    public int getEdgeID(int i) {
        return EdgeID[i];
    }

    public int[] getNeighbors() {
        return Neighbors;
    }

    public int getNeighbor(int i) {
        return Neighbors[i];
    }
}
