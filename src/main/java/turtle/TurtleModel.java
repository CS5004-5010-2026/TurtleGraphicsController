package turtle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model for a Turtle Graphics system.
 * The turtle maintains position, heading, and pen state.
 * When the pen is down, movements are recorded as drawing paths.
 */
public class TurtleModel {
    private double x;
    private double y;
    private double heading; // in degrees, 0 = East, 90 = North
    private boolean penDown;
    private final List<Line> path;
    
    /**
     * Represents a line segment in the drawing.
     */
    public static class Line {
        public final double x1, y1, x2, y2;
        
        public Line(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
        
        @Override
        public String toString() {
            return String.format("(%.1f, %.1f) -> (%.1f, %.1f)", x1, y1, x2, y2);
        }
    }
    
    /**
     * Create a new turtle at origin (0, 0) facing East with pen down.
     */
    public TurtleModel() {
        this.x = 0.0;
        this.y = 0.0;
        this.heading = 0.0;
        this.penDown = true;
        this.path = new ArrayList<>();
    }
    
    /**
     * Move the turtle forward by the given distance.
     * If pen is down, record the line segment.
     * 
     * @param distance the distance to move
     */
    public void move(double distance) {
        double oldX = x;
        double oldY = y;
        
        // Calculate new position based on heading
        // heading is in degrees: 0 = East, 90 = North, 180 = West, 270 = South
        double radians = Math.toRadians(heading);
        x += distance * Math.cos(radians);
        y += distance * Math.sin(radians);
        
        // If pen is down, record the line
        if (penDown) {
            path.add(new Line(oldX, oldY, x, y));
        }
    }
    
    /**
     * Turn the turtle by the given angle.
     * Positive angles turn counterclockwise.
     * 
     * @param angle the angle to turn in degrees
     */
    public void turn(double angle) {
        heading += angle;
        // Normalize to 0-360 range
        heading = heading % 360;
        if (heading < 0) {
            heading += 360;
        }
    }
    
    /**
     * Lift the pen up. Subsequent moves will not draw.
     */
    public void penUp() {
        penDown = false;
    }
    
    /**
     * Put the pen down. Subsequent moves will draw.
     */
    public void penDown() {
        penDown = true;
    }
    
    /**
     * Get the current x position.
     * 
     * @return the x coordinate
     */
    public double getX() {
        return x;
    }
    
    /**
     * Get the current y position.
     * 
     * @return the y coordinate
     */
    public double getY() {
        return y;
    }
    
    /**
     * Get the current heading in degrees.
     * 
     * @return the heading (0 = East, 90 = North, etc.)
     */
    public double getHeading() {
        return heading;
    }
    
    /**
     * Check if the pen is currently down.
     * 
     * @return true if pen is down, false if up
     */
    public boolean isPenDown() {
        return penDown;
    }
    
    /**
     * Get the drawing path as an unmodifiable list.
     * 
     * @return list of line segments drawn
     */
    public List<Line> getPath() {
        return Collections.unmodifiableList(path);
    }
    
    /**
     * Reset the turtle to initial state.
     */
    public void reset() {
        x = 0.0;
        y = 0.0;
        heading = 0.0;
        penDown = true;
        path.clear();
    }
}
