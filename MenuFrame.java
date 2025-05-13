package Yg_Final_Project;

import java.awt.*;
import javax.swing.*;

import Yg_Final_Project.enums.GameType;
import Yg_Final_Project.enums.PlayersType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MenuFrame extends JFrame{

    private JScrollPane scrollPane;
    private JPanel aboutPanel;
    private JPanel mainPanel;
    private JPanel learnPanel;
    private JMenuBar menuBar;

    public MenuFrame(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        int width = (int) (screenSize.width * 0.5);
        int height = (int) (screenSize.height * 0.6);

        setSize(width,height);
        setTitle("The Maze Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        aboutPanel = new JPanel();
        mainPanel = new JPanel();
        learnPanel = new JPanel();

        setAboutPanel();
        setMenuBar();
        setMainPanel();
        setLearnPanel();


        scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        setContentPane(scrollPane);
        scrollPane.setViewportView(mainPanel);
        setJMenuBar(menuBar);
        setVisible(true);
    }

    public void setAboutPanel() {
        try {
            File aboutFile = new File("C:\\Users\\USER\\Java_VScode\\Yg_Final_Project\\textFiles\\aboutAlgorithms.txt");
            Scanner myReader = new Scanner(aboutFile);
    
            // Using StringBuilder for efficient text building
            StringBuilder data = new StringBuilder("");
    
            while (myReader.hasNextLine()) {
                data.append(myReader.nextLine()).append("\n");
            }
    
            // Create JTextArea and apply formatting
            JTextArea dataArea = new JTextArea(data.toString());
            dataArea.setLineWrap(true);
            dataArea.setWrapStyleWord(true);
            dataArea.setFont(new Font("Serif", Font.PLAIN, 20));
            dataArea.setEditable(false);
            dataArea.setMargin(new Insets(10, 10, 10, 10)); // Add padding
    
            // Wrap in a JScrollPane for scrolling
            JScrollPane scrollPane = new JScrollPane(dataArea);
    
            // Set panel layout and add components
            aboutPanel.setLayout(new BorderLayout());
            aboutPanel.add(scrollPane, BorderLayout.CENTER);
    
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR! Data File Not Found...");
            e.printStackTrace();
        }
    }
    
    public void setMenuBar(){
        menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenuItem gameItem = new JMenuItem("game");
        gameItem.addActionListener(e -> {
            scrollPane.setViewportView(mainPanel);
            revalidate();
            repaint(); 
        });
        gameMenu.add(gameItem);

        JMenu learnMenu = new JMenu("Learn About the App");
        JMenuItem learnItem = new JMenuItem("learn");
        learnItem.addActionListener(e-> {
            scrollPane.setViewportView(learnPanel);
            revalidate();
            repaint(); 
        });
        learnMenu.add(learnItem);

        JMenu aboutMenu = new JMenu("About Algorithms");
        JMenuItem aboutItem = new JMenuItem("about");
        aboutItem.addActionListener(e -> {
            scrollPane.setViewportView(aboutPanel);
            revalidate();
            repaint(); 
        });
        aboutMenu.add(aboutItem);

        menuBar.add(gameMenu);
        menuBar.add(learnMenu);
        menuBar.add(aboutMenu);
    }

    public void setMainPanel(){
        mainPanel.setLayout(new BorderLayout());

        // StringBuilder builder = new StringBuilder();
        // builder.append("<html>Choose an Option:<br>the option you choose will affect the maze</html>");
        ImageIcon originalIcon = new ImageIcon("C:\\Users\\USER\\Java_VScode\\Yg_Final_Project\\textFiles\\mazeWithTimer.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(900, 500, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Create a label for explanation
        JLabel label = new JLabel(scaledIcon);
        //label.setAlignmentX(Component.CENTER_ALIGNMENT);
        //label.setFont(new Font("Arial", Font.BOLD, 25));

        // Create buttons
        JButton button1 = new JButton("Race each other");
        button1.addActionListener(e->buttonCallback(1));
        JButton button2 = new JButton("Collect More Prizes");
        button2.addActionListener(e->buttonCallback(2));
        JButton button3 = new JButton("Race in the darkness");
        button3.addActionListener(e->buttonCallback(3));

        button1.setFont(new Font("Arial", Font.BOLD, 20));
        button2.setFont(new Font("Arial", Font.BOLD, 20));
        button3.setFont(new Font("Arial", Font.BOLD, 20));


        JPanel panel = new JPanel(new GridLayout(0,3));
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);

        // Add label and buttons to the panel
        mainPanel.add(label, BorderLayout.CENTER);
        mainPanel.add(panel, BorderLayout.SOUTH);
    }

    public void setLearnPanel() {
        try {
            File aboutFile = new File("C:\\Users\\USER\\Java_VScode\\Yg_Final_Project\\textFiles\\aboutDeveloper.txt");
            Scanner myReader = new Scanner(aboutFile);
    
            // Using StringBuilder for efficient text building
            StringBuilder data = new StringBuilder();
    
            while (myReader.hasNextLine()) {
                data.append(myReader.nextLine()).append("\n");
            }
    
            // Create JTextArea and apply formatting
            JTextArea dataArea = new JTextArea(data.toString());
            dataArea.setLineWrap(true);
            dataArea.setWrapStyleWord(true);
            dataArea.setFont(new Font("Serif", Font.PLAIN, 16));
            dataArea.setEditable(false);
            dataArea.setMargin(new Insets(10, 10, 10, 10)); // Add padding
    
            // Wrap in a JScrollPane for scrolling
            JScrollPane scrollPane = new JScrollPane(dataArea);
    
            // Set panel layout and add components
            learnPanel.setLayout(new BorderLayout());
            learnPanel.add(scrollPane, BorderLayout.CENTER);
    
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR! Data File Not Found...");
            e.printStackTrace();
        }
    }

    public void buttonCallback(int btID){
        int size = -1;
        PlayersType mode = PlayersType.PLAYER_vs_PLAYER;
        switch (btID) {
            case 1: // race Mode
                size = chooseDifficulty();
                if(size == -1) {disposeAndStart(); return;}
                mode = chooseMode();
                if(mode == PlayersType.EXIT) {disposeAndStart(); return;}
                new GameFrame(size, mode, GameType.RACE);
                break;
            case 2: // Collect prizes
                size = chooseDifficulty();
                if(size == -1) {disposeAndStart(); return;}
                mode = chooseMode();
                if(mode == PlayersType.EXIT) {disposeAndStart(); return;}
                new GameFrame(size, mode, GameType.PRIZES);
                break;

            case 3: // race in the dark
                size = chooseDifficulty();
                if(size == -1) {disposeAndStart(); return;}
                mode = chooseMode();
                if(mode == PlayersType.EXIT) {disposeAndStart(); return;}
                new GameFrame(size, mode, GameType.Fog_OF_WAR);
                break;
        }
        this.dispose();
    }

    public int chooseDifficulty(){
        String[] responses = {"Easy", "Medium", "Hard"};

        int answer = JOptionPane.showOptionDialog(null, "Choose your difficulty", "Difficulty Level", JOptionPane.YES_NO_CANCEL_OPTION, 
        JOptionPane.INFORMATION_MESSAGE, null, responses, 0);

        switch (answer) {
            case -1: // exit
                return -1;
            case 0: // easy Mode
                return 15;
            case 1: // medium
                return 20;
            case 2: // hard
                return 25;
            default:
                this.dispose();
        }

        return -1;
    }

    public PlayersType chooseMode(){
        String[] responses = {"Player vs Player", "Player vs Computer", "Computer vs Computer"};

        int answer = JOptionPane.showOptionDialog(null, "Choose a mode", "mode Level", JOptionPane.YES_NO_OPTION, 
        JOptionPane.INFORMATION_MESSAGE, null, responses, 0);

        switch (answer) {
            case -1: // easy Mode
                return PlayersType.EXIT;
            case 0:
                return PlayersType.PLAYER_vs_PLAYER;
            case 1:
                return PlayersType.PLAYER_vs_COMPUTER;
            case 2:
                return PlayersType.COMPUTER_vs_COMPUTER;
            default:
                this.dispose();
        }

        return PlayersType.PLAYER_vs_PLAYER;
    }

    public void disposeAndStart(){
        this.dispose();
        new MenuFrame();
    }

    public static void main(String[] args) {
        new MenuFrame();
    }
    
}
