package Boruvka.getCC;

import java.util.HashMap;

public class Graph {
    Vertex[] vertices;

    public Graph(int[] input, int n){
        HashMap<Integer,Integer> count = this.parseInput(input);
        this.vertices = new Vertex[n];
        for (int v = 0; v < n; v++) {
            vertices[v] = new Vertex(count.getOrDefault(v,0));
        }
        for (int j = 0; j < input.length; j+=2) {
            vertices[input[j]].addEdge(input[j+1]);
            vertices[input[j+1]].addEdge(input[j]);
        }
    }


    public HashMap<Integer,Integer> parseInput(int[] input){
        HashMap<Integer,Integer> countMap = new HashMap<>();
        for (int i:input) {
            int counter = countMap.getOrDefault(i,0);
            countMap.put(i,counter+1); countMap.put(i,counter+1);
        }
        return countMap;
    }

    public class Vertex {
        private final int[] Neighbors;
        private int nextWrite = 0;
        public Vertex(int Size){
            this.Neighbors = new int[Size];
        }

        public void addEdge(int target){
            this.Neighbors[nextWrite++] = target;
        }

        public int[] getNeighbors() {
            return Neighbors;
        }

        public int getNeighbors(int i) {
            return Neighbors[i];
        }
    }
}
