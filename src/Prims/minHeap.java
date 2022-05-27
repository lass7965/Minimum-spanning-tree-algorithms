package Prims;

public class minHeap {
    public int[] heap;
    int[] index;
    int size;


    public minHeap(int n){
        heap = new int[2*n]; // [vertexID, Key]
        index = new int[2*n]; // [Index,Parent]
        this.size = 0;
        for (int i = 0; i < n; i++) {
            index[2*i] = -2;
        }
    }

    public void markTakenOut(int vertex){
        index[2*vertex] = -1;
    }

    public void minHeapify(int i){
        if (this.heap.length == 0) return;
        int smallest = i;
        int lChild = i*2+1;
        int rChild = i*2+2;
        if(lChild < this.size && heap[lChild*2+1] < heap[i*2+1]) smallest = lChild;
        if(rChild < this.size && heap[rChild*2+1] < heap[smallest*2+1]) smallest = rChild;
        if(smallest != i) {
            exchange(i,smallest);
            minHeapify(smallest);
        }
    }

    public void decreaseValue(int id, int weight,int parentID){
        int i = index[id*2];
        heap[i*2+1] = weight;
        index[id*2+1]  = parentID;
        int parent = (i-1)/2;
        while( i > 0 &&  heap[parent*2+1] > heap[i*2+1]){
            exchange(parent,i);
            i = parent;
            parent = (i-1)/2;
        }
    }

    public void insert(int vertexID, int weight, int parentID){
        int i = this.size++;
        heap[i*2] = vertexID;
        heap[i*2+1] = weight;
        index[vertexID*2] = i;
        index[vertexID*2+1] = parentID;
        int parent = (i-1)/2;
        while(i > 0 && heap[parent*2+1] > heap[i*2+1] ) {
            exchange(i,parent);
            i = parent;
            parent = (i-1)/2;
        }
    }

    public int[] getMin(){
        return new int[]{heap[0],heap[1],index[heap[0]*2+1]}; //[vertexID, Weight, Parent]
    }

    public int getKey(int id){
        return heap[index[id*2]*2+1];
    }

    public int[] popMin(){
        if(size == 0) return null;
        int[] ret = getMin();
        index[ret[0]*2] = -1;
        if(size-- > 1) {
            heap[0] = heap[size*2];
            heap[1] = heap[size*2+1];
            index[heap[0]*2] = 0;
        }
        minHeapify(0);
        return ret;
    }

    public boolean isInMST(int id){
        return index[2*id] == -1;
    }

    public boolean shouldDecrement(int id, int weight,int parentID) {
        int i = index[2 * id];
        if(i == -1) return false;
        else if (i >= 0) return weight < heap[2*i+1];
        insert(id, weight, parentID);
        return false;
    }

    public boolean contains(int id, int weight){
        return index[2*id] >= 0 && weight < heap[index[2*id]*2+1];

    }

    public void exchange(int swap1, int swap2){
        int[] data = new int[]{heap[swap1*2],heap[swap1*2+1]};
        index[heap[swap1*2]*2] = swap2;
        index[heap[swap2*2]*2] = swap1;
        heap[swap1*2] = heap[swap2*2];
        heap[swap1*2+1] = heap[swap2*2+1];
        heap[swap2*2] = data[0];
        heap[swap2*2+1] = data[1];
    }
}
