package FT;

import Boruvka.ArrayList;

import java.util.*;

public class FibonacciHeap extends Heap {
    Node min;
    Node[] index;
    Boruvka.ArrayList modified;

    public FibonacciHeap(int n){
        index = new Node[n];
        modified = new ArrayList();
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void insert(int vertexID, int weight,int parentID, int edgeID){
        Node x = new Node(vertexID,weight,parentID,edgeID);
        if(this.min == null){
            x.createRootList();
            this.min = x;
        } else {
            min.insertIntoRootList(x);
            if(x.key < this.min.key)
                this.min = x;
        }
        this.size++;
        index[vertexID] = x;
        modified.add(vertexID);
    }

    public int[] popMin(){
        Node z = this.min;
        if(z != null) {
            if (z.child != null) {
                for (Node x:z.child) {
                    this.min.insertIntoRootList(x);
                    x.parent = null;
                }
            }
            z.remove();
            markTakenOut(z.vertexID);
            if(z.right == z) this.min = null;
            else {
                this.min = z.right;
                consolidate();
            }
            this.size--;
            return new int[]{z.vertexID, z.edgeID, z.parentID};
        }
        return null;
    }

    public void markTakenOut(int id){
        if(index[id] != null) index[id].key = Integer.MAX_VALUE;
        else index[id] = new Node(id,Integer.MAX_VALUE,-1,-1);
        modified.add(id);
    }

    public void consolidate(){
        Node[] A = new Node[depthCalc(this.size)];
        for (Node x:this.min) {
            int d = x.degree;
            while(A[d] != null){
                Node y = A[d];
                if(x.key > y.key) {
                    Node oldX = x;
                    x = y;
                    y = oldX;
                }
                link(x,y);
                A[d] = null;
                d++;
            }
            A[d] = x;
        }
        this.min = null;
        for (Node currNode:A) {
            if(currNode != null){
                if(this.min == null){
                    currNode.createRootList();
                    this.min = currNode;
                } else {
                    this.min.insertIntoRootList(currNode);
                    if(currNode.key < this.min.key){
                        this.min = currNode;
                    }
                }
            }
        }
    }

    public void link(Node x, Node y){
        y.remove();
        x.adoptChild(y);
        x.degree++;
        y.mark = false;
    }

    public int depthCalc(int n){
        return (int) Math.ceil(Math.log(n) / Math.log(1.61802));
    }

    public void decreaseIfContains(int id, int weight,int parentID, int edgeID) {
        Node x = index[id];
        if(x == null) insert(id, weight,parentID,edgeID);
        else if (x.key == Integer.MAX_VALUE) return;
        else if(weight < x.key) decreaseKey(id, weight,parentID,edgeID);
    }

    public void decreaseKey(int id, int key,int parentID, int edgeID){
        Node x = index[id];
        x.key = key;
        x.parentID = parentID;
        x.edgeID = edgeID;
        Node y = x.parent;
        if(y != null && x.key < y.key){
            cut(x,y);
            cascasdingCut(y);
        }
        if(x.key < this.min.key) this.min = x;
    }

    public void cut(Node x, Node y){
        if(y.degree == 1) {
            y.degree--;
            y.child = null;
        } else {
            x.remove();
        }
        this.min.insertIntoRootList(x);
        x.parent = null;
        x.mark = false;
    }

    public void cascasdingCut(Node y){
        Node z = y.parent;
        if(z != null){
            if(!y.mark) y.mark = true;
            else {
                cut(y,z);
                cascasdingCut(z);
            }
        }
    }

    public void reset(){
        for (int i = 0; i < modified.size(); i++) {
            this.index[modified.get(i)] = null;
        }
        this.min = null;
        this.size = 0;
        modified.clear();
    }
}
