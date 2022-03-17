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

        ArrayList MST = new ArrayList();
        Graph g = new Graph(edges,n);
        minHeap PQ = new minHeap(n);
        for (int root = 0; root < n; root++) {
            PQ.markTakenOut(root);
            int[] Neighbors = g.vertices[root];
            for (int i = 0; i < Neighbors.length;i+=2) {
                int neighbor = Neighbors[i];
                int weight = Neighbors[i+1];
                if(PQ.contains(neighbor,weight,root)){
                    PQ.decreaseValue(neighbor, weight,root);
                }
            }
            while (PQ.size != 0) {
                int[] v = PQ.popMin();
                int VertexID = v[0];
                int parent = v[2];
                if (parent > VertexID) {
                    MST.add(VertexID); // Add vertex
                    MST.add(parent); // Add parent
                } else {
                    MST.add(parent); // Add parent
                    MST.add(VertexID); // Add vertex
                }
                MST.add(v[1]); // Add weight
                Neighbors = g.vertices[VertexID];
                for (int i = 0; i < Neighbors.length; i += 2) {
                    int neighbor = Neighbors[i];
                    int weight = Neighbors[i + 1];
                    if (PQ.contains(neighbor, weight, VertexID)) {
                        PQ.decreaseValue(neighbor, weight, VertexID);
                    }
                }
            }
        }
        int[] ret = MST.getArray();
        /**                         End timer                            **/
        profilerMain.labels.add("Prims");
        profilerMain.profiling.add(System.nanoTime());
        /**                         End timer                            **/
        return ret;
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(Arrays.toString(MST("./graph.txt")));
    }



}
