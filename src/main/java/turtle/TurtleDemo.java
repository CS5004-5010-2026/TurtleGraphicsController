package turtle;

import java.io.StringReader;

/**
 * Demo program showing Turtle Graphics in action.
 */
public class TurtleDemo {
    public static void main(String[] args) throws Exception {
        System.out.println("=== TURTLE GRAPHICS DEMO: Drawing a Square ===\n");
        
        // Commands to draw a square
        String input = "move 100\nturn 90\nmove 100\nturn 90\nmove 100\nturn 90\nmove 100\ntrace\nquit\n";
        
        // Create model, view, and controller
        TurtleModel model = new TurtleModel();
        StringBuilder output = new StringBuilder();
        TurtleView view = new TurtleView(model, output);
        TurtleController controller = new TurtleController(
            model, view,
            new StringReader(input),
            output
        );
        
        // Run the controller
        controller.run();
        
        // Print the output
        System.out.println(output.toString());
        
        System.out.println("\n=== The turtle drew a square! ===");
    }
}
