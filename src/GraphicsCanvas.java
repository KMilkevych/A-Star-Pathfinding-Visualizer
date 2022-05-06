import java.awt.*;
import javax.swing.plaf.DimensionUIResource;

/**
 * A Graphics Canvas, used as the main viewport in the application.
 */
public class GraphicsCanvas extends Canvas {
    
    // Define initial cell size
    private int cellDimension = 20; //px

    private Board board;

    /**
     * Constructor for GraphicsCanvas class.
     */
    public GraphicsCanvas() {
        super();
        
        // Predefine size of board
        int cellCountX = 30;
        int cellCountY = 30;

        Dimension preferredDimension = new DimensionUIResource(cellCountX * cellDimension, cellCountY * cellDimension); // old: 600x400
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(preferredDimension);

        // Create a board to contain all user input cells
        board = new Board(cellCountX, cellCountY);

        // Fill in some sample cells
        board.setTile(Cell.START, 2, 4);
        board.setTile(Cell.END, 18, 9);
        board.setTile(Cell.WALL, 10, 10);
        board.setTile(Cell.WALL, 10, 11);
        board.setTile(Cell.WALL, 10, 12);
    }

    /**
     * Paints content to the canvas using specified graphics.
     */
    public void paint(Graphics g) {

        // Update grid dimensions based on possible resize
        cellDimension = Math.min(this.getWidth()/board.getXSize(), this.getHeight()/board.getYSize());

        // Paint the board
        paintBoard(g);

        // Paint the grid
        paintGrid(g);

    }

    /**
     * Draws the grid to the screen.
     * @param g - Graphics object to draw grid with.
     */
    private void paintGrid(Graphics g) {

        g.setColor(Color.BLACK);

        int xLength = cellDimension * board.getXSize();
        int yLength = cellDimension * board.getYSize();

        for (int x = 0; x < board.getXSize() + 1; x++) {
            int p = x * cellDimension;
            g.drawLine(p, 0, p, yLength);
        }

        for (int y = 0; y < board.getYSize() + 1; y++) {
            int p = y * cellDimension;
            g.drawLine(0, p, xLength, p);
        }
    }

    /**
     * Draws the saved board contents to the screen.
     * @param g - Graphics object to draw board with.
     */
    private void paintBoard(Graphics g) {

        // Fetch board
        Cell[][] board = this.board.getBoard();

        // Iterate through each cell in board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                
                // Fetch cell type, and calculate cell position
                Cell c = board[i][j];
                int xPos = j*cellDimension;
                int yPos = i*cellDimension;

                // Draw color depending on cell type
                switch (c) {
                    case FREE:
                        break;
                    case START:
                        g.setColor(Color.RED);
                        g.fillRect(xPos, yPos, cellDimension, cellDimension);
                        break;
                    case WALL:
                        g.setColor(Color.BLACK);
                        g.fillRect(xPos, yPos, cellDimension, cellDimension);
                        break;
                    case END:
                        g.setColor(Color.BLUE);
                        g.fillRect(xPos, yPos, cellDimension, cellDimension);
                        break;
                    default:
                        break;
                }
            }
        }
    }

}
