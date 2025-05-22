package Yg_Final_Project.generation_algorithms;

import java.util.ArrayList;
import java.util.Collections;

import Yg_Final_Project.base_classes.Cell;

public class DFSAlgorithm implements MazeGenerationStrategy{

    @Override
    public void generateMaze(Cell[][] mat) {
        int rows = mat.length, cols = mat[0].length;
        int startRow = (int) (Math.random() * rows);
        int startCol = (int) (Math.random() * cols);
        recursiveDFS(mat, startRow, startCol);

        // Open entrance and exit
        mat[0][0].setLeft(false);
        mat[0][0].setHumanVisit(true);
        mat[rows - 1][cols - 1].setRight(false);
        mat[rows - 1][cols - 1].setComputerVisit(true);
    }

    public void recursiveDFS(Cell[][] mat, int row, int col) {
        mat[row][col].setAlgoVisit(true);
        ArrayList<Cell> neighbors = getCellsWith4Walls(mat, row, col);
        Collections.shuffle(neighbors);

        for (Cell neighbor : neighbors) {
            int newRow = neighbor.getRow();
            int newCol = neighbor.getColumn();

            if (!neighbor.getAlgoVisit()) {
                if (newRow < row) { // UP
                    mat[row][col].setTop(false); 
                    neighbor.setBottom(false); 
                }
                if (newRow > row) { // DOWN
                    mat[row][col].setBottom(false); 
                    neighbor.setTop(false); 
                }
                if (newCol < col) { // LEFT
                    mat[row][col].setLeft(false); 
                    neighbor.setRight(false); 
                }
                if (newCol > col) { // RIGHT
                    mat[row][col].setRight(false); 
                    neighbor.setLeft(false);
                }

                recursiveDFS(mat, newRow, newCol);
            }
        }
    }

    public ArrayList<Cell> getCellsWith4Walls(Cell[][] mat, int i, int j) {
        ArrayList<Cell> validCells = new ArrayList<>();

        if (i > 0 && mat[i - 1][j].has4walls()) validCells.add(mat[i - 1][j]);
        if (i < mat.length - 1 && mat[i + 1][j].has4walls()) validCells.add(mat[i + 1][j]);
        if (j > 0 && mat[i][j - 1].has4walls()) validCells.add(mat[i][j - 1]);
        if (j < mat[0].length - 1 && mat[i][j + 1].has4walls()) validCells.add(mat[i][j + 1]);

        return validCells;
    }
    
}
