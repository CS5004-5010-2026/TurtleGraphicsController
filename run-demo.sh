#!/bin/bash
# Demo script to show Turtle Graphics in action

cd "$(dirname "$0")"

echo "=== Building project ==="
./gradlew build -q

echo ""
echo "=== Running Turtle Graphics Demo ==="
echo ""
echo "Commands being executed:"
echo "  move 100"
echo "  turn 90"
echo "  move 100"
echo "  turn 90"
echo "  move 100"
echo "  turn 90"
echo "  move 100"
echo "  trace"
echo "  quit"
echo ""
echo "Output:"
echo ""

# Run the demo program that shows output
java -cp build/classes/java/main turtle.TurtleDemo

echo ""
echo "=== Demo Complete ==="
