package turtlegraphics.model;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The TurtleModel class represents the core data model for the turtle graphics application.
 * It maintains the turtle's state including position, heading (direction), pen state,
 * and the history of all drawn lines.
 * 
 * <p>The turtle operates in a Cartesian coordinate system with the origin at the center
 * of the canvas. The heading is measured in degrees where 0° points right (positive X-axis),
 * 90° points up (positive Y-axis), 180° points left, and 270° points down.</p>
 * 
 * <p>This class follows the Model component of the MVC architecture and has no dependencies
 * on View or Controller components.</p>
 */
public class TurtleModel {
    
    /**
     * The current position of the turtle in model coordinates.
     * The origin (0, 0) is at the center of the canvas.
     */
    private Point2D.Double position;
    
    /**
     * The current heading (direction) of the turtle in degrees.
     * 0° = right, 90° = up, 180° = left, 270° = down.
     * Always normalized to the range [0, 360).
     */
    private double heading;
    
    /**
     * The state of the turtle's pen.
     * When true, the turtle draws lines as it moves.
     * When false, the turtle moves without drawing.
     */
    private boolean penDown;
    
    /**
     * The list of all lines drawn by the turtle.
     * Lines are stored in the order they were drawn.
     */
    private List<Line2D.Double> lines;
    
    /**
     * The list of observers that are notified when the model state changes.
     * Observers are typically View components that need to update their display
     * when the turtle's state changes.
     */
    private List<ModelObserver> observers;
    
    /**
     * Constructs a new TurtleModel with the turtle positioned at the center (0, 0),
     * facing right (0° heading), with the pen down.
     * 
     * <p>This represents the initial state of the turtle when the application starts.</p>
     */
    public TurtleModel() {
        this.position = new Point2D.Double(0.0, 0.0);
        this.heading = 0.0;
        this.penDown = true;
        this.lines = new ArrayList<>();
        this.observers = new ArrayList<>();
    }
    
    /**
     * Returns the current position of the turtle.
     * 
     * <p>Returns a defensive copy to prevent external modification of the turtle's state.</p>
     * 
     * @return a new Point2D.Double representing the turtle's current position
     */
    public Point2D.Double getPosition() {
        return new Point2D.Double(position.x, position.y);
    }
    
    /**
     * Returns the current heading (direction) of the turtle in degrees.
     * 
     * <p>The heading is always in the range [0, 360) where:
     * <ul>
     *   <li>0° = right (positive X-axis)</li>
     *   <li>90° = up (positive Y-axis)</li>
     *   <li>180° = left (negative X-axis)</li>
     *   <li>270° = down (negative Y-axis)</li>
     * </ul>
     * </p>
     * 
     * @return the current heading in degrees [0, 360)
     */
    public double getHeading() {
        return heading;
    }
    
    /**
     * Returns whether the turtle's pen is currently down.
     * 
     * <p>When the pen is down, the turtle draws lines as it moves.
     * When the pen is up, the turtle moves without drawing.</p>
     * 
     * @return true if the pen is down (drawing), false if the pen is up (not drawing)
     */
    public boolean isPenDown() {
        return penDown;
    }
    
    /**
     * Returns an immutable view of all lines drawn by the turtle.
     * 
     * <p>The returned list cannot be modified. Lines are in the order they were drawn.</p>
     * 
     * @return an unmodifiable list of all drawn lines
     */
    public List<Line2D.Double> getLines() {
        return Collections.unmodifiableList(lines);
    }
    
    /**
     * Moves the turtle forward (or backward if distance is negative) by the specified distance
     * in the direction of the current heading.
     * 
     * <p>The new position is calculated using trigonometry:
     * <ul>
     *   <li>newX = currentX + distance * cos(heading * π / 180)</li>
     *   <li>newY = currentY + distance * sin(heading * π / 180)</li>
     * </ul>
     * </p>
     * 
     * <p>If the pen is down, a line is drawn from the old position to the new position.
     * If the pen is up, the turtle moves without drawing.</p>
     * 
     * <p>The turtle can move beyond the canvas boundaries (off-screen movement is allowed).</p>
     * 
     * @param distance the distance to move (positive = forward, negative = backward)
     */
    public void move(double distance) {
        // Store the old position for line drawing
        Point2D.Double oldPosition = new Point2D.Double(position.x, position.y);
        
        // Convert heading from degrees to radians for trigonometric functions
        double headingRadians = Math.toRadians(heading);
        
        // Calculate new position using trigonometry
        double newX = position.x + distance * Math.cos(headingRadians);
        double newY = position.y + distance * Math.sin(headingRadians);
        
        // Update position to new coordinates
        position.x = newX;
        position.y = newY;
        
        // If pen is down, create a line from old position to new position
        if (penDown) {
            Line2D.Double line = new Line2D.Double(oldPosition, position);
            lines.add(line);
        }
        
        // Notify observers of state change
        notifyObservers();
    }
    
    /**
     * Sets the pen state (up or down).
     * 
     * <p>When the pen is down (true), the turtle draws lines as it moves.
     * When the pen is up (false), the turtle moves without drawing.</p>
     * 
     * @param down true to put the pen down (drawing), false to lift the pen up (not drawing)
     */
    public void setPenDown(boolean down) {
        this.penDown = down;
        notifyObservers();
    }
    
    /**
     * Lifts the pen up, so the turtle moves without drawing.
     * 
     * <p>This is a convenience method equivalent to calling {@code setPenDown(false)}.</p>
     * 
     * <p>After calling this method, subsequent move operations will not create lines
     * until {@code penDown()} is called.</p>
     */
    public void penUp() {
        setPenDown(false);
    }
    
    /**
     * Puts the pen down, so the turtle draws lines as it moves.
     * 
     * <p>This is a convenience method equivalent to calling {@code setPenDown(true)}.</p>
     * 
     * <p>After calling this method, subsequent move operations will create visible lines.</p>
     */
    public void penDown() {
        setPenDown(true);
    }
    
    /**
     * Clears all drawn lines from the canvas.
     * 
     * <p>This method removes all lines from the drawing history, effectively clearing
     * the canvas. However, it does NOT modify the turtle's current state:</p>
     * <ul>
     *   <li>Position remains unchanged</li>
     *   <li>Heading remains unchanged</li>
     *   <li>Pen state remains unchanged</li>
     * </ul>
     * 
     * <p>After calling this method, the turtle can continue drawing from its current
     * position and state.</p>
     */
    public void clear() {
        lines.clear();
        notifyObservers();
    }
    
    /**
     * Resets the turtle to its initial state.
     * 
     * <p>This method returns the turtle to the state it had when first created:</p>
     * <ul>
     *   <li>Position is set to (0, 0) - the center of the canvas</li>
     *   <li>Heading is set to 0° - facing right</li>
     *   <li>Pen state is set to down - ready to draw</li>
     *   <li>All drawn lines are cleared from the canvas</li>
     * </ul>
     * 
     * <p>This is equivalent to creating a new TurtleModel instance, but reuses the
     * existing object and notifies any observers of the state change.</p>
     */
    public void reset() {
        this.position.x = 0.0;
        this.position.y = 0.0;
        this.heading = 0.0;
        this.penDown = true;
        this.lines.clear();
        notifyObservers();
    }
    
    /**
     * Rotates the turtle by the specified angle.
     * 
     * <p>Positive angles rotate counterclockwise, negative angles rotate clockwise.
     * The heading is automatically normalized to the range [0, 360) after rotation.</p>
     * 
     * <p>This method does not affect the turtle's position or pen state.</p>
     * 
     * @param angle the angle to rotate in degrees (positive = counterclockwise, negative = clockwise)
     */
    public void turn(double angle) {
        this.heading = normalizeHeading(this.heading + angle);
        notifyObservers();
    }
    
    /**
     * Normalizes an angle to the range [0, 360).
     * 
     * <p>This method handles:
     * <ul>
     *   <li>Angles greater than or equal to 360: reduced by multiples of 360</li>
     *   <li>Negative angles: converted to equivalent positive angles</li>
     *   <li>Angles already in range: returned unchanged</li>
     * </ul>
     * </p>
     * 
     * <p>Examples:
     * <ul>
     *   <li>370° → 10°</li>
     *   <li>-30° → 330°</li>
     *   <li>720° → 0°</li>
     *   <li>180° → 180° (unchanged)</li>
     * </ul>
     * </p>
     * 
     * @param angle the angle to normalize in degrees
     * @return the normalized angle in the range [0, 360)
     */
    private double normalizeHeading(double angle) {
        // Use modulo to get remainder when divided by 360
        double normalized = angle % 360.0;
        
        // If negative, add 360 to get positive equivalent
        if (normalized < 0) {
            normalized += 360.0;
        }
        
        return normalized;
    }
    
    /**
     * Registers an observer to be notified of model state changes.
     * 
     * <p>The observer's {@code modelChanged()} method will be called whenever
     * the turtle's state changes (position, heading, pen state, or lines).</p>
     * 
     * <p>Multiple observers can be registered. Each will be notified in the
     * order they were added.</p>
     * 
     * @param observer the observer to register (must not be null)
     * @throws NullPointerException if observer is null
     */
    public void addObserver(ModelObserver observer) {
        if (observer == null) {
            throw new NullPointerException("Observer cannot be null");
        }
        observers.add(observer);
    }
    
    /**
     * Notifies all registered observers that the model state has changed.
     * 
     * <p>This method calls {@code modelChanged()} on each registered observer
     * in the order they were added. It is called automatically by all state-modifying
     * methods (move, turn, setPenDown, clear, reset).</p>
     * 
     * <p>If an observer throws an exception, it is caught and logged, but does not
     * prevent other observers from being notified.</p>
     */
    private void notifyObservers() {
        for (ModelObserver observer : observers) {
            try {
                observer.modelChanged();
            } catch (Exception e) {
                // Log the error but continue notifying other observers
                System.err.println("Error notifying observer: " + e.getMessage());
            }
        }
    }
}
