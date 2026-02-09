package turtlegraphics.controller;

import turtlegraphics.model.TurtleModel;

/**
 * Command to rotate the turtle by a specified angle.
 * 
 * <p>The turn command takes a single numeric argument representing the angle to rotate in degrees.
 * Positive values rotate counterclockwise, while negative values rotate clockwise.</p>
 * 
 * <p>The turtle's heading is automatically normalized to the range [0, 360) after rotation.
 * This command does not affect the turtle's position or pen state.</p>
 * 
 * <p>Usage: {@code turn <angle>}</p>
 * <p>Examples:</p>
 * <ul>
 *   <li>{@code turn 90} - Rotate 90 degrees counterclockwise (left)</li>
 *   <li>{@code turn -45} - Rotate 45 degrees clockwise (right)</li>
 *   <li>{@code turn 360} - Complete rotation (returns to same heading)</li>
 * </ul>
 */
public class TurnCommand implements Command {
    
    @Override
    public void execute(TurtleModel model, String[] args) throws CommandException {
        // TODO: Implement turn command
        // 1. Validate that exactly 1 argument is provided
        // 2. Parse the argument as a double (angle)
        // 3. Call model.turn(angle)
        // 4. Throw CommandException if validation fails
        
        throw new UnsupportedOperationException("TODO: Implement TurnCommand.execute");
    }
    
    @Override
    public String getUsage() {
        return "turn <angle>";
    }
    
    @Override
    public String getDescription() {
        return "Rotate turtle by specified angle in degrees (positive = counterclockwise, negative = clockwise)";
    }
}
