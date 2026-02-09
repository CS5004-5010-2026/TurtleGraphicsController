package turtlegraphics.controller;

import turtlegraphics.model.TurtleModel;

/**
 * Command to lift the turtle's pen up.
 * 
 * <p>When the pen is up, the turtle moves without drawing lines. This allows the turtle
 * to reposition without leaving a trail.</p>
 * 
 * <p>The pen state persists across multiple move commands until explicitly changed
 * by a pendown command.</p>
 * 
 * <p>Usage: {@code penup}</p>
 * <p>This command takes no arguments.</p>
 * 
 * @see PenDownCommand
 */
public class PenUpCommand implements Command {
    
    @Override
    public void execute(TurtleModel model, String[] args) throws CommandException {
        // TODO: Implement penup command
        // 1. Validate that no arguments are provided
        // 2. Call model.penUp()
        // 3. Throw CommandException if validation fails
        
        throw new UnsupportedOperationException("TODO: Implement PenUpCommand.execute");
    }
    
    @Override
    public String getUsage() {
        return "penup";
    }
    
    @Override
    public String getDescription() {
        return "Lift pen up (turtle moves without drawing)";
    }
}
