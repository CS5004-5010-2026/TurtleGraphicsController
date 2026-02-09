package turtlegraphics.view;

import turtlegraphics.controller.TurtleController;
import turtlegraphics.model.ModelObserver;
import turtlegraphics.model.TurtleModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main GUI window for the Turtle Graphics application.
 * 
 * <p>The TurtleView is responsible for:</p>
 * <ul>
 *   <li>Creating and laying out all GUI components (canvas, input field, history, status)</li>
 *   <li>Capturing user command input and forwarding it to the controller</li>
 *   <li>Displaying command history and status messages</li>
 *   <li>Observing the model and refreshing the display when it changes</li>
 *   <li>Handling window close events</li>
 * </ul>
 * 
 * <p>This class implements the View component of the MVC architecture pattern.</p>
 */
public class TurtleView extends JFrame implements ModelObserver {
    
    /**
     * The turtle model being displayed.
     */
    private final TurtleModel model;
    
    /**
     * The controller that processes commands.
     */
    private final TurtleController controller;
    
    /**
     * The canvas panel that renders the turtle and lines.
     */
    private CanvasPanel canvasPanel;
    
    /**
     * Text field for entering commands.
     */
    private JTextField commandInput;
    
    /**
     * Text area displaying command history.
     */
    private JTextArea commandHistory;
    
    /**
     * Label displaying status messages.
     */
    private JLabel statusLabel;
    
    /**
     * Constructs a new TurtleView for the given model and controller.
     * 
     * <p>The view is initialized with all GUI components and registered as an
     * observer of the model. The window is not yet visible - call {@code setVisible(true)}
     * to display it.</p>
     * 
     * @param model the turtle model to display (must not be null)
     * @param controller the controller to process commands (must not be null)
     * @throws NullPointerException if model or controller is null
     */
    public TurtleView(TurtleModel model, TurtleController controller) {
        if (model == null) {
            throw new NullPointerException("Model cannot be null");
        }
        if (controller == null) {
            throw new NullPointerException("Controller cannot be null");
        }
        
        this.model = model;
        this.controller = controller;
        
        // Register as observer of the model
        model.addObserver(this);
        
        // Initialize GUI components
        initializeComponents();
        layoutComponents();
        
        // Set up window properties
        setTitle("Turtle Graphics");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClose();
            }
        });
        
        pack();
        setLocationRelativeTo(null); // Center on screen
        
        // Display welcome message
        appendCommandHistory("=== Turtle Graphics Application ===");
        appendCommandHistory("Type 'help' for available commands");
        appendCommandHistory("");
    }
    
    /**
     * Initializes all GUI components.
     * 
     * <p>Creates the canvas panel, command input field, command history text area,
     * and status label. Sets up action listeners for user input.</p>
     */
    private void initializeComponents() {
        // Create canvas panel
        canvasPanel = new CanvasPanel(model);
        
        // Create command input field
        commandInput = new JTextField(40);
        commandInput.addActionListener(this::handleCommandInput);
        
        // Create command history text area
        commandHistory = new JTextArea(10, 40);
        commandHistory.setEditable(false);
        commandHistory.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        // Create status label
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
    
    /**
     * Lays out all GUI components in the frame.
     * 
     * <p>Uses BorderLayout for the main frame with:</p>
     * <ul>
     *   <li>Canvas on the left</li>
     *   <li>Command history and controls on the right</li>
     * </ul>
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Add canvas to left side
        add(canvasPanel, BorderLayout.CENTER);
        
        // Create right panel for command history and controls
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(400, 600));
        
        // Command history in scrollable pane
        JScrollPane historyScroll = new JScrollPane(commandHistory);
        rightPanel.add(historyScroll, BorderLayout.CENTER);
        
        // Input panel at bottom of right side
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JLabel inputLabel = new JLabel("Command: ");
        inputPanel.add(inputLabel, BorderLayout.WEST);
        inputPanel.add(commandInput, BorderLayout.CENTER);
        
        rightPanel.add(inputPanel, BorderLayout.SOUTH);
        
        add(rightPanel, BorderLayout.EAST);
    }
    
    /**
     * Handles command input from the user.
     * 
     * <p>Called when the user presses Enter in the command input field.
     * Forwards the command to the controller for execution and updates the
     * command history.</p>
     * 
     * @param event the action event (not used)
     */
    private void handleCommandInput(ActionEvent event) {
        String commandLine = commandInput.getText().trim();
        
        if (!commandLine.isEmpty()) {
            // Display command in history
            appendCommandHistory("> " + commandLine);
            
            // Execute command through controller
            controller.executeCommand(commandLine);
            
            // Clear input field
            commandInput.setText("");
        }
    }
    
    /**
     * Handles window close event.
     * 
     * <p>Exits the application gracefully when the user clicks the window close button.</p>
     */
    private void handleWindowClose() {
        System.exit(0);
    }
    
    @Override
    public void modelChanged() {
        // Repaint the canvas to reflect model changes
        canvasPanel.repaint();
    }
    
    /**
     * Displays a message in the status bar and console.
     * 
     * <p>Messages are color-coded in the GUI:</p>
     * <ul>
     *   <li>Error messages: Red text</li>
     *   <li>Success messages: Black text</li>
     * </ul>
     * 
     * <p>Messages are also printed to the console for debugging.</p>
     * 
     * @param message the message to display
     * @param isError true if this is an error message, false otherwise
     */
    public void displayMessage(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? Color.RED : Color.BLACK);
        
        // Also print to console
        if (isError) {
            System.err.println("[ERROR] " + message);
        } else {
            System.out.println("[INFO] " + message);
        }
    }
    
    /**
     * Appends text to the command history and console.
     * 
     * <p>The text is added to the end of the history text area, and the view
     * is automatically scrolled to show the new text. The text is also printed
     * to the console.</p>
     * 
     * @param text the text to append
     */
    public void appendCommandHistory(String text) {
        commandHistory.append(text + "\n");
        
        // Auto-scroll to bottom
        commandHistory.setCaretPosition(commandHistory.getDocument().getLength());
        
        // Also print to console
        System.out.println(text);
    }
}
