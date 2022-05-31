package FT;

import Boruvka.ArrayList;

public class Contract {
    public static Graph contract(Graph g, int[] translateTable){
        int[] pileOfEdges = new int[g.edgesCount*8]; //{i,j,org.ID}
        int nextWrite = 0;
        int[] sortKeys = {1,0};
        for(int i = 0; i < g.vertices.length; i++){
            int[] v = g.vertices[i];
            for (int j = 0; j < v.length; j+=3) {
                int edgeID = v[j+1];
                int edgeWeight = v[j+2];
                int h_i = -translateTable[i]-1;
                int h_j = -translateTable[v[j]]-1;
                if(h_i == h_j) continue; // Check if they both got translated to the same vertex.
                pileOfEdges[nextWrite++] = h_j;
                pileOfEdges[nextWrite++] = h_i;
                pileOfEdges[nextWrite++] = edgeID;
                pileOfEdges[nextWrite++] = edgeWeight;
            }
        }
        return radixSort(pileOfEdges,sortKeys,translateTable[translateTable.length-1],nextWrite);
    }

    //Preform counting sort per index defined in sortKeys. First index in sortKeys is the "highest priority" of the sorting.
    public static Graph radixSort(int[] pileOfEdges, int[] sortKeys,int maxID, int size){
        for (int key: sortKeys) {
            pileOfEdges = countingSortReversed(pileOfEdges,key,maxID,size);
        }
        return buildGraph(pileOfEdges,maxID);
    }

    // CountingSort but reversed in the last for loop. Normally it's from size to 1. Psuedocode in Cormen Introduction to algorithms page 195
    public static int[] countingSortReversed(int[] pileOfEdges, int sortKey,int maxID,int size){
        int[] count = new int[maxID];
        int[] result = new int[size];
        for (int i = sortKey; i < size; i+=4) {
            int id = pileOfEdges[i];
            count[id]++;
        }
        for (int i = 1; i < maxID; i++) {
            count[i] += count[i-1];
        }
        for (int i = 0; i < size; i+=4) {
            int id = pileOfEdges[i+sortKey];
            int newIndex = --count[id]*4;
            result[newIndex++] = pileOfEdges[i];
            result[newIndex++] = pileOfEdges[i+1];
            result[newIndex++] = pileOfEdges[i+2];
            result[newIndex] = pileOfEdges[i+3];
        }
        return result;
    }


    // Build a graph from the sortedData set given, here edges should be considered directed, even though they are undirect, but the set contains both {i,j} and {j,i}.
    public static Graph buildGraph(int[] pileOfEdges, int maxID){
        if(pileOfEdges.length == 0){
            return new Graph(0);
        }
        Graph g = new Graph(maxID);
        int currentVertexID = pileOfEdges[0];
        int currentTarget = pileOfEdges[1];
        int currentEdgeID = pileOfEdges[2];
        int currentWeight = pileOfEdges[3];
        ArrayList edgesForVertex = new ArrayList(); // ArrayList for the current vertex in form {target, edgeID}
        for (int i = 4; i < pileOfEdges.length; i+=4) {
            if(currentVertexID == pileOfEdges[i]) { // pileOfEdges still points towards same vertex
                if(currentTarget == pileOfEdges[i+1]){
                    if(currentWeight > pileOfEdges[i+3]) {
                        currentEdgeID = pileOfEdges[i+2];
                        currentWeight = pileOfEdges[i+3];
                    }
                } else {
                    edgesForVertex.add(currentTarget);
                    edgesForVertex.add(currentEdgeID);
                    edgesForVertex.add(currentWeight);
                    currentTarget = pileOfEdges[i+1];
                    currentEdgeID = pileOfEdges[i+2];
                    currentWeight = pileOfEdges[i+3];
                }
            } else {
                edgesForVertex.add(currentTarget);
                edgesForVertex.add(currentEdgeID);
                edgesForVertex.add(currentWeight);
                int[] v = new int[edgesForVertex.size()];
                for (int j = 0;j < edgesForVertex.size(); j+=3) {
                    v[j] = edgesForVertex.get(j);
                    v[j+1] = edgesForVertex.get(j+1);
                    v[j+2] = edgesForVertex.get(j+2);
                    g.edgesCount++;
                }
                g.vertices[currentVertexID] = v;
                edgesForVertex.clear();
                currentVertexID = pileOfEdges[i];
                currentTarget = pileOfEdges[i+1];
                currentEdgeID = pileOfEdges[i+2];
                currentWeight = pileOfEdges[i+3];
            }
        }
        edgesForVertex.add(currentTarget);
        edgesForVertex.add(currentEdgeID);
        edgesForVertex.add(currentWeight);
        int[] v = new int[edgesForVertex.size()];
        for (int j = 0;j < edgesForVertex.size(); j+=3) {
            v[j] = edgesForVertex.get(j);
            v[j+1] = edgesForVertex.get(j+1);
            v[j+2] = edgesForVertex.get(j+2);
            g.edgesCount++;
        }
        g.vertices[currentVertexID] = v;
        return g;
    }
}
