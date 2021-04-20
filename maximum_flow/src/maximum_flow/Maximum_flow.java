package maximum_flow;

import java.sql.SQLOutput;
import java.util.*;

public class Maximum_flow {

    static int noOfNodes = 0; 	//Number of vertices in graph
    //maps array index 0 to "s", & successive indexes to a string equivalent
    private String[] edgesCapacity;//edgesCapacity[0]="S" & edgesCapacity[vertexCount-1]="T"

    public static void main(String[] args) {

        String[] edgesCapacity;

        Scanner vertexScanner = new Scanner(System.in);
        System.out.print("Number of nodes is : ");
        noOfNodes = 6;

        System.out.println(noOfNodes);
        System.out.println("------------------------------");

        edgesCapacity = new String[noOfNodes];

        //Graph is an adjacency Matrix. 0 means no edge between 2 vertices. Positive number means the capacity of the edge
        //Directed graph so order of indexes matters. Row comes 1st, then column
        //graphMatrix[0][0]=0 since S has no edges to itself
        //graphMatrix[0][1]=10 since there's an edge from S to node 2
        //Vertex  = index
        // 		s = 0
        // 		2 = 1
        // 		3 = 2
        // 		4 = 3
        // 		5 = 4
        // 		6 = 5
        // 		7 = 6
        // 		t = 7
        Random r = new Random();
        int j = 0;
        int i = 0;

        while (i < noOfNodes) {

            edgesCapacity[j] = "v-";
            edgesCapacity[j] += i + 1;
            ++i;
            ++j;
        }

        int graphMatrix[][] = new int[][]{
            {0, 2, 7, 0, 0, 0},
            {0, 0, 0, 3, 4, 0},
            {0, 0, 0, 4, 2, 0},
            {0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 5},
            {0, 0, 0, 0, 0, 0} //T's row (no edges leaving T)
        };

        Maximum_flow maxFlowFinder = new Maximum_flow(edgesCapacity);

        int vertexS = 0;
        int vertexT = noOfNodes - 1;  //T is the last thing in the list

        System.out.println("----------------------------------------\n" + "Ford Fulkerson Max Flow: " + maxFlowFinder.maxFlow(graphMatrix, vertexS, vertexT));
        System.out.println("----------------------------------------");

    }

    public Maximum_flow(String[] edgesCapacity) {
        this.edgesCapacity = edgesCapacity;//pass by reference, but don't care since main doesn't modify this
    }

    // Returns max flow from S to T in a graph
    public int maxFlow(int graph[][], int vertexS, int vertexT) {

        int maxFlow = 0;
        int parent[] = new int[noOfNodes];//holds parent of a vertex when a path if found (filled by BFS)
        int vertexU = 0;//iterator vertices to loop over the matrix
        int vertexV = 0;

        int residualGraph[][] = new int[noOfNodes][noOfNodes];	//residualGraph[i][j] tells you if there's an edge between vertex i & j. 0=no edge, positive number=capacity of that edge
        for (vertexU = 0; vertexU < noOfNodes; vertexU++) {//copy over every edge from the original graph into residual
            for (vertexV = 0; vertexV < noOfNodes; vertexV++) {
                residualGraph[vertexU][vertexV] = graph[vertexU][vertexV];
            }
        }

        while (bfs(residualGraph, vertexS, vertexT, parent)) {//if a path exists from S to T
            String pathString = "";//Shows the augmented path taken

            //find bottleneck by looping over path from BFS using parent[] array
            int bottleneckFlow = Integer.MAX_VALUE;//we want the bottleneck (minimum), so initially set it to the largest number possible. Loop updates value if it's smaller
            for (vertexV = vertexT; vertexV != vertexS; vertexV = parent[vertexV]) {//loop backward through the path using parent[] array
                vertexU = parent[vertexV];//get the previous vertex in the path
                bottleneckFlow = Math.min(bottleneckFlow, residualGraph[vertexU][vertexV]);//minimum of previous bottleneck & the capacity of the new edge
                pathString = " --> " + edgesCapacity[vertexV] + pathString;//prepend vertex to path
            }
            pathString = "s-1" + pathString;//loop stops before it gets to S, so add S to the beginning
            System.out.print("Augmentation path \n" + pathString);
            System.out.print(", Flow is " + bottleneckFlow + "\n");
            System.out.println("Updated Flow " + (maxFlow + bottleneckFlow) + "\n");

            //Update residual graph capacities & reverse edges along the path
            for (vertexV = vertexT; vertexV != vertexS; vertexV = parent[vertexV]) {//loop backwards over path (same loop as above)
                vertexU = parent[vertexV];
                residualGraph[vertexU][vertexV] -= bottleneckFlow;//back edge
                residualGraph[vertexV][vertexU] += bottleneckFlow;//forward edge
            }

            maxFlow += bottleneckFlow;//add the smallest flow found in the augmentation path to the overall flow
        }

        
        // The Min-Cut
        // Flow is maximum now, find vertices reachable from s     
        boolean[] isVisited = new boolean[graph.length];
        dfs(residualGraph, vertexS, isVisited);
        System.out.println("----------------------------------------");
        // Print all edges that are from a reachable vertex to non-reachable vertex in the original graph     
        System.out.print("The min-cut is : ");
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (graph[i][j] > 0 && isVisited[i] && !isVisited[j]) {
                    System.out.print("{" + (i + 1) + " - " + (j + 1) + "} ");
                }
            }
        }
        System.out.println("");

        return maxFlow;
    }

    // dfs for mun-cut
    private static void dfs(int[][] rGraph, int s,
            boolean[] visited) {
        visited[s] = true;
        for (int i = 0; i < rGraph.length; i++) {
            if (rGraph[s][i] > 0 && !visited[i]) {
                dfs(rGraph, i, visited);
            }
        }
    }

    //Returns true if it finds a path from S to T saves the vertices in the path in parent[] array
    public boolean bfs(int residualGraph[][], int vertexS, int vertexT, int parent[]) {
        boolean visited[] = new boolean[noOfNodes];//has a vertex been visited when finding a path. Boolean so all values start as false

        LinkedList<Integer> vertexQueue = new LinkedList<Integer>();//queue of vertices to explore (BFS to FIFO queue)
        vertexQueue.add(vertexS);//add source vertex
        visited[vertexS] = true;//visit it
        parent[vertexS] = -1;//"S" has no parent

        while (!vertexQueue.isEmpty()) {
            int vertexU = vertexQueue.remove();	//get a vertex from the queue

            for (int vertexV = 0; vertexV < noOfNodes; vertexV++) {//Check all edges to vertexV by checking all values in the row of the matrix
                if (visited[vertexV] == false && residualGraph[vertexU][vertexV] > 0) {	//residualGraph[u][v] > 0 means there actually is an edge
                    vertexQueue.add(vertexV);
                    parent[vertexV] = vertexU;//used to calculate path later
                    visited[vertexV] = true;
                }
            }
        }
        return visited[vertexT];//return true/false if we found a path to T
    }

}
