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
        Digraph g = new Digraph(orgEdges,weights,n);
        while(g.edgesCount > 0){
            int[] MarkedEdges = findCheapest(g,MST);
            int[] translateTable = UnionFind.getCC(MarkedEdges,g.vertices.length);
            g = contract(g,translateTable);
        }
        /**                         End timer                          **/
        profilerMain.labels.add("Boruvka");
        profilerMain.profiling.add(System.nanoTime());
        /**                         End timer                          **/
        return getMST(orgEdges,weights,orgN,MST);
    }


    public static int[] findCheapest(Digraph g, ArrayList MST){
        HashMap<Integer, Boolean> marked = new HashMap();
        ArrayList MarkedEdges = new ArrayList();
        for (int i = 0; i < g.vertices.length; i++) {
            Vertex currentVertex = g.vertices[i];
            if(currentVertex == null){
                g.vertices[i] = new Vertex(0); //Graph created a singleton while contracting
                continue;
            }
            if(currentVertex.getNeighbors().length == 0) continue; //Graph contained a singleton when given
            int cheapestNeighbor = currentVertex.getNeighbor(0);
            int cheapestEdgeID = currentVertex.getEdgeID(0);
            float cheapestEdgeCost = Digraph.edgeCost[currentVertex.getEdgeID(0)];
            for (int j = 1; j < currentVertex.getNeighbors().length ; j++) {
                int currEdgeID = currentVertex.getEdgeID(j);
                float currEdgeCost = Digraph.edgeCost[currEdgeID];
                if(cheapestEdgeCost > currEdgeCost){
                    cheapestNeighbor = currentVertex.getNeighbor(j);
                    cheapestEdgeID = currEdgeID;
                    cheapestEdgeCost = currEdgeCost;
                }
            }
            if(! marked.containsKey(cheapestEdgeID)){
                marked.put(cheapestEdgeID,true);
                MST.add(cheapestEdgeID);
                MarkedEdges.add(i);
                MarkedEdges.add(cheapestNeighbor);
            }
        }
        return MarkedEdges.getArray();
    }



    public static Digraph contract(Digraph g,int[] translateTable){
        int[][] edges = new int[g.edgesCount*2][3]; //{i,j,org.ID}
        int nextWrite = 0;
        int[] sortKeys = {1,0};
        for(int i = 0; i < g.vertices.length; i++){
            Vertex v = g.vertices[i];
            for (int iterator = 0; iterator < v.getNeighbors().length; iterator++) {
                int neighbor = v.getNeighbor(iterator);
                int[] data = {-translateTable[i]-1, -translateTable[neighbor]-1,v.getEdgeID(iterator)};
                if(data[0] != data[1]) {
                    edges[nextWrite++] = data;
                }
            }
        }
        return Sort.radixSort(edges,sortKeys,translateTable[translateTable.length-1],nextWrite);
    }


    public static int[] getMST(int[] edges,float[] weights, int n, ArrayList MST){
        int[] mstEdges = new int[(n-1)*3];
        int nextWrite = 0;
        for (int i = 0; i < MST.size(); i++) {
            int edgeID = MST.get(i);
            float weight = weights[edgeID];
            if(weight > 0){
                mstEdges[nextWrite++] = edges[edgeID * 2];
                mstEdges[nextWrite++] = edges[edgeID * 2 + 1];
                mstEdges[nextWrite++] = Float.floatToIntBits(weight);
                weights[edgeID] = -weight;
            }
        }
        if(nextWrite != (n-1)*3) mstEdges = Arrays.copyOf(mstEdges,nextWrite);

        return mstEdges;
    }
}
