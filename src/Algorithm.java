import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

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
    public static int[][][] BFS(int[][][][] graph, int[] start, int[] end, LinkedList<ArrayList<CopyOnWriteArrayList<int[]>>> blackgrayNodes) {
        // Define matrix to store node information
        int[][][] nodes = new int[graph.length][graph[0].length][4];
        //                                                               ^ {color, depth, parentX, parentY}
        //                                                                  colors: 0=white, 1=gray, 2=black

        // Fill out matrix with empty values
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[0].length; j++) {
                nodes[i][j] = new int[]{0, Integer.MAX_VALUE, -1, -1}; // Standard values for all nodes.
            }
        }

        // Define queue to hold currently reviewed nodes
        ArrayDeque<int[]> q = new ArrayDeque<int[]>();

        // Enqueue start node
        nodes[start[0]][start[1]] = new int[]{1, 0, -1, -1};
        q.addLast(start);

        // Define ArrayList to hold black nodes
        ArrayList<int[]> blacknodes = new ArrayList<>();

        // Define ArrayList to hold gray nodes
        ArrayList<int[]> graynodes = new ArrayList<>();

        // While queue not empty
        while (q.size() > 0) {

            // Remove first node in queue
            int[] cnode = q.removeFirst();

            // Fetch adjacent nodes using adjacency map
            int[][] adjacent = graph[cnode[0]][cnode[1]];

            // Iterate over each adjacent node (there exists an edge)
            for (int[] n : adjacent) {

                // Only consider node, if there is an edge to it, and its color is white.
                if ((n[2] != 0) && (nodes[n[0]][n[1]][0] == 0)) {
                    // Enqueue new node
                    q.addLast(new int[]{n[0], n[1]});

                    // Set color, depth and parent
                    nodes[n[0]][n[1]] = new int[] {1, nodes[cnode[0]][cnode[1]][1] + 1, cnode[0], cnode[1]}; // Set color to gray

                    // Add to gray nodes
                    graynodes.add(new int[]{n[0], n[1]});
                }  
            }

            // Set color to black
            nodes[cnode[0]][cnode[1]][0] = 2;

            // Remove from gray nodes and add to black nodes
            graynodes.remove(cnode); // Might cause problems, because references
            blacknodes.add(cnode);

            // Copy arraylists
            CopyOnWriteArrayList<int[]> gnc = new CopyOnWriteArrayList<>();
            for (int[] n : graynodes) {
                gnc.add(n.clone());
            }

            // Copy arraylists
            CopyOnWriteArrayList<int[]> bnc = new CopyOnWriteArrayList<>();
            for (int[] n : blacknodes) {
                bnc.add(n.clone());
            }

            // Add to linkedlist
            ArrayList<CopyOnWriteArrayList<int[]>> bgn = new ArrayList<>();
            bgn.add(gnc);
            bgn.add(bnc);
            blackgrayNodes.addLast(bgn);
        }

        // Returns nodes matrix
        return nodes;
    }

    /**
     * OTHER ALGORITHMS GO HERE
     */

}
