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

    /*
    public int[] getStart() {
        if (startset) {
            return start;
        } else {
            //
        }
    }
    */

    public Cell[][] getBoard() {
        return board;
    }

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    /**
     * Parses the board and computes weighted bidirectional graph.
     * @return - weighted bidirectional graph in matrix representation
     */
    public int[][] getGraph() {
        
        /**
         * NEEDS FILLING
         */

        return new int[][]{};
    }
}
