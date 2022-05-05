import java.awt.*;

import javax.swing.plaf.DimensionUIResource;

/**
 * A Graphics Canvas, used as the main viewport in the application.
 */
public class GraphicsCanvas extends Canvas {
    
    private int gridCellCountX = 30;
    private int gridCellCountY = 20;

    /**
     * Constructor for GraphicsCanvas class.
     */
    public GraphicsCanvas() {
        super();
        setBackground(Color.LIGHT_GRAY);
        Dimension preferredDimension = new DimensionUIResource(600, 400);
        setPreferredSize(preferredDimension);
    }

    /**
     * Paints content to the canvas using specified graphics.
     */
    public void paint(Graphics g) {
        
        paintGrid(g);

    }

    /**
     * Draws the grid to the screen.
     * @param g - Graphics object to draw grid with.
     */
    private void paintGrid(Graphics g) {
        g.setColor(Color.BLACK);

        int gridSize = Math.min(this.getWidth()/gridCellCountX, this.getHeight()/gridCellCountY);
        int xLength = gridSize * gridCellCountX;
        int yLength = gridSize * gridCellCountY;

        for (int x = 0; x < gridCellCountX + 1; x++) {
            int p = x * gridSize;
            g.drawLine(p, 0, p, yLength);
        }

        for (int y = 0; y < gridCellCountY + 1; y++) {
            int p = y * gridSize;
            g.drawLine(0, p, xLength, p);
        }
    }

}
