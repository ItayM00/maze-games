package Yg_Final_Project.solving_algorithms;

import java.util.List;

import Yg_Final_Project.Mazes.Maze;
import Yg_Final_Project.base_classes.Cell;

public interface MazeSolvingStrategy {
    

    void solveMaze(Cell[][] mat, Maze maze);

    List<Cell> solveFrom(Cell[][] mat, int startRow, int startCol, int goalRow, int goalCol);

    
}
