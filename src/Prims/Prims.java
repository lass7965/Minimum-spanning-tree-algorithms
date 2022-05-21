package Prims;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import Boruvka.ArrayList;
import Profiler.profilerMain;
public class Prims {
    public static int[] MST(String FilePath) throws FileNotFoundException {
        int n;
        int m;
        int[] edges;
        // TODO revert this file back
        /**                         File reader                          **/
        Scanner fileReader = new Scanner(new File(FilePath));
        String[] line1 = fileReader.nextLine().split(",");
        n = Integer.parseInt(line1[0]);
        m = Integer.parseInt(line1[1]);
        edges = new int[m*3]; // [Endpoint1, Endpoint2, Weight,...]
        int nextWrite = 0;
        while(fileReader.hasNext()){
            String line = fileReader.nextLine();
            String[] values = line.split(",");
            edges[nextWrite++] = Integer.parseInt(values[0]);
            edges[nextWrite++] = Integer.parseInt(values[1]);
            edges[nextWrite++] = Float.floatToIntBits(Float.parseFloat(values[2]));
        }
        /**                         File reader                          **/

        /**                         Start timer                          **/
        profilerMain.labels.add("Start time");
        profilerMain.profiling.add(System.nanoTime());
        /**                         Start timer                          **/

        int root = 0;
        Graph g = new Graph(edges,n);
        minHeap PQ = new minHeap();
        int[] weights = new int[n];
        int[] parents = new int[n];
        for (int u = 0; u < n; u++) {
            PQ.insert(u,Integer.MAX_VALUE);
        }
        parents[root] = -1;
        PQ.decreaseValue(root,0);
        while(PQ.size != 0){
            int[] v = PQ.popMin();
            int VertexID =  v[0];
            weights[VertexID] = v[1];
            if(v[1] == Integer.MAX_VALUE){
                parents[VertexID] = -1; //Mark it as root for MSF.
            }
            int[] Neighbors = g.vertices[VertexID];
            for (int i = 0; i < Neighbors.length;i+=2) {
                int neighbor = Neighbors[i];
                int weight = Neighbors[i+1];
                if(PQ.contains(neighbor) && weight < PQ.getKey(neighbor)){
                    parents[neighbor] = VertexID;
                    PQ.decreaseValue(neighbor, weight);
                }
            }
        }
        int[] ret = normalizeOutput(parents,weights);

        /**                         End timer                            **/
        profilerMain.labels.add("Prims");
        profilerMain.profiling.add(System.nanoTime());
        /**                         End timer                            **/

        return ret;
    }


    public static int[] normalizeOutput(int[] parents, int[] weights){
        int[] MST = new int[3*(parents.length-1)];
        int nextWrite = 0;
        for (int i = 0; i < parents.length; i++) {
            if(parents[i] == -1) continue; //Is root for MST
            int weight = weights[i];
            if(weight>=0){
                int target = parents[i];
                if(parents[target] == i) {
                    weights[target] = -weight;
                }
                weights[i] = -weight;
                if( i < target) {
                    MST[nextWrite++] = i;
                    MST[nextWrite++] = target;
                } else {
                    MST[nextWrite++] = target;
                    MST[nextWrite++] = i;
                }
                MST[nextWrite++] = weight;
            }
        }
        if(nextWrite != (parents.length-1)*3) MST = Arrays.copyOf(MST,nextWrite);
        return MST;
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(Arrays.toString(MST("./graph.txt")));
    }



}
