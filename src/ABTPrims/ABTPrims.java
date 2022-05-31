package ABTPrims;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Boruvka.ArrayList;
import Profiler.profilerMain;

public class ABTPrims {
    public static int[] MST(String FilePath) throws FileNotFoundException {
        int n;
        int m;
        int[] edges;

        /**                         File reader                          **/
        Scanner fileReader = new Scanner(new File(FilePath));
        String[] line1 = fileReader.nextLine().split(",");
        n = Integer.parseInt(line1[0]);
        m = Integer.parseInt(line1[1]);
        edges = new int[m * 3]; // [Endpoint1, Endpoint2, Weight,...]
        int nextWrite = 0;
        while (fileReader.hasNext()) {
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

        Graph graph = new Graph(edges,n);
        ArrayList MST = new ArrayList();
        minHeap PQ = new minHeap();
        for (int root = 0; root < n; root++) {
            if (graph.vertices[root] == null) continue;
            for (int i = 0; i < graph.vertices[root].length; i += 2) {
                int neighbor = graph.vertices[root][i];
                int weight = graph.vertices[root][i + 1];
                PQ.insert(root, neighbor, weight);
            }
            graph.vertices[root] = null;
            int[] elem1;
            int[] elem2;
            while (PQ.size > 0) {
                elem1 = PQ.popMin();
                elem2 = PQ.getMin();
                if (elem2 != null && elem1[2] == elem2[2] && elem1[0] == elem2[1] && elem1[1] == elem2[0]) PQ.popMin();
                else {
                    MST.add(elem1);
                    int u = elem1[0];
                    int v = elem1[1];
                    for (int i = 0; i < graph.vertices[v].length; i += 2) {
                        int neighbor = graph.vertices[v][i];
                        if (neighbor == u) continue;
                        int weight = graph.vertices[v][i + 1];
                        PQ.insert(v, neighbor, weight);
                    }
                    graph.vertices[v] = null;
                }
            }
        }

        int[] ret = MST.getArray();
        /**                         End timer                            **/
        profilerMain.labels.add("Brodahl-Prim");
        profilerMain.profiling.add(System.nanoTime());
        /**                         End timer                            **/

        return ret;
    }
}
