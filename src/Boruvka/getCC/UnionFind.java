package Boruvka.getCC;

import java.util.HashMap;

public class UnionFind {
    int counter = -1;
    int[] parent;
    int[] rank;
    public UnionFind(int n){
        parent = new int[n+1];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int findSet(int x){
        int xp = parent[x];
        if(x != xp) {
            xp = findSet(xp);
            parent[x] = xp; //Path compression
        }
        return xp;
    }

    //Assumes x and y are roots
    public void union(int x, int y){
        if (rank[x] > rank[y]) parent[y] = x;
        else {
            parent[x] = y;
            if(rank[x] == rank[y]) rank[y]++;
        }
    }

    public void name(int vertex){
        int parentVertex = parent[vertex];
        if(parentVertex >= 0){ // If not named
            if(parentVertex == vertex) { //Is root
                parent[vertex] = counter--;
            } else {
                name(parentVertex);
                parent[vertex] = parent[parentVertex];
            }
        }
    }

    //v2
    /*public static int[] getCC(int[] edges, int n){
        UnionFind UF = new UnionFind(n);
        for (int i = 0; i < edges.length; i+=2) {
            int rootX = UF.findSet(edges[i]);
            int rootY = UF.findSet(edges[i+1]);
            if(rootX != rootY)
                UF.union(rootX,rootY);
        }
        for (int i = 0; i < n; i++) {
            if(UF.parent[i] == i) continue; //Avoid naming a singleton
            UF.name(i);
        }
        UF.parent[n] = -UF.counter-1;
        return UF.parent;
    }*/

    //v1
   public static int[] getCC(int[] edges, int n){
        UnionFind UF = new UnionFind(n);
        for (int i = 0; i < edges.length; i+=2) {
            int rootX = UF.findSet(edges[i]);
            int rootY = UF.findSet(edges[i+1]);
            if(rootX != rootY)
                UF.union(rootX,rootY);
        }
        int forestCount = -1;
        HashMap<Integer,Integer> map = new HashMap<>();
        int[] translateTable = new int[n+1];
        for (int i = 0; i < n; i++) {
            int parent = UF.findSet(i); //Make sure that all have path compression.
            if(! map.containsKey(parent)){
                map.put(parent,forestCount);
                translateTable[parent] = forestCount;
                translateTable[i] = forestCount--;
            } else {
                int forestId = map.get(parent);
                translateTable[i] = forestId;
            }
        }
        translateTable[n] = -forestCount-1;
        return translateTable;
    }
}
