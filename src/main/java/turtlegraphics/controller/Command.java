package turtlegraphics.controller;

import turtlegraphics.model.TurtleModel;

/**
 * Interface for turtle graphics commands.
 * 
 * <p>This interface follows the Command pattern, encapsulating each turtle operation
 * as an executable object. Each command implementation knows how to parse its arguments
 * and execute the appropriate operations on the TurtleModel.</p>
 * 
 * <p>Commands are registered with the TurtleController and invoked when the user
 * enters the corresponding command text.</p>
 * 
 * <p>Example implementation:</p>
 * <pre>
 * public class MoveCommand implements Command {
 *     {@literal @}Override
 *     public void execute(TurtleModel model, String[] args) throws CommandException {
 *         if (args.length != 1) {
 *             throw new CommandException("Usage: " + getUsage());
 *         }
 *         double distance = Double.parseDouble(args[0]);
 *         model.move(distance);
 *     }
 *     
 *     {@literal @}Override
 *     public String getUsage() {
 *         return "move &lt;distance&gt;";
 *     }
 *     
 *     {@literal @}Override
 *     public String getDescription() {
 *         return "Move turtle forward (positive) or backward (negative)";
 *     }
 * }
 * </pre>
 * 
 * @see TurtleController
 * @see CommandException
 */
public interface Command {
    
    /**
     * Executes this command on the given turtle model with the provided arguments.
     * 
     * <p>Implementations should:</p>
     * <ul>
     *   <li>Validate the number and types of arguments</li>
     *   <li>Parse arguments into appropriate types (e.g., String to double)</li>
     *   <li>Execute the appropriate operations on the model</li>
     *   <li>Throw CommandException for any validation or execution errors</li>
     * </ul>
     * 
     * @param model the turtle model to operate on (never null)
     * @param args the command arguments (may be empty array, never null)
     * @throws CommandException if the command cannot be executed due to invalid arguments
     *                          or other execution errors
     */
    void execute(TurtleModel model, String[] args) throws CommandException;
    
    /**
     * Returns the usage string for this command.
     * 
     * <p>The usage string should show the command name and any required parameters
     * in a format that helps users understand how to use the command.</p>
     * 
     * <p>Examples:</p>
     * <ul>
     *   <li>"move &lt;distance&gt;"</li>
     *   <li>"turn &lt;angle&gt;"</li>
     *   <li>"penup" (no parameters)</li>
     * </ul>
     * 
     * @return a string describing how to use this command
     */
    String getUsage();
    
    /**
     * Returns a human-readable description of what this command does.
     * 
     * <p>The description should be concise but informative, helping users
     * understand the purpose and behavior of the command.</p>
     * 
     * <p>Examples:</p>
     * <ul>
     *   <li>"Move turtle forward (positive) or backward (negative)"</li>
     *   <li>"Rotate turtle by specified angle (positive = counterclockwise)"</li>
     *   <li>"Lift pen up (stop drawing)"</li>
     * </ul>
     * 
     * @return a string describing what this command does
     */
    String getDescription();
}
