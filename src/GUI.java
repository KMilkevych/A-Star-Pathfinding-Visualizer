import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.plaf.InsetsUIResource;
/**
 * GUI Class for managing the Graphical User Interface of the application.
 */
public class GUI {

    // Main frame of the application (main window)
    private JFrame frame;

    // Component references that are necessary for communication between components
    private GraphicsCanvas graphicsCanvas;

    private JCheckBox showVizualizationCheckbox;
    private JCheckBox enableDiagonalsCB;
    private JSlider vizualizationSpeedSlider;
    private JComboBox algorithmComboBox;

    private JLabel startPointLabel;
    private JLabel endPointLabel;
    private JLabel shortestPathLabel;
    private JLabel computationalTimeLabel;

    private JTextArea outputLog;

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
        Component westLayout = makeWestLayout();
        frame.add(westLayout, BorderLayout.WEST);

        Component southLayout = makeSouthLayout();
        frame.add(southLayout, BorderLayout.SOUTH);

        Component centerLayout = makeCenterLayout();
        frame.add(centerLayout, BorderLayout.CENTER);

        Component eastLayout = makeEastLayout();
        frame.add(eastLayout, BorderLayout.EAST);
        
        // Make a menubar for the frame
        JMenuBar menuBar = makeMenuBar();
        frame.setJMenuBar(menuBar);

        // Pack frame and set visible
        frame.pack();
        frame.setVisible(true);
        frame.setMinimumSize(frame.getPreferredSize());
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
     * @return - The created layout packed inside a Component
     */
    private Component makeWestLayout() {
        // Create a panel to hold the components
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new GridBagLayout());

        // Create GridBagContraints
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new InsetsUIResource(10, 10, 10, 10);

        // Make settings panel
        Component settingsPanel = makeSettingsSubPanel();
        c.gridy = 0;
        //c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        westPanel.add(settingsPanel, c);

        // Make configuration panel 
        Component configurationPanel = makeConfigurationSubPanel();
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
    private Component makeSettingsSubPanel() {
        // Create a "Settings" panel to hold some components
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridBagLayout());
        settingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Settings"));

        // Create GridBagConstraits
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new InsetsUIResource(5, 10, 5, 5);
        
        // Create "Show vizualization:" label
        JLabel showVizualizationLabel = new JLabel("Show vizualization:");
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        settingsPanel.add(showVizualizationLabel, c);
        
        // Create checkbox
        JCheckBox showVizualizationCB = new JCheckBox();
        showVizualizationCB.setSelected(true);
        showVizualizationCB.addActionListener(e -> graphicsCanvas.setShowVizualization());
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        settingsPanel.add(showVizualizationCB, c);
        this.showVizualizationCheckbox = showVizualizationCB;

        // Create "Enable diagonals: " label
        JLabel enableDiagonalsLabel = new JLabel("Enable diagonals:");
        c.gridx = 0;
        c.gridy = 1;
        c.anchor =  GridBagConstraints.EAST;
        settingsPanel.add(enableDiagonalsLabel, c);

        // Create checkbox
        JCheckBox enableDiagonalsCB = new JCheckBox();
        enableDiagonalsCB.setSelected(false);
        enableDiagonalsCB.addActionListener(e -> graphicsCanvas.setEnableDiagonals());
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        settingsPanel.add(enableDiagonalsCB, c);
        this.enableDiagonalsCB = enableDiagonalsCB;

        // Create "Vizualization speed:" label
        JLabel vizualizationSpeedLabel = new JLabel("Vizualization speed:");
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        settingsPanel.add(vizualizationSpeedLabel, c);

        // Create slider
        JSlider vizualizationSpeedSlider = new JSlider(10, 100, 50);
        vizualizationSpeedSlider.addChangeListener(e -> graphicsCanvas.updateTimer());
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.WEST;
        settingsPanel.add(vizualizationSpeedSlider, c);
        this.vizualizationSpeedSlider = vizualizationSpeedSlider;

        // Create "Pathfinding algorithm" label
        JLabel pathfindingAlgorithmLabel = new JLabel("Algorithm:");
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.EAST;
        settingsPanel.add(pathfindingAlgorithmLabel, c);
        
        // Add combobox for selection of algorithm
        JComboBox algorithmComboBox = new JComboBox<>(new String[] {"A*", "Breadth First Search"});
        algorithmComboBox.addActionListener(e -> graphicsCanvas.updateComputationalMethod());
        c.gridx = 1;
        c.gridy = 3;
        c.anchor = GridBagConstraints.WEST;
        settingsPanel.add(algorithmComboBox, c);
        this.algorithmComboBox = algorithmComboBox;

        return settingsPanel;
    }

    /**
     * Creates a Configuration sub panel containing information about current/last simulation, as well as RUN and CLEAR buttons
     * @return - created configurationpanel
     */
    private Component makeConfigurationSubPanel() {
        // Create configuration panel
        JPanel configurationPanel = new JPanel();
        configurationPanel.setLayout(new GridBagLayout());
        configurationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Configuration"));

        // Create GridBagConstraints for layout
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new InsetsUIResource(5, 5, 5, 5);

        // Create Labels for showing info on startpoint, endpoint, shortest path length and computation time
        JLabel startLabel = new JLabel("Startpoint:");

        JLabel startValLabel = new JLabel("NOT SET");
        this.startPointLabel = startValLabel;
        
        JLabel endLabel = new JLabel("Endpoint:");

        JLabel endValLabel = new JLabel("NOT SET");
        this.endPointLabel = endValLabel;

        JLabel shortestPathLabel = new JLabel("Shortest path:");

        JLabel shortestPathValLabel = new JLabel("N/A");
        this.shortestPathLabel = shortestPathValLabel;

        JLabel computationTimeLabel = new JLabel("Time:");

        JLabel computationTimeValLabel = new JLabel("N/A");
        this.computationalTimeLabel = computationTimeValLabel;

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
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;

        JButton runButton = new JButton("Run");
        runButton.addActionListener(e -> graphicsCanvas.run());
        c.gridx = 0;
        c.anchor = GridBagConstraints.EAST;
        configurationPanel.add(runButton, c);
        
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> graphicsCanvas.reset());
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;
        configurationPanel.add(clearButton, c);

        return configurationPanel;
    }

    /**
     * Creates the center part of the content in the Borderlayout of the specified content pane
     * @return - The created layout packed indside a Component
     */
    private Component makeCenterLayout() {
        // Create a sample canvas
        GraphicsCanvas canvas = new GraphicsCanvas(showVizualizationCheckbox, enableDiagonalsCB, vizualizationSpeedSlider, algorithmComboBox, startPointLabel, endPointLabel, shortestPathLabel, computationalTimeLabel, outputLog);
        canvas.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                canvas.resized();
            }
        });
        this.graphicsCanvas = canvas;

        return canvas;
    }

    /**
     * Creates the south part of the content in the Borderlayout of the specified content pane
     * @return - the created layout packed inside a Component
     */
    private Component makeSouthLayout() {
        
        // Create text area for output log
        JTextArea outputLog = new JTextArea("Welcome to Pathfinding Vizualizer!\n", 6, 1);
        outputLog.setEditable(false);
        outputLog.setLineWrap(true);
        /*
        DefaultCaret caret = (DefaultCaret)outputLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        */
        this.outputLog = outputLog;

        // Create a JScrollPane to house outputLog
        JScrollPane logPanel = new JScrollPane(outputLog);

        return logPanel;
    }

    private Component makeEastLayout() {

        // Create a panel for containt components
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.X_AXIS));

        // Create a vertical separator
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        eastPanel.add(separator);

        // Create a toolbar
        JToolBar toolbar = new JToolBar(JToolBar.VERTICAL);
        eastPanel.add(toolbar);

        // Create four buttons for toolbar
        JButton whiteButton = new JButton(createImageIcon(Color.LIGHT_GRAY, 32, 32));
        whiteButton.addActionListener(e -> graphicsCanvas.setMode(Mode.FREEPLACE));
        whiteButton.setToolTipText("Erase Node");
        toolbar.add(whiteButton);

        JButton redButton = new JButton(createImageIcon(Color.RED, 32, 32));
        redButton.addActionListener(e -> graphicsCanvas.setMode(Mode.STARTPLACE));
        redButton.setToolTipText("Start Node");
        toolbar.add(redButton);

        JButton blueButton = new JButton(createImageIcon(Color.BLUE, 32, 32));
        blueButton.addActionListener(e -> graphicsCanvas.setMode(Mode.ENDPLACE));
        blueButton.setToolTipText("End Node");
        toolbar.add(blueButton);

        JButton blackButton = new JButton(createImageIcon(Color.BLACK, 32, 32));
        blackButton.addActionListener(e -> graphicsCanvas.setMode(Mode.WALLPLACE));
        blackButton.setToolTipText("Wall Node");
        toolbar.add(blackButton);

        // Return created eastPanel
        return eastPanel;
    }

    private ImageIcon createImageIcon(Color color, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        return new ImageIcon(img);
    }

    /**
     * Calmly terminates the application 
     */
    private void quit() {
        System.exit(0);
    }
    
}
