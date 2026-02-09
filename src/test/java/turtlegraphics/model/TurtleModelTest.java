package turtlegraphics.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TurtleModel class.
 * Tests verify correct initialization and state management.
 */
class TurtleModelTest {
    
    private TurtleModel turtle;
    
    @BeforeEach
    void setUp() {
        turtle = new TurtleModel();
    }
    
    /**
     * Test that the turtle is initialized at position (0, 0).
     * Validates Requirement 2.5: Initial position at center.
     */
    @Test
    void testInitialPosition() {
        Point2D.Double position = turtle.getPosition();
        assertEquals(0.0, position.x, 0.0001, "Initial X position should be 0");
        assertEquals(0.0, position.y, 0.0001, "Initial Y position should be 0");
    }
    
    /**
     * Test that the turtle is initialized with heading 0° (facing right).
     * Validates Requirement 2.5: Initial heading 0°.
     */
    @Test
    void testInitialHeading() {
        assertEquals(0.0, turtle.getHeading(), 0.0001, "Initial heading should be 0°");
    }
    
    /**
     * Test that the turtle is initialized with pen down.
     * Validates Requirement 7.3: Initial pen state is down.
     */
    @Test
    void testInitialPenState() {
        assertTrue(turtle.isPenDown(), "Initial pen state should be down");
    }
    
    /**
     * Test that the turtle is initialized with an empty lines list.
     * Validates Requirement 9.5: Initial state has no drawn lines.
     */
    @Test
    void testInitialLinesEmpty() {
        assertTrue(turtle.getLines().isEmpty(), "Initial lines list should be empty");
    }
    
    /**
     * Test that getPosition returns a defensive copy.
     * Modifying the returned position should not affect the turtle's internal state.
     * Validates Requirement 13.5: Immutable access to state.
     */
    @Test
    void testGetPositionReturnsDefensiveCopy() {
        Point2D.Double position1 = turtle.getPosition();
        position1.x = 100.0;
        position1.y = 200.0;
        
        Point2D.Double position2 = turtle.getPosition();
        assertEquals(0.0, position2.x, 0.0001, "Modifying returned position should not affect turtle state");
        assertEquals(0.0, position2.y, 0.0001, "Modifying returned position should not affect turtle state");
    }
    
    /**
     * Test that getLines returns an immutable list.
     * Attempting to modify the returned list should throw an exception.
     * Validates Requirement 13.5: Immutable access to state.
     */
    @Test
    void testGetLinesReturnsImmutableList() {
        assertThrows(UnsupportedOperationException.class, () -> {
            turtle.getLines().clear();
        }, "getLines should return an immutable list");
    }
    
    /**
     * Test that move updates position correctly when heading is 0° (facing right).
     * Validates Requirements 5.1, 5.4, 13.3: Movement position calculation.
     */
    @Test
    void testMoveRightWithHeadingZero() {
        turtle.move(100.0);
        Point2D.Double position = turtle.getPosition();
        assertEquals(100.0, position.x, 0.0001, "Moving 100 units right should update X to 100");
        assertEquals(0.0, position.y, 0.0001, "Moving right should not change Y");
    }
    
    /**
     * Test that move updates position correctly when heading is 90° (facing up).
     * Validates Requirements 5.1, 5.4, 13.3: Movement position calculation.
     */
    @Test
    void testMoveUpWithHeadingNinety() {
        turtle.turn(90.0);
        turtle.move(100.0);
        Point2D.Double position = turtle.getPosition();
        assertEquals(0.0, position.x, 0.0001, "Moving up should not change X");
        assertEquals(100.0, position.y, 0.0001, "Moving 100 units up should update Y to 100");
    }
    
    /**
     * Test that move updates position correctly when heading is 180° (facing left).
     * Validates Requirements 5.1, 5.4, 13.3: Movement position calculation.
     */
    @Test
    void testMoveLeftWithHeadingOneEighty() {
        turtle.turn(180.0);
        turtle.move(100.0);
        Point2D.Double position = turtle.getPosition();
        assertEquals(-100.0, position.x, 0.0001, "Moving 100 units left should update X to -100");
        assertEquals(0.0, position.y, 0.0001, "Moving left should not change Y");
    }
    
    /**
     * Test that move updates position correctly when heading is 270° (facing down).
     * Validates Requirements 5.1, 5.4, 13.3: Movement position calculation.
     */
    @Test
    void testMoveDownWithHeadingTwoSeventy() {
        turtle.turn(270.0);
        turtle.move(100.0);
        Point2D.Double position = turtle.getPosition();
        assertEquals(0.0, position.x, 0.0001, "Moving down should not change X");
        assertEquals(-100.0, position.y, 0.0001, "Moving 100 units down should update Y to -100");
    }
    
    /**
     * Test that move with negative distance moves backward.
     * Validates Requirement 5.4: Negative distance moves backward.
     */
    @Test
    void testMoveBackward() {
        turtle.move(-50.0);
        Point2D.Double position = turtle.getPosition();
        assertEquals(-50.0, position.x, 0.0001, "Moving -50 units should move backward (left) to X = -50");
        assertEquals(0.0, position.y, 0.0001, "Moving backward should not change Y");
    }
    
    /**
     * Test that move with pen down creates a line.
     * Validates Requirements 3.1, 5.2: Pen down creates lines.
     */
    @Test
    void testMovePenDownCreatesLine() {
        assertTrue(turtle.isPenDown(), "Pen should start down");
        turtle.move(100.0);
        assertEquals(1, turtle.getLines().size(), "Moving with pen down should create one line");
    }
    
    /**
     * Test that move with pen up does not create a line.
     * Validates Requirements 3.2, 5.3: Pen up no lines.
     */
    @Test
    void testMovePenUpNoLine() {
        turtle.setPenDown(false);
        assertFalse(turtle.isPenDown(), "Pen should be up");
        turtle.move(100.0);
        assertEquals(0, turtle.getLines().size(), "Moving with pen up should not create any lines");
    }
    
    /**
     * Test that multiple moves with pen down create multiple lines.
     * Validates Requirement 3.4: Line preservation.
     */
    @Test
    void testMultipleMovesCreateMultipleLines() {
        turtle.move(50.0);
        turtle.turn(90.0);
        turtle.move(50.0);
        turtle.turn(90.0);
        turtle.move(50.0);
        assertEquals(3, turtle.getLines().size(), "Three moves with pen down should create three lines");
    }
    
    /**
     * Test that move can go beyond canvas boundaries.
     * Validates Requirement 5.5: Off-screen movement allowed.
     */
    @Test
    void testMoveOffScreen() {
        turtle.move(10000.0);
        Point2D.Double position = turtle.getPosition();
        assertEquals(10000.0, position.x, 0.0001, "Turtle should be able to move off-screen");
    }
    
    /**
     * Test that reset sets position to (0, 0).
     * Validates Requirement 8.3: Reset returns to center position.
     */
    @Test
    void testResetPosition() {
        // Move turtle away from origin
        turtle.move(100.0);
        turtle.turn(90.0);
        turtle.move(100.0);
        
        // Verify turtle is not at origin
        Point2D.Double positionBefore = turtle.getPosition();
        assertNotEquals(0.0, positionBefore.x, 0.0001, "Turtle should not be at origin before reset");
        assertNotEquals(0.0, positionBefore.y, 0.0001, "Turtle should not be at origin before reset");
        
        // Reset
        turtle.reset();
        
        // Verify position is (0, 0)
        Point2D.Double positionAfter = turtle.getPosition();
        assertEquals(0.0, positionAfter.x, 0.0001, "Reset should set X position to 0");
        assertEquals(0.0, positionAfter.y, 0.0001, "Reset should set Y position to 0");
    }
    
    /**
     * Test that reset sets heading to 0°.
     * Validates Requirement 8.3: Reset returns to 0° heading.
     */
    @Test
    void testResetHeading() {
        // Turn turtle to a different heading
        turtle.turn(135.0);
        assertEquals(135.0, turtle.getHeading(), 0.0001, "Turtle should be at 135° before reset");
        
        // Reset
        turtle.reset();
        
        // Verify heading is 0°
        assertEquals(0.0, turtle.getHeading(), 0.0001, "Reset should set heading to 0°");
    }
    
    /**
     * Test that reset sets pen state to down.
     * Validates Requirement 8.3: Reset sets pen down.
     */
    @Test
    void testResetPenState() {
        // Lift pen up
        turtle.penUp();
        assertFalse(turtle.isPenDown(), "Pen should be up before reset");
        
        // Reset
        turtle.reset();
        
        // Verify pen is down
        assertTrue(turtle.isPenDown(), "Reset should set pen state to down");
    }
    
    /**
     * Test that reset clears all lines.
     * Validates Requirement 8.4: Reset clears all drawn lines.
     */
    @Test
    void testResetClearsLines() {
        // Draw some lines
        turtle.move(50.0);
        turtle.turn(90.0);
        turtle.move(50.0);
        turtle.turn(90.0);
        turtle.move(50.0);
        
        // Verify lines exist
        assertEquals(3, turtle.getLines().size(), "Should have 3 lines before reset");
        
        // Reset
        turtle.reset();
        
        // Verify lines are cleared
        assertEquals(0, turtle.getLines().size(), "Reset should clear all lines");
    }
    
    /**
     * Test that reset returns turtle to complete initial state.
     * Validates Requirements 8.3, 8.4: Reset returns to initial state.
     */
    @Test
    void testResetCompleteState() {
        // Modify turtle state completely
        turtle.move(200.0);
        turtle.turn(270.0);
        turtle.penUp();
        turtle.move(100.0);
        turtle.penDown();
        turtle.move(50.0);
        
        // Verify state is modified
        assertNotEquals(0.0, turtle.getPosition().x, 0.0001, "Position should be modified");
        assertNotEquals(0.0, turtle.getHeading(), 0.0001, "Heading should be modified");
        assertTrue(turtle.getLines().size() > 0, "Should have lines");
        
        // Reset
        turtle.reset();
        
        // Verify complete initial state
        assertEquals(0.0, turtle.getPosition().x, 0.0001, "Reset should set X to 0");
        assertEquals(0.0, turtle.getPosition().y, 0.0001, "Reset should set Y to 0");
        assertEquals(0.0, turtle.getHeading(), 0.0001, "Reset should set heading to 0°");
        assertTrue(turtle.isPenDown(), "Reset should set pen down");
        assertEquals(0, turtle.getLines().size(), "Reset should clear all lines");
    }
    
    /**
     * Test that reset can be called multiple times.
     * Validates Requirements 8.3, 8.4: Reset is idempotent.
     */
    @Test
    void testResetMultipleTimes() {
        // First reset (from initial state)
        turtle.reset();
        assertEquals(0.0, turtle.getPosition().x, 0.0001, "First reset should work");
        assertEquals(0.0, turtle.getPosition().y, 0.0001, "First reset should work");
        assertEquals(0.0, turtle.getHeading(), 0.0001, "First reset should work");
        assertTrue(turtle.isPenDown(), "First reset should work");
        assertEquals(0, turtle.getLines().size(), "First reset should work");
        
        // Modify state
        turtle.move(100.0);
        turtle.turn(45.0);
        
        // Second reset
        turtle.reset();
        assertEquals(0.0, turtle.getPosition().x, 0.0001, "Second reset should work");
        assertEquals(0.0, turtle.getPosition().y, 0.0001, "Second reset should work");
        assertEquals(0.0, turtle.getHeading(), 0.0001, "Second reset should work");
        assertTrue(turtle.isPenDown(), "Second reset should work");
        assertEquals(0, turtle.getLines().size(), "Second reset should work");
        
        // Third reset (from reset state)
        turtle.reset();
        assertEquals(0.0, turtle.getPosition().x, 0.0001, "Third reset should work");
        assertEquals(0.0, turtle.getPosition().y, 0.0001, "Third reset should work");
        assertEquals(0.0, turtle.getHeading(), 0.0001, "Third reset should work");
        assertTrue(turtle.isPenDown(), "Third reset should work");
        assertEquals(0, turtle.getLines().size(), "Third reset should work");
    }
}

