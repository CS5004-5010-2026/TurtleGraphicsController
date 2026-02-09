package turtlegraphics.controller;

import turtlegraphics.model.TurtleModel;

import java.util.Map;

/**
 * Command to display help information about available commands.
 * 
 * <p>This command lists all available commands with their usage and descriptions,
 * helping users understand how to interact with the turtle graphics application.</p>
 * 
 * <p>The help information is obtained from the controller's command map, ensuring
 * it stays synchronized with the actual available commands.</p>
 * 
 * <p>Usage: {@code help}</p>
 * <p>This command takes no arguments.</p>
 */
public class HelpCommand implements Command {
    
    /**
     * Reference to the command map for generating help text.
     * This is set by the controller when registering commands.
     */
    private Map<String, Command> commandMap;
    
    /**
     * Sets the command map for this help command.
     * 
     * <p>This method is called by the controller during command registration
     * to provide access to all available commands.</p>
     * 
     * @param commandMap the map of command names to command implementations
     */
    public void setCommandMap(Map<String, Command> commandMap) {
        this.commandMap = commandMap;
    }
    
    @Override
    public void execute(TurtleModel model, String[] args) throws CommandException {
        // TODO: Implement help command
        // 1. Validate that no arguments are provided
        // 2. Generate help text from commandMap
        // 3. Throw CommandException with help text (special case for help command)
        
        throw new UnsupportedOperationException("TODO: Implement HelpCommand.execute");
    }
    
    /**
     * Generates formatted help text listing all available commands.
     * 
     * @return a formatted string with all command usage and descriptions
     */
    private String generateHelpText() {
        // TODO: Implement help text generation
        // 1. Check if commandMap is null or empty
        // 2. Build formatted string with all commands
        // 3. Sort commands alphabetically
        // 4. Format as: "usage - description"
        
        throw new UnsupportedOperationException("TODO: Implement generateHelpText");
    }
    
    @Override
    public String getUsage() {
        return "help";
    }
    
    @Override
    public String getDescription() {
        return "Display this help message";
    }
}
