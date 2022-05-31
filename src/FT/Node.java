package FT;

import java.util.ArrayList;
import java.util.Iterator;

public class Node implements Iterable<Node> {
    Node parent;
    Node child;
    Node left;
    Node right;

    boolean mark;
    int degree;
    int key;
    int vertexID;
    int edgeID;
    int parentID;

    public Node(int vertexID, int weight,int parentID, int edgeID){
        this.degree = 0;
        this.parent = null;
        this.child = null;
        this.mark = false;
        this.key = weight;
        this.vertexID = vertexID;
        this.parentID = parentID;
        this.edgeID = edgeID;
    }

    public void createRootList(){
        this.left = this;
        this.right = this;
    }

    public void adoptChild(Node toInsert){
        if(this.child == null) {
            toInsert.createRootList();
        } else {
            this.child.insertIntoRootList(toInsert);
        }
        toInsert.parent = this;
        this.child = toInsert;
    }

    public void insertIntoRootList(Node toInsert){
        Node prevRight = this.right;
        this.right = toInsert;
        toInsert.left = this;
        toInsert.right = prevRight;
        prevRight.left = toInsert;
    }

    public class NodeIterator implements Iterator<Node> {
        ArrayList<Node> toRun;
        int counter = 0;

        public NodeIterator(){
            toRun = new ArrayList<Node>();
            Node startNode = right;
            toRun.add(startNode);
            Node nextNode = startNode.right;
            while(nextNode != startNode){
                toRun.add(nextNode);
                nextNode = nextNode.right;
            }
        }

        @Override
        public boolean hasNext()  {
            return toRun.size() > counter;
        }

        @Override
        public Node next() {
            return toRun.get(counter++);
        }
    }

    @Override
    public Iterator<Node> iterator() {
        return new NodeIterator();
    }

    public void remove(){
        this.removeFromList();
        removeFromParent();
    }

    public void removeFromList(){
        if(this.right == this) return;
        this.left.right = this.right;
        this.right.left = this.left;
    }

    public void removeFromParent(){
        if(this.parent == null) return;
        if(this.right == this){
            this.parent.child = null;
            this.parent.degree = 0;
            return;
        }
        this.parent.child = this.right;
        this.parent.degree--;
    }
}
