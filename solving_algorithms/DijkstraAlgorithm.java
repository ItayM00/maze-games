package Yg_Final_Project.solving_algorithms;

import java.util.*;

import Yg_Final_Project.Cell;
import Yg_Final_Project.NodeData;
import Yg_Final_Project.Mazes.Maze;

public class DijkstraAlgorithm implements MazeSolvingStrategy {

    private Map<Cell, NodeData> nodeDataMap = new HashMap<>(); // stores info for each cell
    private PriorityQueue<Cell> openSet = new PriorityQueue<>(Comparator.comparingInt(c -> nodeDataMap.get(c).g)); // picks the next closest cell
    private Set<Cell> closedSet = new HashSet<>(); // which ones we’ve already finished
    private List<Cell> path = new ArrayList<>(); // shortest path moves list

    @Override
    public void solveMaze(Cell[][] mat, Maze maze) {
        clearStructs();
        
        Cell start = mat[mat.length - 1][mat[0].length - 1];

        nodeDataMap.put(start, new NodeData(0, null));
        openSet.add(start);

        while(!openSet.isEmpty()){
            Cell current = openSet.poll();

            // if we reached the Goal
            if(current.getRow() == 0 && current.getColumn() == 0){
                buildPath(nodeDataMap, mat[0][0]);
                maze.setSolutionPath(path);
                return;
            }

            closedSet.add(current);

            for (Cell neighbor : getNeighbors(current, mat)) {
                if (!isAccessible(current, neighbor) || closedSet.contains(neighbor)) {
                    continue; // Skip walls or already processed
                }

                int gCost = nodeDataMap.get(current).g + 1;

                // If this neighbor is not in map or we found a better path:
                if(!nodeDataMap.containsKey(neighbor) || gCost < nodeDataMap.get(neighbor).g){
                    nodeDataMap.put(neighbor, new NodeData(gCost, current));
                    openSet.add(neighbor);
                }
            }

        }
    }

    private boolean isAccessible(Cell current, Cell neighbor) {
       int cRow = current.getRow() , cCol = current.getColumn();
       int nRow = neighbor.getRow(), nCol = neighbor.getColumn();

       if(cRow > nRow){ // UP
            return (!current.isTopOn() && !neighbor.isBottomOn() ? true : false);
       }
       else if(nRow > cRow){ // DOWN
            return (!current.isBottomOn() && !neighbor.isTopOn() ? true : false);
        }
       else if(cCol > nCol){ // LEFT
            return (!current.isLeftOn() && !neighbor.isRightOn() ? true : false);
        }
       else if(nCol > cCol){ // RIGHT
            return (!current.isRightOn() && !neighbor.isLeftOn() ? true : false);
        }
       return false;
    }

    private List<Cell> getNeighbors(Cell current, Cell[][] mat) {
        List<Cell> neighbors = new ArrayList<>();
        int x = current.getColumn();
        int y = current.getRow();
    
        // Up
        if (y > 0) { neighbors.add(mat[y - 1][x]); }
        // Down
        if (y < mat.length - 1) { neighbors.add(mat[y + 1][x]); }
        // Left
        if (x > 0) { neighbors.add(mat[y][x - 1]); }
        // Right
        if (x < mat[0].length - 1) { neighbors.add(mat[y][x + 1]); }
    
        return neighbors;
    }

    private void buildPath(Map<Cell, NodeData> map, Cell goal){
        Cell current = goal;

        while (current != null) {
            path.add(current);
            current = map.get(current).parent;
        }

        Collections.reverse(path);
    }

    private void clearStructs(){
        path.clear();
        nodeDataMap.clear();
        openSet.clear();
        closedSet.clear();
    }
    
}
