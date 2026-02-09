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
        // TODO: Implement move command
        // 1. Validate that exactly 1 argument is provided
        // 2. Parse the argument as a double (distance)
        // 3. Call model.move(distance)
        // 4. Throw CommandException if validation fails
        
        throw new UnsupportedOperationException("TODO: Implement MoveCommand.execute");
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
