import java.util.HashMap;

/**
 * Enum representing different cell types
 */
enum Cell {
    FREE(0),
    START(1),
    WALL(2),
    END(3);

    private final int value;
    private Cell(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}

/**
 * A class representing a board/grid
 */
public class Board {

    private Cell[][] board;
    private int xSize;
    private int ySize;

    private int[] start;
    private boolean startset = false;

    private int[] end;
    private boolean endset = false;

    public Board(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.board = new Cell[ySize][xSize];
        fillBoard();
    }

    private void fillBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = Cell.FREE;
            }
        }
    }

    public void clearBoard() {
        fillBoard();
        startset = false;
        endset = false;
    }

    public void setTile(Cell tileType, int xPos, int yPos) {
        // If tile being set is START or END tile, update information automatically, and reset already placed start and end tiles
        if (tileType == Cell.START) {
            if (startset) {
                setTile(Cell.FREE, start[0], start[1]); // remove already placed start cell
            } 
            start = new int[]{xPos, yPos}; // place current startcell
            startset = true;
        } else if (tileType == Cell.END) {
            if (endset) {
                setTile(Cell.FREE, end[0], end[1]); // remove already placed end cell
            }
            end = new int[]{xPos, yPos}; // place current endcell
            endset = true;
        } else {
            // check if tile that is being overwritten is a start or end tile
            if (getTile(xPos, yPos) == Cell.START) {
                startset = false;
            } else if (getTile(xPos, yPos) == Cell.END) {
                endset = false;
            } 
        }

        board[yPos][xPos] = tileType; // place cell to actual board
    }

    public Cell getTile(int xPos, int yPos) {
        return board[yPos][xPos];
    }

    public Cell[][] getBoard() {
        return board;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    private int[][] getAdjacent(int xPos, int yPos) {
        // NO DIAGONALS FOR NOW
        /*
        int[][] adj = new int[8][2];
        // Hard coding, because not wanting to use ArrayList to avoid adding self
        adj[0] = new int[] {xPos - 1, yPos - 1};
        adj[1] = new int[] {xPos - 1, yPos};
        adj[2] = new int[] {xPos - 1, yPos + 1};
        adj[3] = new int[] {xPos, yPos - 1};
        // self goes here
        adj[4] = new int[] {xPos, yPos + 1};
        adj[5] = new int[] {xPos + 1, yPos - 1};
        adj[6] = new int[] {xPos + 1, yPos};
        adj[7] = new int[] {xPos + 1, yPos + 1};
        */

        
        int[][] adj = new int[4][2];
        adj[0] = new int[] {xPos - 1, yPos};
        adj[1] = new int[] {xPos + 1, yPos};
        adj[2] = new int[] {xPos, yPos - 1};
        adj[3] = new int[] {xPos, yPos + 1};


        return adj;
    }

    /**
     * Parses the board and computes weighted bidirectional graph.
     * @return - weighted bidirectional graph in adjacency map representation.
     */
    public HashMap<Integer[], Integer[][]> getGraph() {
        
        /**
         * Edges are saved in an adjacency map on the form:
         * HashMap<Integer[2], Integer[4][3]>
         *                                ^ x, y, and weight of possible edge (weight 0 means no edge/connection)     
         *                             ^ 4 possible edges
         *                 ^ coordinates of initial node
         */
        
        // Create empty adjacency map (similar to adjacency list but easier to manage with two coordinates)
        HashMap<Integer[], Integer[][]> adj = new HashMap<Integer[], Integer[][]>();
        
        // Iterate for each cell
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {

                // Put empty array as entry in hashmap
                Integer[] currentCell = new Integer[] {x, y};
                Integer[][] edges = new Integer[4][3];
                adj.put(currentCell, edges);

                // Grab adjacent nodes
                int[][] adjacent = getAdjacent(x, y);

                // Fetch if current cell isnt wall
                boolean isWall = getTile(x, y) == Cell.WALL;

                // Iterate for each adjacent node
                for (int i = 0; i < adjacent.length; i++) {
                    // Save coordinates of adjacent cell
                    int ax = adjacent[i][0];
                    int ay = adjacent[i][1];

                    // Put no connection in as default.
                    edges[i] = new Integer[] {ax, ay, 0};

                    // If current cell isnt wall and adjacent cell isnt wall, and in boundaries
                    if (!isWall && (0 <= ax && ax < xSize) && (0 <= ay && ay < ySize) && !(getTile(ax, ay) == Cell.WALL)) {
                        // Put in edge with weight 1
                        edges[i] = new Integer[] {ax, ay, 1};
                    }
                }
            }
        }

        // Return adjacency map
        return adj;
    }
}
