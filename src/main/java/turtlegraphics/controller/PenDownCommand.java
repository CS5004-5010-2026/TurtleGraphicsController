package turtlegraphics.controller;

import turtlegraphics.model.TurtleModel;

/**
 * Command to put the turtle's pen down.
 * 
 * <p>When the pen is down, the turtle draws lines as it moves. This is the default
 * state when the application starts.</p>
 * 
 * <p>The pen state persists across multiple move commands until explicitly changed
 * by a penup command.</p>
 * 
 * <p>Usage: {@code pendown}</p>
 * <p>This command takes no arguments.</p>
 * 
 * @see PenUpCommand
 */
public class PenDownCommand implements Command {
    
    @Override
    public void execute(TurtleModel model, String[] args) throws CommandException {
        // Validate no arguments provided
        if (args.length != 0) {
            throw new CommandException("This command takes no arguments. Usage: " + getUsage());
        }
        
        // Execute pen down
        model.penDown();
    }
    
    @Override
    public String getUsage() {
        return "pendown";
    }
    
    @Override
    public String getDescription() {
        return "Put pen down (turtle draws lines as it moves)";
    }
}
