package Yg_Final_Project.solving_algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import Yg_Final_Project.Cell;
import Yg_Final_Project.Mazes.Maze;

public class BFSAlgorithm implements MazeSolvingStrategy{

    private List<Cell> path = new ArrayList<>();

    @Override
    public void solveMaze(Cell[][] mat, Maze maze){
        resetAlgoVisits(mat);

        Queue<Cell> queue = new LinkedList<>();
        Map<Cell, Cell> parentMap = new HashMap<>();

        Cell start = mat[mat.length - 1][mat[0].length - 1];
        start.setAlgoVisit(true);
        queue.add(start);

        while(!queue.isEmpty()){
            Cell cur = queue.remove();
        
            ArrayList<Cell> list = getCellsWithoutAlgoVisit(cur.getRow(), cur.getColumn(), mat);
        
            for (Cell cell : list) {
                if (!cell.getAlgoVisit()) {
                    parentMap.put(cell, cur); // track path
                    cell.setAlgoVisit(true);  // mark as visited for the path
                    queue.add(cell);
        
                    // Goal check
                    if (cell.getRow() == 0 && cell.getColumn() == 0) {
                        markPath(cell, parentMap);
                        maze.setSolutionPath(path);
                        return;
                    }
                }
            }
        }
        
    }

    // Get the son -> mark it -> get its parent from the hash map
    public void markPath(Cell end, Map<Cell, Cell> parentMap) {
        Cell cur = end;
        path.add(0,cur);

        while (cur != null) {
            path.add(0,cur);
            cur = parentMap.get(cur);
        }
    }

    // check if frame OR algo is on OR symetric check for the wall between
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

    // Reset algo visit attribute to track the path
    public void resetAlgoVisits(Cell[][] mat){
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                mat[i][j].setAlgoVisit(false);
            }
        }
    }

    
}
