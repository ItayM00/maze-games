package Yg_Final_Project.Mazes;

import java.awt.*;

import Yg_Final_Project.base_classes.Cell;
import Yg_Final_Project.generation_algorithms.MazeGenerationStrategy;
import Yg_Final_Project.solving_algorithms.MazeSolvingStrategy;

public class RaceMaze extends Maze{

    

    public RaceMaze(int size, MazeGenerationStrategy generationStrategy, MazeSolvingStrategy solvingStrategy){
        super(size, generationStrategy, solvingStrategy);        
    }
    

    // ----------------- Display The Maze On The Screen -------------------
  
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int availableWidth = getWidth() - (2 * PADDING);
        int availableHeight = getHeight() - (2 * PADDING);
        cellSize = Math.min(availableWidth / colsNum, availableHeight / rowsNum);

        int startX = (getWidth() - (cellSize * colsNum)) / 2;
        int startY = (getHeight() - (cellSize * rowsNum)) / 2;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));

        for (int i = 0; i < rowsNum; i++) {
            for (int j = 0; j < colsNum; j++) {
                int x = startX + (j * cellSize);
                int y = startY + (i * cellSize);
                Cell cell = mat[i][j];

                drawCellBackground(g, cell, x, y);
                drawCellWalls(g2d, cell, x, y);
            }
        }
    }

    public void drawCellBackground(Graphics g, Cell cell, int x, int y) {
        int curRow = cell.getRow(), curCol = cell.getColumn();
        if(( curRow == 0 && curCol == 0) || ( curRow == rowsNum - 1 && curCol == colsNum - 1)){
            g.setColor(Color.GRAY);
            g.fillRect(x , y, cellSize, cellSize);
        }
        if (cell.getHumanVisit() || cell.getComputer2Visit()) {
            g.setColor(Color.GREEN);
            g.fillOval(x + cellSize / 4, y + cellSize / 4, cellSize / 2, cellSize / 2);
        } 
        if (cell.getComputerVisit() ) {
            g.setColor(Color.ORANGE);
            g.fillOval(x + cellSize / 4, y + cellSize / 4, cellSize / 2, cellSize / 2);
        }
    }

    public void drawCellWalls(Graphics2D g2d, Cell cell, int x, int y) {
        g2d.setColor(Color.BLACK);
        if (cell.isLeftOn()) g2d.drawLine(x, y, x, y + cellSize);
        if (cell.isRightOn()) g2d.drawLine(x + cellSize, y, x + cellSize, y + cellSize);
        if (cell.isTopOn()) g2d.drawLine(x, y, x + cellSize, y);
        if (cell.isBottomOn()) g2d.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
    }
}
