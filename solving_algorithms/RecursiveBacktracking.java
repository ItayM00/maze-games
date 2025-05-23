package Yg_Final_Project.solving_algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Yg_Final_Project.Mazes.Maze;
import Yg_Final_Project.base_classes.Cell;

public class RecursiveBacktracking implements MazeSolvingStrategy{

    private List<Cell> path = new ArrayList<>();
    private boolean[][] visitedMat;

    @Override
    public void solveMaze(Cell[][] mat, Maze maze){
        visitedMat = new boolean[mat.length][mat[0].length];
        resetMatVisits();
        
        boolean result = moveDFSAlgorithm(mat.length-1, mat.length-1, mat, 0, 0);

        if (result) {
            System.out.println("Path length: " + path.size());
            Collections.reverse(path); // reverse the path for easier extruction for a move
            maze.setSolutionPath(path); 
        }
    }

    public boolean moveDFSAlgorithm(int row, int col, Cell[][] mat, int goalRow, int goalCol) {        
        Cell currentCell = mat[row][col];
        visitedMat[row][col] = true;
        // currentCell.setAlgoVisit(true);
        
        if (row == goalRow && col == goalCol) {
            path.add(currentCell);
            return true;
        }

        ArrayList<Cell> validCells = getCellsWithoutAlgoVisit(row, col, mat);

        for (Cell neighbor : validCells) {
            int newRow = neighbor.getRow();
            int newCol = neighbor.getColumn();
                        
            if (!visitedMat[newRow][newCol]) {
                if (moveDFSAlgorithm(newRow, newCol, mat, goalRow, goalCol)) {
                    path.add(currentCell);
                    return true;
                }
            }
        }

        visitedMat[row][col] = false;
        // currentCell.setAlgoVisit(false);
        return false;
    }

    public ArrayList<Cell> getCellsWithoutAlgoVisit(int i, int j, Cell[][] mat) {
        ArrayList<Cell> validCells = new ArrayList<>();
        Cell current = mat[i][j];
        
        // Check up
        if (i > 0 && !visitedMat[i - 1][j] && !current.isTopOn() && !mat[i-1][j].isBottomOn()){validCells.add(mat[i - 1][j]);}   
        // Check down
        if (i < mat.length - 1 && !visitedMat[i + 1][j] && !current.isBottomOn() && !mat[i+1][j].isTopOn()) { validCells.add(mat[i + 1][j]); }
        // Check left
        if (j > 0 && !visitedMat[i][j - 1]&& !current.isLeftOn() && !mat[i][j-1].isRightOn()) { validCells.add(mat[i][j - 1]); }
        // Check right
        if (j < mat[0].length - 1 && !visitedMat[i][j + 1] && !current.isRightOn() && !mat[i][j+1].isLeftOn()) { validCells.add(mat[i][j + 1]); }
        
        return validCells;
    }
    
    public void resetMatVisits(){
        for (int i = 0; i < visitedMat.length; i++) {
            for (int j = 0; j < visitedMat[0].length; j++) {
                visitedMat[i][j] = false;
            }
        }
    }

    @Override
    public List<Cell> solveFrom(Cell[][] mat, int startRow, int startCol, int goalRow, int goalCol) {
        visitedMat = new boolean[mat.length][mat[0].length];
        resetMatVisits();

        moveDFSAlgorithm(startRow, startCol, mat, goalRow, goalCol);

        return path;
    }

}
