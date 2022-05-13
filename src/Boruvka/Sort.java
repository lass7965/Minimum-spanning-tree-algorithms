package Boruvka;

public class Sort {
    //Preform counting sort per index defined in sortKeys. First index in sortKeys is the "highest priority" of the sorting.
    public static Graph radixSort(int[] pileOfEdges, int[] sortKeys,int maxID, int size){
        for (int key: sortKeys) {
            pileOfEdges = countingSortReversed(pileOfEdges,key,maxID,size);
        }
        return buildGraph(pileOfEdges, maxID);
    }

    // CountingSort but reversed in the last for loop. Normally it's from size to 1. Psuedocode in Cormen Introduction to algorithms page 195
    public static int[] countingSortReversed(int[] pileOfEdges, int sortKey,int maxID,int size){
        int[] count = new int[maxID];
        int[] result = new int[size];
        for (int i = sortKey; i < size; i+=3) {
            int id = pileOfEdges[i];
            count[id]++;
        }
        for (int i = 1; i < maxID; i++) {
            count[i] += count[i-1];
        }
        for (int i = 0; i < size; i+=3) {
            int id = pileOfEdges[i+sortKey];
            int newIndex = --count[id]*3;
            result[newIndex++] = pileOfEdges[i];
            result[newIndex++] = pileOfEdges[i+1];
            result[newIndex] = pileOfEdges[i+2];
        }
        return result;
    }

    // Build a graph from the sortedData set given, here edges should be considered directed, even though they are undirect, but the set contains both {i,j} and {j,i}.
    public static Graph buildGraph(int[] pileOfEdges, int maxID){
        if(pileOfEdges.length == 0){
            return new Graph(0);
        }
        int currentVertexID = pileOfEdges[0];
        int currentTarget = pileOfEdges[1];
        int currentEdgeID = pileOfEdges[2];
        ArrayList edges = new ArrayList(); // ArrayList for the current vertex in form {target, edgeID}
        for (int i = 3; i < pileOfEdges.length; i+=3) {
            if(currentVertexID == pileOfEdges[i]) { // pileOfEdges still points towards same vertex
                if(currentTarget == pileOfEdges[i+1]){
                    if(Graph.edgeCost[currentEdgeID] > Graph.edgeCost[pileOfEdges[i+2]]) currentEdgeID = pileOfEdges[i+2];
                } else {
                    edges.add(currentVertexID);
                    edges.add(currentTarget);
                    edges.add(currentEdgeID);
                    currentTarget = pileOfEdges[i+1];
                    currentEdgeID = pileOfEdges[i+2];
                }
            } else {
                edges.add(currentVertexID);
                edges.add(currentTarget);
                edges.add(currentEdgeID);
                currentVertexID = pileOfEdges[i];
                currentTarget = pileOfEdges[i+1];
                currentEdgeID = pileOfEdges[i+2];
            }
        }
        edges.add(currentVertexID);
        edges.add(currentTarget);
        edges.add(currentEdgeID);

        return new Graph(edges.getArray(),Graph.edgeCost,maxID);// Build new graph from the ArrayList newEdges
    }
}

