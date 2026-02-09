package turtle;

import org.junit.jupiter.api.Test;
import java.io.StringReader;

/**
 * Demo test showing the Turtle Graphics program in action.
 */
class TurtleDemoTest {
    
    @Test
    void demonstrateDrawingSquare() throws Exception {
        // Commands to draw a square
        String input = "move 100\nturn 90\nmove 100\nturn 90\nmove 100\nturn 90\nmove 100\ntrace\nquit\n";
        Readable in = new StringReader(input);
        StringBuilder out = new StringBuilder();
        
        TurtleModel model = new TurtleModel();
        TurtleView view = new TurtleView(model, out);
        TurtleController controller = new TurtleController(model, view, in, out);
        
        controller.run();
        
        // Print the output to see what happened
        System.out.println("\n=== TURTLE GRAPHICS DEMO: Drawing a Square ===");
        System.out.println(out.toString());
        System.out.println("=== END DEMO ===\n");
    }
    
    @Test
    void demonstrateTriangle() throws Exception {
        // Commands to draw a triangle
        String input = "move 100\nturn 120\nmove 100\nturn 120\nmove 100\ntrace\nquit\n";
        Readable in = new StringReader(input);
        StringBuilder out = new StringBuilder();
        
        TurtleModel model = new TurtleModel();
        TurtleView view = new TurtleView(model, out);
        TurtleController controller = new TurtleController(model, view, in, out);
        
        controller.run();
        
        // Print the output to see what happened
        System.out.println("\n=== TURTLE GRAPHICS DEMO: Drawing a Triangle ===");
        System.out.println(out.toString());
        System.out.println("=== END DEMO ===\n");
    }
}
