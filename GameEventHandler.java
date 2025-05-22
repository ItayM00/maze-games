package Yg_Final_Project;

public interface GameEventHandler {
    
    void updateScoreBoard(int playerIndex);
    void printEndMessage(int winnerIndex);
    void dispose();
    void addPoints(int playerIndex, int points);
    
}
