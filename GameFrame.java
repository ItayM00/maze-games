package Yg_Final_Project;

import Yg_Final_Project.Mazes.*;
import Yg_Final_Project.generation_algorithms.*;
import Yg_Final_Project.solving_algorithms.*;
import Yg_Final_Project.enums.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Collections;

public class GameFrame extends JFrame implements KeyListener{

    private Maze maze;
    private Cell[][] mat;
    private int size;

    private List<Cell> path;
    private List<Cell> path2;

    private int playerRow;
    private int playerCol;
    private int computerRow;
    private int computerCol;

    private int player1points = 0;
    private int player2points = 0;
    private JLabel label1Points, label2Points;

    private PlayersType mode;

    private int seconds;
    private int timerDelay;
    private Timer gameTimer;
    private Timer computerTimer;
    private Timer computerTimer2;


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
        computerRow = mat.length - 1;
        computerCol = mat[0].length - 1;

        timerDelay = 350;

        labelForMiniGame(maze);

        this.add(maze, BorderLayout.CENTER);
        this.setVisible(true);
        
        if(mode != PlayersType.COMPUTER_vs_COMPUTER) this.addKeyListener(this);
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

        path = maze.getSolutionPath();
                
        if (mode == PlayersType.COMPUTER_vs_COMPUTER) {
            Collections.reverse(path);// becase one computer starts at the other side
            maze.setSolvingStrategy(new PrizeSearchAlgorithm());             
            path2 = maze.getSolutionPath();

            playerRow = playerCol = 0;           // Computer 1 starts at top-left
            computerRow = mat.length - 1;        // Computer 2 starts at bottom-right
            computerCol = mat[0].length - 1;
            
            // Mark initial positions
            mat[playerRow][playerCol].setHumanVisit(true);
            mat[computerRow][computerCol].setComputerVisit(true);
            
            computerTimer = new Timer(350, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        // Move computer 1 (using player variables)
                        if (!path.isEmpty()) {
                            // Clear current position
                            mat[playerRow][playerCol].setHumanVisit(false);
                            
                            Cell nextCell1 = path.remove(0);
                            playerRow = nextCell1.getRow();
                            playerCol = nextCell1.getColumn();
                            
                            // Handle prize collection for computer 1
                            if(maze instanceof PrizeMaze) {
                                Prize prize = mat[playerRow][playerCol].getPrize();
                                if(prize != null && !prize.getIsCollected()) {
                                    player1points += prize.getPointsWorth();
                                    prize.setCollected(true);
                                    updateScoreBoard(1);
                                }
                            }
                            
                            mat[playerRow][playerCol].setHumanVisit(true);
                            mat[playerRow][playerCol].setDiscovred(true);
                            
                            // Check if computer 1 reached goal (bottom-right)
                            if (playerRow == mat.length - 1 && playerCol == mat[0].length - 1) {
                                stopComputerMovement();
                                if(maze instanceof RaceMaze) gameTimer.stop();
                                printEndMessage(1); // Computer 1 won
                                dispose();
                                return;
                            }
                        }
                        else{
                            System.out.println("computer 1 path is empty...");
                            computerTimer.stop();
                            return;
                        }

                        maze.repaint(); // Refresh GUI
                    } catch (Exception ex) {
                        System.out.println("Error in computer movement: " + ex.getMessage());
                        ex.printStackTrace();
                        computerTimer.stop();
                    }
                }
            });

            computerTimer2 = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {        
                        // Move computer 2
                        if (!path2.isEmpty()) {
                            // Clear current position
                            mat[computerRow][computerCol].setComputerVisit(false);
                            
                            Cell nextCell2 = path2.remove(0);
                            computerRow = nextCell2.getRow();
                            computerCol = nextCell2.getColumn();
                            
                            // Handle prize collection for computer 2
                            if(maze instanceof PrizeMaze) {
                                Prize prize = mat[computerRow][computerCol].getPrize();
                                if(prize != null && !prize.getIsCollected()) {
                                    player2points += prize.getPointsWorth();
                                    prize.setCollected(true);
                                    updateScoreBoard(0);
                                }
                            }
                            
                            mat[computerRow][computerCol].setComputerVisit(true);
                            mat[computerRow][computerCol].setDiscovred(true);
                            
                            // Check if computer 2 reached goal (bottom-right)
                            if (computerRow == 0 && computerCol == 0) {
                                stopComputerMovement();
                                if(maze instanceof RaceMaze) gameTimer.stop();
                                printEndMessage(2); // Computer 2 won
                                dispose();
                                return;
                            }
                        }
                        else{
                            System.out.println("computer 2 path is empty...");
                            computerTimer2.stop();
                            return;
                        }
                        
                        maze.repaint(); // Refresh GUI
                    } catch (Exception ex) {
                        System.out.println("Error in computer movement: " + ex.getMessage());
                        ex.printStackTrace();
                        computerTimer.stop();
                    }
                }
            });
            
            computerTimer.start();
            computerTimer2.start();
            System.out.println("Both timer started...");
            return;
        }
        
        // PLAYER_vs_COMPUTER Mode
        computerTimer = new Timer(timerDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Clear current position
                    mat[computerRow][computerCol].setComputerVisit(false);

                    Cell cur = path.remove(0);

                    computerRow = cur.getRow();
                    computerCol = cur.getColumn();
    
                    // Check if computer reached the goal (top-left corner)
                    if (computerRow == 0 && computerCol == 0) {
                        computerTimer.stop();
                        if(maze instanceof RaceMaze) gameTimer.stop(); // Stop the game timer
                        printEndMessage(3);
                        dispose();
                        return;
                    }

                    Prize curCompPrize = mat[computerRow][computerCol].getPrize();
                    if(maze instanceof PrizeMaze && curCompPrize != null && !curCompPrize.getIsCollected()){
                    
                        System.out.println("computer collected prize... + " + curCompPrize.getPointsWorth() + " pts");
                        player2points += curCompPrize.getPointsWorth();
                        mat[computerRow][computerCol].getPrize().setCollected(true);
                        updateScoreBoard(0);
                    }
    
                    // Check if there are no moves left
                    if (path.isEmpty()) {
                        System.out.println("No more moves available. Stopping computer.");
                        computerTimer.stop();
                        return;
                    }

                    mat[computerRow][computerCol].setComputerVisit(true);
                    mat[computerRow][computerCol].setDiscovred(true);
                    maze.repaint(); // Refresh GUI   
                    
                    // changing the computer delay to make moves more believable
                    // if(seconds % 5 == 0){
                    //     timerDelay = (int) (Math.random() * 1500 + 350); 
                    //     computerTimer.setDelay(timerDelay);
                    //     System.out.println("timer delay is: " + timerDelay);
                    // }
    
                } catch (Exception ex) {
                    System.out.println("Error in computer movement: " + ex.getMessage());
                    ex.printStackTrace();
                    computerTimer.stop();
                }
            }
        });
    
        computerTimer.start();
    }

    public void stopComputerMovement() {
        if (computerTimer != null && computerTimer.isRunning()) {
            computerTimer.stop();
        }
        if (computerTimer2 != null && computerTimer2.isRunning()) {
            computerTimer2.stop();
        }
    }

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
                    if(mode == PlayersType.PLAYER_vs_PLAYER && computerCol > 0 && !mat[computerRow][computerCol].isLeftOn()){
                        mat[computerRow][computerCol].setComputerVisit(false);
                        computerCol--;
                        mat[computerRow][computerCol].setDiscovred(true);
                        mat[computerRow][computerCol].setComputerVisit(true);
                    }
                    break;
                case KeyEvent.VK_W: // Move up Key (w) 87
                    if(mode == PlayersType.PLAYER_vs_PLAYER && computerRow > 0 && !mat[computerRow][computerCol].isTopOn()){
                        mat[computerRow][computerCol].setComputerVisit(false);
                        computerRow--;
                        mat[computerRow][computerCol].setDiscovred(true);
                        mat[computerRow][computerCol].setComputerVisit(true);
                    }
                    break;
                case KeyEvent.VK_D: // Move right Key (d) 68
                    if(mode == PlayersType.PLAYER_vs_PLAYER && computerCol < size - 1 && !mat[computerRow][computerCol].isRightOn()){
                        mat[computerRow][computerCol].setComputerVisit(false);
                        computerCol++;
                        mat[computerRow][computerCol].setDiscovred(true);
                        mat[computerRow][computerCol].setComputerVisit(true);
                    }
                    break;
                case KeyEvent.VK_S: // Move down Key (s) 83
                    if(mode == PlayersType.PLAYER_vs_PLAYER && computerRow < size - 1 && !mat[computerRow][computerCol].isBottomOn()){
                        mat[computerRow][computerCol].setComputerVisit(false);
                        computerRow++;
                        mat[computerRow][computerCol].setDiscovred(true);
                        mat[computerRow][computerCol].setComputerVisit(true);
                    }
                    break;
                }
                maze.repaint();

                // if(maze instanceof FogMaze ){
                //     ((FogMaze)maze).setHumanPlayerPosition(playerRow, playerCol);
                //     ((FogMaze)maze).setComputerPlayerPosition(computerRow, computerCol);
                // } 

                handleMoveBasedOnMode();

                if(mat[mat.length - 1][mat[0].length - 1].getHumanVisit()) {
                    if(maze instanceof RaceMaze) gameTimer.stop();
                    if(mode == PlayersType.PLAYER_vs_COMPUTER || mode == PlayersType.COMPUTER_vs_COMPUTER) stopComputerMovement();
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
        if(type == GameType.RACE) maze = new RaceMaze(size, new PrimsAlgorithm(), new DijkstraAlgorithm());
        if(type == GameType.PRIZES) maze = new PrizeMaze(size, new PrimsAlgorithm(), new BFSAlgorithm());
        if(type == GameType.Fog_OF_WAR) maze = new FogMaze(size, new DFSAlgorithm(), new RecursiveBacktracking());
    }

    public void handleMoveBasedOnMode(){
        if(maze instanceof PrizeMaze) handlePrizeMove();
        else return;
    }
    
    public void handlePrizeMove(){
        Prize curPlayerPrize = mat[playerRow][playerCol].getPrize();
        Prize curCompPrize = mat[computerRow][computerCol].getPrize();
                
        if(curPlayerPrize != null && !curPlayerPrize.getIsCollected()){
            System.out.println("player 1 collected prize... + " + curPlayerPrize.getPointsWorth() + " pts");
            player1points += curPlayerPrize.getPointsWorth();
            mat[playerRow][playerCol].getPrize().setCollected(true);
            updateScoreBoard(1);
        }
        if(curCompPrize != null && !curCompPrize.getIsCollected()){
            System.out.println("player 2 collected prize... + " + curCompPrize.getPointsWorth() + " pts");
            player2points += curCompPrize.getPointsWorth();
            mat[computerRow][computerCol].getPrize().setCollected(true);
            updateScoreBoard(2);
        }
    }


    public void printEndMessage(int idFinished) {
        if (maze instanceof RaceMaze) {
            if (mode == PlayersType.COMPUTER_vs_COMPUTER) {
                if (idFinished == 1) {
                    JOptionPane.showMessageDialog(null, "Computer 1 (BFS) finished in " + seconds + "s", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                } else if (idFinished == 2) {
                    JOptionPane.showMessageDialog(null, "Computer 2 (DFS) finished in " + seconds + "s", "Game Over", JOptionPane.INFORMATION_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "Computer 1 (BFS) won with " + player1points + " points", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                } else if (player1points < player2points) {
                    JOptionPane.showMessageDialog(null, "Computer 2 (DFS) won with " + player2points + " points", "Game Over", JOptionPane.INFORMATION_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "Computer 1 (BFS) finished the dark maze", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                } else if (idFinished == 2) {
                    JOptionPane.showMessageDialog(null, "Computer 2 (DFS) finished the dark maze", "Game Over", JOptionPane.INFORMATION_MESSAGE);
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
