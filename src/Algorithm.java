import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;

public class Algorithm {
    
    public static void A_Star(Integer[][][][] graph, Integer[] start, Integer[] end) {

    }

    /**
     * Computes length of shortest path from all nodes reachable from start node.
     * Returns a matrix representing the final node and depth, as well as parent of each color,
     * which allows for reconstruction of shortest path to an arbitrary node.
     * @param graph
     * @param start
     * @param end
     * @return
     */
    public static Integer[][][] BFS(Integer[][][][] graph, Integer[] start, Integer[] end, LinkedList<Integer[][][]> computationList) {
        // Define matrix to store node information
        Integer[][][] nodes = new Integer[graph.length][graph[0].length][4];
        //                                                               ^ {color, depth, parentX, parentY}
        //                                                                  colors: 0=white, 1=gray, 2=black

        // Fill out matrix with empty values
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[0].length; j++) {
                nodes[i][j] = new Integer[]{0, Integer.MAX_VALUE, null, null}; // Standard values for all nodes.
            }
        }

        // Define queue to hold currently reviewed nodes
        ArrayDeque<Integer[]> q = new ArrayDeque<Integer[]>();

        // Enqueue start node
        nodes[start[0]][start[1]] = new Integer[]{1, 0, null, null};
        q.addLast(start);

        // While queue not empty
        while (q.size() > 0) {
            // Remove first node in queue
            Integer[] cnode = q.removeFirst();

            // Fetch adjacent nodes using adjacency map
            Integer[][] adjacent = graph[cnode[0]][cnode[1]];

            // Iterate over each adjacent node (there exists an edge)
            for (Integer[] n : adjacent) {

                // Only consider node, if there is an edge to it, and its color is white.
                if ((n[2] != 0) && (nodes[n[0]][n[1]][0] == 0)) {
                    // Enqueue new node
                    q.addLast(n);

                    // Set color, depth and parent
                    nodes[n[0]][n[1]] = new Integer[] {1, nodes[cnode[0]][cnode[1]][1] + 1, cnode[0], cnode[1]}; // Set color to gray
                }  
            }

            // Set color to black
            nodes[cnode[0]][cnode[1]][0] = 2;

            // Clone current instance of nodes
            Integer[][][] nodescopy = new Integer[nodes.length][nodes[0].length][nodes[0][0].length];
            for (int i = 0; i < nodescopy.length; i++) {
                for (int j = 0; j < nodescopy[0].length; j++) {
                    nodescopy[i][j] = new Integer[]{nodes[i][j][0], nodes[i][j][1], nodes[i][j][2]};
                }
            }

            // Add current instance of nodes to end of linked list
            computationList.addLast(nodescopy);
        }

        // Returns nodes matrix
        return nodes;
    }

    /**
     * OTHER ALGORITHMS GO HERE
     */

}
