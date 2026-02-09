package turtle;

import org.junit.jupiter.api.Test;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TurtleController.
 */
class TurtleControllerTest {
    
    @Test
    void testMoveCommand() throws Exception {
        String input = "move 100\nquit\n";
        Readable in = new StringReader(input);
        StringBuilder out = new StringBuilder();
        
        TurtleModel model = new TurtleModel();
        TurtleView view = new TurtleView(model, out);
        TurtleController controller = new TurtleController(model, view, in, out);
        
        controller.run();
        
        String output = out.toString();
        assertTrue(output.contains("100.0"), "Output should contain turtle position");
        assertTrue(output.contains("Goodbye"), "Output should contain goodbye message");
    }
    
    @Test
    void testTurnCommand() throws Exception {
        String input = "turn 90\nquit\n";
        Readable in = new StringReader(input);
        StringBuilder out = new StringBuilder();
        
        TurtleModel model = new TurtleModel();
        TurtleView view = new TurtleView(model, out);
        TurtleController controller = new TurtleController(model, view, in, out);
        
        controller.run();
        
        String output = out.toString();
        assertTrue(output.contains("90.0"), "Output should contain heading");
    }
    
    @Test
    void testPenUpCommand() throws Exception {
        String input = "penup\nquit\n";
        Readable in = new StringReader(input);
        StringBuilder out = new StringBuilder();
        
        TurtleModel model = new TurtleModel();
        TurtleView view = new TurtleView(model, out);
        TurtleController controller = new TurtleController(model, view, in, out);
        
        controller.run();
        
        String output = out.toString();
        assertTrue(output.contains("pen up"), "Output should show pen is up");
    }
    
    @Test
    void testTraceCommand() throws Exception {
        String input = "move 50\nmove 50\ntrace\nquit\n";
        Readable in = new StringReader(input);
        StringBuilder out = new StringBuilder();
        
        TurtleModel model = new TurtleModel();
        TurtleView view = new TurtleView(model, out);
        TurtleController controller = new TurtleController(model, view, in, out);
        
        controller.run();
        
        String output = out.toString();
        assertTrue(output.contains("Drawing path"), "Output should contain drawing path");
    }
    
    @Test
    void testUnknownCommand() throws Exception {
        String input = "invalid\nquit\n";
        Readable in = new StringReader(input);
        StringBuilder out = new StringBuilder();
        
        TurtleModel model = new TurtleModel();
        TurtleView view = new TurtleView(model, out);
        TurtleController controller = new TurtleController(model, view, in, out);
        
        controller.run();
        
        String output = out.toString();
        assertTrue(output.contains("ERROR"), "Output should contain error message");
        assertTrue(output.contains("Unknown command"), "Output should mention unknown command");
    }
    
    @Test
    void testMissingArgument() throws Exception {
        String input = "move\nquit\n";
        Readable in = new StringReader(input);
        StringBuilder out = new StringBuilder();
        
        TurtleModel model = new TurtleModel();
        TurtleView view = new TurtleView(model, out);
        TurtleController controller = new TurtleController(model, view, in, out);
        
        controller.run();
        
        String output = out.toString();
        assertTrue(output.contains("ERROR"), "Output should contain error message");
    }
}
