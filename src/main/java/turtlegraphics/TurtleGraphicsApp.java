package turtlegraphics;

import turtlegraphics.controller.TurtleController;
import turtlegraphics.model.TurtleModel;
import turtlegraphics.view.TurtleView;

import javax.swing.SwingUtilities;

/**
 * Main entry point for the Turtle Graphics GUI application.
 * 
 * <p>This application demonstrates the Model-View-Controller (MVC) architectural pattern
 * through an interactive turtle graphics environment. Users control a visible turtle on
 * a graphical canvas using text commands, creating drawings in real-time.</p>
 * 
 * <p>The application consists of three primary components:</p>
 * <ul>
 *   <li><b>Model</b>: Maintains turtle state (position, heading, pen state) and drawing history</li>
 *   <li><b>View</b>: Renders the canvas, turtle, and user interface using Java Swing</li>
 *   <li><b>Controller</b>: Processes text commands and coordinates model updates with view refreshes</li>
 * </ul>
 * 
 * <p>To run the application:</p>
 * <pre>
 * ./gradlew runGUI
 * </pre>
 * 
 * @author CS 5004/5010 Course Staff
 * @version 1.0.0
 */
public class TurtleGraphicsApp {
    
    /**
     * Main method to launch the Turtle Graphics GUI application.
     * 
     * <p>This method:</p>
     * <ol>
     *   <li>Creates a TurtleModel instance</li>
     *   <li>Creates a TurtleController instance with the model</li>
     *   <li>Creates a TurtleView instance with the model and controller</li>
     *   <li>Wires the components together by setting the view in the controller</li>
     *   <li>Displays the GUI window on the Event Dispatch Thread</li>
     * </ol>
     * 
     * <p>The GUI is launched on the Event Dispatch Thread to ensure thread safety
     * with Swing components.</p>
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Launch GUI on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            // Create the model
            TurtleModel model = new TurtleModel();
            
            // Create the controller with the model
            TurtleController controller = new TurtleController(model);
            
            // Create the view with the model and controller
            TurtleView view = new TurtleView(model, controller);
            
            // Complete the MVC wiring by setting the view in the controller
            controller.setView(view);
            
            // Display the GUI
            view.setVisible(true);
        });
    }
}

