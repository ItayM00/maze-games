package Yg_Final_Project.Mazes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;

import Yg_Final_Project.base_classes.Cell;
import Yg_Final_Project.base_classes.Prize;
import Yg_Final_Project.generation_algorithms.MazeGenerationStrategy;
import Yg_Final_Project.solving_algorithms.MazeSolvingStrategy;
import Yg_Final_Project.enums.PrizeType;

public class PrizeMaze extends Maze{

    private List<Prize> prizes;
    private int num_of_prizes = 15;


    public PrizeMaze(int size, MazeGenerationStrategy generationStrategy, MazeSolvingStrategy solvingStrategy){ // 
        super(size, generationStrategy, solvingStrategy);

        prizes = new ArrayList<Prize>();
        createRandomPrizes(size);
    }


    public void createRandomPrizes(int size){
        PrizeType type = PrizeType.COIN; // default value
        int points = 0, row, col;
        boolean prizespots[][] = new boolean[size][size];
        
        for (int i = 0; i < num_of_prizes; i++) {
            type = getRandomPrizeType();
            points = pointsBasedOnPrize(type);

            if(type == null || points == -1) {System.out.println("error in creating prizes..."); return;}

            do {
                row = (int) (Math.random() * size);
                col = (int) (Math.random() * size);
            } while (prizespots[row][col] || (row == 0 && col == 0) || (row == size - 1 && col == size - 1));

            prizespots[row][col] = true;
            
            Prize prize = new Prize(row, col, points, false, type);
            mat[row][col].setPrize(prize);
            prizes.add(prize);
        }
    }

    public PrizeType getRandomPrizeType(){
        int rand = (int) (Math.random() * 3);
        PrizeType type = null;

        if(rand == 0) type = PrizeType.COIN;
        if(rand == 1) type = PrizeType.GEM;
        if(rand == 2) type = PrizeType.STAR;

        return type;
    }

    public int pointsBasedOnPrize(PrizeType type){
        if(type == PrizeType.COIN) return 5;
        if(type == PrizeType.GEM) return 20;
        if(type == PrizeType.STAR) return 10;
        return -1;
    }

    public void collectPrize(int row, int col){
        for (Prize prize : prizes) {
            if(prize.getRow() == row && prize.getCol() == col){
                prize.setCollected(true);
                num_of_prizes--;
            }
        }
    }

    public int getTotalPoints(){
        int sum = 0;

        for (Prize prize : prizes) {
            sum += prize.getPointsWorth();    
        }
        return sum;
    }

    public int getNuumOfPrizes(){
        return num_of_prizes;
    }

    public List<Prize> getPrizes() {
        return prizes;
    }

// --------------------------------------------------------------------------------

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
                drawPrize(g, cell, x, y);
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

    public void drawPrize(Graphics g, Cell cell, int x, int y){
        Prize prize = cell.getPrize();
        if(prize != null && !prize.getIsCollected()){
            if(prize.getType() == PrizeType.COIN){
                g.setColor(Color.RED);
                g.fillArc(x + cellSize / 4, y + cellSize / 4, cellSize / 2, cellSize / 2, 0, 80);  
            } 
            if(prize.getType() == PrizeType.GEM){
                g.setColor(Color.GREEN);
                g.fillRect(x + cellSize / 4, y + cellSize / 4, cellSize / 2, cellSize / 2);
            } 
            if(prize.getType() == PrizeType.STAR){
                g.setColor(Color.YELLOW);
                g.fillOval(x + cellSize / 4, y + cellSize / 4, cellSize / 2, cellSize / 2);
            } 
        }
    }

    public void drawCellWalls(Graphics2D g2d, Cell cell, int x, int y) {
        g2d.setColor(Color.BLACK);
        if (cell.isLeftOn()) g2d.drawLine(x, y, x, y + cellSize);
        if (cell.isRightOn()) g2d.drawLine(x + cellSize, y, x + cellSize, y + cellSize);
        if (cell.isTopOn()) g2d.drawLine(x, y, x + cellSize, y);
        if (cell.isBottomOn()) g2d.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
    }

// ---------

    
}
