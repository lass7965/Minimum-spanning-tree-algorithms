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
        orgEdges = new int[m * 4]; // [Endpoint1, Endpoint2, edgeID, WeightAsInt,...]
        int nextWrite = 0;
        int edgeID = 0;
        while (fileReader.hasNext()) {
            String line = fileReader.nextLine();
            String[] values = line.split(",");
            orgEdges[nextWrite++] = Integer.parseInt(values[0]);
            orgEdges[nextWrite++] = Integer.parseInt(values[1]);
            orgEdges[nextWrite++] = edgeID++;
            orgEdges[nextWrite++] = Float.floatToIntBits(Float.parseFloat(values[2]));
        }
        /**                         File reader                          **/
        /**                         Start timer                          **/
        profilerMain.labels.add("Start time");
        profilerMain.profiling.add(System.nanoTime());
        /**                         Start timer                          **/
        ArrayList MST = new ArrayList();
        int[] edges = Arrays.copyOf(orgEdges,orgEdges.length);
        while(edges.length > 1){
            int[] markedEdges = findCheapest(edges,MST,n);
            int[] translateTable = UnionFind.getCC(markedEdges,n);
            edges = contract(edges,translateTable);
            n = edges[edges.length-1];
        }
        int[] ret = getMST(orgEdges,orgN,MST);
        /**                         End timer                          **/
        profilerMain.labels.add("Boruvka");
        profilerMain.profiling.add(System.nanoTime());
        /**                         End timer                          **/
        return ret;
    }


    public static int[] findCheapest(int[] edges, ArrayList MST, int n){
        int[] candidateList = new int[n*3]; //[edgeID, edgeWeight, endpoint2]
        for (int i = 0; i < n; i+=1) {
            candidateList[i*3+1] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < edges.length-1; i+=4) {
            int endpoint1 = edges[i];
            int endpoint2 = edges[i+1];
            int edgeID = edges[i+2];
            int edgeWeight = edges[i+3];
            checkWeight(candidateList,edgeWeight,edgeID,endpoint1,endpoint2);
            checkWeight(candidateList,edgeWeight,edgeID,endpoint2,endpoint1);
        }
        return getMarked(MST,n,candidateList);
    }

    public static void checkWeight(int[] candidateList, int edgeWeight, int edgeID, int source, int target){
        if(edgeWeight < candidateList[source*3+1]){
            candidateList[source*3] = edgeID;
            candidateList[source*3+1] = edgeWeight;
            candidateList[source*3+2] = target;
        }
    }

    public static int[] getMarked(ArrayList MST, int n, int[] candidateList){
        int[] markedEdges = new int[n*2];
        int nextWrite = 0;
        for (int i = 0; i < candidateList.length; i+=3) {
            if(candidateList[i+1] == Integer.MAX_VALUE) continue;
            MST.add(candidateList[i]);
            markedEdges[nextWrite++] = i/3;
            markedEdges[nextWrite++] = candidateList[i+2];
        }
        return markedEdges;
    }

    public static int[] contract(int[] edges, int[] translateTable){
        int nextWrite = 0;
        int[] sortKeys = {1,0};
        for (int i = 0; i < edges.length-1; i+=4) {
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
            edges[nextWrite++] = edges[i+3];
        }
        if(nextWrite == 0) return new int[1];
        return Sort.radixSort(edges,sortKeys,translateTable[translateTable.length-1],nextWrite);
    }


    public static int[] getMST(int[] edges, int n, ArrayList MST){
        int[] mstEdges = new int[(n-1)*3];
        int nextWrite = 0;
        for (int i = 0; i < MST.size(); i++) {
            int edgeID = MST.get(i);
            int weight = edges[edgeID*4+3];
            if(weight >= 0) {
                mstEdges[nextWrite++] = edges[edgeID * 2];
                mstEdges[nextWrite++] = edges[edgeID * 2 + 1];
                mstEdges[nextWrite++] = weight;
                edges[edgeID*4+3] = -weight;
            }
        }
        if(nextWrite != (n-1)*3) mstEdges = Arrays.copyOf(mstEdges,nextWrite);
        return mstEdges;
    }
}
