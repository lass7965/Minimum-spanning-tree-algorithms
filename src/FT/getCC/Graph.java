package FT.getCC;

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
            this.vertices[i] = new int[countMap[i]]; // Target
        }
        for (int i = 0; i < input.length; i+=2) {
            int x = input[i];
            int y = input[i+1];
            int index = nextWrite[x];
            this.vertices[x][index] = y;
            nextWrite[x]++;

            index = nextWrite[y];
            this.vertices[y][index] = x;
            nextWrite[y]++;
        }
        this.edgesCount = input.length;
    }

    public Graph(int size){
        this.vertices = new int[size][];
        edgesCount = 0;
    }

    // Count the number of neighbors that a vertex has, stored and returned in a hashmap to initialize vertices of appropriate sizes.
    // Input of form {i,j,x,y} where vertices i,j are connected and x,y are connected.
    public static int[] parseInput(int[] input, int n){
        int[] counter = new int[n];
        for (int i = 0; i < input.length; i+=2) {
            counter[input[i]]++;
            counter[input[i+1]]++;
        }
        return counter;
    }

    public int[] getNeighbors(int v) {
        return this.vertices[v];
    }
}
