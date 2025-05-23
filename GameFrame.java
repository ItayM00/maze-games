package Yg_Final_Project;

import Yg_Final_Project.Mazes.*;
import Yg_Final_Project.base_classes.Cell;
import Yg_Final_Project.base_classes.Prize;
import Yg_Final_Project.generation_algorithms.*;
import Yg_Final_Project.solving_algorithms.*;
import Yg_Final_Project.enums.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame extends JFrame implements KeyListener, GameEventHandler{

    private Maze maze;
    private Cell[][] mat;
    private int size;

    private int playerRow;
    private int playerCol;
    private int player2Row;
    private int player2Col;

    private int player1points = 0;
    private int player2points = 0;
    private JLabel label1Points, label2Points;

    private PlayersType mode;

    private int seconds;
    private Timer gameTimer;

    private ComputerPlayer computer1;
    private ComputerPlayer computer2;

// ---------------- Constructor ----------------------

    public GameFrame(int size, PlayersType mode, GameType type){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        int frameHeight = (int) (screenSize.height / 1.2);
        int frameWidth = frameHeight;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(frameWidth, frameHeight);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        this.size = size;
        this.mode = mode;
        
        createMaze(type, size);
        mat = maze.getMat();

        playerRow = playerCol = 0;
        player2Row = mat.length - 1;
        player2Col = mat[0].length - 1;

        labelForMiniGame(maze);

        this.add(maze, BorderLayout.CENTER);
        this.setVisible(true);
        
        if(mode != PlayersType.COMPUTER_vs_COMPUTER) this.addKeyListener(this);
        if(mode == PlayersType.COMPUTER_vs_COMPUTER) {
            mat[0][0].setHumanVisit(false);
            mat[0][0].setComputer2Visit(true);
        }
        if(mode == PlayersType.PLAYER_vs_COMPUTER || mode == PlayersType.COMPUTER_vs_COMPUTER) startComputerMovement();
    }

// ------------------ GUI Based On Mode Methods -----------------------------

    public JLabel setTimerLabel(){
        JLabel label = new JLabel("Time: 0s");
        label.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 45));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        
        seconds = 0;
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    seconds++;
                    label.setText("Time: " + seconds + "s");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An error occurred with the timer!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        return label;
    }

    public JPanel createScoreBoard(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        label1Points = new JLabel("player 1: ");
        label1Points.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
        label1Points.setHorizontalAlignment(JLabel.RIGHT);
        label1Points.setVerticalAlignment(JLabel.CENTER);

        label2Points = (mode == PlayersType.PLAYER_vs_COMPUTER) ? new JLabel("player 2: ") : new JLabel("computer: ");
        label2Points.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 20));
        label2Points.setForeground(Color.BLUE);
        label2Points.setHorizontalAlignment(JLabel.LEFT);
        label2Points.setVerticalAlignment(JLabel.CENTER);

        panel.add(label1Points);
        panel.add(label2Points);

        return panel;
    }

    public void startComputerMovement() {
        // Don't start computer movement in PVP mode
        if (mode == PlayersType.PLAYER_vs_PLAYER) {
            return;
        }
                
        else if(mode == PlayersType.PLAYER_vs_COMPUTER){
            System.out.println("mode: PLAYER_vs_COMPUTER");
            Point goal1 = new Point(0, 0);

            computer1 = new ComputerPlayer(this, maze, mat, mat.length - 1, mat[0].length - 1, goal1, 3, 400, new BFSAlgorithm());

            computer1.start();
        }

        else if (mode == PlayersType.COMPUTER_vs_COMPUTER) {
            System.out.println("mode: COMPUTER_vs_COMPUTER");

            Point goal1 = new Point(mat.length - 1, mat[0].length - 1);
            Point goal2 = new Point(0, 0);

            computer1 = new ComputerPlayer(this, maze, mat, mat.length - 1, mat[0].length - 1, goal2, 1, 500, new BFSAlgorithm());
            computer2 = new ComputerPlayer(this, maze, mat, 0, 0, goal1, 2, 500, new DijkstraAlgorithm());

            computer1.start();
            computer2.start();
        }
        
        System.out.println("started computer movement");
    }

    public void stopComputerMovement() {
        if (computer1 != null ) {
            computer1.stop();
        }
        if (computer2 != null) {
            computer2.stop();
        }
    }

    @Override
    public void updateScoreBoard(int id){
        switch (id) {
            case 0:
                label2Points.setText("computer: " + (player2points) + " pts");
                break;
            case 1:
                label1Points.setText("player 1: " + (player1points) + " pts");
                break;
            case 2:
                label2Points.setText("player 2: " + (player2points) + " pts");
                break;
            case 3:
                label2Points.setText("computer: " + (player2points) + " pts");
                break;
        }
    }

    public void labelForMiniGame(Maze maze){
        if(maze instanceof RaceMaze){
            JLabel timerLabel = setTimerLabel();
            this.add(timerLabel, BorderLayout.NORTH);
            gameTimer.start();
            System.out.println("race instance");
        }
        else if(maze instanceof PrizeMaze){
            JPanel scorePanel = createScoreBoard();
            this.add(scorePanel, BorderLayout.NORTH);
            System.out.println("prize instance");
        }
        else if(maze instanceof FogMaze){
            JLabel progress = new JLabel("progress made: 0%");
            this.add(progress, BorderLayout.NORTH);
            System.out.println("fog instance");
        }
        else{
            System.out.println("Error with instance of maze... please check maze object!");
            System.exit(1);
        }
    }
    
    // ------------------- Key Events (players controls) -----------------------
    
    @Override
    public void keyPressed(KeyEvent e) { 
        try{
            switch (e.getKeyCode()) 
            {
                case KeyEvent.VK_LEFT: // Move left Arrow 37
                    if(playerCol > 0 && !mat[playerRow][playerCol].isLeftOn()){
                        mat[playerRow][playerCol].setHumanVisit(false);
                        playerCol--;
                        mat[playerRow][playerCol].setDiscovred(true);
                        mat[playerRow][playerCol].setHumanVisit(true);
                    }
                    break;
                case KeyEvent.VK_UP: // Move up Arrow 38
                    if(playerRow > 0 && !mat[playerRow][playerCol].isTopOn()){
                        mat[playerRow][playerCol].setHumanVisit(false);
                        playerRow--;
                        mat[playerRow][playerCol].setDiscovred(true);
                        mat[playerRow][playerCol].setHumanVisit(true);
                    }
                    break;
                case KeyEvent.VK_RIGHT: // Move right Arrow 39
                    if(playerCol < size - 1 && !mat[playerRow][playerCol].isRightOn()){
                        mat[playerRow][playerCol].setHumanVisit(false);
                        playerCol++;
                        mat[playerRow][playerCol].setDiscovred(true);
                        mat[playerRow][playerCol].setHumanVisit(true);
                    }
                    break;
                case KeyEvent.VK_DOWN: // Move down Arrow 40s
                    if(playerRow < size - 1 && !mat[playerRow][playerCol].isBottomOn()){
                        mat[playerRow][playerCol].setHumanVisit(false);
                        playerRow++;
                        mat[playerRow][playerCol].setDiscovred(true);
                        mat[playerRow][playerCol].setHumanVisit(true);
                    }
                    break;
                case KeyEvent.VK_A: // Move left Key (a) 65
                    if(mode == PlayersType.PLAYER_vs_PLAYER && player2Col > 0 && !mat[player2Row][player2Col].isLeftOn()){
                        mat[player2Row][player2Col].setComputerVisit(false);
                        player2Col--;
                        mat[player2Row][player2Col].setDiscovred(true);
                        mat[player2Row][player2Col].setComputerVisit(true);
                    }
                    break;
                case KeyEvent.VK_W: // Move up Key (w) 87
                    if(mode == PlayersType.PLAYER_vs_PLAYER && player2Row > 0 && !mat[player2Row][player2Col].isTopOn()){
                        mat[player2Row][player2Col].setComputerVisit(false);
                        player2Row--;
                        mat[player2Row][player2Col].setDiscovred(true);
                        mat[player2Row][player2Col].setComputerVisit(true);
                    }
                    break;
                case KeyEvent.VK_D: // Move right Key (d) 68
                    if(mode == PlayersType.PLAYER_vs_PLAYER && player2Col < size - 1 && !mat[player2Row][player2Col].isRightOn()){
                        mat[player2Row][player2Col].setComputerVisit(false);
                        player2Col++;
                        mat[player2Row][player2Col].setDiscovred(true);
                        mat[player2Row][player2Col].setComputerVisit(true);
                    }
                    break;
                case KeyEvent.VK_S: // Move down Key (s) 83
                    if(mode == PlayersType.PLAYER_vs_PLAYER && player2Row < size - 1 && !mat[player2Row][player2Col].isBottomOn()){
                        mat[player2Row][player2Col].setComputerVisit(false);
                        player2Row++;
                        mat[player2Row][player2Col].setDiscovred(true);
                        mat[player2Row][player2Col].setComputerVisit(true);
                    }
                    break;
                }
                maze.repaint();

                handleMoveBasedOnMode();

                if(mat[mat.length - 1][mat[0].length - 1].getHumanVisit()) {
                    if(maze instanceof RaceMaze) gameTimer.stop();
                    if(mode == PlayersType.PLAYER_vs_COMPUTER) stopComputerMovement();
                    printEndMessage(1);
                    this.dispose();
                } else if(mat[0][0].getComputerVisit()) {
                    if(maze instanceof RaceMaze) gameTimer.stop();
                    printEndMessage(2);
                    this.dispose();
                }

        } catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Index out of bounds: " + ex.getMessage());
        } catch (NullPointerException ex) {
            System.err.println("NullPointerException: A component might not be initialized.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "An unexpected error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
    
    // -----------------------------------------
    

    
    public int getMazeSize() {
        return size;
    }
    
    public PlayersType getModePVP(){
        return mode;
    }
    
    public void createMaze(GameType type, int size){
        if(type == GameType.RACE) maze = new RaceMaze(size, new PrimsAlgorithm(), new BFSAlgorithm());
        if(type == GameType.PRIZES) maze = new PrizeMaze(size, new PrimsAlgorithm(), new BFSAlgorithm());
        if(type == GameType.Fog_OF_WAR) maze = new FogMaze(size, new DFSAlgorithm(), new BFSAlgorithm());
    }
    
    @Override
    public void addPoints(int playerIndex, int points) {
        if(playerIndex == 1) this.player1points += points;
        else this.player2points += points;
    }

    public void handleMoveBasedOnMode(){
        if(maze instanceof PrizeMaze) handlePrizeMove();
        else return;
    }
    
    public void handlePrizeMove(){
        Prize curPlayerPrize = mat[playerRow][playerCol].getPrize();
        Prize curCompPrize = mat[player2Row][player2Col].getPrize();
                
        if(curPlayerPrize != null && !curPlayerPrize.getIsCollected()){
            System.out.println("player 1 collected prize... + " + curPlayerPrize.getPointsWorth() + " pts");
            addPoints(1, curPlayerPrize.getPointsWorth());
            mat[playerRow][playerCol].getPrize().setCollected(true);
            updateScoreBoard(1);
        }
        if(curCompPrize != null && !curCompPrize.getIsCollected()){
            System.out.println("player 2 collected prize... + " + curCompPrize.getPointsWorth() + " pts");
            addPoints(2, curPlayerPrize.getPointsWorth());
            mat[player2Row][player2Col].getPrize().setCollected(true);
            updateScoreBoard(2);
        }
    }

    @Override
    public void printEndMessage(int idFinished) {
        if(maze instanceof RaceMaze) gameTimer.stop();
        if(mode != PlayersType.PLAYER_vs_PLAYER) stopComputerMovement();

        if (maze instanceof RaceMaze) {
            if (mode == PlayersType.COMPUTER_vs_COMPUTER) {
                if (idFinished == 1) {
                    JOptionPane.showMessageDialog(null, "Computer 1 finished in " + seconds + "s", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                } else if (idFinished == 2) {
                    JOptionPane.showMessageDialog(null, "Computer 2 finished in " + seconds + "s", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                if (idFinished == 1) {
                    JOptionPane.showMessageDialog(null, "Player 1 finished in " + seconds + "s", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                } else if (idFinished == 2) {
                    JOptionPane.showMessageDialog(null, "Player 2 finished in " + seconds + "s", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Computer finished in " + seconds + "s", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        
        if (maze instanceof PrizeMaze) {
            if (mode == PlayersType.COMPUTER_vs_COMPUTER) {
                if (player1points > player2points) {
                    JOptionPane.showMessageDialog(null, "Computer 1 won with " + player1points + " points", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                } else if (player1points < player2points) {
                    JOptionPane.showMessageDialog(null, "Computer 2  won with " + player2points + " points", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "It's a tie! Both computers scored " + player1points + " points", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Existing prize maze message code
                if (player1points > player2points) {
                    JOptionPane.showMessageDialog(null, "Player 1 won with " + player1points + " points", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                } else if (mode == PlayersType.PLAYER_vs_COMPUTER && player1points < player2points) {
                    JOptionPane.showMessageDialog(null, "Computer won with " + player2points + " points", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Player 2 won with " + player2points + " points", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        
        if (maze instanceof FogMaze) {
            if (mode == PlayersType.COMPUTER_vs_COMPUTER) {
                if (idFinished == 1) {
                    JOptionPane.showMessageDialog(null, "Computer 1 finished the dark maze", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                } else if (idFinished == 2) {
                    JOptionPane.showMessageDialog(null, "Computer 2 finished the dark maze", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Existing fog maze message code
                if (idFinished == 1) {
                    JOptionPane.showMessageDialog(null, "Player 1 (Arrow Keys) finished the dark maze", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                } else if (idFinished == 2) {
                    JOptionPane.showMessageDialog(null, "Player 2 (WASD Keys) finished the dark maze", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Computer finished the dark maze", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

        new MenuFrame();
    }


}
