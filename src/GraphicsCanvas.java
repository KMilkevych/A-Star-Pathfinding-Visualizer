import java.awt.*;

/**
 * A Graphics Canvas, used as the main viewport in the application.
 */
public class GraphicsCanvas extends Canvas {
    
    private int gridCellCountX = 40;
    private int gridCellCountY = 20;

    /**
     * Constructor for GraphicsCanvas class.
     */
    public GraphicsCanvas() {
        super();
        setBackground(Color.LIGHT_GRAY);
        setSize(600, 400);
    }

    /**
     * Paints content to the canvas using specified graphics.
     */
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        int gridSize = Math.min(this.getWidth()/gridCellCountX, this.getHeight()/gridCellCountY);

        for (int x = 0; x < gridCellCountX; x++) {
            int p = x * gridSize;
            g.drawLine(p, 0, p, this.getHeight());
        }

        for (int y = 0; y < gridCellCountY; y++) {
            int p = y * gridSize;
            g.drawLine(0, p, this.getWidth(), p);
        }
    }

}
