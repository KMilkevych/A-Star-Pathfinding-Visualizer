import java.awt.*;

/**
 * A Graphics Canvas, used as the main viewport in the application.
 */
public class GraphicsCanvas extends Canvas {
    
    /**
     * Constructor for GraphicsCanvas class.
     */
    public GraphicsCanvas() {
        setBackground(Color.GRAY);
        //setSize(300, 200);
    }

    public void paint(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(75, 75, 150, 75);
    }

}
