package Boruvka;

import Profiler.profilerMain;
import Boruvka.getCC.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Boruvka {
    public static int[] MST(String FilePath) throws FileNotFoundException {
        int n;
        int orgN;
        int m;
        int[] orgEdges;
        float[] weights;

        /**                         File reader                          **/
        Scanner fileReader = new Scanner(new File(FilePath));
        String[] line1 = fileReader.nextLine().split(",");
        orgN = n = Integer.parseInt(line1[0]);
        m = Integer.parseInt(line1[1]);
        orgEdges = new int[m * 3]; // [Endpoint1, Endpoint2, Weight,...]
        weights = new float[m];
        int nextWrite = 0;
        int nextWeight = 0;
        while (fileReader.hasNext()) {
            String line = fileReader.nextLine();
            String[] values = line.split(",");
            orgEdges[nextWrite++] = Integer.parseInt(values[0]);
            orgEdges[nextWrite++] = Integer.parseInt(values[1]);
            orgEdges[nextWrite++] = nextWeight;
            weights[nextWeight++] = Float.parseFloat(values[2]);
        }
        /**                         File reader                          **/
        /**                         Start timer                          **/
        profilerMain.labels.add("Start time");
        profilerMain.profiling.add(System.nanoTime());
        /**                         Start timer                          **/
        ArrayList MST = new ArrayList();
        Graph g = new Graph(orgEdges,weights,n);
        while(g.edgesCount > 0){
            int[] MarkedEdges = findCheapest(g,MST);
            int[] translateTable = UnionFind.getCC(MarkedEdges,g.vertices.length);
            g = contract(g,translateTable);
        }
        int[] ret = getMST(orgEdges,weights,orgN,MST);
        /**                         End timer                          **/
        profilerMain.labels.add("Boruvka");
        profilerMain.profiling.add(System.nanoTime());
        /**                         End timer                          **/
        return ret;
    }


    public static int[] findCheapest(Graph g, ArrayList MST) {
        // A list containing two values per vertex which shows the cheapest edge candidates for a vertex.
        // These values are initially the ID of the edge that connects to this vertex along with the other endpoint of this edge.
        int[] candidateList = new int[g.vertices.length * 2];
        for (int vertexID = 0; vertexID < g.vertices.length; vertexID++) {
            Vertex currVertex = g.vertices[vertexID];
            if(currVertex == null){
                g.vertices[vertexID] = new Vertex(0); //Graph created a singleton while contracting
                continue;
            }
            int[] Neighbors = currVertex.getNeighbors();
            for (int i = 0; i < Neighbors.length; i++) {
                int target = Neighbors[i]; //The target of the edge stored at Neighbors[i]
                int edgeID = currVertex.getEdgeID(i); //The ID of the edge stored at Neighbors[i]
                // Check if the edge is cheaper than the currently cheapest edge in the candidate list for the SOURCE of the edge
                if (isCheaper(candidateList, vertexID, edgeID)) {
                    candidateList[vertexID * 2] = edgeID + 1;
                    candidateList[vertexID * 2 + 1] = target;
                }
                // Check if the edge is cheaper than the currently cheapest edge in the candidate list for the TARGET of the edge
                if (isCheaper(candidateList, target, edgeID)) {
                    candidateList[target * 2] = edgeID + 1;
                    candidateList[target * 2 + 1] = vertexID;
                }
            }
        }
        HashMap<Integer, Boolean> marked = new HashMap();
        for (int i = 0; i < candidateList.length; i += 2) {
            int edgeID = candidateList[i] - 1;
            if (edgeID == -1) continue;
            if(! marked.containsKey(edgeID)) {
                MST.add(edgeID);
                marked.put(edgeID,true);
            }
            candidateList[i] = i / 2;//Replace the edgeID for every vertex, with the ID of every vertex. This creates a array of edges for DFS to run on.
        }
        return candidateList;
    }

    public static boolean isCheaper(int[] candidateList, int target, int edgeID){
        if(candidateList[target*2] == 0) return true; //Check if it's initialized.
        float edgeCost = Graph.edgeCost[edgeID];
        return edgeCost < Graph.edgeCost[candidateList[target * 2]-1];
    }



    public static Graph contract(Graph g,int[] translateTable){
        int[] pileOfEdges = new int[g.edgesCount*6]; //{i,j,org.ID}
        int nextWrite = 0;
        int[] sortKeys = {1,0};
        for(int i = 0; i < g.vertices.length; i++){
            Vertex v = g.vertices[i];
            for (int iterator = 0; iterator < v.getNeighbors().length; iterator++) {
                int edgeID = v.getEdgeID(iterator);
                int h_i = -translateTable[i]-1;
                int h_j = -translateTable[v.getNeighbor(iterator)]-1;
                if(h_i == h_j) continue; // Check if they both got translated to the same vertex.
                if(h_i > h_j) { // First value in data must be lowest. To ensure that {1,3} and {3,1} refer to the same endPoints.
                    pileOfEdges[nextWrite++] = h_j;
                    pileOfEdges[nextWrite++] = h_i;
                    pileOfEdges[nextWrite++] = edgeID;
                } else {
                    pileOfEdges[nextWrite++] = h_i;
                    pileOfEdges[nextWrite++] = h_j;
                    pileOfEdges[nextWrite++] = edgeID;
                }
            }
        }
        return Sort.radixSort(pileOfEdges,sortKeys,translateTable[translateTable.length-1],nextWrite);
    }


    public static int[] getMST(int[] edges,float[] weights, int n, ArrayList MST){
        int[] mstEdges = new int[(n-1)*3];
        int nextWrite = 0;
        for (int i = 0; i < MST.size(); i++) {
            int edgeID = MST.get(i);
            float weight = weights[edgeID];
            mstEdges[nextWrite++] = edges[edgeID * 2];
            mstEdges[nextWrite++] = edges[edgeID * 2 + 1];
            mstEdges[nextWrite++] = Float.floatToIntBits(weight);
        }
        if(nextWrite != (n-1)*3) mstEdges = Arrays.copyOf(mstEdges,nextWrite);

        return mstEdges;
    }
}
