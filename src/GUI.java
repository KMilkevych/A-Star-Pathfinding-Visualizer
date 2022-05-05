import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * GUI Class for managing the Graphical User Interface
 */
public class GUI {

    // Main frame of the application (main window)
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
        JPanel westLayout = makeWestLayout();
        frame.add(westLayout, BorderLayout.WEST);

        JPanel centerLayout = makeCenterLayout();
        frame.add(centerLayout, BorderLayout.CENTER);

        JPanel southLayout = makeSouthLayout();
        frame.add(southLayout, BorderLayout.SOUTH);

        JSeparator eastSpearator = new JSeparator(JSeparator.VERTICAL);
        frame.add(eastSpearator, BorderLayout.EAST);
        
        // Make a menubar for the frame
        JMenuBar menuBar = makeMenuBar();
        frame.setJMenuBar(menuBar);

        // Pack frame and set visible
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Initializes a menu bar for the specified frame
     * @return - the created menubar
     */
    private JMenuBar makeMenuBar() {
        // Create a menubar and assign it to frame
        JMenuBar menubar = new JMenuBar();
        //frame.setJMenuBar(menubar);

        // Create File Menu
        JMenu fileMenu = new JMenu("File");
        menubar.add(fileMenu);

        // Create menu items for File Menu
        //JMenuItem openItem = new JMenuItem("Open");
        //fileMenu.add(openItem);
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(e -> quit());
        fileMenu.add(quitItem);

        // Create Help Menu
        JMenu helpMenu = new JMenu("Help");
        menubar.add(helpMenu);

        // Create menu items for Help Menu
        JMenuItem showHelpItem = new JMenuItem("Show Help");
        showHelpItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Software written by Kostiantyn V. Milkevych.\nSource and Information on GitHub.com/KMilkevych", "Help", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(showHelpItem);

        return menubar;
    }

    /**
     * Creates the west side of the content in the Borderlayout of the specified content pane
     * @return - The created layout packed inside a JPanel
     */
    private JPanel makeWestLayout() {
        // Create a panel to hold the components
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new GridBagLayout());

        // Create GridBagContraints
        GridBagConstraints c = new GridBagConstraints();

        // Make settings panel
        JPanel settingsPanel = makeSettingsSubPanel();
        c.gridy = 0;
        //c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        westPanel.add(settingsPanel, c);

        // Make configuration panel 
        JPanel configurationPanel = makeConfigurationSubPanel();
        c.gridy = 1;
        //c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        westPanel.add(configurationPanel, c);

        return westPanel;
    }

    /**
     * Creates a Settings panel containing controls for all settings, such as vizualization speed, and vizualization type
     * @return - Created settingspanel
     */
    private JPanel makeSettingsSubPanel() {
        // Create a "Settings" panel to hold some components
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridBagLayout());
        settingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Settings"));

        // Create GridBagConstraits
        GridBagConstraints c = new GridBagConstraints();
        
        // Create "Show vizualization:" label
        JLabel showVizualizationLabel = new JLabel("Show vizualization:");
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        settingsPanel.add(showVizualizationLabel, c);
        
        // Create checkbox
        JCheckBox showVizualizationCB = new JCheckBox();
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        settingsPanel.add(showVizualizationCB, c);

        // Create "Vizualization speed:" label
        JLabel vizualizationSpeedLabel = new JLabel("Vizualization speed:");
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        settingsPanel.add(vizualizationSpeedLabel, c);

        // Create slider
        JSlider vizualizationSpeedSlider = new JSlider();
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        settingsPanel.add(vizualizationSpeedSlider, c);

        // Create "Pathfinding algorithm" label
        JLabel pathfindingAlgorithmLabel = new JLabel("Algorithm:");
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        settingsPanel.add(pathfindingAlgorithmLabel, c);
        
        // Add combobox for selection of algorithm
        JComboBox algorithmComboBox = new JComboBox<>(new String[] {"A*", "Breadth First Search", "Dijkstra's Algorithm", "Bellman Ford"});
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.WEST;
        settingsPanel.add(algorithmComboBox, c);

        return settingsPanel;
    }

    /**
     * Creates a Configuration sub panel containing information about current/last simulation, as well as RUN and CLEAR buttons
     * @return - created configurationpanel
     */
    private JPanel makeConfigurationSubPanel() {
        // Create configuration panel
        JPanel configurationPanel = new JPanel();
        configurationPanel.setLayout(new GridBagLayout());
        configurationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Configuration"));

        // Create GridBagConstraints for layout
        GridBagConstraints c = new GridBagConstraints();

        // Create Labels for showing info on startpoint, endpoint, shortest path length and computation time
        JLabel startLabel = new JLabel("Startpoint:");

        JLabel startValLabel = new JLabel("(X, X)");
        
        JLabel endLabel = new JLabel("Endpoint:");

        JLabel endValLabel = new JLabel("(X, X)");

        JLabel shortestPathLabel = new JLabel("Shortest path:");

        JLabel shortestPathValLabel = new JLabel("N/A");

        JLabel computationTimeLabel = new JLabel("Time:");

        JLabel computationTimeValLabel = new JLabel("Xs");

        // Add left-side labels to layout
        c.gridx = 0;
        c.anchor = GridBagConstraints.EAST;

        c.gridy = 0;
        configurationPanel.add(startLabel, c);

        c.gridy = 1;
        configurationPanel.add(endLabel, c);

        c.gridy = 2;
        configurationPanel.add(shortestPathLabel, c);

        c.gridy = 3;
        configurationPanel.add(computationTimeLabel, c);

        // Add right-side labels to layout
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;

        c.gridy = 0;
        configurationPanel.add(startValLabel, c);

        c.gridy = 1;
        configurationPanel.add(endValLabel, c);

        c.gridy = 2;
        configurationPanel.add(shortestPathValLabel, c);

        c.gridy = 3;
        configurationPanel.add(computationTimeValLabel, c);

        // Now create the RUN and CLEAR buttons
        c.gridy = 4;
        JButton runButton = new JButton("Run");
        c.gridx = 0;
        configurationPanel.add(runButton, c);
        
        JButton clearButton = new JButton("Clear");
        c.gridx = 1;
        configurationPanel.add(clearButton, c);

        return configurationPanel;
    }

    /**
     * Creates the center part of the content in the Borderlayout of the specified content pane
     * @return - The created layout packed indside a JPanel
     */
    private JPanel makeCenterLayout() {
        // Create a new panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));

        // Create a sample canvas
        GraphicsCanvas canvas = new GraphicsCanvas();
        //canvas.setSize(600, 400);
        centerPanel.add(canvas);

        return centerPanel;
    }

    /**
     * Creates the south part of the content in the Borderlayout of the specified content pane
     * @return - the created layout packed inside a JPanel
     */
    private JPanel makeSouthLayout() {
        // Create a new panel
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));

        // Add a separator
        southPanel.add(new JSeparator());

        // Create a sub-panel
        JPanel southSubPanel = new JPanel();
        southSubPanel.setLayout(new BoxLayout(southSubPanel, BoxLayout.X_AXIS));
        southPanel.add(southSubPanel);

        // Create text area for output log
        JTextArea outputLog = new JTextArea("Some basic output log", 4, 1);
        southSubPanel.add(outputLog);

        // Add a scrollbar
        JScrollBar scrollBar = new JScrollBar();
        southSubPanel.add(scrollBar);

        return southPanel;
    }

    /**
     * Calmly terminates the application
     */
    private void quit() {
        System.exit(0);
    }
    
}
