package Yg_Final_Project;

import java.util.Collections;
import java.util.List;

import javax.swing.SwingUtilities;
import Yg_Final_Project.Mazes.Maze;
import Yg_Final_Project.Mazes.PrizeMaze;
import Yg_Final_Project.base_classes.Cell;
import Yg_Final_Project.base_classes.Prize;
import Yg_Final_Project.solving_algorithms.MazeSolvingStrategy;
import Yg_Final_Project.solving_algorithms.RecursiveBacktracking;

import java.awt.Point;

public class ComputerPlayer implements Runnable{

    private GameEventHandler handler;
    private Maze maze;
    private Cell[][] mat;
    private int row, col;
    private Point goal;
    private int playerIndex; // 1 or 2
    private Thread thread;
    private boolean running = false;
    private int delay;
    private MazeSolvingStrategy strategy;


    public ComputerPlayer(GameEventHandler handler, Maze maze, Cell[][] mat, int startRow, int startCol, Point goal,  int playerIndex, int delay, MazeSolvingStrategy strategy) {
        this.handler = handler;
        this.maze = maze;
        this.mat = mat;
        this.row = startRow;
        this.col = startCol;
        this.goal = goal;
        this.playerIndex = playerIndex;
        this.delay = delay;
        this.strategy = strategy;
    }

    public void start(){
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop(){
        running = false;
        if(thread != null && thread.isAlive()){
            thread.interrupt();
        }
    }

    @Override
    public void run() {
        while(running){
            try {

                List<Cell> path = strategy.solveFrom(mat, row, col, goal.x, goal.y);

                if (path == null || path.size() < 2){
                    System.out.println("path is null, cannot continue!");
                    stop();
                    break;
                }
                if(strategy instanceof RecursiveBacktracking && playerIndex != 2){
                    Collections.reverse(path);
                }

                Cell next = path.get(1);
                int nextRow = next.getRow();
                int nextCol = next.getColumn();

                if(playerIndex == 2) mat[row][col].setComputer2Visit(false);
                else mat[row][col].setComputerVisit(false);

                mat[row][col].setComputerVisit(false);
                row = nextRow;
                col = nextCol; 

                // check for prizes if its the correct mode
                if (maze instanceof PrizeMaze) {
                    Prize p = mat[row][col].getPrize();
                    if (p != null && !p.getIsCollected()) {
                        if (playerIndex == 1) {
                            handler.addPoints(1, p.getPointsWorth());
                        } else {
                            handler.addPoints(2, p.getPointsWorth());
                        }
                        p.setCollected(true);
                        SwingUtilities.invokeLater(() -> handler.updateScoreBoard(playerIndex));
                    }
                }

                if(playerIndex == 2) mat[row][col].setComputer2Visit(true);
                else mat[row][col].setComputerVisit(true);
                mat[row][col].setDiscovred(true);

                // Check for win
                if (row == goal.x && col == goal.y) {
                    running = false;
                    SwingUtilities.invokeLater(() -> {
                        handler.printEndMessage(playerIndex);
                        handler.dispose();
                    });
                    break;
                }

                SwingUtilities.invokeLater(() -> maze.repaint());

                Thread.sleep(delay);
                
            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        
    }

// --------------- Getters and Setters ---------------

    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
}
