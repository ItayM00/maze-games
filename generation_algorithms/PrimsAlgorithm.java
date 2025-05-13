package Yg_Final_Project.generation_algorithms;

import Yg_Final_Project.Cell;
import java.util.ArrayList;
import java.util.Random;

public class PrimsAlgorithm implements MazeGenerationStrategy {
    private Random random = new Random();
    private double multiPathProbability = 0.15;

    @Override
    public void generateMaze(Cell[][] mat) {
        int rows = mat.length, cols = mat[0].length;
        
        // Mark all cells as unvisited
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mat[i][j].setAlgoVisit(false);
            }
        }
        
        // Start at a random cell
        int startRow = random.nextInt(rows);
        int startCol = random.nextInt(cols);
        
        // Mark the starting cell as part of the maze
        mat[startRow][startCol].setAlgoVisit(true);
        
        // Add all neighbors of the starting cell to the frontier
        ArrayList<Cell> frontier = new ArrayList<>();
        addNeighborsToFrontier(mat, startRow, startCol, frontier);
        
        // continue until there are no more cells in the frontier
        while (!frontier.isEmpty()) {
            // Pick a random cell from the frontier
            int index = random.nextInt(frontier.size());
            Cell current = frontier.get(index);
            frontier.remove(index);
            
            int currRow = current.getRow();
            int currCol = current.getColumn();
            
            // Find all visited neighbors of the current frontier cell
            ArrayList<Cell> visitedNeighbors = getVisitedNeighbors(mat, currRow, currCol);
            
            if (!visitedNeighbors.isEmpty()) {
                // Choose a random visited neighbor
                Cell neighbor = visitedNeighbors.get(random.nextInt(visitedNeighbors.size()));
                int neighborRow = neighbor.getRow();
                int neighborCol = neighbor.getColumn();
                
                // Connect the current cell with the chosen neighbor by removing walls
                if (neighborRow < currRow) { // UP
                    mat[currRow][currCol].setTop(false);
                    neighbor.setBottom(false);
                }
                if (neighborRow > currRow) { // DOWN
                    mat[currRow][currCol].setBottom(false);
                    neighbor.setTop(false);
                }
                if (neighborCol < currCol) { // LEFT
                    mat[currRow][currCol].setLeft(false);
                    neighbor.setRight(false);
                }
                if (neighborCol > currCol) { // RIGHT
                    mat[currRow][currCol].setRight(false);
                    neighbor.setLeft(false);
                }
                
                // Mark the current cell as part of the maze
                current.setAlgoVisit(true);
                
                // Add neighbors of the current cell to the frontier
                addNeighborsToFrontier(mat, currRow, currCol, frontier);
            }
        }

        // makes sure there are multiple paths
        addMultiplePaths(mat);
        
        // Open entrance and exit
        mat[0][0].setLeft(false);
        mat[0][0].setHumanVisit(true);
        mat[rows - 1][cols - 1].setRight(false);
        mat[rows - 1][cols - 1].setComputerVisit(true);
    }
    
    // Add all unvisited neighbors to the frontier if they're not already there
    private void addNeighborsToFrontier(Cell[][] mat, int row, int col, ArrayList<Cell> frontier) {
        int rows = mat.length, cols = mat[0].length;
        
        // Check all four directions
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // up, down, left, right
        
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            
            // Check if neighbor is valid and unvisited
            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                Cell neighbor = mat[newRow][newCol];
                if (!neighbor.getAlgoVisit() && !frontier.contains(neighbor)) {
                    frontier.add(neighbor);
                }
            }
        }
    }
    
    // Get all neighbors that are already part of the maze (visited)
    private ArrayList<Cell> getVisitedNeighbors(Cell[][] mat, int row, int col) {
        ArrayList<Cell> visitedNeighbors = new ArrayList<>();
        int rows = mat.length, cols = mat[0].length;
        
        // Check all four directions
        if (row > 0 && mat[row - 1][col].getAlgoVisit()) {
            visitedNeighbors.add(mat[row - 1][col]); // UP
        }
        if (row < rows - 1 && mat[row + 1][col].getAlgoVisit()) {
            visitedNeighbors.add(mat[row + 1][col]); // DOWN
        }
        if (col > 0 && mat[row][col - 1].getAlgoVisit()) {
            visitedNeighbors.add(mat[row][col - 1]); // LEFT
        }
        if (col < cols - 1 && mat[row][col + 1].getAlgoVisit()) {
            visitedNeighbors.add(mat[row][col + 1]); // RIGHT
        }
        
        return visitedNeighbors;
    }

    // method to open up the maze more and make multiple paths by removing random walls
    private void addMultiplePaths(Cell[][] mat) {
        int rows = mat.length, cols = mat[0].length;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Try to break the right wall
                if (j < cols - 1 && random.nextDouble() < multiPathProbability) {
                    mat[i][j].setRight(false);
                    mat[i][j+1].setLeft(false);
                }
                
                // Try to break the bottom wall
                if (i < rows - 1 && random.nextDouble() < multiPathProbability) {
                    mat[i][j].setBottom(false);
                    mat[i+1][j].setTop(false);
                }
            }
        }
    }

}