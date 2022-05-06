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
    private double zoom = 1;
    private double panX = 0;
    private double panY = 0;
    private double startPanX = 0;
    private double startPanY = 0;

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

        int width = cellCountX * cellDimension;
        int height = cellCountY * cellDimension;
        Dimension preferredDimension = new DimensionUIResource(width, height); // old: 600x400
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(preferredDimension);

        // Define panX and panY MAYBE DROP IF NOT WORK AS EXPECTED
        //panX = -width / 2.0;
        //panY = -height / 2.0;

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
        //cellDimension = Math.min(this.getWidth()/board.getXSize(), this.getHeight()/board.getYSize());

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

        // Calculate lengths and clean offsets
        double[] sc = new double[]{cellDimension * board.getXSize() * zoom,cellDimension * board.getYSize() * zoom};
        double[] offsets = new double[]{(-panX * zoom) % sc[0], (-panY * zoom) % sc[1]};

        // Set grid color to black
        g.setColor(Color.BLACK);
        
        // Draw grid
        for (int x = 0; x < board.getXSize() + 1; x++) {
            double p = x * cellDimension * zoom;
            g.drawLine((int)(p + offsets[0]), (int)(0 + offsets[1]), (int)(p + offsets[0]), (int)(sc[1] + offsets[1]));
        }

        for (int y = 0; y < board.getYSize() + 1; y++) {
            double p = y * cellDimension * zoom;
            g.drawLine((int)(0 + offsets[0]), (int)(p + offsets[1]), (int)(sc[0] + offsets[0]), (int)(p + offsets[1]));
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
                        drawTile(g, Color.RED, xPos, yPos, cellDimension);
                        break;
                    case WALL:
                        drawTile(g, Color.BLACK, xPos, yPos, cellDimension);
                        break;
                    case END:
                        drawTile(g, Color.BLUE, xPos, yPos, cellDimension);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void drawTile(Graphics g, Color c, int xPos, int yPos, int size) {
        // Apply zoom before drawing
        double[] pos = worldToScreen(xPos, yPos);
        int scale = (int)Math.ceil(size*zoom);

        // Draw tile
        g.setColor(c);
        g.fillRect((int) pos[0], (int) pos[1], scale, scale);
    }

    private double[] worldToScreen(int x, int y) {
        double newX = ((x - panX)*zoom);
        double newY = ((y - panY)*zoom);
        return new double[]{newX, newY};
    }

    private double[] screenToWorld(int x, int y) {
        double newX = (x/zoom + panX);
        double newY = (y/zoom + panY);
        return new double[]{newX, newY};
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

                if (e.getButton() == 1) { // if left-click
                    // Transform pressed coordinates into proper tile coordinates in board
                    double[] worldPos = screenToWorld(e.getX(), e.getY());
                    int xTile = (int) (worldPos[0] / cellDimension);
                    int yTile = (int) (worldPos[1] / cellDimension);

                    // Set the tile on board using transformed coordinates
                    board.setTile(Cell.values()[mode.getValue()], xTile, yTile); // This is bad code but enum implementation stops me from doing otherwise

                    // Repaint the canvas
                    repaint();
                } else if (e.getButton() == 3) { // if right-click
                    // Update startPans
                    startPanX = e.getX();
                    startPanY = e.getY();

                    writeLog("panX: " + panX + ", panY: " + panY + "\n");
                } 
            }
        };
    }

    private MouseWheelListener makeMouseWheelListener() {
        return new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                // Capture mouse position in world space before zoom
                double[] beforeZoomMPos = screenToWorld(e.getX(), e.getY());

                // Calculate new zoom
                double scroll = e.getWheelRotation();
                zoom = zoom * (1 - scroll/100.0);
                writeLog("Zoom: " + zoom + "\n");

                // Capture mouse position in world space after zoom
                double[] afterZoomMPos = screenToWorld(e.getX(), e.getY());

                // Calculate new pan
                panX = panX + (beforeZoomMPos[0] - afterZoomMPos[0]);
                panY = panY + (beforeZoomMPos[1] - afterZoomMPos[1]);
                writeLog("Pan: " + panX + ", " + panY + "\n");

                // Repaint canvas
                repaint();
            }
        };
    }

    private MouseMotionListener makeMouseMotionListener() {
        return new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {}

            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    panX = panX - (e.getX() - startPanX)/zoom;
                    panY = panY - (e.getY() - startPanY)/zoom;
                    startPanX = e.getX();
                    startPanY = e.getY();
                    writeLog("panX: " + panX + ", panY: " + panY + "\n");
                    repaint();
                }
            }
        };
    }

}
