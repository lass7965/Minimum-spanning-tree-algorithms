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
        int[] edges = Arrays.copyOf(orgEdges,orgEdges.length);
        while(edges.length > 1){
            int[] markedEdges = findCheapest(edges,weights,MST,n);
            int[] translateTable = UnionFind.getCC(markedEdges,n);
            edges = contract(edges,weights,translateTable);
            n = edges[edges.length-1];
        }
        int[] ret = getMST(orgEdges,weights,orgN,MST);
        /**                         End timer                          **/
        profilerMain.labels.add("Boruvka");
        profilerMain.profiling.add(System.nanoTime());
        /**                         End timer                          **/
        return ret;
    }


    public static int[] findCheapest(int[] edges, float[] weights, ArrayList MST, int n){
        int[] candidateList = new int[n*2]; //[edgeID, endpoint2]
        for (int i = 0; i < edges.length-1; i+=3) {
            int edgeID = edges[i+2];
            int endpoint1 = edges[i];
            int endpoint2 = edges[i+1];
            if (isCheaper(candidateList,weights,endpoint1,edgeID)){
                candidateList[endpoint1*2] = edgeID+1;
                candidateList[endpoint1*2+1] = endpoint2;
            }
            if (isCheaper(candidateList,weights,endpoint2,edgeID)){
                candidateList[endpoint2*2] = edgeID+1;
                candidateList[endpoint2*2+1] = endpoint1;
            }
        }
        for (int i = 0; i < candidateList.length; i+=2) {
            int edgeID = candidateList[i] - 1;
            if (edgeID == -1) continue;
            MST.add(edgeID);
            candidateList[i] = i / 2;
        }
        return candidateList;
    }

    public static boolean isCheaper(int[] candidateList, float[] weights, int target, int edgeID){
        int candidateEdgeID = candidateList[target*2];
        if (candidateEdgeID == 0) return true; //Check if it's the first edge candidate.
        return weights[candidateEdgeID-1] > weights[edgeID];
    }



    public static int[] contract(int[] edges, float[] weights, int[] translateTable){
        int nextWrite = 0;
        int[] sortKeys = {1,0};
        for (int i = 0; i < edges.length-1; i+=3) {
            int h_i = -translateTable[edges[i]]-1;
            int h_j = -translateTable[edges[i+1]]-1;
            if (h_i == h_j) continue;
            if( h_i < h_j) {
                edges[nextWrite++] = h_i;
                edges[nextWrite++] = h_j;
            } else {
                edges[nextWrite++] = h_j;
                edges[nextWrite++] = h_i;
            }
            edges[nextWrite++] = edges[i+2];
        }
        if(nextWrite == 0) return new int[1];
        return Sort.radixSort(edges, weights,sortKeys,translateTable[translateTable.length-1],nextWrite);
    }


    public static int[] getMST(int[] edges,float[] weights, int n, ArrayList MST){
        int[] mstEdges = new int[(n-1)*3];
        int nextWrite = 0;
        for (int i = 0; i < MST.size(); i++) {
            int edgeID = MST.get(i);
            float weight = weights[edgeID];
            if(weight >= 0) {
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
