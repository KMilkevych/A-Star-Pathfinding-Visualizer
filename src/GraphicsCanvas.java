import java.awt.*;

/**
 * A Graphics Canvas, used as the main viewport in the application.
 */
public class GraphicsCanvas extends Canvas {
    
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
        g.setColor(Color.RED);
        g.fillOval(75, 75, 150, 75);
    }

}
