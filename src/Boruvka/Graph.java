package Boruvka;

import java.util.HashMap;

public class Graph {
    Vertex[] vertices;
    static float[] edgeCost; // Static field, only changed when a graph is initialized in the first recursion step.
    int edgesCount;

    // Normal constructor for the graph, this constructor is used to take in the users input in a input int array filled with 2 values per edge and a float array with their weights
    // Input of form {i,j,x,y} where vertices i,j are connected and x,y are connected both with the connection weigh stored in another array.
    public Graph(int[] input, float[] cost, int n){
        HashMap<Integer,Integer> countMap = parseInput(input); // Get a count of neighbors for every vertex.
        this.vertices = new Vertex[n];
        for (int i = 0; i < n; i++) {
            Vertex v = new Vertex(countMap.getOrDefault(i,0));
            this.vertices[i] = v;
        }
        edgeCost = cost;
        for (int i = 0; i < input.length; i+=2) {
            this.addEdge(input[i],input[i+1],i/2); // Add edge between {x,y} given the ID i/2, as an edge in the input reserves two positions.
        }
    }

    // Count the number of neighbors that a vertex has, stored and returned in a hashmap to initialize vertices of appropriate sizes.
    // Input of form {i,j,x,y} where vertices i,j are connected and x,y are connected.
    public static HashMap<Integer,Integer> parseInput(int[] input){
        HashMap<Integer,Integer> countMap = new HashMap<>();
        for (int i = 0; i < input.length; i+=2) {
            int x = input[i];
            int y = input[i+1];
            if(x < y){
                int counter = countMap.getOrDefault(x,0);
                countMap.put(x,counter+1);
            } else {
                input[i] = y;
                input[i+1] = x;
                int counter = countMap.getOrDefault(y,0);
                countMap.put(y,counter+1);
            }

        }
        return countMap;
    }

    public Graph(int size){
        this.vertices = new Vertex[size];
        edgesCount = 0;
    }

    public void createVertex(int id,int size){
        Vertex v = new Vertex(size);
        this.vertices[id] = v;
    }

    //Add an edge between two vertices i,j with edgeID ID
    private void addEdge(int source, int target, int ID){
        Vertex sourceVertex = this.vertices[source];
        sourceVertex.addEdge(target,ID);
        this.edgesCount++;
    }
}

