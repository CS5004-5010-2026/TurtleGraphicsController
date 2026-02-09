package turtlegraphics.controller;

import turtlegraphics.model.TurtleModel;

/**
 * Command to move the turtle forward or backward.
 * 
 * <p>The move command takes a single numeric argument representing the distance to move.
 * Positive values move the turtle forward in the direction it's facing, while negative
 * values move it backward.</p>
 * 
 * <p>If the pen is down, a line is drawn from the starting position to the ending position.
 * If the pen is up, the turtle moves without drawing.</p>
 * 
 * <p>Usage: {@code move <distance>}</p>
 * <p>Examples:</p>
 * <ul>
 *   <li>{@code move 100} - Move forward 100 units</li>
 *   <li>{@code move -50} - Move backward 50 units</li>
 *   <li>{@code move 0} - No movement (valid but does nothing)</li>
 * </ul>
 */
public class MoveCommand implements Command {
    
    @Override
    public void execute(TurtleModel model, String[] args) throws CommandException {
        // Validate argument count
        if (args.length != 1) {
            throw new CommandException("Invalid number of arguments. Usage: " + getUsage());
        }
        
        // Parse distance argument
        double distance;
        try {
            distance = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            throw new CommandException("Invalid distance: '" + args[0] + "' is not a valid number", e);
        }
        
        // Execute the move
        model.move(distance);
    }
    
    @Override
    public String getUsage() {
        return "move <distance>";
    }
    
    @Override
    public String getDescription() {
        return "Move turtle forward (positive) or backward (negative) by the specified distance";
    }
}
