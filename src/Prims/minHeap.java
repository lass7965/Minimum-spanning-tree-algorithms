package Prims;

import Boruvka.ArrayList;
import java.util.HashMap;

public class minHeap {
    public int[] heap;
    int[] index;
    int size;


    public minHeap(int n){
        heap = new int[3*n]; // [vertexID, Key, Parent]
        index = new int[2*n]; // [Index, Key]
        this.size = 0;
    }

    public void minHeapify(int i){
        if (this.heap.length == 0) return;
        int smallest = i;
        int lChild = i*2+1;
        int rChild = i*2+2;
        if(lChild < this.size && heap[lChild*3+1] < heap[i*3+1]) smallest = lChild;
        if(rChild < this.size && heap[rChild*3+1] < heap[smallest*3+1]) smallest = rChild;
        if(smallest != i) {
            exchange(i,smallest);
            minHeapify(smallest);
        }
    }

    public void decreaseValue(int id, int weight,int parentID){
        int i = index[id*2];
        index[2*id+1] = weight;
        heap[i*3+1] = weight;
        heap[i*3+2]  = parentID;
        int parent = (i-1)/2;
        while( i > 0 &&  heap[parent*3+1] > heap[i*3+1]){
            exchange(parent,i);
            i = parent;
            parent = (i-1)/2;
        }
    }

    public void insert(int vertexID, int weight){
        int i = this.size++;
        heap[i*3] = vertexID;
        heap[i*3+1] = weight;
        heap[i*3+2] = -1;
        index[vertexID*2] = i;
        index[vertexID*2+1] = weight;
    }

    public int[] getMin(){
        return new int[]{heap[0],heap[1],heap[2]}; //[vertexID, Weight, Parent]
    }

    public int getKey(int id){
        return index[id*2+1];
    }

    public int[] popMin(){
        if(size == 0) return null;
        int[] ret = getMin();
        index[ret[0]*2] = -1;
        if(size-- > 1) {
            heap[0] = heap[size*3]; // GET vertex ID
            heap[1] = heap[size*3+1]; // GET vertex ID
            heap[2] = heap[size*3+2]; // GET vertex ID
            index[heap[0]*2] = 0;
        }
        minHeapify(0);
        return ret;
    }

    public boolean contains(int id, int weight){
        return index[2*id] >= 0 && weight < index[2*id+1];

    }

    public void exchange(int swap1, int swap2){
        int[] data = new int[]{heap[swap1*3],heap[swap1*3+1],heap[swap1*3+2]};
        index[heap[swap1*3]*2] = swap2;
        index[heap[swap2*3]*2] = swap1;
        heap[swap1*3] = heap[swap2*3];
        heap[swap1*3+1] = heap[swap2*3+1];
        heap[swap1*3+2] = heap[swap2*3+2];
        heap[swap2*3] = data[0];
        heap[swap2*3+1] = data[1];
        heap[swap2*3+2] = data[2];
    }
}
