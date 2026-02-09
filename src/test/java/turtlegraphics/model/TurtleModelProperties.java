package turtlegraphics.model;

import net.jqwik.api.*;
import net.jqwik.api.constraints.DoubleRange;
import net.jqwik.api.constraints.IntRange;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property-based tests for TurtleModel using jqwik.
 * 
 * <p>These tests validate universal properties that should hold across all valid inputs.
 * Each property is tested with 100+ randomized iterations to ensure comprehensive coverage.
 * 
 * <p>Property-based testing complements unit tests by verifying that certain invariants
 * hold true across a wide range of inputs, not just specific examples.
 * 
 * @author CS5004 Teaching Team
 * @version 1.0
 */
public class TurtleModelProperties {
    
    /**
     * Property 6: Heading Normalization
     * 
     * <p>For any angle value (positive, negative, or greater than 360), when setting or 
     * updating the turtle's heading, the resulting heading should always be normalized 
     * to the range [0, 360).
     * 
     * <p>Examples:
     * <ul>
     *   <li>370° → 10°</li>
     *   <li>-30° → 330°</li>
     *   <li>720° → 0°</li>
     *   <li>-360° → 0°</li>
     * </ul>
     * 
     * <p><strong>Validates: Requirements 6.2</strong>
     * 
     * @param angle any angle value to test normalization
     */
    @Property(tries = 100)
    void headingNormalizationProperty(
            @ForAll @DoubleRange(min = -3600.0, max = 3600.0) double angle) {
        
        // Create a turtle and turn it by the given angle
        TurtleModel turtle = new TurtleModel();
        turtle.turn(angle);
        
        // Get the resulting heading
        double heading = turtle.getHeading();
        
        // Verify heading is in the range [0, 360)
        assertTrue(heading >= 0.0, 
            String.format("Heading %.2f should be >= 0 after turning by %.2f degrees", 
                heading, angle));
        assertTrue(heading < 360.0, 
            String.format("Heading %.2f should be < 360 after turning by %.2f degrees", 
                heading, angle));
        
        // Verify the heading is mathematically equivalent to the angle
        // (i.e., they differ by a multiple of 360)
        double normalizedAngle = angle % 360.0;
        if (normalizedAngle < 0) {
            normalizedAngle += 360.0;
        }
        
        assertEquals(normalizedAngle, heading, 0.0001,
            String.format("Heading %.2f should equal normalized angle %.2f (from %.2f degrees)", 
                heading, normalizedAngle, angle));
    }
    
    /**
     * Property 6 (Edge Cases): Heading Normalization for Specific Values
     * 
     * <p>Tests specific edge cases for heading normalization:
     * <ul>
     *   <li>Exact multiples of 360 should normalize to 0</li>
     *   <li>Values just below 360 should remain unchanged</li>
     *   <li>Values just above 0 should remain unchanged</li>
     * </ul>
     * 
     * <p><strong>Validates: Requirements 6.2</strong>
     */
    @Property(tries = 50)
    void headingNormalizationEdgeCases(
            @ForAll @IntRange(min = -10, max = 10) int multiplier) {
        
        TurtleModel turtle = new TurtleModel();
        
        // Test exact multiples of 360
        double exactMultiple = multiplier * 360.0;
        turtle.turn(exactMultiple);
        assertEquals(0.0, turtle.getHeading(), 0.0001,
            String.format("Turning by %.0f° (multiple of 360) should result in heading 0°", 
                exactMultiple));
        
        // Reset for next test
        turtle = new TurtleModel();
        
        // Test values just below 360
        double justBelow360 = 359.9;
        turtle.turn(justBelow360);
        assertEquals(359.9, turtle.getHeading(), 0.0001,
            "Turning by 359.9° should result in heading 359.9°");
        
        // Reset for next test
        turtle = new TurtleModel();
        
        // Test small positive values
        double smallPositive = 0.1;
        turtle.turn(smallPositive);
        assertEquals(0.1, turtle.getHeading(), 0.0001,
            "Turning by 0.1° should result in heading 0.1°");
    }
    
    /**
     * Property 6 (Cumulative): Multiple Turns Maintain Normalization
     * 
     * <p>Tests that heading normalization is maintained across multiple turn operations.
     * After any sequence of turns, the heading should always remain in [0, 360).
     * 
     * <p><strong>Validates: Requirements 6.2</strong>
     * 
     * @param angle1 first turn angle
     * @param angle2 second turn angle
     * @param angle3 third turn angle
     */
    @Property(tries = 100)
    void multipleTurnsMaintainNormalization(
            @ForAll @DoubleRange(min = -720.0, max = 720.0) double angle1,
            @ForAll @DoubleRange(min = -720.0, max = 720.0) double angle2,
            @ForAll @DoubleRange(min = -720.0, max = 720.0) double angle3) {
        
        TurtleModel turtle = new TurtleModel();
        
        // Perform multiple turns
        turtle.turn(angle1);
        double heading1 = turtle.getHeading();
        assertTrue(heading1 >= 0.0 && heading1 < 360.0,
            String.format("After first turn by %.2f°, heading %.2f should be in [0, 360)", 
                angle1, heading1));
        
        turtle.turn(angle2);
        double heading2 = turtle.getHeading();
        assertTrue(heading2 >= 0.0 && heading2 < 360.0,
            String.format("After second turn by %.2f°, heading %.2f should be in [0, 360)", 
                angle2, heading2));
        
        turtle.turn(angle3);
        double heading3 = turtle.getHeading();
        assertTrue(heading3 >= 0.0 && heading3 < 360.0,
            String.format("After third turn by %.2f°, heading %.2f should be in [0, 360)", 
                angle3, heading3));
        
        // Verify the final heading is equivalent to the sum of all angles
        double totalAngle = angle1 + angle2 + angle3;
        double normalizedTotal = totalAngle % 360.0;
        if (normalizedTotal < 0) {
            normalizedTotal += 360.0;
        }
        
        assertEquals(normalizedTotal, heading3, 0.0001,
            String.format("Final heading %.2f should equal normalized sum %.2f of angles (%.2f + %.2f + %.2f)", 
                heading3, normalizedTotal, angle1, angle2, angle3));
    }
    
    /**
     * Property 1: Movement Position Calculation
     * 
     * <p>For any turtle position, heading, and distance, when the turtle moves, the new 
     * position should be calculated using the correct trigonometric formulas:
     * <ul>
     *   <li>newX = currentX + distance * cos(heading * π / 180)</li>
     *   <li>newY = currentY + distance * sin(heading * π / 180)</li>
     * </ul>
     * </p>
     * 
     * <p>This property ensures that turtle movement follows correct trigonometric calculations
     * regardless of the starting position, heading, or distance (positive or negative).</p>
     * 
     * <p><strong>Validates: Requirements 5.1, 5.4, 13.3</strong>
     * 
     * @param startX initial X position
     * @param startY initial Y position
     * @param heading turtle heading in degrees
     * @param distance distance to move (positive or negative)
     */
    @Property(tries = 100)
    void movementPositionCalculationProperty(
            @ForAll @DoubleRange(min = -1000.0, max = 1000.0) double startX,
            @ForAll @DoubleRange(min = -1000.0, max = 1000.0) double startY,
            @ForAll @DoubleRange(min = 0.0, max = 360.0) double heading,
            @ForAll @DoubleRange(min = -500.0, max = 500.0) double distance) {
        
        // Create a turtle at the origin
        TurtleModel turtle = new TurtleModel();
        
        // Move turtle to the starting position
        // First, we need to position the turtle at (startX, startY)
        // We'll do this by directly moving from origin
        double distanceToStart = Math.sqrt(startX * startX + startY * startY);
        if (distanceToStart > 0.0001) {
            double angleToStart = Math.toDegrees(Math.atan2(startY, startX));
            turtle.turn(angleToStart);
            turtle.move(distanceToStart);
        }
        
        // Set the turtle to the desired heading
        turtle.turn(heading - turtle.getHeading());
        
        // Record the position before moving
        java.awt.geom.Point2D.Double positionBefore = turtle.getPosition();
        
        // Move the turtle by the specified distance
        turtle.move(distance);
        
        // Get the new position
        java.awt.geom.Point2D.Double positionAfter = turtle.getPosition();
        
        // Calculate the expected new position using trigonometry
        double headingRadians = Math.toRadians(heading);
        double expectedX = positionBefore.x + distance * Math.cos(headingRadians);
        double expectedY = positionBefore.y + distance * Math.sin(headingRadians);
        
        // Verify the new position matches the expected calculation
        assertEquals(expectedX, positionAfter.x, 0.0001,
            String.format("X position should be %.4f after moving %.2f units from (%.2f, %.2f) at heading %.2f°",
                expectedX, distance, positionBefore.x, positionBefore.y, heading));
        
        assertEquals(expectedY, positionAfter.y, 0.0001,
            String.format("Y position should be %.4f after moving %.2f units from (%.2f, %.2f) at heading %.2f°",
                expectedY, distance, positionBefore.x, positionBefore.y, heading));
    }
    
    /**
     * Property 1 (Simplified): Movement Position Calculation from Origin
     * 
     * <p>A simplified version of the movement position calculation property that tests
     * movement from the origin (0, 0) with various headings and distances. This makes
     * the test easier to understand and debug.</p>
     * 
     * <p><strong>Validates: Requirements 5.1, 5.4, 13.3</strong>
     * 
     * @param heading turtle heading in degrees
     * @param distance distance to move (positive or negative)
     */
    @Property(tries = 100)
    void movementFromOriginProperty(
            @ForAll @DoubleRange(min = 0.0, max = 359.99) double heading,
            @ForAll @DoubleRange(min = -500.0, max = 500.0) double distance) {
        
        // Create a turtle at the origin (0, 0) facing right (0°)
        TurtleModel turtle = new TurtleModel();
        
        // Turn to the desired heading
        turtle.turn(heading);
        
        // Verify heading is set correctly (accounting for normalization)
        double expectedHeading = heading % 360.0;
        if (expectedHeading < 0) {
            expectedHeading += 360.0;
        }
        assertEquals(expectedHeading, turtle.getHeading(), 0.0001,
            String.format("Heading should be %.2f° after turning by %.2f°", expectedHeading, heading));
        
        // Move the turtle by the specified distance
        turtle.move(distance);
        
        // Get the new position
        java.awt.geom.Point2D.Double position = turtle.getPosition();
        
        // Calculate the expected position using trigonometry
        // Use the actual heading from the turtle (after normalization)
        double actualHeading = turtle.getHeading();
        double headingRadians = Math.toRadians(actualHeading);
        double expectedX = distance * Math.cos(headingRadians);
        double expectedY = distance * Math.sin(headingRadians);
        
        // Verify the position matches the expected calculation
        assertEquals(expectedX, position.x, 0.0001,
            String.format("X position should be %.4f after moving %.2f units at heading %.2f°",
                expectedX, distance, actualHeading));
        
        assertEquals(expectedY, position.y, 0.0001,
            String.format("Y position should be %.4f after moving %.2f units at heading %.2f°",
                expectedY, distance, actualHeading));
    }
    
    /**
     * Property 1 (Cardinal Directions): Movement in Cardinal Directions
     * 
     * <p>Tests movement in the four cardinal directions (0°, 90°, 180°, 270°) to verify
     * that the trigonometric calculations produce the expected results for these common cases.</p>
     * 
     * <p><strong>Validates: Requirements 5.1, 5.4, 13.3</strong>
     * 
     * @param distance distance to move (positive or negative)
     */
    @Property(tries = 50)
    void movementCardinalDirectionsProperty(
            @ForAll @DoubleRange(min = -500.0, max = 500.0) double distance) {
        
        // Test movement to the right (0°)
        TurtleModel turtle = new TurtleModel();
        turtle.move(distance);
        assertEquals(distance, turtle.getPosition().x, 0.0001,
            String.format("Moving %.2f units at 0° should change X by %.2f", distance, distance));
        assertEquals(0.0, turtle.getPosition().y, 0.0001,
            String.format("Moving %.2f units at 0° should not change Y", distance));
        
        // Test movement upward (90°)
        turtle = new TurtleModel();
        turtle.turn(90.0);
        turtle.move(distance);
        assertEquals(0.0, turtle.getPosition().x, 0.0001,
            String.format("Moving %.2f units at 90° should not change X", distance));
        assertEquals(distance, turtle.getPosition().y, 0.0001,
            String.format("Moving %.2f units at 90° should change Y by %.2f", distance, distance));
        
        // Test movement to the left (180°)
        turtle = new TurtleModel();
        turtle.turn(180.0);
        turtle.move(distance);
        assertEquals(-distance, turtle.getPosition().x, 0.0001,
            String.format("Moving %.2f units at 180° should change X by %.2f", distance, -distance));
        assertEquals(0.0, turtle.getPosition().y, 0.0001,
            String.format("Moving %.2f units at 180° should not change Y", distance));
        
        // Test movement downward (270°)
        turtle = new TurtleModel();
        turtle.turn(270.0);
        turtle.move(distance);
        assertEquals(0.0, turtle.getPosition().x, 0.0001,
            String.format("Moving %.2f units at 270° should not change X", distance));
        assertEquals(-distance, turtle.getPosition().y, 0.0001,
            String.format("Moving %.2f units at 270° should change Y by %.2f", distance, -distance));
    }
    
    /**
     * Property 2: Pen Down Creates Lines
     * 
     * <p>For any turtle position and movement distance, when the turtle moves with pen down,
     * a line should be added to the drawing context from the old position to the new position.</p>
     * 
     * <p>This property verifies that:
     * <ul>
     *   <li>A new line is added to the lines list when moving with pen down</li>
     *   <li>The line starts at the position before the move</li>
     *   <li>The line ends at the position after the move</li>
     *   <li>The line count increases by exactly 1 for each move with pen down</li>
     * </ul>
     * </p>
     * 
     * <p><strong>Validates: Requirements 3.1, 5.2</strong>
     * 
     * @param heading turtle heading in degrees
     * @param distance distance to move (positive or negative)
     */
    @Property(tries = 100)
    void penDownCreatesLinesProperty(
            @ForAll @DoubleRange(min = 0.0, max = 359.99) double heading,
            @ForAll @DoubleRange(min = -500.0, max = 500.0) double distance) {
        
        // Create a turtle at the origin with pen down (default state)
        TurtleModel turtle = new TurtleModel();
        
        // Verify pen is down initially
        assertTrue(turtle.isPenDown(), "Pen should be down initially");
        
        // Verify no lines initially
        int initialLineCount = turtle.getLines().size();
        assertEquals(0, initialLineCount, "Should have no lines initially");
        
        // Turn to the desired heading
        turtle.turn(heading);
        
        // Record the position before moving
        java.awt.geom.Point2D.Double positionBefore = turtle.getPosition();
        
        // Move the turtle by the specified distance (pen is down)
        turtle.move(distance);
        
        // Get the position after moving
        java.awt.geom.Point2D.Double positionAfter = turtle.getPosition();
        
        // Verify that exactly one line was added
        int finalLineCount = turtle.getLines().size();
        assertEquals(1, finalLineCount,
            String.format("Should have exactly 1 line after moving %.2f units with pen down", distance));
        
        // Get the line that was created
        java.awt.geom.Line2D.Double createdLine = turtle.getLines().get(0);
        
        // Verify the line starts at the old position
        assertEquals(positionBefore.x, createdLine.x1, 0.0001,
            String.format("Line should start at X=%.4f (old position)", positionBefore.x));
        assertEquals(positionBefore.y, createdLine.y1, 0.0001,
            String.format("Line should start at Y=%.4f (old position)", positionBefore.y));
        
        // Verify the line ends at the new position
        assertEquals(positionAfter.x, createdLine.x2, 0.0001,
            String.format("Line should end at X=%.4f (new position)", positionAfter.x));
        assertEquals(positionAfter.y, createdLine.y2, 0.0001,
            String.format("Line should end at Y=%.4f (new position)", positionAfter.y));
    }
    
    /**
     * Property 2 (Multiple Moves): Pen Down Creates Lines for Multiple Moves
     * 
     * <p>Tests that each move with pen down creates exactly one line, and that the line
     * count increases monotonically with each move.</p>
     * 
     * <p><strong>Validates: Requirements 3.1, 5.2</strong>
     * 
     * @param distance1 first move distance
     * @param distance2 second move distance
     * @param distance3 third move distance
     */
    @Property(tries = 100)
    void penDownCreatesLinesMultipleMovesProperty(
            @ForAll @DoubleRange(min = -200.0, max = 200.0) double distance1,
            @ForAll @DoubleRange(min = -200.0, max = 200.0) double distance2,
            @ForAll @DoubleRange(min = -200.0, max = 200.0) double distance3) {
        
        // Create a turtle with pen down
        TurtleModel turtle = new TurtleModel();
        assertTrue(turtle.isPenDown(), "Pen should be down initially");
        
        // Verify no lines initially
        assertEquals(0, turtle.getLines().size(), "Should have no lines initially");
        
        // First move
        java.awt.geom.Point2D.Double pos1Before = turtle.getPosition();
        turtle.move(distance1);
        java.awt.geom.Point2D.Double pos1After = turtle.getPosition();
        
        // Verify one line was added
        assertEquals(1, turtle.getLines().size(),
            String.format("Should have 1 line after first move (%.2f units)", distance1));
        
        // Verify the first line connects the correct positions
        java.awt.geom.Line2D.Double line1 = turtle.getLines().get(0);
        assertEquals(pos1Before.x, line1.x1, 0.0001, "First line should start at first position");
        assertEquals(pos1Before.y, line1.y1, 0.0001, "First line should start at first position");
        assertEquals(pos1After.x, line1.x2, 0.0001, "First line should end at second position");
        assertEquals(pos1After.y, line1.y2, 0.0001, "First line should end at second position");
        
        // Second move
        java.awt.geom.Point2D.Double pos2Before = turtle.getPosition();
        turtle.move(distance2);
        java.awt.geom.Point2D.Double pos2After = turtle.getPosition();
        
        // Verify two lines now exist
        assertEquals(2, turtle.getLines().size(),
            String.format("Should have 2 lines after second move (%.2f units)", distance2));
        
        // Verify the second line connects the correct positions
        java.awt.geom.Line2D.Double line2 = turtle.getLines().get(1);
        assertEquals(pos2Before.x, line2.x1, 0.0001, "Second line should start at second position");
        assertEquals(pos2Before.y, line2.y1, 0.0001, "Second line should start at second position");
        assertEquals(pos2After.x, line2.x2, 0.0001, "Second line should end at third position");
        assertEquals(pos2After.y, line2.y2, 0.0001, "Second line should end at third position");
        
        // Third move
        java.awt.geom.Point2D.Double pos3Before = turtle.getPosition();
        turtle.move(distance3);
        java.awt.geom.Point2D.Double pos3After = turtle.getPosition();
        
        // Verify three lines now exist
        assertEquals(3, turtle.getLines().size(),
            String.format("Should have 3 lines after third move (%.2f units)", distance3));
        
        // Verify the third line connects the correct positions
        java.awt.geom.Line2D.Double line3 = turtle.getLines().get(2);
        assertEquals(pos3Before.x, line3.x1, 0.0001, "Third line should start at third position");
        assertEquals(pos3Before.y, line3.y1, 0.0001, "Third line should start at third position");
        assertEquals(pos3After.x, line3.x2, 0.0001, "Third line should end at fourth position");
        assertEquals(pos3After.y, line3.y2, 0.0001, "Third line should end at fourth position");
    }
    
    /**
     * Property 2 (With Turns): Pen Down Creates Lines with Direction Changes
     * 
     * <p>Tests that lines are created correctly when the turtle changes direction between moves.
     * This verifies that the line endpoints match the actual positions, not just the expected
     * positions based on heading.</p>
     * 
     * <p><strong>Validates: Requirements 3.1, 5.2</strong>
     * 
     * @param distance distance to move
     * @param turnAngle angle to turn between moves
     */
    @Property(tries = 100)
    void penDownCreatesLinesWithTurnsProperty(
            @ForAll @DoubleRange(min = 10.0, max = 200.0) double distance,
            @ForAll @DoubleRange(min = -180.0, max = 180.0) double turnAngle) {
        
        // Create a turtle with pen down
        TurtleModel turtle = new TurtleModel();
        
        // Move forward
        java.awt.geom.Point2D.Double pos1 = turtle.getPosition();
        turtle.move(distance);
        java.awt.geom.Point2D.Double pos2 = turtle.getPosition();
        
        // Verify first line
        assertEquals(1, turtle.getLines().size(), "Should have 1 line after first move");
        java.awt.geom.Line2D.Double line1 = turtle.getLines().get(0);
        assertEquals(pos1.x, line1.x1, 0.0001, "First line should start at origin");
        assertEquals(pos1.y, line1.y1, 0.0001, "First line should start at origin");
        assertEquals(pos2.x, line1.x2, 0.0001, "First line should end at second position");
        assertEquals(pos2.y, line1.y2, 0.0001, "First line should end at second position");
        
        // Turn
        turtle.turn(turnAngle);
        
        // Move again
        java.awt.geom.Point2D.Double pos3 = turtle.getPosition();
        turtle.move(distance);
        java.awt.geom.Point2D.Double pos4 = turtle.getPosition();
        
        // Verify second line
        assertEquals(2, turtle.getLines().size(), "Should have 2 lines after second move");
        java.awt.geom.Line2D.Double line2 = turtle.getLines().get(1);
        assertEquals(pos3.x, line2.x1, 0.0001,
            String.format("Second line should start at position after turn (%.2f, %.2f)", pos3.x, pos3.y));
        assertEquals(pos3.y, line2.y1, 0.0001,
            String.format("Second line should start at position after turn (%.2f, %.2f)", pos3.x, pos3.y));
        assertEquals(pos4.x, line2.x2, 0.0001,
            String.format("Second line should end at final position (%.2f, %.2f)", pos4.x, pos4.y));
        assertEquals(pos4.y, line2.y2, 0.0001,
            String.format("Second line should end at final position (%.2f, %.2f)", pos4.x, pos4.y));
        
        // Verify the two lines are connected (end of first line = start of second line)
        assertEquals(line1.x2, line2.x1, 0.0001, "Lines should be connected (X coordinate)");
        assertEquals(line1.y2, line2.y1, 0.0001, "Lines should be connected (Y coordinate)");
    }
    
    /**
     * Property 3: Pen Up No Lines
     * 
     * <p>For any turtle position and movement distance, when the turtle moves with pen up,
     * no new lines should be added to the drawing context.</p>
     * 
     * <p>This property verifies that:
     * <ul>
     *   <li>No lines are added to the lines list when moving with pen up</li>
     *   <li>The line count remains unchanged after moving with pen up</li>
     *   <li>The turtle's position still updates correctly even with pen up</li>
     * </ul>
     * </p>
     * 
     * <p><strong>Validates: Requirements 3.2, 5.3</strong>
     * 
     * @param heading turtle heading in degrees
     * @param distance distance to move (positive or negative)
     */
    @Property(tries = 100)
    void penUpNoLinesProperty(
            @ForAll @DoubleRange(min = 0.0, max = 359.99) double heading,
            @ForAll @DoubleRange(min = -500.0, max = 500.0) double distance) {
        
        // Create a turtle at the origin
        TurtleModel turtle = new TurtleModel();
        
        // Lift the pen up
        turtle.setPenDown(false);
        
        // Verify pen is up
        assertFalse(turtle.isPenDown(), "Pen should be up after calling setPenDown(false)");
        
        // Verify no lines initially
        int initialLineCount = turtle.getLines().size();
        assertEquals(0, initialLineCount, "Should have no lines initially");
        
        // Turn to the desired heading
        turtle.turn(heading);
        
        // Record the position before moving
        java.awt.geom.Point2D.Double positionBefore = turtle.getPosition();
        
        // Move the turtle by the specified distance (pen is up)
        turtle.move(distance);
        
        // Get the position after moving
        java.awt.geom.Point2D.Double positionAfter = turtle.getPosition();
        
        // Verify that NO lines were added
        int finalLineCount = turtle.getLines().size();
        assertEquals(0, finalLineCount,
            String.format("Should have 0 lines after moving %.2f units with pen up", distance));
        
        // Verify that the position still changed (turtle moved even with pen up)
        double headingRadians = Math.toRadians(turtle.getHeading());
        double expectedX = positionBefore.x + distance * Math.cos(headingRadians);
        double expectedY = positionBefore.y + distance * Math.sin(headingRadians);
        
        assertEquals(expectedX, positionAfter.x, 0.0001,
            String.format("X position should be %.4f after moving %.2f units with pen up", 
                expectedX, distance));
        assertEquals(expectedY, positionAfter.y, 0.0001,
            String.format("Y position should be %.4f after moving %.2f units with pen up", 
                expectedY, distance));
    }
    
    /**
     * Property 3 (Multiple Moves): Pen Up No Lines for Multiple Moves
     * 
     * <p>Tests that multiple moves with pen up do not create any lines, and that the
     * line count remains at zero throughout the sequence.</p>
     * 
     * <p><strong>Validates: Requirements 3.2, 5.3</strong>
     * 
     * @param distance1 first move distance
     * @param distance2 second move distance
     * @param distance3 third move distance
     */
    @Property(tries = 100)
    void penUpNoLinesMultipleMovesProperty(
            @ForAll @DoubleRange(min = -200.0, max = 200.0) double distance1,
            @ForAll @DoubleRange(min = -200.0, max = 200.0) double distance2,
            @ForAll @DoubleRange(min = -200.0, max = 200.0) double distance3) {
        
        // Create a turtle and lift the pen
        TurtleModel turtle = new TurtleModel();
        turtle.setPenDown(false);
        assertFalse(turtle.isPenDown(), "Pen should be up");
        
        // Verify no lines initially
        assertEquals(0, turtle.getLines().size(), "Should have no lines initially");
        
        // First move with pen up
        turtle.move(distance1);
        assertEquals(0, turtle.getLines().size(),
            String.format("Should have 0 lines after first move (%.2f units) with pen up", distance1));
        
        // Second move with pen up
        turtle.move(distance2);
        assertEquals(0, turtle.getLines().size(),
            String.format("Should have 0 lines after second move (%.2f units) with pen up", distance2));
        
        // Third move with pen up
        turtle.move(distance3);
        assertEquals(0, turtle.getLines().size(),
            String.format("Should have 0 lines after third move (%.2f units) with pen up", distance3));
    }
    
    /**
     * Property 3 (Mixed Pen States): Pen Up No Lines Mixed with Pen Down
     * 
     * <p>Tests that when the pen state is toggled between up and down, only the moves
     * with pen down create lines. This verifies that the pen state is correctly checked
     * for each move operation.</p>
     * 
     * <p><strong>Validates: Requirements 3.2, 5.3</strong>
     * 
     * @param distance distance to move for each segment
     */
    @Property(tries = 100)
    void penUpNoLinesMixedWithPenDownProperty(
            @ForAll @DoubleRange(min = 10.0, max = 200.0) double distance) {
        
        // Create a turtle with pen down (default)
        TurtleModel turtle = new TurtleModel();
        assertTrue(turtle.isPenDown(), "Pen should be down initially");
        
        // Move with pen down - should create a line
        turtle.move(distance);
        assertEquals(1, turtle.getLines().size(),
            "Should have 1 line after first move with pen down");
        
        // Lift pen up
        turtle.setPenDown(false);
        assertFalse(turtle.isPenDown(), "Pen should be up");
        
        // Move with pen up - should NOT create a line
        turtle.move(distance);
        assertEquals(1, turtle.getLines().size(),
            "Should still have 1 line after move with pen up");
        
        // Move again with pen up - should still NOT create a line
        turtle.move(distance);
        assertEquals(1, turtle.getLines().size(),
            "Should still have 1 line after second move with pen up");
        
        // Put pen down
        turtle.setPenDown(true);
        assertTrue(turtle.isPenDown(), "Pen should be down");
        
        // Move with pen down - should create another line
        turtle.move(distance);
        assertEquals(2, turtle.getLines().size(),
            "Should have 2 lines after move with pen down again");
        
        // Lift pen up again
        turtle.setPenDown(false);
        
        // Move with pen up - should NOT create a line
        turtle.move(distance);
        assertEquals(2, turtle.getLines().size(),
            "Should still have 2 lines after final move with pen up");
    }
    
    /**
     * Property 7: Turn Updates Heading Only
     * 
     * <p>For any turtle state (position, heading, pen state), when the turn command is 
     * executed with any angle, only the heading should change—position and pen state must 
     * remain unchanged.</p>
     * 
     * <p>This property verifies that:
     * <ul>
     *   <li>The turtle's position (X and Y coordinates) remains unchanged after turning</li>
     *   <li>The turtle's pen state remains unchanged after turning</li>
     *   <li>Only the heading is modified by the turn operation</li>
     * </ul>
     * </p>
     * 
     * <p><strong>Validates: Requirements 6.5</strong>
     * 
     * @param startX initial X position
     * @param startY initial Y position
     * @param startHeading initial heading in degrees
     * @param penState initial pen state (true = down, false = up)
     * @param turnAngle angle to turn in degrees
     */
    @Property(tries = 100)
    void turnUpdatesHeadingOnlyProperty(
            @ForAll @DoubleRange(min = -1000.0, max = 1000.0) double startX,
            @ForAll @DoubleRange(min = -1000.0, max = 1000.0) double startY,
            @ForAll @DoubleRange(min = 0.0, max = 359.99) double startHeading,
            @ForAll boolean penState,
            @ForAll @DoubleRange(min = -720.0, max = 720.0) double turnAngle) {
        
        // Create a turtle at the origin
        TurtleModel turtle = new TurtleModel();
        
        // Set up the turtle to the desired starting state
        // Move to starting position
        double distanceToStart = Math.sqrt(startX * startX + startY * startY);
        if (distanceToStart > 0.0001) {
            double angleToStart = Math.toDegrees(Math.atan2(startY, startX));
            turtle.turn(angleToStart);
            turtle.move(distanceToStart);
        }
        
        // Set to starting heading
        turtle.turn(startHeading - turtle.getHeading());
        
        // Set pen state
        turtle.setPenDown(penState);
        
        // Record the state before turning
        java.awt.geom.Point2D.Double positionBefore = turtle.getPosition();
        double headingBefore = turtle.getHeading();
        boolean penStateBefore = turtle.isPenDown();
        int lineCountBefore = turtle.getLines().size();
        
        // Turn the turtle by the specified angle
        turtle.turn(turnAngle);
        
        // Record the state after turning
        java.awt.geom.Point2D.Double positionAfter = turtle.getPosition();
        double headingAfter = turtle.getHeading();
        boolean penStateAfter = turtle.isPenDown();
        int lineCountAfter = turtle.getLines().size();
        
        // Verify position did NOT change
        assertEquals(positionBefore.x, positionAfter.x, 0.0001,
            String.format("X position should remain %.4f after turning by %.2f°", 
                positionBefore.x, turnAngle));
        assertEquals(positionBefore.y, positionAfter.y, 0.0001,
            String.format("Y position should remain %.4f after turning by %.2f°", 
                positionBefore.y, turnAngle));
        
        // Verify pen state did NOT change
        assertEquals(penStateBefore, penStateAfter,
            String.format("Pen state should remain %s after turning by %.2f°", 
                penStateBefore ? "down" : "up", turnAngle));
        
        // Verify line count did NOT change
        assertEquals(lineCountBefore, lineCountAfter,
            String.format("Line count should remain %d after turning by %.2f°", 
                lineCountBefore, turnAngle));
        
        // Verify heading DID change (unless turn angle is a multiple of 360)
        double expectedHeading = (headingBefore + turnAngle) % 360.0;
        if (expectedHeading < 0) {
            expectedHeading += 360.0;
        }
        assertEquals(expectedHeading, headingAfter, 0.0001,
            String.format("Heading should be %.2f° after turning by %.2f° from %.2f°", 
                expectedHeading, turnAngle, headingBefore));
    }
    
    /**
     * Property 7 (Simplified): Turn Updates Heading Only from Origin
     * 
     * <p>A simplified version of the turn updates heading only property that tests
     * turning from the origin with default state. This makes the test easier to 
     * understand and debug.</p>
     * 
     * <p><strong>Validates: Requirements 6.5</strong>
     * 
     * @param turnAngle angle to turn in degrees
     */
    @Property(tries = 100)
    void turnUpdatesHeadingOnlySimplifiedProperty(
            @ForAll @DoubleRange(min = -720.0, max = 720.0) double turnAngle) {
        
        // Create a turtle at the origin (0, 0) facing right (0°) with pen down
        TurtleModel turtle = new TurtleModel();
        
        // Record the initial state
        java.awt.geom.Point2D.Double initialPosition = turtle.getPosition();
        double initialHeading = turtle.getHeading();
        boolean initialPenState = turtle.isPenDown();
        int initialLineCount = turtle.getLines().size();
        
        // Verify initial state
        assertEquals(0.0, initialPosition.x, 0.0001, "Initial X should be 0");
        assertEquals(0.0, initialPosition.y, 0.0001, "Initial Y should be 0");
        assertEquals(0.0, initialHeading, 0.0001, "Initial heading should be 0°");
        assertTrue(initialPenState, "Initial pen state should be down");
        assertEquals(0, initialLineCount, "Initial line count should be 0");
        
        // Turn the turtle
        turtle.turn(turnAngle);
        
        // Record the state after turning
        java.awt.geom.Point2D.Double finalPosition = turtle.getPosition();
        double finalHeading = turtle.getHeading();
        boolean finalPenState = turtle.isPenDown();
        int finalLineCount = turtle.getLines().size();
        
        // Verify position did NOT change
        assertEquals(0.0, finalPosition.x, 0.0001,
            String.format("X position should remain 0 after turning by %.2f°", turnAngle));
        assertEquals(0.0, finalPosition.y, 0.0001,
            String.format("Y position should remain 0 after turning by %.2f°", turnAngle));
        
        // Verify pen state did NOT change
        assertTrue(finalPenState,
            String.format("Pen state should remain down after turning by %.2f°", turnAngle));
        
        // Verify line count did NOT change
        assertEquals(0, finalLineCount,
            String.format("Line count should remain 0 after turning by %.2f°", turnAngle));
        
        // Verify heading DID change correctly
        double expectedHeading = turnAngle % 360.0;
        if (expectedHeading < 0) {
            expectedHeading += 360.0;
        }
        assertEquals(expectedHeading, finalHeading, 0.0001,
            String.format("Heading should be %.2f° after turning by %.2f°", 
                expectedHeading, turnAngle));
    }
    
    /**
     * Property 8: Turn Angle Addition
     * 
     * <p>For any current heading and turn angle, when the turtle turns, the new heading 
     * should equal the old heading plus the turn angle (after normalization to [0, 360)).</p>
     * 
     * <p>This property verifies that:
     * <ul>
     *   <li>The turn operation correctly adds the angle to the current heading</li>
     *   <li>The result is properly normalized to the range [0, 360)</li>
     *   <li>Positive angles rotate counterclockwise</li>
     *   <li>Negative angles rotate clockwise</li>
     * </ul>
     * </p>
     * 
     * <p><strong>Validates: Requirements 6.1, 6.3</strong>
     * 
     * @param initialHeading the starting heading in degrees
     * @param turnAngle the angle to turn in degrees
     */
    @Property(tries = 100)
    void turnAngleAdditionProperty(
            @ForAll @DoubleRange(min = 0.0, max = 359.99) double initialHeading,
            @ForAll @DoubleRange(min = -720.0, max = 720.0) double turnAngle) {
        
        // Create a turtle at the origin
        TurtleModel turtle = new TurtleModel();
        
        // Set the turtle to the initial heading
        turtle.turn(initialHeading);
        
        // Verify the initial heading is set correctly
        assertEquals(initialHeading, turtle.getHeading(), 0.0001,
            String.format("Initial heading should be %.2f°", initialHeading));
        
        // Turn the turtle by the specified angle
        turtle.turn(turnAngle);
        
        // Get the final heading
        double finalHeading = turtle.getHeading();
        
        // Calculate the expected heading (initial + turn, normalized to [0, 360))
        double expectedHeading = (initialHeading + turnAngle) % 360.0;
        if (expectedHeading < 0) {
            expectedHeading += 360.0;
        }
        
        // Verify the final heading equals the expected heading
        assertEquals(expectedHeading, finalHeading, 0.0001,
            String.format("Heading should be %.2f° after turning by %.2f° from %.2f° (%.2f + %.2f = %.2f)", 
                expectedHeading, turnAngle, initialHeading, initialHeading, turnAngle, expectedHeading));
    }
    
    /**
     * Property 8 (Multiple Turns): Turn Angle Addition with Multiple Turns
     * 
     * <p>Tests that multiple consecutive turns correctly accumulate the angle changes.
     * The final heading should equal the sum of all turn angles (after normalization).</p>
     * 
     * <p><strong>Validates: Requirements 6.1, 6.3</strong>
     * 
     * @param angle1 first turn angle
     * @param angle2 second turn angle
     * @param angle3 third turn angle
     */
    @Property(tries = 100)
    void turnAngleAdditionMultipleTurnsProperty(
            @ForAll @DoubleRange(min = -360.0, max = 360.0) double angle1,
            @ForAll @DoubleRange(min = -360.0, max = 360.0) double angle2,
            @ForAll @DoubleRange(min = -360.0, max = 360.0) double angle3) {
        
        // Create a turtle at the origin (heading = 0°)
        TurtleModel turtle = new TurtleModel();
        double currentHeading = 0.0;
        
        // First turn
        turtle.turn(angle1);
        currentHeading = (currentHeading + angle1) % 360.0;
        if (currentHeading < 0) {
            currentHeading += 360.0;
        }
        assertEquals(currentHeading, turtle.getHeading(), 0.0001,
            String.format("After first turn by %.2f°, heading should be %.2f°", 
                angle1, currentHeading));
        
        // Second turn
        turtle.turn(angle2);
        currentHeading = (currentHeading + angle2) % 360.0;
        if (currentHeading < 0) {
            currentHeading += 360.0;
        }
        assertEquals(currentHeading, turtle.getHeading(), 0.0001,
            String.format("After second turn by %.2f°, heading should be %.2f°", 
                angle2, currentHeading));
        
        // Third turn
        turtle.turn(angle3);
        currentHeading = (currentHeading + angle3) % 360.0;
        if (currentHeading < 0) {
            currentHeading += 360.0;
        }
        assertEquals(currentHeading, turtle.getHeading(), 0.0001,
            String.format("After third turn by %.2f°, heading should be %.2f°", 
                angle3, currentHeading));
        
        // Verify the final heading equals the sum of all angles (normalized)
        double totalAngle = angle1 + angle2 + angle3;
        double expectedFinalHeading = totalAngle % 360.0;
        if (expectedFinalHeading < 0) {
            expectedFinalHeading += 360.0;
        }
        assertEquals(expectedFinalHeading, turtle.getHeading(), 0.0001,
            String.format("Final heading should be %.2f° (sum of %.2f + %.2f + %.2f = %.2f)", 
                expectedFinalHeading, angle1, angle2, angle3, totalAngle));
    }
    
    /**
     * Property 8 (Positive and Negative): Turn Angle Addition with Positive and Negative Angles
     * 
     * <p>Tests that positive angles (counterclockwise) and negative angles (clockwise) 
     * are correctly added to the heading.</p>
     * 
     * <p><strong>Validates: Requirements 6.1, 6.3</strong>
     * 
     * @param positiveAngle a positive turn angle (counterclockwise)
     * @param negativeAngle a negative turn angle (clockwise)
     */
    @Property(tries = 100)
    void turnAngleAdditionPositiveAndNegativeProperty(
            @ForAll @DoubleRange(min = 1.0, max = 180.0) double positiveAngle,
            @ForAll @DoubleRange(min = -180.0, max = -1.0) double negativeAngle) {
        
        // Test positive angle (counterclockwise)
        TurtleModel turtle1 = new TurtleModel();
        turtle1.turn(positiveAngle);
        assertEquals(positiveAngle, turtle1.getHeading(), 0.0001,
            String.format("Turning by positive angle %.2f° should result in heading %.2f°", 
                positiveAngle, positiveAngle));
        
        // Test negative angle (clockwise)
        TurtleModel turtle2 = new TurtleModel();
        turtle2.turn(negativeAngle);
        double expectedNegativeHeading = 360.0 + negativeAngle; // Convert negative to positive equivalent
        assertEquals(expectedNegativeHeading, turtle2.getHeading(), 0.0001,
            String.format("Turning by negative angle %.2f° should result in heading %.2f°", 
                negativeAngle, expectedNegativeHeading));
        
        // Test combination: positive then negative
        TurtleModel turtle3 = new TurtleModel();
        turtle3.turn(positiveAngle);
        turtle3.turn(negativeAngle);
        double expectedCombinedHeading = (positiveAngle + negativeAngle) % 360.0;
        if (expectedCombinedHeading < 0) {
            expectedCombinedHeading += 360.0;
        }
        assertEquals(expectedCombinedHeading, turtle3.getHeading(), 0.0001,
            String.format("Turning by %.2f° then %.2f° should result in heading %.2f°", 
                positiveAngle, negativeAngle, expectedCombinedHeading));
    }
    
    /**
     * Property 8 (Edge Cases): Turn Angle Addition Edge Cases
     * 
     * <p>Tests specific edge cases for turn angle addition:
     * <ul>
     *   <li>Turning by 0° should not change heading</li>
     *   <li>Turning by 360° should return to the same heading</li>
     *   <li>Turning by -360° should return to the same heading</li>
     *   <li>Turning by 180° twice should return to the same heading</li>
     * </ul>
     * </p>
     * 
     * <p><strong>Validates: Requirements 6.1, 6.3</strong>
     * 
     * @param initialHeading the starting heading in degrees
     */
    @Property(tries = 50)
    void turnAngleAdditionEdgeCasesProperty(
            @ForAll @DoubleRange(min = 0.0, max = 359.99) double initialHeading) {
        
        // Test turning by 0° (no change)
        TurtleModel turtle1 = new TurtleModel();
        turtle1.turn(initialHeading);
        double headingBefore = turtle1.getHeading();
        turtle1.turn(0.0);
        assertEquals(headingBefore, turtle1.getHeading(), 0.0001,
            String.format("Turning by 0° should not change heading from %.2f°", headingBefore));
        
        // Test turning by 360° (full rotation, return to same heading)
        TurtleModel turtle2 = new TurtleModel();
        turtle2.turn(initialHeading);
        headingBefore = turtle2.getHeading();
        turtle2.turn(360.0);
        assertEquals(headingBefore, turtle2.getHeading(), 0.0001,
            String.format("Turning by 360° should return to heading %.2f°", headingBefore));
        
        // Test turning by -360° (full rotation clockwise, return to same heading)
        TurtleModel turtle3 = new TurtleModel();
        turtle3.turn(initialHeading);
        headingBefore = turtle3.getHeading();
        turtle3.turn(-360.0);
        assertEquals(headingBefore, turtle3.getHeading(), 0.0001,
            String.format("Turning by -360° should return to heading %.2f°", headingBefore));
        
        // Test turning by 180° twice (should return to same heading)
        TurtleModel turtle4 = new TurtleModel();
        turtle4.turn(initialHeading);
        headingBefore = turtle4.getHeading();
        turtle4.turn(180.0);
        turtle4.turn(180.0);
        assertEquals(headingBefore, turtle4.getHeading(), 0.0001,
            String.format("Turning by 180° twice should return to heading %.2f°", headingBefore));
    }
    
    /**
     * Property 10: Pen State Persistence
     * 
     * <p>For any sequence of move commands without pen control commands, the pen state 
     * should remain constant throughout all movements.</p>
     * 
     * <p>This property verifies that:
     * <ul>
     *   <li>The pen state does not change during move operations</li>
     *   <li>The pen state does not change during turn operations</li>
     *   <li>The pen state only changes when explicitly set via setPenDown(), penUp(), or penDown()</li>
     * </ul>
     * </p>
     * 
     * <p><strong>Validates: Requirements 7.4</strong>
     * 
     * @param initialPenState the initial pen state (true = down, false = up)
     * @param distance1 first move distance
     * @param distance2 second move distance
     * @param distance3 third move distance
     * @param angle1 first turn angle
     * @param angle2 second turn angle
     */
    @Property(tries = 100)
    void penStatePersistenceProperty(
            @ForAll boolean initialPenState,
            @ForAll @DoubleRange(min = -200.0, max = 200.0) double distance1,
            @ForAll @DoubleRange(min = -200.0, max = 200.0) double distance2,
            @ForAll @DoubleRange(min = -200.0, max = 200.0) double distance3,
            @ForAll @DoubleRange(min = -180.0, max = 180.0) double angle1,
            @ForAll @DoubleRange(min = -180.0, max = 180.0) double angle2) {
        
        // Create a turtle and set the initial pen state
        TurtleModel turtle = new TurtleModel();
        turtle.setPenDown(initialPenState);
        
        // Verify initial pen state
        assertEquals(initialPenState, turtle.isPenDown(),
            String.format("Initial pen state should be %s", initialPenState ? "down" : "up"));
        
        // Perform a sequence of moves and turns WITHOUT changing pen state
        turtle.move(distance1);
        assertEquals(initialPenState, turtle.isPenDown(),
            String.format("Pen state should remain %s after first move", initialPenState ? "down" : "up"));
        
        turtle.turn(angle1);
        assertEquals(initialPenState, turtle.isPenDown(),
            String.format("Pen state should remain %s after first turn", initialPenState ? "down" : "up"));
        
        turtle.move(distance2);
        assertEquals(initialPenState, turtle.isPenDown(),
            String.format("Pen state should remain %s after second move", initialPenState ? "down" : "up"));
        
        turtle.turn(angle2);
        assertEquals(initialPenState, turtle.isPenDown(),
            String.format("Pen state should remain %s after second turn", initialPenState ? "down" : "up"));
        
        turtle.move(distance3);
        assertEquals(initialPenState, turtle.isPenDown(),
            String.format("Pen state should remain %s after third move", initialPenState ? "down" : "up"));
        
        // Verify the pen state is still the same as initially set
        assertEquals(initialPenState, turtle.isPenDown(),
            String.format("Pen state should remain %s throughout all operations", 
                initialPenState ? "down" : "up"));
    }
    
    /**
     * Property 10 (Pen Down): Pen State Persistence with Pen Down
     * 
     * <p>Tests that when the pen is down, it remains down through multiple operations,
     * and lines are created for each move.</p>
     * 
     * <p><strong>Validates: Requirements 7.4</strong>
     * 
     * @param distance1 first move distance
     * @param distance2 second move distance
     * @param distance3 third move distance
     */
    @Property(tries = 100)
    void penStatePersistencePenDownProperty(
            @ForAll @DoubleRange(min = 10.0, max = 200.0) double distance1,
            @ForAll @DoubleRange(min = 10.0, max = 200.0) double distance2,
            @ForAll @DoubleRange(min = 10.0, max = 200.0) double distance3) {
        
        // Create a turtle with pen down (default state)
        TurtleModel turtle = new TurtleModel();
        assertTrue(turtle.isPenDown(), "Pen should be down initially");
        
        // Move three times without changing pen state
        turtle.move(distance1);
        assertTrue(turtle.isPenDown(), "Pen should remain down after first move");
        assertEquals(1, turtle.getLines().size(), "Should have 1 line after first move");
        
        turtle.move(distance2);
        assertTrue(turtle.isPenDown(), "Pen should remain down after second move");
        assertEquals(2, turtle.getLines().size(), "Should have 2 lines after second move");
        
        turtle.move(distance3);
        assertTrue(turtle.isPenDown(), "Pen should remain down after third move");
        assertEquals(3, turtle.getLines().size(), "Should have 3 lines after third move");
    }
    
    /**
     * Property 10 (Pen Up): Pen State Persistence with Pen Up
     * 
     * <p>Tests that when the pen is up, it remains up through multiple operations,
     * and no lines are created.</p>
     * 
     * <p><strong>Validates: Requirements 7.4</strong>
     * 
     * @param distance1 first move distance
     * @param distance2 second move distance
     * @param distance3 third move distance
     */
    @Property(tries = 100)
    void penStatePersistencePenUpProperty(
            @ForAll @DoubleRange(min = 10.0, max = 200.0) double distance1,
            @ForAll @DoubleRange(min = 10.0, max = 200.0) double distance2,
            @ForAll @DoubleRange(min = 10.0, max = 200.0) double distance3) {
        
        // Create a turtle and lift the pen
        TurtleModel turtle = new TurtleModel();
        turtle.penUp();
        assertFalse(turtle.isPenDown(), "Pen should be up after calling penUp()");
        
        // Move three times without changing pen state
        turtle.move(distance1);
        assertFalse(turtle.isPenDown(), "Pen should remain up after first move");
        assertEquals(0, turtle.getLines().size(), "Should have 0 lines after first move with pen up");
        
        turtle.move(distance2);
        assertFalse(turtle.isPenDown(), "Pen should remain up after second move");
        assertEquals(0, turtle.getLines().size(), "Should have 0 lines after second move with pen up");
        
        turtle.move(distance3);
        assertFalse(turtle.isPenDown(), "Pen should remain up after third move");
        assertEquals(0, turtle.getLines().size(), "Should have 0 lines after third move with pen up");
    }
    
    /**
     * Property 10 (Convenience Methods): Pen State Persistence with Convenience Methods
     * 
     * <p>Tests that the convenience methods penUp() and penDown() correctly set the pen state
     * and that the state persists through subsequent operations.</p>
     * 
     * <p><strong>Validates: Requirements 7.4</strong>
     * 
     * @param distance move distance
     */
    @Property(tries = 100)
    void penStatePersistenceConvenienceMethodsProperty(
            @ForAll @DoubleRange(min = 10.0, max = 200.0) double distance) {
        
        // Create a turtle (pen down by default)
        TurtleModel turtle = new TurtleModel();
        assertTrue(turtle.isPenDown(), "Pen should be down initially");
        
        // Test penUp() convenience method
        turtle.penUp();
        assertFalse(turtle.isPenDown(), "Pen should be up after calling penUp()");
        
        // Move with pen up
        turtle.move(distance);
        assertFalse(turtle.isPenDown(), "Pen should remain up after move");
        assertEquals(0, turtle.getLines().size(), "Should have 0 lines with pen up");
        
        // Test penDown() convenience method
        turtle.penDown();
        assertTrue(turtle.isPenDown(), "Pen should be down after calling penDown()");
        
        // Move with pen down
        turtle.move(distance);
        assertTrue(turtle.isPenDown(), "Pen should remain down after move");
        assertEquals(1, turtle.getLines().size(), "Should have 1 line with pen down");
        
        // Test setPenDown(false)
        turtle.setPenDown(false);
        assertFalse(turtle.isPenDown(), "Pen should be up after calling setPenDown(false)");
        
        // Move with pen up
        turtle.move(distance);
        assertFalse(turtle.isPenDown(), "Pen should remain up after move");
        assertEquals(1, turtle.getLines().size(), "Should still have 1 line with pen up");
        
        // Test setPenDown(true)
        turtle.setPenDown(true);
        assertTrue(turtle.isPenDown(), "Pen should be down after calling setPenDown(true)");
        
        // Move with pen down
        turtle.move(distance);
        assertTrue(turtle.isPenDown(), "Pen should remain down after move");
        assertEquals(2, turtle.getLines().size(), "Should have 2 lines with pen down");
    }
    
    /**
     * Property 9: Clear Preserves Turtle State
     * 
     * <p>For any turtle state (position, heading, pen state), when the clear command is 
     * executed, the turtle's position, heading, and pen state should remain unchanged—only 
     * the line list should be cleared.</p>
     * 
     * <p>This property verifies that:
     * <ul>
     *   <li>The turtle's position (X and Y coordinates) remains unchanged after clearing</li>
     *   <li>The turtle's heading remains unchanged after clearing</li>
     *   <li>The turtle's pen state remains unchanged after clearing</li>
     *   <li>All drawn lines are removed from the lines list</li>
     * </ul>
     * </p>
     * 
     * <p><strong>Validates: Requirements 8.2</strong>
     * 
     * @param posX turtle X position
     * @param posY turtle Y position
     * @param heading turtle heading in degrees
     * @param penState turtle pen state (true = down, false = up)
     */
    @Property(tries = 100)
    void clearPreservesTurtleStateProperty(
            @ForAll @DoubleRange(min = -1000.0, max = 1000.0) double posX,
            @ForAll @DoubleRange(min = -1000.0, max = 1000.0) double posY,
            @ForAll @DoubleRange(min = 0.0, max = 359.99) double heading,
            @ForAll boolean penState) {
        
        // Create a turtle and set it to the desired state
        TurtleModel turtle = new TurtleModel();
        
        // Move to the desired position
        double distanceToPos = Math.sqrt(posX * posX + posY * posY);
        if (distanceToPos > 0.0001) {
            double angleToPos = Math.toDegrees(Math.atan2(posY, posX));
            turtle.turn(angleToPos);
            turtle.move(distanceToPos);
        }
        
        // Set to desired heading
        turtle.turn(heading - turtle.getHeading());
        
        // Set pen state
        turtle.setPenDown(penState);
        
        // Draw some lines if pen is down
        if (penState) {
            turtle.move(50.0);
            turtle.turn(90.0);
            turtle.move(50.0);
        }
        
        // Record the state before clearing
        java.awt.geom.Point2D.Double positionBefore = turtle.getPosition();
        double headingBefore = turtle.getHeading();
        boolean penStateBefore = turtle.isPenDown();
        int lineCountBefore = turtle.getLines().size();
        
        // Clear the canvas
        turtle.clear();
        
        // Record the state after clearing
        java.awt.geom.Point2D.Double positionAfter = turtle.getPosition();
        double headingAfter = turtle.getHeading();
        boolean penStateAfter = turtle.isPenDown();
        int lineCountAfter = turtle.getLines().size();
        
        // Verify position did NOT change
        assertEquals(positionBefore.x, positionAfter.x, 0.0001,
            String.format("X position should remain %.4f after clear", positionBefore.x));
        assertEquals(positionBefore.y, positionAfter.y, 0.0001,
            String.format("Y position should remain %.4f after clear", positionBefore.y));
        
        // Verify heading did NOT change
        assertEquals(headingBefore, headingAfter, 0.0001,
            String.format("Heading should remain %.2f° after clear", headingBefore));
        
        // Verify pen state did NOT change
        assertEquals(penStateBefore, penStateAfter,
            String.format("Pen state should remain %s after clear", penStateBefore ? "down" : "up"));
        
        // Verify lines WERE cleared
        assertEquals(0, lineCountAfter,
            String.format("Line count should be 0 after clear (was %d before)", lineCountBefore));
    }
    
    /**
     * Property 9 (Simplified): Clear Preserves Turtle State from Origin
     * 
     * <p>A simplified version of the clear preserves turtle state property that tests
     * clearing from a known state. This makes the test easier to understand and debug.</p>
     * 
     * <p><strong>Validates: Requirements 8.2</strong>
     * 
     * @param distance distance to move before clearing
     * @param turnAngle angle to turn before clearing
     */
    @Property(tries = 100)
    void clearPreservesTurtleStateSimplifiedProperty(
            @ForAll @DoubleRange(min = 10.0, max = 200.0) double distance,
            @ForAll @DoubleRange(min = -180.0, max = 180.0) double turnAngle) {
        
        // Create a turtle at the origin (0, 0) facing right (0°) with pen down
        TurtleModel turtle = new TurtleModel();
        
        // Move and turn to create some lines
        turtle.move(distance);
        turtle.turn(turnAngle);
        turtle.move(distance);
        
        // Verify we have lines
        int lineCountBefore = turtle.getLines().size();
        assertEquals(2, lineCountBefore, "Should have 2 lines before clear");
        
        // Record the state before clearing
        java.awt.geom.Point2D.Double positionBefore = turtle.getPosition();
        double headingBefore = turtle.getHeading();
        boolean penStateBefore = turtle.isPenDown();
        
        // Clear the canvas
        turtle.clear();
        
        // Verify position did NOT change
        assertEquals(positionBefore.x, turtle.getPosition().x, 0.0001,
            "X position should remain unchanged after clear");
        assertEquals(positionBefore.y, turtle.getPosition().y, 0.0001,
            "Y position should remain unchanged after clear");
        
        // Verify heading did NOT change
        assertEquals(headingBefore, turtle.getHeading(), 0.0001,
            "Heading should remain unchanged after clear");
        
        // Verify pen state did NOT change
        assertEquals(penStateBefore, turtle.isPenDown(),
            "Pen state should remain unchanged after clear");
        
        // Verify lines WERE cleared
        assertEquals(0, turtle.getLines().size(),
            "Line count should be 0 after clear");
    }
    
    /**
     * Property 9 (Multiple Clears): Clear Preserves Turtle State with Multiple Clears
     * 
     * <p>Tests that multiple clear operations preserve the turtle state each time,
     * and that clearing an already empty canvas has no effect on the turtle state.</p>
     * 
     * <p><strong>Validates: Requirements 8.2</strong>
     * 
     * @param distance distance to move
     */
    @Property(tries = 100)
    void clearPreservesTurtleStateMultipleClearsProperty(
            @ForAll @DoubleRange(min = 10.0, max = 200.0) double distance) {
        
        // Create a turtle and draw some lines
        TurtleModel turtle = new TurtleModel();
        turtle.move(distance);
        turtle.turn(90.0);
        turtle.move(distance);
        
        // Record state before first clear
        java.awt.geom.Point2D.Double pos1 = turtle.getPosition();
        double heading1 = turtle.getHeading();
        boolean penState1 = turtle.isPenDown();
        
        // First clear
        turtle.clear();
        assertEquals(0, turtle.getLines().size(), "Should have 0 lines after first clear");
        assertEquals(pos1.x, turtle.getPosition().x, 0.0001, "Position should be preserved after first clear");
        assertEquals(pos1.y, turtle.getPosition().y, 0.0001, "Position should be preserved after first clear");
        assertEquals(heading1, turtle.getHeading(), 0.0001, "Heading should be preserved after first clear");
        assertEquals(penState1, turtle.isPenDown(), "Pen state should be preserved after first clear");
        
        // Draw more lines
        turtle.move(distance);
        turtle.turn(90.0);
        turtle.move(distance);
        
        // Record state before second clear
        java.awt.geom.Point2D.Double pos2 = turtle.getPosition();
        double heading2 = turtle.getHeading();
        boolean penState2 = turtle.isPenDown();
        
        // Second clear
        turtle.clear();
        assertEquals(0, turtle.getLines().size(), "Should have 0 lines after second clear");
        assertEquals(pos2.x, turtle.getPosition().x, 0.0001, "Position should be preserved after second clear");
        assertEquals(pos2.y, turtle.getPosition().y, 0.0001, "Position should be preserved after second clear");
        assertEquals(heading2, turtle.getHeading(), 0.0001, "Heading should be preserved after second clear");
        assertEquals(penState2, turtle.isPenDown(), "Pen state should be preserved after second clear");
        
        // Clear again (empty canvas)
        turtle.clear();
        assertEquals(0, turtle.getLines().size(), "Should still have 0 lines after clearing empty canvas");
        assertEquals(pos2.x, turtle.getPosition().x, 0.0001, "Position should be preserved after clearing empty canvas");
        assertEquals(pos2.y, turtle.getPosition().y, 0.0001, "Position should be preserved after clearing empty canvas");
        assertEquals(heading2, turtle.getHeading(), 0.0001, "Heading should be preserved after clearing empty canvas");
        assertEquals(penState2, turtle.isPenDown(), "Pen state should be preserved after clearing empty canvas");
    }
}
