# Turtle Graphics Controller - In-Class Exercise

Build a Turtle Graphics Controller using MVC architecture and the Command Pattern.

## What You'll Build

In this exercise, you'll implement the Controller layer for a Turtle Graphics application. The Model and View are provided - you focus on command processing.

### Provided (Complete)
- ✅ **TurtleModel** - State management (position, heading, pen state)
- ✅ **TurtleView** - GUI with canvas and command history
- ✅ **Test Suite** - Comprehensive tests to verify your implementation

### Your Task (Implement)
- ❌ **TurtleController** - Parse and execute commands
- ❌ **8 Command Classes** - move, turn, penup, pendown, clear, reset, quit, help

## Getting Started

### Prerequisites
- Java 11 or later
- Gradle (wrapper included)

### Setup
```bash
# Clone the repository
git clone https://github.com/CS5004-5010-2026/TurtleGraphicsController.git
cd TurtleGraphicsController

# Verify it builds (tests will fail until you implement the controller)
./gradlew build

# Run tests (will fail initially)
./gradlew test
```

### Implementation Steps

1. **Implement TurtleController.executeCommand()**
   - Parse command string into command name and arguments
   - Look up command in command map
   - Execute command and handle errors
   - Provide feedback to view

2. **Implement TurtleController.registerCommands()**
   - Create instances of all 8 command classes
   - Register them in the command map

3. **Implement each Command.execute() method**
   - MoveCommand - Parse distance, call model.move()
   - TurnCommand - Parse angle, call model.turn()
   - PenUpCommand - Call model.penUp()
   - PenDownCommand - Call model.penDown()
   - ClearCommand - Call model.clear()
   - ResetCommand - Call model.reset()
   - QuitCommand - Call System.exit(0)
   - HelpCommand - Generate help text from command map

4. **Run tests to verify**
   ```bash
   ./gradlew test
   ```

5. **Run the GUI**
   ```bash
   ./gradlew runGUI
   ```

## Available Commands

| Command | Usage | Description |
|---------|-------|-------------|
| `move` | `move <distance>` | Move turtle forward/backward |
| `turn` | `turn <angle>` | Rotate turtle (positive = counterclockwise) |
| `penup` | `penup` | Lift pen (stop drawing) |
| `pendown` | `pendown` | Lower pen (start drawing) |
| `clear` | `clear` | Clear all lines |
| `reset` | `reset` | Reset to initial state |
| `quit` | `quit` | Exit application |
| `help` | `help` | Show available commands |

## Testing Your Implementation

All tests should pass when your implementation is complete:

```bash
./gradlew test
```

## Architecture

### MVC Pattern
- **Model** (Provided) - TurtleModel maintains state
- **View** (Provided) - TurtleView displays GUI
- **Controller** (You Build) - TurtleController processes commands

### Command Pattern
- Each command is an object implementing the Command interface
- Commands are registered in a map for easy lookup
- Supports extensibility - easy to add new commands

## Example Drawing Sequences

### Draw a Square
```
move 100
turn 90
move 100
turn 90
move 100
turn 90
move 100
```

### Draw a Triangle
```
move 100
turn 120
move 100
turn 120
move 100
```

### Draw with Gaps
```
move 50
penup
move 50
pendown
move 50
```

## Need Help?

- Check the Javadoc comments in the provided code
- Review the test cases to understand expected behavior
- Ask your instructor or TA

## License

Educational use only. Part of CS 5004/5010 course materials.
