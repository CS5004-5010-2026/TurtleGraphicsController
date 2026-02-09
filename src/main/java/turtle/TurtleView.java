package turtle;

import java.io.IOException;

/**
 * View for displaying Turtle Graphics information.
 * Displays turtle status, drawing path, messages, and errors.
 */
public class TurtleView {
    private final TurtleModel model;
    private final Appendable output;
    
    /**
     * Create a new view for the given model and output.
     * 
     * @param model the turtle model to display
     * @param output where to write output
     */
    public TurtleView(TurtleModel model, Appendable output) {
        this.model = model;
        this.output = output;
    }
    
    /**
     * Display the current status of the turtle.
     */
    public void displayStatus() {
        try {
            output.append(String.format("Turtle at (%.1f, %.1f), heading %.1f°, pen %s\n",
                model.getX(),
                model.getY(),
                model.getHeading(),
                model.isPenDown() ? "down" : "up"));
        } catch (IOException e) {
            throw new RuntimeException("Error writing to output", e);
        }
    }
    
    /**
     * Display the complete drawing path.
     */
    public void displayTrace() {
        try {
            if (model.getPath().isEmpty()) {
                output.append("No drawing path yet.\n");
                return;
            }
            
            output.append("Drawing path:\n");
            for (TurtleModel.Line line : model.getPath()) {
                output.append("  ").append(line.toString()).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to output", e);
        }
    }
    
    /**
     * Display a general message.
     * 
     * @param message the message to display
     */
    public void displayMessage(String message) {
        try {
            output.append(message).append("\n");
        } catch (IOException e) {
            throw new RuntimeException("Error writing to output", e);
        }
    }
    
    /**
     * Display an error message.
     * 
     * @param error the error message to display
     */
    public void displayError(String error) {
        try {
            output.append("ERROR: ").append(error).append("\n");
        } catch (IOException e) {
            throw new RuntimeException("Error writing to output", e);
        }
    }
}
