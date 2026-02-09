package turtlegraphics.controller;

import turtlegraphics.model.TurtleModel;

/**
 * Command to reset the turtle to its initial state.
 * 
 * <p>This command returns the turtle to the state it had when the application started:</p>
 * <ul>
 *   <li>Position is set to (0, 0) - the center of the canvas</li>
 *   <li>Heading is set to 0° - facing right</li>
 *   <li>Pen state is set to down - ready to draw</li>
 *   <li>All drawn lines are cleared from the canvas</li>
 * </ul>
 * 
 * <p>This is useful for starting a new drawing from scratch.</p>
 * 
 * <p>Usage: {@code reset}</p>
 * <p>This command takes no arguments.</p>
 * 
 * @see ClearCommand
 */
public class ResetCommand implements Command {
    
    @Override
    public void execute(TurtleModel model, String[] args) throws CommandException {
        // Validate no arguments provided
        if (args.length != 0) {
            throw new CommandException("This command takes no arguments. Usage: " + getUsage());
        }
        
        // Execute reset
        model.reset();
    }
    
    @Override
    public String getUsage() {
        return "reset";
    }
    
    @Override
    public String getDescription() {
        return "Reset turtle to initial state (center, facing right, pen down) and clear canvas";
    }
}
