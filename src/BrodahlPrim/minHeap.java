package BrodahlPrim;

import Boruvka.ArrayList;

public class minHeap {
    public ArrayList heap;
    int size;


    public minHeap(){
        heap = new ArrayList();
        this.size = 0;
    }

    public void clear(){
        this.size = 0;
        heap.clear();
    }

    public void minHeapify(int i){
        if (this.heap.size() == 0) return;
        int smallest = i;
        int lChild = i*2+1;
        int rChild = i*2+2;
        if (lChild < this.size && comparator(i,lChild)) smallest = lChild;
        if (rChild < this.size && comparator(smallest,rChild)) smallest = rChild;
        if (smallest != i){
            exchange(i,smallest);
            minHeapify(smallest);
        }
    }

    public boolean comparator(int parent,int child){
        if (child >= this.size) return false;
        // If x got a smaller weight than y
        if (heap.get(parent*3) > heap.get(child*3)) return true;
        else if (heap.get(parent*3) == heap.get(child*3))  {
            int ux = heap.get(parent*3+1);
            int vx = heap.get(parent*3+2);
            int uy = heap.get(child*3+1);
            int vy = heap.get(child*3+2);
            int temp;

            // Make sure that ux < vx
            if(ux > vx) {
                temp = vx;
                vx = ux;
                ux = temp;
            }

            // Make sure that uy < vy
            if(uy > vy) {
                temp = vy;
                vy = uy;
                uy = temp;
            }

            // If x and y got an equal weight and the source of the edge of x is lower than y
            if(ux > uy) return true;
            else if (ux == uy) {
                // If both weight and source is equal, and target of edge x is lower than y
                if(vx > vy) return true;
            }
        }
        return false;
    }

    public void insert(int u, int v, int weight){
        int i = this.size++;
        heap.add(weight);
        heap.add(u);
        heap.add(v);
        int parent = (i-1)/2;
        while(i > 0 && comparator(parent,i)) {
            exchange(i,parent);
            i = parent;
            parent = (i-1)/2;
        }
    }

    public int[] getMin(){
        if(this.size == 0) return null;
        return new int[]{heap.get(1),heap.get(2),heap.get(0)};
    }

    public int[] popMin(){
        if(size == 0) return null;
        int[] ret = getMin();
        if(size-- > 1) {
            heap.set(2,heap.remove());
            heap.set(1,heap.remove());
            heap.set(0,heap.remove());
        } else {
            heap.clear();
        }
        minHeapify(0);
        return ret;
    }

    public void exchange(int swap1, int swap2){
        int[] data = {heap.get(swap1*3), heap.get(swap1*3+1), heap.get(swap1*3+2)};
        heap.set(swap1*3,heap.get(swap2*3));
        heap.set(swap1*3+1,heap.get(swap2*3+1));
        heap.set(swap1*3+2,heap.get(swap2*3+2));
        heap.set(swap2*3,data[0]);
        heap.set(swap2*3+1,data[1]);
        heap.set(swap2*3+2,data[2]);

    }
}
