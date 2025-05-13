package Yg_Final_Project;

import javax.swing.JLabel;

public class Cell extends JLabel{

    private int row;
    private int column;

    // if true there is a wall
    private boolean top;
    private boolean bottom;
    private boolean right;
    private boolean left;

    // if viseted by player / computer / algorithm
    private boolean humanVisit;
    private boolean computerVisit;
    private boolean algoVisit;

    // for fog mode
    private boolean discovred;

    private Prize prize;

    public Cell(int row, int column, Prize prize){
        this.row = row;
        this.column = column;

        top = bottom = left = right = true;
		humanVisit = computerVisit = algoVisit = discovred = false;

        this.prize = prize;
    }

    
// ------------------ Setters ----------------------

    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }

    public void setTop(boolean top) {
        this.top = top;
    }
    
    public void setLeft(boolean left) {
        this.left = left;
    }
    
    public void setRight(boolean right) {
        this.right = right;
    }

    public void setHumanVisit(boolean con){
         humanVisit = con;
    }

    public void setComputerVisit(boolean con){
        computerVisit = con;
    }

    public void setAlgoVisit(boolean con) {
        algoVisit = con;
    }

    public void setPrize(Prize prize) {
        this.prize = prize;
    }

    public void setDiscovred(boolean discovred) {
        this.discovred = discovred;
    }

// --------- Check walls methods (getters) -------------

    public boolean isBottomOn() {
		return bottom;
	}

	public boolean isTopOn() {
		return top;
	}

	public boolean isLeftOn() {
		return left;
	}

	public boolean isRightOn() {
		return right;
	}

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean getHumanVisit(){
        return humanVisit;
    }

    public boolean getComputerVisit(){
        return computerVisit;
    }

    public boolean getAlgoVisit(){
        return algoVisit;
    }

    public boolean getIsDiscovred(){
        return discovred;
    }

// ------------------- Other Methods -----------------------

    public boolean has4walls(){
		return (top && bottom && left && right); 
	}

    public Prize getPrize() {
        return prize;
    }
    

    // @Override
    // public boolean equals(Object o) {
    //     if (this == o) return true;
    //     if (o == null || getClass() != o.getClass()) return false;

    //     Cell cell = (Cell) o;

    //     return this.row == cell.row && this.column == cell.column;
    // }

    // @Override
    // public int hashCode() {
    //     return 31 * row + column;
    // }


}