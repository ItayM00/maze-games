package Yg_Final_Project.solving_algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Yg_Final_Project.Cell;
import Yg_Final_Project.Mazes.Maze;

public class RecursiveBacktracking implements MazeSolvingStrategy{

    private List<Cell> path = new ArrayList<>();

    @Override
    public void solveMaze(Cell[][] mat, Maze maze){
        resetAlgoVisits(mat);
        System.out.println("Starting DFS from: " + (mat.length-1) + "," + (mat.length-1));
        boolean result = moveDFSAlgorithm(mat.length-1, mat.length-1, mat);

        if (result) {
            System.out.println("Path length: " + path.size());
            Collections.reverse(path); // reverse the path for easier extruction for a move
            maze.setSolutionPath(path); 
        }

        // Print the maze after DFS
        System.out.println("DFS result: " + result);
    }

    public boolean moveDFSAlgorithm(int row, int col, Cell[][] mat) {        
        Cell currentCell = mat[row][col];
        currentCell.setAlgoVisit(true);
        
        if (row == 0 && col == 0) {
            path.add(currentCell);
            return true;
        }

        ArrayList<Cell> validCells = getCellsWithoutAlgoVisit(row, col, mat);

        for (Cell neighbor : validCells) {
            int newRow = neighbor.getRow();
            int newCol = neighbor.getColumn();
                        
            if (!neighbor.getAlgoVisit()) {
                if (moveDFSAlgorithm(newRow, newCol, mat)) {
                    path.add(currentCell);
                    return true;
                }
            }
        }

        currentCell.setAlgoVisit(false);
        return false;
    }

    public ArrayList<Cell> getCellsWithoutAlgoVisit(int i, int j, Cell[][] mat) {
        ArrayList<Cell> validCells = new ArrayList<>();
        Cell current = mat[i][j];
        
        // Check up
        if (i > 0 && !mat[i - 1][j].getAlgoVisit() && !current.isTopOn() && !mat[i-1][j].isBottomOn()){validCells.add(mat[i - 1][j]);}   
        // Check down
        if (i < mat.length - 1 && !mat[i + 1][j].getAlgoVisit() && !current.isBottomOn() && !mat[i+1][j].isTopOn()) { validCells.add(mat[i + 1][j]); }
        // Check left
        if (j > 0 && !mat[i][j - 1].getAlgoVisit() && !current.isLeftOn() && !mat[i][j-1].isRightOn()) { validCells.add(mat[i][j - 1]); }
        // Check right
        if (j < mat[0].length - 1 && !mat[i][j + 1].getAlgoVisit() && !current.isRightOn() && !mat[i][j+1].isLeftOn()) { validCells.add(mat[i][j + 1]); }
        
        return validCells;
    }
    
    public void resetAlgoVisits(Cell[][] mat){
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                mat[i][j].setAlgoVisit(false);
            }
        }
    }

}
