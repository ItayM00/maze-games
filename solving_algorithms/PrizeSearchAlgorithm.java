package Yg_Final_Project.solving_algorithms;

import Yg_Final_Project.Mazes.*;
import Yg_Final_Project.base_classes.Cell;
import java.util.*;

public class PrizeSearchAlgorithm implements MazeSolvingStrategy{

    // Because it wouldnt be efficient to search for all the prizes in the maze
    // we will search for the closest one with BFS algorithm
    // after the computer player reaches it we will solve again 
    
    @Override
    public void solveMaze(Cell[][] mat, Maze maze) {
        List<Cell> path = solveFrom(mat, mat.length - 1, mat[0].length - 1, 0, 0);
        maze.setSolutionPath(path);
    }
    
    // Find a path from start to end using BFS
    private List<Cell> findPath(Cell[][] mat, Cell start, Cell end) {
        // Reset algorithm visit flags
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                mat[i][j].setAlgoVisit(false);
            }
        }
        
        Queue<Cell> queue = new LinkedList<>();
        Map<Cell, Cell> parentMap = new HashMap<>();
        
        queue.add(start);
        start.setAlgoVisit(true);
        
        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            
            if (current.equals(end)) {
                break;  // Found the destination
            }
            
            // Check all four directions
            ArrayList<Cell> neighbors = getNeighbors(current, mat);
            
            for (Cell neighbor : neighbors) {
                if (!neighbor.getAlgoVisit()) {
                    queue.add(neighbor);
                    neighbor.setAlgoVisit(true);
                    parentMap.put(neighbor, current);
                }
            }
        }
        
        // If we can't find a path to the end
        if (!end.getAlgoVisit()) {
            return null;
        }
        
        // Reconstruct the path
        List<Cell> path = new ArrayList<>();
        Cell current = end;
        
        while (!current.equals(start)) {
            path.add(0, current);
            current = parentMap.get(current);
        }
        
        return path;
    }
    
    // Get valid neighbors (cells we can move to)
    private ArrayList<Cell> getNeighbors(Cell cell, Cell[][] mat) {
        ArrayList<Cell> neighbors = new ArrayList<>();
        int row = cell.getRow();
        int col = cell.getColumn();
        
        // Check up
        if (row > 0 && !cell.isTopOn()) {
            neighbors.add(mat[row - 1][col]);
        }
        
        // Check down
        if (row < mat.length - 1 && !cell.isBottomOn()) {
            neighbors.add(mat[row + 1][col]);
        }
        
        // Check left
        if (col > 0 && !cell.isLeftOn()) {
            neighbors.add(mat[row][col - 1]);
        }
        
        // Check right
        if (col < mat[0].length - 1 && !cell.isRightOn()) {
            neighbors.add(mat[row][col + 1]);
        }
        
        return neighbors;
    }

    @Override
    public List<Cell> solveFrom(Cell[][] mat, int startRow, int startCol, int goalRow, int goalCol) {
        List<Cell> prizes = new ArrayList<>();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                if (mat[i][j].getPrize() != null && !mat[i][j].getPrize().getIsCollected()) {
                    prizes.add(mat[i][j]);
                }
            }
        }
        
        System.out.println("Found " + prizes.size() + " prizes in the maze");
        
        Cell currentPosition = mat[startRow][startCol];
        
        while (!prizes.isEmpty()) {
            // Find the closest prize
            Cell closestPrizeCell = null;
            List<Cell> bestPath = null;
            
            for (Cell prize : prizes) {
                List<Cell> path = findPath(mat, currentPosition, prize);
                if (path != null) {
                    if (closestPrizeCell == null || path.size() < bestPath.size()) {
                        closestPrizeCell = prize;
                        bestPath = path;
                    }
                }
            }
            
            if (closestPrizeCell == null) {
                System.out.println("Unable to reach any more prizes");
                break;
            }
            
            return bestPath;
        }

        List<Cell> path = findPath(mat, currentPosition, mat[goalRow][goalCol]);
        return path;
    }
    
}
