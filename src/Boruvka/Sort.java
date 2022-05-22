package Boruvka;

public class Sort {
    //Preform counting sort per index defined in sortKeys. First index in sortKeys is the "highest priority" of the sorting.
    public static int[] radixSort(int[] pileOfEdges, int[] sortKeys,int maxID, int size){
        for (int key: sortKeys) {
            pileOfEdges = countingSortReversed(pileOfEdges,key,maxID,size);
        }
        return pileOfEdges;
    }

    // CountingSort but reversed in the last for loop. Normally it's from size to 1. Psuedocode in Cormen Introduction to algorithms page 195
    public static int[] countingSortReversed(int[] pileOfEdges, int sortKey,int maxID,int size){
        int[] count = new int[maxID];
        int[] result = new int[size+1];
        for (int i = sortKey; i < size; i+=4) {
            int id = pileOfEdges[i];
            count[id]++;
        }
        for (int i = 1; i < maxID; i++) {
            count[i] += count[i-1];
        }
        for (int i = 0; i < size; i+=4) {
            int id = pileOfEdges[i+sortKey];
            int newIndex = --count[id]*4;
            result[newIndex++] = pileOfEdges[i];
            result[newIndex++] = pileOfEdges[i+1];
            result[newIndex++] = pileOfEdges[i+2];
            result[newIndex] = pileOfEdges[i+3];
        }
        result[size] = maxID;
        return result;
    }


    public static int[] removeDuplicates(int[] pileOfEdges, int n){
        ArrayList edges = new ArrayList();
        int endpoint1 = pileOfEdges[0];
        int endpoint2 = pileOfEdges[1];
        int edgeID = pileOfEdges[2];
        int weight = pileOfEdges[3];
        for (int i = 4; i < pileOfEdges.length; i+=4) {
            if(endpoint1 != pileOfEdges[i] || endpoint2 != pileOfEdges[i+1]){
                edges.add(endpoint1);
                edges.add(endpoint2);
                edges.add(edgeID);
                edges.add(weight);
                endpoint1 = pileOfEdges[i];
                endpoint2 = pileOfEdges[i+1];
                edgeID = pileOfEdges[i+2];
                weight = pileOfEdges[i+3];
            } else {
                if(pileOfEdges[i+3] < weight) {
                    edgeID = pileOfEdges[i+2];
                    weight = pileOfEdges[i+3];
                }
            }
        }
        edges.add(endpoint1);
        edges.add(endpoint2);
        edges.add(edgeID);
        edges.add(weight);
        edges.add(n);
        return edges.getArray();
    }
}

