package turtle;

import java.io.IOException;
import java.util.Scanner;

/**
 * Controller for Turtle Graphics application.
 * Parses commands from input and coordinates Model and View.
 */
public class TurtleController {
    private final TurtleModel model;
    private final TurtleView view;
    private final Readable input;
    private final Appendable output;
    
    /**
     * Create a new controller.
     * 
     * @param model the turtle model
     * @param view the turtle view
     * @param input where to read commands from
     * @param output where to write output to
     */
    public TurtleController(TurtleModel model, TurtleView view, 
                           Readable input, Appendable output) {
        this.model = model;
        this.view = view;
        this.input = input;
        this.output = output;
    }
    
    /**
     * Run the controller loop.
     * Reads commands and executes them until 'quit' is entered.
     */
    public void run() throws IOException {
        Scanner scanner = new Scanner(input);
        output.append("Turtle Graphics Controller\n");
        output.append("Type 'quit' to exit\n\n");
        
        while (scanner.hasNext()) {
            output.append("> ");
            String command = scanner.next();
            
            try {
                switch (command.toLowerCase()) {
                    case "move":
                        if (!scanner.hasNextDouble()) {
                            view.displayError("move requires a distance argument");
                            scanner.nextLine(); // consume rest of line
                            break;
                        }
                        double distance = scanner.nextDouble();
                        model.move(distance);
                        view.displayStatus();
                        break;
                        
                    case "turn":
                        if (!scanner.hasNextDouble()) {
                            view.displayError("turn requires an angle argument");
                            scanner.nextLine(); // consume rest of line
                            break;
                        }
                        double angle = scanner.nextDouble();
                        model.turn(angle);
                        view.displayStatus();
                        break;
                        
                    case "penup":
                        model.penUp();
                        view.displayStatus();
                        break;
                        
                    case "pendown":
                        model.penDown();
                        view.displayStatus();
                        break;
                        
                    case "trace":
                        view.displayTrace();
                        break;
                        
                    case "quit":
                        output.append("Goodbye!\n");
                        return;
                        
                    default:
                        view.displayError("Unknown command: " + command);
                        scanner.nextLine(); // consume rest of line
                        break;
                }
            } catch (Exception e) {
                view.displayError("Error: " + e.getMessage());
                scanner.nextLine(); // consume rest of line
            }
        }
    }
}
