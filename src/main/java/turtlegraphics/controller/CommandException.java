package turtlegraphics.controller;

/**
 * Exception thrown when a command cannot be executed.
 * 
 * <p>This exception is used to signal various command processing errors, including:</p>
 * <ul>
 *   <li>Unrecognized command names</li>
 *   <li>Invalid number of arguments</li>
 *   <li>Invalid argument types (e.g., non-numeric value where number expected)</li>
 *   <li>Invalid argument values (e.g., out of range)</li>
 *   <li>Command execution failures</li>
 * </ul>
 * 
 * <p>The exception message should provide clear feedback to the user about what
 * went wrong and how to correct it.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * if (args.length != 1) {
 *     throw new CommandException("Invalid number of arguments. Usage: move &lt;distance&gt;");
 * }
 * 
 * try {
 *     double distance = Double.parseDouble(args[0]);
 * } catch (NumberFormatException e) {
 *     throw new CommandException("Invalid distance: must be a number");
 * }
 * </pre>
 * 
 * @see Command
 * @see TurtleController
 */
public class CommandException extends Exception {
    
    /**
     * Constructs a new CommandException with the specified error message.
     * 
     * <p>The message should clearly describe what went wrong and ideally
     * provide guidance on how to correct the error.</p>
     * 
     * @param message the error message describing why the command failed
     */
    public CommandException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new CommandException with the specified error message and cause.
     * 
     * <p>This constructor is useful when wrapping another exception (e.g., NumberFormatException)
     * to provide more context about the command processing error.</p>
     * 
     * @param message the error message describing why the command failed
     * @param cause the underlying exception that caused this command to fail
     */
    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
