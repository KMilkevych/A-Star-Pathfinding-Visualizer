import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI {

    private JFrame frame;

    /**
     * Constructor for GUI class
     */
    public GUI() {
        makeFrame();
    }

    /**
     * Initializes the frame for this class instance
     */
    private void makeFrame() {
        // Create frame
        frame = new JFrame("Algorithm Vizualizer");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Allow application to terminate peacefully when being exited

        // Create content pane
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout(0, 0));

        // "Fill out" the Borderlayout with widgets and stuff
        makeWestLayout(contentPane);
        makeCenterLayout(contentPane);
        makeSouthLayout(contentPane);
        
        // Make a menubar for the frame
        makeMenuBar(frame);

        // Pack frame and set visible
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Initializes a menu bar for the specified frame
     * @param frame - Frame to create menu bar for
     */
    private void makeMenuBar(JFrame frame) {
        // Create a menubar and assign it to frame
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        // Create File Menu
        JMenu fileMenu = new JMenu("File");
        menubar.add(fileMenu);

        // Create menu items for File Menu
        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);
        JMenuItem quitItem = new JMenuItem("Quit");
        fileMenu.add(quitItem);
        quitItem.addActionListener(e -> quit());


        // Create Help Menu
        JMenu helpMenu = new JMenu("Help");
        menubar.add(helpMenu);

        // Create menu items for Help Menu
        JMenuItem showHelpItem = new JMenuItem("Show Help");
        helpMenu.add(showHelpItem);
    }

    /**
     * Creates the west side of the content in the Borderlayout of the specified content pane
     * @param contentPane
     */
    private void makeWestLayout(Container contentPane) {
        // Create a panel to hold the components
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.X_AXIS));
        contentPane.add(westPanel, BorderLayout.WEST);

        // Create a parent panel
        JPanel westSuperPanel = new JPanel();
        westSuperPanel.setLayout(new BoxLayout(westSuperPanel, BoxLayout.Y_AXIS));
        westPanel.add(westSuperPanel);

        // Create a "Settings" label
        JLabel label = new JLabel("Settings");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        westSuperPanel.add(label); 

        // Create a sub panel to hold some components
        JPanel westSubPanel = new JPanel();
        westSubPanel.setLayout(new GridBagLayout());
        westSuperPanel.add(westSubPanel);

        // Create GridBagConstraits
        GridBagConstraints c = new GridBagConstraints();
        
        // Create "Show vizualization:" label
        JLabel showVizualizationLabel = new JLabel("Show vizualization:");
        c.gridx = 0;
        c.gridy = 0;
        westSubPanel.add(showVizualizationLabel, c);
        
        // Create checkbox
        JCheckBox showVizualizationCB = new JCheckBox();
        c.gridx = 1;
        c.gridy = 0;
        westSubPanel.add(showVizualizationCB, c);

        // Create "Vizualization speed:" label
        JLabel vizualizationSpeedLabel = new JLabel("Vizualization speed:");
        c.gridx = 0;
        c.gridy = 1;
        westSubPanel.add(vizualizationSpeedLabel, c);

        // Create slider
        JSlider vizualizationSpeedSlider = new JSlider();
        c.gridx = 1;
        c.gridy = 1;
        westSubPanel.add(vizualizationSpeedSlider, c);

        // Create RUN button
        JButton runButton = new JButton("Run");
        c.gridx = 0;
        c.gridy = 2;
        westSubPanel.add(runButton, c);
        
        // Add combobox for selection of algorithm
        JComboBox algorithmComboBox = new JComboBox<>(new String[] {"A*", "Breadth First Search", "Dijkstra's Algorithm", "Bellman Ford"});
        c.gridx = 1;
        c.gridy = 2;
        westSubPanel.add(algorithmComboBox, c);

        // Add a big CLEAR button
        JButton clearButton = new JButton("Clear");
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        westSuperPanel.add(clearButton);

        // Add separator
        westPanel.add(new JSeparator(SwingConstants.VERTICAL));
    }

    /**
     * Creates the center part of the content in the Borderlayout of the specified content pane
     * @param contentPane
     */
    private void makeCenterLayout(Container contentPane) {
        // Create a new panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        contentPane.add(centerPanel, BorderLayout.CENTER);

        // Add separator
        //centerPanel.add(new JSeparator());

        // Create a sample canvas
        GraphicsCanvas canvas = new GraphicsCanvas();
        //canvas.setSize(600, 400);
        centerPanel.add(canvas); 
    }

    /**
     * Creates the south part of the content in the Borderlayout of the specified content pane
     * @param contentPane
     */
    private void makeSouthLayout(Container contentPane) {
        // Create a new panel
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        contentPane.add(southPanel, BorderLayout.SOUTH);

        // Add a separator
        southPanel.add(new JSeparator());

        // Create a sub-panel
        JPanel southSubPanel = new JPanel();
        southSubPanel.setLayout(new BoxLayout(southSubPanel, BoxLayout.X_AXIS));
        southPanel.add(southSubPanel);

        // Create text area for output log
        JTextArea outputLog = new JTextArea("Some basic output log", 4, 1);
        outputLog.setSize(contentPane.getWidth(), 300);
        southSubPanel.add(outputLog);

        // Add a scrollbar
        JScrollBar scrollBar = new JScrollBar();
        southSubPanel.add(scrollBar);
    }

    /**
     * Calmly terminates the application
     */
    private void quit() {
        System.exit(0);
    }
    
}
