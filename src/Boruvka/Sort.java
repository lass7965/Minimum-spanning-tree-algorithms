package Boruvka;

public class Sort {
    //Preform counting sort per index defined in sortKeys. First index in sortKeys is the "highest priority" of the sorting.
    public static int[] radixSort(int[] pileOfEdges, float[] weights, int[] sortKeys,int maxID, int size){
        for (int key: sortKeys) {
            pileOfEdges = countingSortReversed(pileOfEdges,key,maxID,size);
        }
        return removeDuplicates(pileOfEdges,weights, maxID);
    }

    // CountingSort but reversed in the last for loop. Normally it's from size to 1. Psuedocode in Cormen Introduction to algorithms page 195
    public static int[] countingSortReversed(int[] pileOfEdges, int sortKey,int maxID,int size){
        int[] count = new int[maxID];
        int[] result = new int[size];
        for (int i = sortKey; i < size; i+=3) {
            int id = pileOfEdges[i];
            count[id]++;
        }
        for (int i = 1; i < maxID; i++) {
            count[i] += count[i-1];
        }
        for (int i = 0; i < size; i+=3) {
            int id = pileOfEdges[i+sortKey];
            int newIndex = --count[id]*3;
            result[newIndex++] = pileOfEdges[i];
            result[newIndex++] = pileOfEdges[i+1];
            result[newIndex] = pileOfEdges[i+2];
        }
        return result;
    }


    public static int[] removeDuplicates(int[] pileOfEdges, float[] weights, int n){
        ArrayList edges = new ArrayList();
        int endpoint1 = pileOfEdges[0];
        int endpoint2 = pileOfEdges[1];
        int edgeID = pileOfEdges[2];
        for (int i = 3; i < pileOfEdges.length; i+=3) {
            if(endpoint1 != pileOfEdges[i] || endpoint2 != pileOfEdges[i+1]){
                edges.add(endpoint1);
                edges.add(endpoint2);
                edges.add(edgeID);
                endpoint1 = pileOfEdges[i];
                endpoint2 = pileOfEdges[i+1];
                edgeID = pileOfEdges[i+2];
            } else {
                if(weights[pileOfEdges[i+2]] < weights[edgeID]) {
                    edgeID = pileOfEdges[i+2];
                }
            }
        }
        edges.add(endpoint1);
        edges.add(endpoint2);
        edges.add(edgeID);
        edges.add(n);
        //Profiler.profilerMain.labels.add("06 Contract - Remove duplicates");
        //Profiler.profilerMain.profiling.add(System.nanoTime());
        return edges.getArray();
    }
}

