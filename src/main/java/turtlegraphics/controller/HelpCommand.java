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
        // Validate no arguments provided
        if (args.length != 0) {
            throw new CommandException("This command takes no arguments. Usage: " + getUsage());
        }
        
        // Help command doesn't modify the model, but we need to return help text
        // The controller will handle displaying the help information
        // This is a special case where the command needs to communicate back to the view
        // For now, we'll throw a special exception with the help text
        throw new CommandException(generateHelpText());
    }
    
    /**
     * Generates formatted help text listing all available commands.
     * 
     * @return a formatted string with all command usage and descriptions
     */
    private String generateHelpText() {
        if (commandMap == null || commandMap.isEmpty()) {
            return "No commands available.";
        }
        
        StringBuilder help = new StringBuilder();
        help.append("Available commands:\n");
        help.append("==================\n\n");
        
        // Sort commands alphabetically for consistent display
        commandMap.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                String name = entry.getKey();
                Command cmd = entry.getValue();
                help.append(String.format("%-15s - %s\n", cmd.getUsage(), cmd.getDescription()));
            });
        
        return help.toString();
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
