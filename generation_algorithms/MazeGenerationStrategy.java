package Yg_Final_Project.generation_algorithms;

import Yg_Final_Project.base_classes.Cell;

public interface MazeGenerationStrategy {
    
/*
    GENERAL METHOD THAT CREATES THE MAZE BASED 
    ON A CERTAIN ALGORITHM.
*/
    void generateMaze(Cell[][] mat);
/*
    IN THE MAZE CLASS WE WILL CREATE AN ATTRIBUTE:
    PRIVATE MAZE_GENERATION_STRATEGY STRATEGY;

    IN THE INIT FUNC WE WILL GET THIS INTERFACE "OBJECT"
    AND THE WE WILL CALL THIS FUNCTION ON THE MAT.
    IT WILL LOOK SOMTHING LIKE THIS:
    public Maze(int size, MazeGenerationStrategy strategy) {
        // REST OF THE CODE
        
        this.strategy = strategy;
        strategy.generateMaze(mat);
    }

    AND IN THE GAMEFRAME WE WILL WRITE THIS:
    MAZE.SET_STRATEGY(SIZE, NEW DFS_ALGORITHM())
*/

}
