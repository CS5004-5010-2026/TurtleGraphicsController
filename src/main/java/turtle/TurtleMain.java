package turtle;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Main entry point for Turtle Graphics application.
 */
public class TurtleMain {
    public static void main(String[] args) throws Exception {
        TurtleModel model = new TurtleModel();
        TurtleView view = new TurtleView(model, new OutputStreamWriter(System.out));
        TurtleController controller = new TurtleController(
            model, view,
            new InputStreamReader(System.in),
            new OutputStreamWriter(System.out)
        );
        
        controller.run();
    }
}
