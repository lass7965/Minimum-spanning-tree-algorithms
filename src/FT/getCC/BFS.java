package FT.getCC;

import java.util.LinkedList;

public class BFS {
    // Go through all vertices and figure out which are connected to which. Creates and returns a table which can be used to translate their current ID to the forest they belong to.
    // Optionally the forest_id should start with 0, but as arrays initialize as 0 and not null, forest_id start at 1, and all values should be decremented once when used.
    public static int[] BFS(int[] markedEdges, int n){
        Graph g = new Graph(markedEdges,n);
        int forest_count = -1;
        int[] translateTable = new int[n+1]; // Translate table for all n vertices, many/none of these might refer the same value, last extra space is to mark the largest referred value. Used in sorting
        for (int v = 0; v < n; v++) {
            if(translateTable[v] != 0) continue; // Check if vertex id v was discovered to be in a forest.
            if(g.getNeighbors(v).length != 0) { // Vertex is not a singleton.
                BFS_visit(g,v,forest_count,translateTable);
                forest_count -= 1;
            } else {
                translateTable[v] = forest_count--; // Singleton
            }
        }
        translateTable[n] = -forest_count-1;; // Mark the highest ID
        return translateTable;
    }

    private static void BFS_visit(Graph g, int start, int forest_count, int[] translateTable){
        LinkedList<Integer> next = new LinkedList<>();
        boolean[] checkSeen = new boolean[g.vertices.length]; // O(1) checking if a vertex has been seen
        next.add(start);
        checkSeen[start] = true; // color it gray/black
        while(! next.isEmpty()){
            Integer v = next.poll(); // Take the element in the front of the queue.
            translateTable[v] = forest_count;
            int[] neighbors = g.getNeighbors(v);
            for (int neighbor:neighbors) {
                if(! checkSeen[neighbor]){
                    next.push(neighbor);
                    checkSeen[neighbor] = true;
                }
            }
        }
    }
}
