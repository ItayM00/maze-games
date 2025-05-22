package Yg_Final_Project.base_classes;

import Yg_Final_Project.enums.PrizeType;

public class Prize {

    private int row;
    private int col;
    private int pointsWorth;
    private boolean isCollected;
    private PrizeType type;


    public Prize(int row, int col, int pointsWorth, boolean isCollected, PrizeType type){
        this.row = row;
        this.col = col;
        this.pointsWorth = pointsWorth;
        this.isCollected = isCollected;
        this.type = type;
    }


    public boolean getIsCollected(){
        return isCollected;
    }

    public void setCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }

    public PrizeType getType() {
        return type;
    }

    public int getPointsWorth() {
        return pointsWorth;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
    
}
