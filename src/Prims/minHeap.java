package Prims;

import Boruvka.ArrayList;
import java.util.HashMap;

public class minHeap {
    public ArrayList heap; // TODO int[] of size n
    HashMap<Object, Integer> index; // TODO Make this to a int[] of size n
    int size;


    public minHeap(){
        heap = new ArrayList();
        index = new HashMap<>();
        this.size = 0;
    }

    public void minHeapify(int i){
        if (this.heap.size() == 0) return;
        int smallest = i;
        int lChild = i*2+1;
        int rChild = i*2+2;
        if(lChild < this.size && heap.get(lChild*2+1) < heap.get(i*2+1)) smallest = lChild;
        if(rChild < this.size && heap.get(rChild*2+1) < heap.get(smallest*2+1)) smallest = rChild;
        if (smallest != i){
            exchange(i,smallest);
            minHeapify(smallest);
        }
    }

    public void decreaseValue(int id, int weight){
        int i = index.get(id);
        heap.set(i*2+1,weight);
        int parent = (i-1)/2;
        while( i > 0 &&  heap.get(parent*2+1) > heap.get(i*2+1)){
            exchange(parent,i);
            i = parent;
            parent = (i-1)/2;
        }
    }

    public void insert(int vertexID, int weight){
        int i = this.size++;
        heap.add(vertexID);
        heap.add(weight);
        index.put(vertexID,i);

        int parent = (i-1)/2;

        while(i > 0 && heap.get(parent*2+1) > heap.get(i*2+1) ) {
            exchange(i,parent);
            i = parent;
            parent = (i-1)/2;
        }
    }

    public void markTakenOut(int id){
        this.index.put(id,-1);
    }

    public int[] getMin(){
        return new int[]{heap.get(0),heap.get(1)}; //[vertexID, Weight, Parent]
    }

    public int getKey(int id){
        return heap.get(index.get(id)*2+1);
    }

    public int[] popMin(){
        if(size == 0) return null;
        int[] ret = getMin();
        index.remove(ret[0]);
        if(size-- > 1) {
            heap.set(1, heap.remove());
            heap.set(0, heap.remove());
        }
        minHeapify(0);
        return ret;
    }

    public boolean contains(int id) {
        return index.get(id) != null;
    }

    public void exchange(int swap1, int swap2){
        int[] swap1Obj = {heap.get(swap1*2),heap.get(swap1*2+1)};
        int[] swap2Obj = {heap.get(swap2*2),heap.get(swap2*2+1)};
        heap.set(swap1*2,heap.get(swap2*2));
        heap.set(swap1*2+1,heap.get(swap2*2+1));
        heap.set(swap2*2,swap1Obj[0]);
        heap.set(swap2*2+1,swap1Obj[1]);
        index.put(swap1Obj[0],swap2);
        index.put(swap2Obj[0],swap1);
    }

    public boolean checkStructure(int i){
        int lchild = i*2+1;
        int rchild = i*2+2;
        if(i > size) return true;
        if(lchild < size && heap.get(i*2+1) > heap.get(lchild*2+1)) { // Parent bigger than left child
            System.out.println(i*2+1 + " is bigger than " + (lchild*2+1));
            return false;
        } else if (rchild < size && heap.get(i*2+1) > heap.get(rchild*2+1)){
            System.out.println(i*2+1 + " is bigger than " + (rchild*2+1));
            return false;
        } else {
            return checkStructure(lchild) && checkStructure(rchild);
        }
    }
}
