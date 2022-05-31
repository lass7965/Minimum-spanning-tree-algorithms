package FT;

import Boruvka.ArrayList;
import Profiler.profilerMain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import FT.getCC.*;

public class FT {
    int n;
    int m;
    int k;
    Graph g;
    byte[] marked;
    Heap PQ;
    ArrayList MST = new ArrayList();
    ArrayList markedEdges = new ArrayList();


    public FT(int[] edges, int n, int m) {
        this.n = n;
        this.m = m;
        this.g = new Graph(edges, n);
        this.marked = new byte[n];
        this.PQ = new FibonacciHeap(n);
    }

    public static int[] MST(String FilePath) throws FileNotFoundException {
        int n;
        int m;
        int[] edges;

        /**                         File reader                          **/
        Scanner fileReader = new Scanner(new File(FilePath));
        String[] line1 = fileReader.nextLine().split(",");
        n = Integer.parseInt(line1[0]);
        m = Integer.parseInt(line1[1]);
        edges = new int[m * 4+1]; // [Endpoint1, Endpoint2, Weight,...]
        int nextWrite = 0;
        int j = 0;
        while (fileReader.hasNext()) {
            String line = fileReader.nextLine();
            String[] values = line.split(",");
            edges[nextWrite++] = Integer.parseInt(values[0]);
            edges[nextWrite++] = Integer.parseInt(values[1]);
            edges[nextWrite++] = j++;
            edges[nextWrite++] = Float.floatToIntBits(Float.parseFloat(values[2]));
        }
        edges[m*4] = n;
        /**                         File reader                          **/
        /**                         Start timer                          **/
        profilerMain.labels.add("Start time");
        profilerMain.profiling.add(System.nanoTime());
        /**                         Start timer                          **/
        FT MST = new FT(edges.clone(), n, m);
        while (MST.g.edgesCount > 1) {
            MST.k = (int) Math.pow(2,2*( (double) m/MST.n));
            for (int i = 0; i < MST.n; i++) {
                if(MST.marked[i] == -1) continue;
                MST.marked[i] = -1;
                MST.grow(i);
            }
            MST.condense(MST.markedEdges.getArray());
        }
        int[] ret = getMST(edges,n,MST.MST);
        /**                         End timer                            **/
        profilerMain.labels.add("Fibonacci MST - FibHeap w/ Node[]");
        profilerMain.profiling.add(System.nanoTime());
        /**                         End timer                            **/
        return ret;
    }

    public void grow(int root) {
        int[] Neighbors = g.vertices[root];
        if(Neighbors == null) {
            g.vertices[root] = new int[0];
            return;
        }
        for (int i = 0; i < Neighbors.length; i += 3) {
            int neighbor = Neighbors[i];
            int edgeID = Neighbors[i + 1];
            int weight = Neighbors[i + 2];
            PQ.decreaseIfContains(neighbor, weight, root,edgeID); // Decreases value if needed and inserts value if needed
        }
        PQ.markTakenOut(root);
        while (PQ.size < k && PQ.size > 0) {
            int[] v = PQ.popMin();
            int VertexID = v[0];
            int edgeID = v[1];
            int parent = v[2];
            /**                        MST Maintaining                       **/
            markedEdges.add(VertexID);
            markedEdges.add(parent);
            MST.add(edgeID); // Add weight
            /**                        MST Maintaining                       **/
            if( this.marked[VertexID] == -1) {
                break;
            }
            this.marked[VertexID] = -1;
            Neighbors = g.vertices[VertexID];
            if(PQ.size + Neighbors.length/3 >= k) {
                break;
            }
            for (int i = 0; i < Neighbors.length; i += 3) {
                int neighbor = Neighbors[i];
                int expandedEdgeID = Neighbors[i + 1];
                int weight = Neighbors[i + 2];
                PQ.decreaseIfContains(neighbor, weight, VertexID,expandedEdgeID); // Decreases value if needed and inserts value if needed
            }
        }
        PQ.reset();
    }

    public void condense(int[] markedEdges){
        int[] translateTable = UnionFind.getCC(markedEdges,n);
        g = Contract.contract(g,translateTable);
        this.n = g.vertices.length;
        this.marked = new byte[n];
        this.markedEdges.clear();
    }

    public static int[] getMST(int[] edges, int n, ArrayList MST){
        int[] mstEdges = new int[(n-1)*3];
        int nextWrite = 0;
        for (int i = 0; i < MST.size(); i++) {
            int edgeID = MST.get(i);
            int weight = edges[edgeID*4+3];
            if(weight > 0){
                mstEdges[nextWrite++] = edges[edgeID * 4];
                mstEdges[nextWrite++] = edges[edgeID * 4 + 1];
                mstEdges[nextWrite++] = edges[edgeID * 4 + 3];
                edges[edgeID*4+3] = -weight;
            }
        }
        if(nextWrite != (n-1)*3) mstEdges = Arrays.copyOf(mstEdges,nextWrite);

        return mstEdges;
    }

    public static void main(String[] args) {
        try {
            MST("./Graph1.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
