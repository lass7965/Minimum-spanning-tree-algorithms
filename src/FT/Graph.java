package FT;

public class Graph {
    public int[][] vertices;
    int edgesCount;

    // Normal constructor for the graph, this constructor is used to take in the users input in a input int array filled with 2 values per edge and a float array with their weights
    // Input of form {i,j,x,y} where vertices i,j are connected and x,y are connected both with the connection weigh stored in another array.
    public Graph(int[] input, int n){
        int[] countMap = parseInput(input,n); // Get a count of neighbors for every vertex.
        int[] nextWrite = new int[n];
        this.vertices = new int[n][];
        for (int i = 0; i < n; i++) {
            this.vertices[i] = new int[countMap[i]*3]; // Target, Weight, EdgeID
        }
        for (int i = 0; i < input.length-1; i+=4) {
            int x = input[i];
            int y = input[i+1];
            int edgeID = input[i+2];
            int weight = input[i+3];
            int index = nextWrite[x];
            this.vertices[x][index] = y;
            this.vertices[x][index+1] = edgeID;
            this.vertices[x][index+2] = weight;
            nextWrite[x]+=3;

            index = nextWrite[y];
            this.vertices[y][index] = x;
            this.vertices[y][index+1] = edgeID;
            this.vertices[y][index+2] = weight;
            nextWrite[y]+=3;
        }
        this.edgesCount = (input.length-1)/4;
    }

    public Graph(int size){
        this.vertices = new int[size][];
        edgesCount = 0;
    }

    // Count the number of neighbors that a vertex has, stored and returned in a hashmap to initialize vertices of appropriate sizes.
    // Input of form {i,j,x,y} where vertices i,j are connected and x,y are connected.
    public static int[] parseInput(int[] input, int n){
        int[] counter = new int[n];
        for (int i = 0; i < input.length-1; i+=4) {
            counter[input[i]]++;
            counter[input[i+1]]++;
        }
        return counter;
    }

    public int[] getNeighbors(int v) {
        return this.vertices[v];
    }
}
