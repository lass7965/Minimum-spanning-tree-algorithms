package Boruvka.getCC;

import Boruvka.Boruvka;

import java.util.Stack;

public class DFS {
    static int forest_count = 1;
    // Go through all vertices and figure out which are connected to which. Creates and returns a table which can be used to translate their current ID to the forest they belong to.
    // Optionally the forest_id should start with 0, but as arrays initialize as 0 and not null, forest_id start at 1, and all values should be decremented once when used.
    public static int[] DFS(Graph g){
        forest_count = 1;
        int n = g.vertices.length; // # of vertices
        int[] translateTable = new int[n+1]; // Translate table for all n vertices, many/none of these might refer the same value, last extra space is to mark the largest referred value. Used in sorting
        for (int v = 0; v < g.vertices.length; v++) {
            if(translateTable[v] != 0) continue; // Check if vertex id v was discovered to be in a forest.
            if(g.vertices[v].getNeighbors().length != 0) { // Vertex is not a singleton.
                DFS_visit(g,v,translateTable);
                forest_count += 1;
            } else {
                translateTable[v] = forest_count;
            }
        }
        translateTable[n] = forest_count-1; // Mark the highest ID
        return translateTable;
    }

    private static void DFS_visit(Graph g, int start, int[] translateTable){
        Stack<Integer> next = new Stack<>();
        boolean[] checkSeen = new boolean[g.vertices.length]; // O(1) checking if a vertex has been seen
        next.push(start);
        checkSeen[start] = true;
        while(! next.isEmpty()){
            Integer v = next.pop();
            translateTable[v] = forest_count;
            int[] neighbors = g.vertices[v].getNeighbors();
            for (int neighbor:neighbors) {
                if(! checkSeen[neighbor]){
                    next.push(neighbor);
                    checkSeen[neighbor] = true;
                }
            }
        }
    }
}
