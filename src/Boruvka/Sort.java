package Boruvka;

public class Sort {
    //Preform counting sort per index defined in sortKeys. First index in sortKeys is the "highest priority" of the sorting.
    public static Graph radixSort(int[][] data, int[] sortKeys,int maxID, int size){
        for (int key: sortKeys) {
            data = countingSortReversed(data,key,maxID,size);
        }
        return buildGraph(data, maxID);
    }

    // CountingSort but reversed in the last for loop. Normally it's from size to 1. Psuedocode in Cormen Introduction to algorithms page 195
    public static int[][] countingSortReversed(int[][] data, int sortKey,int maxID,int size){
        int[] count = new int[maxID];
        int[][] result = new int[size][3];
        for (int i = 0; i < size; i++) {
            count[data[i][sortKey]]++;
        }
        for (int i = 1; i < maxID; i++) {
            count[i] += count[i-1];
        }
        for (int i = 0; i < size; i++) {
            result[--count[data[i][sortKey]]] = data[i];
        }
        return result;
    }

    // Build a graph from the sortedData set given, here edges should be considered directed, even though they are undirect, but the set contains both {i,j} and {j,i}.
    public static Graph buildGraph(int[][] sortedData, int maxID){
        Graph g = new Graph(maxID); // Create a graph with vertices matching the maxID stored in data. See DFS.
        if(sortedData.length == 0) { // Whole graph has been contracted to a single vertex.
            return g;
        }
        ArrayList edgesForVertex = new ArrayList(); // ArrayList for the current vertex in form {target, edgeID}
        int[] prevRow = sortedData[0]; // Row = {i,j,ID}
        int currentVertexID = prevRow[0];
        for (int i = 1; i < sortedData.length; i++) {
            if(sortedData[i][0] == prevRow[0]){
                if (sortedData[i][1] == prevRow[1]){ // Source and target is the same as previous row, save the cheapest of the current row and the previous.
                    if (Graph.edgeCost[sortedData[i][2]] < Graph.edgeCost[prevRow[2]]) prevRow = sortedData[i];
                } else { // Source is the same but target changed, therefore the prevRow is the cheapest edge from source to target, and should be added to the graph.
                    edgesForVertex.add(prevRow[1]);
                    edgesForVertex.add(prevRow[2]);
                    prevRow = sortedData[i];
                }
            } else { // Source of the edge changed from the previous row, a new vertex has been met, and edgesForVertex contains all edges for the previous vertex.
                Vertex v = new Vertex(edgesForVertex.size()/2+1);
                for (int j = 0;j < edgesForVertex.size; j+=2) {
                    v.addEdge(edgesForVertex.get(j),edgesForVertex.get(j+1));
                    g.edgesCount++;
                }
                v.addEdge(prevRow[1],prevRow[2]); // prevRow never gets added to edgesForVertex
                g.vertices[currentVertexID] = v;
                g.edgesCount++;
                edgesForVertex.clear();
                prevRow = sortedData[i];
                currentVertexID = prevRow[0];
            }
        }
        if(prevRow[0] == prevRow[1]) {
            Vertex v = new Vertex(0);
            g.vertices[currentVertexID] = v;
            return g;
        }
        Vertex v = new Vertex(edgesForVertex.size()/2+1);
        for (int j = 0;j < edgesForVertex.size; j+=2) {
            v.addEdge(edgesForVertex.get(j),edgesForVertex.get(j+1));
        }
        v.addEdge(prevRow[1],prevRow[2]); // prevRow never gets added to edgesForVertex
        g.vertices[currentVertexID] = v;
        g.edgesCount = (g.edgesCount+1)/2; // Last edge added, but all edges are added twice.
        return g;
    }
}

