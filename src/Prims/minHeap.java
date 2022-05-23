package Prims;

public class minHeap {
    public int[] heap;
    int[] index;
    int size;


    public minHeap(int n){
        heap = new int[2*n]; // [vertexID, Key]
        index = new int[3*n]; // [Index, Key,Parent]
        this.size = 0;
        for (int i = 0; i < n; i++) {
            index[i*3] = -2; // Mark all as not inserted yet
        }
    }

    public void minHeapify(int i){
        if (this.heap.length == 0) return;
        int smallest = i;
        int lChild = i*2+1;
        int rChild = i*2+2;
        if(lChild < this.size && heap[lChild*2+1] < heap[i*2+1]) smallest = lChild;
        if(rChild < this.size && heap[rChild*2+1] < heap[smallest*2+1]) smallest = rChild;
        if (smallest != i){
            exchange(i,smallest);
            minHeapify(smallest);
        }
    }

    public void decreaseValue(int id, int weight,int parentID){
        int i = index[id*3];
        index[3*id+1] = weight;
        heap[i*2+1] = weight;
        index[id*3+2]  = parentID;
        int parent = (i-1)/2;
        while( i > 0 &&  heap[parent*2+1] > heap[i*2+1]){
            exchange(parent,i);
            i = parent;
            parent = (i-1)/2;
        }
    }

    public void insert(int vertexID, int weight,int parentID){
        int i = this.size++;
        heap[i*2] = vertexID;
        heap[i*2+1] = weight;
        index[vertexID*3] = i;
        index[vertexID*3+1] = weight;
        index[vertexID*3+2] = parentID;

        int parent = (i-1)/2;

        while(i > 0 && heap[parent*2+1] > heap[i*2+1] ) {
            exchange(i,parent);
            i = parent;
            parent = (i-1)/2;
        }
    }

    public void markTakenOut(int id){
        this.index[id*3] = -1;
    }

    public int[] getMin(){
        return new int[]{heap[0],heap[1],index[heap[0]*3+2]}; //[vertexID, Weight, Parent]
    }

    public int getKey(int id){
        return index[id*3+1];
    }

    public int[] popMin(){
        if(size == 0) return null;
        int[] ret = getMin();
        index[ret[0]*3] = -1;
        if(size-- > 1) {
            heap[0] = heap[size*2];
            heap[1] = heap[size*2+1];
            index[heap[0]*3] = 0;
        }
        minHeapify(0);
        return ret;
    }

    public boolean contains(int id, int weight,int parentID) {
        int i = index[3 * id];
        if(i == -1) return false;
        else if (i >= 0) return weight < index[3*id+1];
        insert(id, weight, parentID);
        return false;
    }

    public void exchange(int swap1, int swap2){
        int[] data = new int[]{heap[swap1*2],heap[swap1*2+1]};
        index[heap[swap1*2]*3] = swap2;
        index[heap[swap2*2]*3] = swap1;
        heap[swap1*2] = heap[swap2*2];
        heap[swap1*2+1] = heap[swap2*2+1];
        heap[swap2*2] = data[0];
        heap[swap2*2+1] = data[1];
    }

    public boolean checkStructure(int i){
        int lchild = i*2+1;
        int rchild = i*2+2;
        if(i > size) return true;
        if(lchild < size && heap[i*2+1] > heap[lchild*2+1]) { // Parent bigger than left child
            System.out.println(i*2+1 + " is bigger than " + (lchild*2+1));
            return false;
        } else if (rchild < size && heap[i*2+1] > heap[rchild*2+1]){
            System.out.println(i*2+1 + " is bigger than " + (rchild*2+1));
            return false;
        } else {
            return checkStructure(lchild) && checkStructure(rchild);
        }
    }
}
