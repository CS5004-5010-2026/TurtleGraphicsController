package turtlegraphics.controller;

import turtlegraphics.model.TurtleModel;
import turtlegraphics.view.TurtleView;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller component for the Turtle Graphics application.
 * 
 * <p>The TurtleController mediates between user input (from the View) and the data model.
 * It is responsible for:</p>
 * <ul>
 *   <li>Parsing command strings into command objects and arguments</li>
 *   <li>Validating command syntax and parameters</li>
 *   <li>Executing commands on the TurtleModel</li>
 *   <li>Providing feedback to the TurtleView about command success or failure</li>
 *   <li>Handling errors gracefully without crashing the application</li>
 * </ul>
 * 
 * <p>This class follows the Controller component of the MVC architecture pattern.
 * It has no direct knowledge of GUI components, communicating with the view only
 * through the TurtleView interface.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * TurtleModel model = new TurtleModel();
 * TurtleController controller = new TurtleController(model);
 * TurtleView view = new TurtleView(model, controller);
 * controller.setView(view);
 * 
 * // User enters command
 * controller.executeCommand("move 100");
 * controller.executeCommand("turn 90");
 * </pre>
 * 
 * @see TurtleModel
 * @see TurtleView
 * @see Command
 */
public class TurtleController {
    
    /**
     * The turtle model that this controller operates on.
     */
    private final TurtleModel model;
    
    /**
     * The view that this controller provides feedback to.
     * May be null if not yet set.
     */
    private TurtleView view;
    
    /**
     * Map of command names to command implementations.
     * Populated by registerCommands() in the constructor.
     */
    private final Map<String, Command> commandMap;
    
    /**
     * Constructs a new TurtleController for the given model.
     * 
     * <p>The controller is initialized with all available commands registered
     * and ready to process user input. The view should be set using {@link #setView(TurtleView)}
     * before executing commands that require user feedback.</p>
     * 
     * @param model the turtle model to control (must not be null)
     * @throws NullPointerException if model is null
     */
    public TurtleController(TurtleModel model) {
        if (model == null) {
            throw new NullPointerException("Model cannot be null");
        }
        this.model = model;
        this.commandMap = new HashMap<>();
        registerCommands();
    }
    
    /**
     * Sets the view that this controller will provide feedback to.
     * 
     * <p>This method completes the MVC wiring by connecting the controller to the view.
     * It should be called after both the controller and view have been constructed.</p>
     * 
     * @param view the view to provide feedback to (must not be null)
     * @throws NullPointerException if view is null
     */
    public void setView(TurtleView view) {
        if (view == null) {
            throw new NullPointerException("View cannot be null");
        }
        this.view = view;
    }
    
    /**
     * Executes a command string entered by the user.
     * 
     * <p>The command string is parsed into a command name and arguments (split on whitespace).
     * The command name is looked up in the command map, and if found, the command is executed
     * with the provided arguments.</p>
     * 
     * <p>If the command executes successfully, the view is notified with a success message.
     * If the command fails (invalid command, invalid arguments, or execution error), the view
     * is notified with an error message.</p>
     * 
     * <p>This method never throws exceptions - all errors are caught and reported to the view.</p>
     * 
     * @param commandLine the complete command string entered by the user (e.g., "move 100")
     */
    public void executeCommand(String commandLine) {
        // Handle empty or null input
        if (commandLine == null || commandLine.trim().isEmpty()) {
            if (view != null) {
                view.displayMessage("Please enter a command", true);
            }
            return;
        }
        
        // Parse command line into command name and arguments
        String[] parts = commandLine.trim().split("\\s+");
        String commandName = parts[0].toLowerCase();
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, args.length);
        
        // Look up command in command map
        Command command = commandMap.get(commandName);
        
        if (command == null) {
            // Unknown command
            if (view != null) {
                view.displayMessage("Unknown command: '" + commandName + "'. Type 'help' for available commands.", true);
            }
            return;
        }
        
        // Execute the command
        try {
            command.execute(model, args);
            
            // Notify view of success - add to command history
            if (view != null) {
                if (!commandName.equals("help")) {
                    view.appendCommandHistory("  ✓ Success");
                }
            }
        } catch (CommandException e) {
            // Command execution failed - notify view with error message
            if (view != null) {
                // Special handling for help command which uses exception to return help text
                if (commandName.equals("help")) {
                    view.appendCommandHistory("");
                    view.appendCommandHistory(e.getMessage());
                    view.appendCommandHistory("");
                } else {
                    view.appendCommandHistory("  ✗ Error: " + e.getMessage());
                }
            }
        } catch (NumberFormatException e) {
            // Number parsing failed
            if (view != null) {
                view.appendCommandHistory("  ✗ Error: Invalid number format in arguments");
            }
        } catch (Exception e) {
            // Unexpected error - log and notify view
            System.err.println("Unexpected error executing command: " + e.getMessage());
            e.printStackTrace();
            if (view != null) {
                view.appendCommandHistory("  ✗ Unexpected error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Registers all available commands with the controller.
     * 
     * <p>This method populates the commandMap with instances of all command implementations.
     * Commands are registered with their command name as the key (e.g., "move", "turn").</p>
     * 
     * <p>This method is called by the constructor and should not be called externally.</p>
     */
    private void registerCommands() {
        // Register all command implementations
        commandMap.put("move", new MoveCommand());
        commandMap.put("turn", new TurnCommand());
        commandMap.put("penup", new PenUpCommand());
        commandMap.put("pendown", new PenDownCommand());
        commandMap.put("clear", new ClearCommand());
        commandMap.put("reset", new ResetCommand());
        commandMap.put("quit", new QuitCommand());
        
        // Register help command with reference to command map
        HelpCommand helpCommand = new HelpCommand();
        helpCommand.setCommandMap(commandMap);
        commandMap.put("help", helpCommand);
    }
}
