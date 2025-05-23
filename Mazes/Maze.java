package Yg_Final_Project.Mazes;

import java.util.ArrayList;
import javax.swing.*;

import java.util.List;

import Yg_Final_Project.base_classes.Cell;
import Yg_Final_Project.generation_algorithms.MazeGenerationStrategy;
import Yg_Final_Project.solving_algorithms.MazeSolvingStrategy;

public class Maze extends JPanel
{
    protected int rowsNum;
    protected int colsNum;
    protected int cellSize;

	protected Cell mat[][];
    protected static final int PADDING = 20; 
    protected MazeGenerationStrategy generationStrategy;
    protected MazeSolvingStrategy solvingStrategy;

    protected List<Cell> solutionPath = new ArrayList<>();
    
	
// --------- Constructor ------------

	public Maze (int size, MazeGenerationStrategy generationStrategy, MazeSolvingStrategy solvingStrategy)
	{
        rowsNum = colsNum = size;
        cellSize = 0;
        mat = new Cell [rowsNum][colsNum];
        
        for(int i = 0 ; i < rowsNum ; i++){
            for(int j = 0 ; j < colsNum ; j++){
                Cell c = new Cell(i,j, null);
                mat[i][j] = c;
            }
        }	

        this.generationStrategy = generationStrategy;
        generationStrategy.generateMaze(mat);

        this.solvingStrategy = solvingStrategy;
	}




// ---------------- Helper Methods For The Maze --------------------

    public ArrayList<Cell> getWithAlgoVisit(int i, int j, int previousComputerRow, int previousComputerCol){
        ArrayList<Cell> validCells = new ArrayList<>();
        Cell current = mat[i][j];

        if (i > 0 && mat[i - 1][j].getAlgoVisit() && !current.isTopOn() && (i - 1 != previousComputerRow || j != previousComputerCol)) {
            validCells.add(mat[i - 1][j]);
        }   
        // Check down
        if (i < mat.length - 1 && mat[i + 1][j].getAlgoVisit() && !current.isBottomOn() && (i + 1 != previousComputerRow || j != previousComputerCol)) {
            validCells.add(mat[i + 1][j]);
        }
        // Check left
        if (j > 0 && mat[i][j - 1].getAlgoVisit() && !current.isLeftOn() && (i != previousComputerRow || j - 1 != previousComputerCol)) {
            validCells.add(mat[i][j - 1]);
        }
        // Check right
        if (j < mat[0].length - 1 && mat[i][j + 1].getAlgoVisit() && !current.isRightOn() && (i != previousComputerRow || j + 1 != previousComputerCol)) {
            validCells.add(mat[i][j + 1]);
        }
        
        return validCells;
    }

    public void resetAlgoVisits(){
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                mat[i][j].setAlgoVisit(false);
            }
        }
    }
    
// --------------------- Getter and Setter ------------------------------

    public Cell[][] getMat() {
        return mat;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setSolvingStrategy(MazeSolvingStrategy solvingStrategy) {
        this.solvingStrategy = solvingStrategy;
    }   

    public MazeSolvingStrategy getSolvingStrategy() {
        return solvingStrategy;
    }

    public void setSolutionPath(List<Cell> path) {
        this.solutionPath = path;
    }
    
    public List<Cell> getSolutionPath() {
        return solutionPath;
    }

    
}
