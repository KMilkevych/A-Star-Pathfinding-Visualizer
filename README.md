# A* Pathfinding Visualizer

## Description

The __A* Pathfinding Visualizer__ is a desktop GUI application written in Java using Swing as the main GUI framework.
This application features the following:
- Dynamic GUI.
- Drawing 2D "maze" by placing walls, as well as a start and end node.
- Computing the shortest path from start to end node, using
  - A* Pathfinding Algorithm
  - Breadth First Search
- Visualizing the computational process of the shortest path.

## Getting started

To get started with __A* Pathfinding Visualizer__ download the latest release from the [releases](https://github.com/KMilkevych/A-Star-Pathfinding-Visualizer/releases/) page, or clone this repository and compile using
```
javac -d ./bin ./src/*.java
```
Then run using:
```
cd bin
java ./bin/PathfindingVisualizer
```

## How to use
### Basic controls

- Use the left mouse button to interact with GUI components, as well as draw tiles on the board.
- Use the right mouse button inside board to pan around.
- Use the scroll wheel inside board to zoom in and out.

### Drawing a maze
Select the the kind of tile you'd like to draw with, in the toolbar to the right:
![Toolbar](link)
The colors symbolize different kind of tiles:
- RED: Start tile
- BLUE: End tile
- BLACK: Wall tile
- GRAY: Eraser/Free tile

Then draw the tile unto the board using left-click:
![Drawing](link)

Remember to place a start and end tile before proceeding to the next step.

### Choosing a pathfinding algorithm

In the Settings menu at the left, select the desired pathfinding algorithm in the combobox:
![Combobox](link)

### Running the visualization

To run the visualization, press the Run button in the Configuration menu at the left:
![Run](link)

### Clearing the board

To clear the board, press the Clear button in the Configuration menu at the left:
![Clear](link)
