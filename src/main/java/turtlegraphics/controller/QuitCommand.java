package turtlegraphics.controller;

import turtlegraphics.model.TurtleModel;

/**
 * Command to exit the application.
 * 
 * <p>This command terminates the Turtle Graphics application gracefully.
 * The application exits with status code 0 (normal termination).</p>
 * 
 * <p>Note: Drawings are not saved when the application exits. All drawing
 * state is ephemeral and will be lost.</p>
 * 
 * <p>Usage: {@code quit}</p>
 * <p>This command takes no arguments.</p>
 */
public class QuitCommand implements Command {
    
    @Override
    public void execute(TurtleModel model, String[] args) throws CommandException {
        // TODO: Implement quit command
        // 1. Validate that no arguments are provided
        // 2. Call System.exit(0) to terminate the application
        // 3. Throw CommandException if validation fails
        
        throw new UnsupportedOperationException("TODO: Implement QuitCommand.execute");
    }
    
    @Override
    public String getUsage() {
        return "quit";
    }
    
    @Override
    public String getDescription() {
        return "Exit the application";
    }
}
