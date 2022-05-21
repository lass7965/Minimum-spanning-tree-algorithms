package Kruskal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import Boruvka.ArrayList;
import Boruvka.getCC.UnionFind;
import Profiler.profilerMain;

public class Kruskal {
    public static int[] NewMST(String FilePath) throws FileNotFoundException {
        int n;
        int m;
        int[] edges;

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
        UnionFind set = new UnionFind(n);
        Sorting.sort(edges);
        for (int i = 0; i < edges.length; i+=3) {
            int u = edges[i];
            int v = edges[i+1];
            int uArray = set.findSet(u);
            int vArray = set.findSet(v);
            if(uArray != vArray) {
                set.union(uArray,vArray);
                MST.add(u);
                MST.add(v);
                MST.add(edges[i+2]);
            }
        }

        int[] ret = MST.getArray();

        /**                         End timer                            **/
        profilerMain.labels.add("Kruskal");
        profilerMain.profiling.add(System.nanoTime());
        /**                         End timer                            **/
        return ret;
    }

    /*OLD CODE*/
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

        ArrayList MST = new ArrayList();
        Edge[] Ed = new Edge[m];
        UnionFind set = new UnionFind(n);
        for (int i = 0; i < m; i++)
            Ed[i] = new Edge(edges[i*3],edges[i*3+1],edges[i*3+2]);
        Arrays.sort(Ed, new comparator());
        for (int i = 0; i < m; i++) {
            int u = Ed[i].data[0];
            int v = Ed[i].data[1];
            int uArray = set.findSet(u);
            int vArray = set.findSet(v);
            if (uArray != vArray) {
                set.union(uArray, vArray);
                MST.add(u);
                MST.add(v);
                MST.add(Ed[i].data[2]);
            }
        }

        int[] ret = MST.getArray();

        /**                         End timer                            **/
        profilerMain.labels.add("Kruskal");
        profilerMain.profiling.add(System.nanoTime());
        /**                         End timer                            **/
        return ret;
    }

    static class comparator implements Comparator<Edge> {
        @Override
        public int compare(Edge Edge1,Edge Edge2) {
            return Float.compare(Edge1.getCost(), Edge2.getCost());
        }
    }

    public static class Edge {
        public int[] data;
        public Edge(int endPoint1,int endPoint2, int cost){
            this.data = new int[3];
            this.data[0] = endPoint1;
            this.data[1] = endPoint2;
            this.data[2] = cost;
        }

        public int getCost(){
            return data[2];
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String FilePath = "./graph.txt";
        System.out.println(Arrays.toString(MST(FilePath)));
    }
}
