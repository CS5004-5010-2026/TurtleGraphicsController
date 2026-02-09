package turtlegraphics.view;

import turtlegraphics.model.TurtleModel;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.Polygon;

/**
 * Custom JPanel for rendering the turtle graphics canvas.
 * 
 * <p>The CanvasPanel is responsible for:</p>
 * <ul>
 *   <li>Rendering all lines drawn by the turtle</li>
 *   <li>Drawing the turtle at its current position and heading</li>
 *   <li>Converting between model coordinates (origin at center) and screen coordinates (origin at top-left)</li>
 *   <li>Applying anti-aliasing for smooth graphics</li>
 * </ul>
 * 
 * <p>The canvas uses a coordinate system where:</p>
 * <ul>
 *   <li>Model coordinates: Origin at center, Y-axis points up</li>
 *   <li>Screen coordinates: Origin at top-left, Y-axis points down</li>
 * </ul>
 * 
 * <p>The turtle is rendered as a triangle that rotates to match its heading.</p>
 */
public class CanvasPanel extends JPanel {
    
    /**
     * Width of the canvas in pixels.
     */
    public static final int WIDTH = 800;
    
    /**
     * Height of the canvas in pixels.
     */
    public static final int HEIGHT = 600;
    
    /**
     * Size of the turtle triangle in pixels (from base to tip).
     */
    private static final int TURTLE_SIZE = 20;
    
    /**
     * The turtle model to render.
     */
    private final TurtleModel model;
    
    /**
     * Constructs a new CanvasPanel for the given turtle model.
     * 
     * <p>The panel is initialized with a white background and a preferred size
     * of 800x600 pixels.</p>
     * 
     * @param model the turtle model to render (must not be null)
     * @throws NullPointerException if model is null
     */
    public CanvasPanel(TurtleModel model) {
        if (model == null) {
            throw new NullPointerException("Model cannot be null");
        }
        this.model = model;
        
        // Set panel properties
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Cast to Graphics2D for advanced rendering features
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing for smooth lines and shapes
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw all lines
        drawLines(g2d);
        
        // Draw the turtle
        drawTurtle(g2d);
    }
    
    /**
     * Draws all lines from the turtle model.
     * 
     * <p>Lines are drawn in black with anti-aliasing for smooth appearance.
     * Model coordinates are converted to screen coordinates before drawing.</p>
     * 
     * @param g2d the graphics context to draw on
     */
    private void drawLines(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        
        for (Line2D.Double line : model.getLines()) {
            // Convert model coordinates to screen coordinates
            Point2D.Double start = modelToScreen(new Point2D.Double(line.x1, line.y1));
            Point2D.Double end = modelToScreen(new Point2D.Double(line.x2, line.y2));
            
            // Draw the line
            g2d.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
        }
    }
    
    /**
     * Draws the turtle at its current position and heading.
     * 
     * <p>The turtle is rendered as an isosceles triangle pointing in the direction
     * of its heading. The color indicates the pen state:</p>
     * <ul>
     *   <li>Red: Pen is down (drawing)</li>
     *   <li>Blue: Pen is up (not drawing)</li>
     * </ul>
     * 
     * @param g2d the graphics context to draw on
     */
    private void drawTurtle(Graphics2D g2d) {
        // Get turtle state
        Point2D.Double position = model.getPosition();
        double heading = model.getHeading();
        boolean penDown = model.isPenDown();
        
        // Convert position to screen coordinates
        Point2D.Double screenPos = modelToScreen(position);
        
        // Set color based on pen state
        g2d.setColor(penDown ? Color.RED : Color.BLUE);
        
        // Calculate triangle vertices based on heading
        // The turtle is an isosceles triangle with the tip pointing in the heading direction
        double headingRadians = Math.toRadians(heading);
        
        // Tip of the triangle (in the heading direction)
        int tipX = (int) (screenPos.x + TURTLE_SIZE * Math.cos(headingRadians));
        int tipY = (int) (screenPos.y - TURTLE_SIZE * Math.sin(headingRadians)); // Negative because screen Y is inverted
        
        // Base of the triangle (perpendicular to heading)
        double baseAngle1 = headingRadians + Math.toRadians(150); // 150 degrees from heading
        double baseAngle2 = headingRadians - Math.toRadians(150); // -150 degrees from heading
        
        int base1X = (int) (screenPos.x + (TURTLE_SIZE * 0.6) * Math.cos(baseAngle1));
        int base1Y = (int) (screenPos.y - (TURTLE_SIZE * 0.6) * Math.sin(baseAngle1));
        
        int base2X = (int) (screenPos.x + (TURTLE_SIZE * 0.6) * Math.cos(baseAngle2));
        int base2Y = (int) (screenPos.y - (TURTLE_SIZE * 0.6) * Math.sin(baseAngle2));
        
        // Create and fill the triangle
        Polygon triangle = new Polygon();
        triangle.addPoint(tipX, tipY);
        triangle.addPoint(base1X, base1Y);
        triangle.addPoint(base2X, base2Y);
        
        g2d.fillPolygon(triangle);
    }
    
    /**
     * Converts a point from model coordinates to screen coordinates.
     * 
     * <p>Model coordinates have the origin at the center of the canvas with Y-axis pointing up.
     * Screen coordinates have the origin at the top-left corner with Y-axis pointing down.</p>
     * 
     * <p>Conversion formulas:</p>
     * <ul>
     *   <li>screenX = modelX + WIDTH/2</li>
     *   <li>screenY = HEIGHT/2 - modelY</li>
     * </ul>
     * 
     * @param modelPoint the point in model coordinates
     * @return the point in screen coordinates
     */
    private Point2D.Double modelToScreen(Point2D.Double modelPoint) {
        double screenX = modelPoint.x + WIDTH / 2.0;
        double screenY = HEIGHT / 2.0 - modelPoint.y;
        return new Point2D.Double(screenX, screenY);
    }
    
    /**
     * Converts a point from screen coordinates to model coordinates.
     * 
     * <p>This is the inverse of {@link #modelToScreen(Point2D.Double)}.</p>
     * 
     * <p>Conversion formulas:</p>
     * <ul>
     *   <li>modelX = screenX - WIDTH/2</li>
     *   <li>modelY = HEIGHT/2 - screenY</li>
     * </ul>
     * 
     * @param screenPoint the point in screen coordinates
     * @return the point in model coordinates
     */
    public Point2D.Double screenToModel(Point2D.Double screenPoint) {
        double modelX = screenPoint.x - WIDTH / 2.0;
        double modelY = HEIGHT / 2.0 - screenPoint.y;
        return new Point2D.Double(modelX, modelY);
    }
}
