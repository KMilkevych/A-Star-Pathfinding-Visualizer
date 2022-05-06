import java.awt.*;
import java.awt.event.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.DimensionUIResource;

enum Mode {
    FREEPLACE(0),
    STARTPLACE(1),
    WALLPLACE(2),
    ENDPLACE(3);

    private final int value;
    private Mode(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}

/**
 * A Graphics Canvas, used as the main viewport in the application.
 */
public class GraphicsCanvas extends Canvas {

    // Decleare buffer and buffergraphics
    private Image buffer;
    private Graphics bufferGraphics; 

    // Define initial cell size
    private int cellDimension = 20; //px

    // Declare private field containing board information
    private Board board;

    // Define initial mode
    private Mode mode = Mode.FREEPLACE;

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

        // Fill in some sample start and end cells
        board.setTile(Cell.START, 4, 15);
        board.setTile(Cell.END, 25, 15);

        // Add a mouselistener to listen for different mouse inputs.
        this.addMouseListener(makeMouseInputAdapter());

    }

    /**
     * Sets the mode for this graphicscanvas
     * @param mode - Desired mode.
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * Update method used for creating and updating the double buffer, as well as repainting the canvas using double-buffer.
     * This method is called through regular update() calls by the ui, as well as through paint(), to trigger proper repaint when calling repaint().
     * @param g - The graphics object used to draw to the actual canvas
     */
    public void update(Graphics g) {
        // Update grid dimensions based on possible resize
        cellDimension = Math.min(this.getWidth()/board.getXSize(), this.getHeight()/board.getYSize());

        // Initialize buffer
        buffer = createImage(this.getSize().width, this.getSize().height);
        bufferGraphics = buffer.getGraphics();

        // Clear screen in background
        bufferGraphics.setColor(getBackground());
        bufferGraphics.fillRect(0, 0, this.getSize().width, this.getSize().height);

        // Paint the necessarry content to the buffer
        paintContent(bufferGraphics);

        // Draw buffer to screen
        g.drawImage(buffer, 0, 0, this);
    }

    /**
     * Calls update which utilizes double-buffering.
     */
    public void paint(Graphics g) {
        update(g);
    }

    /**
     * Paints all content using specified Graphics object.
     * @param g - Graphics object used for painting all content.
     */
    private void paintContent(Graphics g) {
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

    private MouseInputAdapter makeMouseInputAdapter() {
        return new MouseInputAdapter() {
            public void mousePressed(MouseEvent e) {

                // Transform pressed coordinates into proper tile coordinates in board
                int xTile = e.getX() / cellDimension;
                int yTile = e.getY() / cellDimension;

                // Set the tile on board using transformed coordinates
                board.setTile(Cell.values()[mode.getValue()], xTile, yTile); // This is bad code but enum implementation stops me from doing otherwise

                // Repaint the canvas
                repaint();
            }
        };
    }

}
