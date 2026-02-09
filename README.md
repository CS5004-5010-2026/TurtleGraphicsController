# Turtle Graphics GUI Application

A Java-based educational application demonstrating the Model-View-Controller (MVC) architectural pattern through an interactive turtle graphics environment. Users control a visible turtle on a graphical canvas using text commands, creating drawings in real-time.

## Features

- **Interactive GUI**: 800x600 pixel canvas with real-time turtle visualization
- **Text-based Commands**: Simple command interface for controlling the turtle
- **MVC Architecture**: Clean separation between Model, View, and Controller components
- **Visual Feedback**: Turtle changes color based on pen state (red = drawing, blue = not drawing)
- **Command History**: Scrollable history of all entered commands
- **Error Handling**: Clear error messages for invalid commands or parameters

## Architecture

The application follows the Model-View-Controller (MVC) design pattern:

### Model Layer (`turtlegraphics.model`)
- **TurtleModel**: Maintains turtle state (position, heading, pen state) and drawing history
- **ModelObserver**: Interface for observing model changes

### View Layer (`turtlegraphics.view`)
- **TurtleView**: Main GUI window with command input and history display
- **CanvasPanel**: Custom JPanel for rendering the turtle and drawn lines

### Controller Layer (`turtlegraphics.controller`)
- **TurtleController**: Processes commands and coordinates model updates
- **Command Interface**: Encapsulates each turtle operation as an executable object
- **Command Implementations**: MoveCommand, TurnCommand, PenUpCommand, PenDownCommand, ClearCommand, ResetCommand, QuitCommand, HelpCommand

## Building and Running

### Prerequisites
- Java 11 or later
- Gradle (wrapper included)

### Build the Application
```bash
./gradlew build
```

### Run the GUI Application
```bash
./gradlew runGUI
```

### Run Tests
```bash
./gradlew test
```

## Available Commands

| Command | Usage | Description |
|---------|-------|-------------|
| `move` | `move <distance>` | Move turtle forward (positive) or backward (negative) |
| `turn` | `turn <angle>` | Rotate turtle by angle in degrees (positive = counterclockwise) |
| `penup` | `penup` | Lift pen up (turtle moves without drawing) |
| `pendown` | `pendown` | Put pen down (turtle draws lines as it moves) |
| `clear` | `clear` | Clear all drawn lines (turtle position unchanged) |
| `reset` | `reset` | Reset turtle to center, facing right, pen down, and clear canvas |
| `quit` | `quit` | Exit the application |
| `help` | `help` | Display list of available commands |

## Example Command Sequences

### Draw a Square
```
move 100
turn 90
move 100
turn 90
move 100
turn 90
move 100
turn 90
```

### Draw a Triangle
```
move 100
turn 120
move 100
turn 120
move 100
turn 120
```

### Draw a Star
```
move 100
turn 144
move 100
turn 144
move 100
turn 144
move 100
turn 144
move 100
turn 144
```

### Draw with Gaps
```
move 50
penup
move 50
pendown
move 50
```

## Coordinate System

- **Model Coordinates**: Origin at center of canvas, Y-axis points up
- **Heading Convention**: 
  - 0° = right (positive X-axis)
  - 90° = up (positive Y-axis)
  - 180° = left (negative X-axis)
  - 270° = down (negative Y-axis)
- **Rotation**: Positive angles rotate counterclockwise, negative angles rotate clockwise

## Turtle Visualization

- **Color**: 
  - Red = pen is down (drawing)
  - Blue = pen is up (not drawing)
- **Shape**: Triangle pointing in the direction of the heading
- **Size**: Approximately 20 pixels from base to tip

## Project Structure

```
src/
├── main/
│   └── java/
│       └── turtlegraphics/
│           ├── TurtleGraphicsApp.java          # Main entry point
│           ├── model/
│           │   ├── TurtleModel.java            # Core data model
│           │   └── ModelObserver.java          # Observer interface
│           ├── view/
│           │   ├── TurtleView.java             # Main GUI window
│           │   └── CanvasPanel.java            # Canvas rendering
│           └── controller/
│               ├── TurtleController.java       # Command processor
│               ├── Command.java                # Command interface
│               ├── CommandException.java       # Command error handling
│               └── [Command implementations]   # Individual commands
└── test/
    └── java/
        └── turtlegraphics/
            └── model/
                ├── TurtleModelTest.java        # Unit tests
                └── TurtleModelProperties.java  # Property-based tests
```

## Design Principles

### MVC Separation
- **Model** has no dependencies on View or Controller
- **View** observes Model through the Observer pattern
- **Controller** mediates between View and Model

### Command Pattern
- Each command is encapsulated as an object implementing the Command interface
- Commands are registered in a map for easy lookup and execution
- Supports easy addition of new commands without modifying existing code

### Observer Pattern
- Model notifies registered observers when state changes
- View automatically refreshes when model updates
- Decouples model from view implementation

## Testing

The project includes comprehensive testing:

- **Unit Tests**: Test specific functionality and edge cases
- **Property-Based Tests**: Verify universal correctness properties across many generated inputs
- **Integration Tests**: Test complete command sequences and end-to-end functionality

Run tests with:
```bash
./gradlew test
```

## Educational Use

This application is designed for CS 5004/5010 courses to demonstrate:
- Model-View-Controller architecture
- Observer pattern
- Command pattern
- Java Swing GUI programming
- Coordinate system transformations
- Test-driven development
- Property-based testing

## License

Educational use only. Part of CS 5004/5010 course materials.

## Authors

CS 5004/5010 Course Staff
