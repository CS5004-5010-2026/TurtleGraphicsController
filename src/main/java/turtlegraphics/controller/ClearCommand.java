package turtlegraphics.controller;

import turtlegraphics.model.TurtleModel;

/**
 * Command to clear all drawn lines from the canvas.
 * 
 * <p>This command removes all lines from the drawing history, effectively clearing
 * the canvas. However, it does NOT modify the turtle's current state - the turtle's
 * position, heading, and pen state remain unchanged.</p>
 * 
 * <p>After clearing, the turtle can continue drawing from its current position.</p>
 * 
 * <p>Usage: {@code clear}</p>
 * <p>This command takes no arguments.</p>
 * 
 * @see ResetCommand
 */
public class ClearCommand implements Command {
    
    @Override
    public void execute(TurtleModel model, String[] args) throws CommandException {
        // TODO: Implement clear command
        // 1. Validate that no arguments are provided
        // 2. Call model.clear()
        // 3. Throw CommandException if validation fails
        
        throw new UnsupportedOperationException("TODO: Implement ClearCommand.execute");
    }
    
    @Override
    public String getUsage() {
        return "clear";
    }
    
    @Override
    public String getDescription() {
        return "Clear all drawn lines from canvas (turtle position and state unchanged)";
    }
}
