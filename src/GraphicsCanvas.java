import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
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
    private int cellDimension = 8; //px

    // Define zoom and pan used for viewport/canvas
    private double zoom = 1.0f;
    private int panX = 0;
    private int panY = 0;

    private int lastMouseX = 0;
    private int lastMouseY = 0;

    // Declare private field containing board information
    private Board board;

    // Define initial mode
    private Mode mode = Mode.FREEPLACE;

    // Declare references to other UI components
    private JCheckBox showVizualizationCheckbox;
    private JSlider vizualizationSpeedSlider;
    private JComboBox algorithmComboBox;

    private JLabel startPointLabel;
    private JLabel endPointLabel;
    private JLabel shortestPathLabel;
    private JLabel computationalTimeLabel;

    private JTextArea outputLog;

    /**
     * Constructor for GraphicsCanvas class. UI references to certain components are required.
     * @param showVizualizationCheckbox
     * @param vizualizationSpeedSlider
     * @param algorithmComboBox
     * @param startPointLabel
     * @param endPointLabel
     * @param shortestPathLabel
     * @param computationalTimeLabel
     * @param outputLog
     */
    public GraphicsCanvas(JCheckBox showVizualizationCheckbox, JSlider vizualizationSpeedSlider, JComboBox algorithmComboBox, JLabel startPointLabel, JLabel endPointLabel, JLabel shortestPathLabel, JLabel computationalTimeLabel, JTextArea outputLog) {
        super();
        
        // Predefine size of board
        int cellCountX = 100;
        int cellCountY = 100;

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
        this.addMouseMotionListener(makeMouseMotionListener());
        this.addMouseWheelListener(makeMouseWheelListener());

        // Set references to ui objects
        this.showVizualizationCheckbox = showVizualizationCheckbox;
        this.vizualizationSpeedSlider = vizualizationSpeedSlider;
        this.algorithmComboBox = algorithmComboBox;
        this.startPointLabel = startPointLabel;
        this.endPointLabel = endPointLabel;
        this.shortestPathLabel = shortestPathLabel;
        this.computationalTimeLabel = computationalTimeLabel;
        this.outputLog = outputLog;
    }

    /**
     * Sets the mode for this graphicscanvas
     * @param mode - Desired mode.
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * Resets the board and repaints canvas.
     */
    public void reset() {
        board.clearBoard();
        repaint();
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

    /**
     * Writex specified text to outputLog. Automatically moves the caret position down to bottom.
     * @param text - text to write
     */
    private void writeLog(String text) {
        outputLog.append(text);
        outputLog.setCaretPosition(outputLog.getDocument().getLength());
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

    private MouseWheelListener makeMouseWheelListener() {
        return new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                // Calculate new zoom
                double scroll = e.getWheelRotation()/2.0;
                zoom = zoom / (1 + scroll/100.0);
                writeLog("Zoom: " + zoom + "\n");

                // Calculate new pan
                panX = lastMouseX;
                panY = lastMouseY;
                writeLog("Pan: " + panX + ", " + panY + "\n");
            }
        };
    }

    private MouseMotionListener makeMouseMotionListener() {
        return new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                // Save last known (these) mouse coordinates
                lastMouseX = e.getX();
                lastMouseY = e.getY();
            }
            public void mouseDragged(MouseEvent e) {}
        };
    }

}
