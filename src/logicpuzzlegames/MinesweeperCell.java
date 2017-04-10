package logicpuzzlegames;


public class MinesweeperCell {
    
    private boolean revealed;
    private boolean mined;
    private boolean flagged;
    private int numAdjacentMines;
    
    MinesweeperCell() {
        this.revealed = false;
        this.mined = false;
        this.flagged = false;
        this.numAdjacentMines = 0;
    };
    
    public boolean isRevealed() {
        return this.revealed;
    }
    
    public boolean isMined() {
        return mined;
    }
    
    public boolean isFlagged() {
        return flagged;
    }
    
    public boolean hasAdjacentMines() {
        return this.numAdjacentMines > 0;
    }
    
    public int getNumAdjacentMines() {
        return this.numAdjacentMines;
    }
    
    public void revealCell() {
        this.revealed = true;
    }
    
    public void mineCell() {
        this.mined = true;
    }
    
    public void flagCell() {
        this.flagged = true;
    }
    
    public void unflagCell() {
        this.flagged = false;
    }
    
    public void incrementNumAdjacentMines() {
        this.numAdjacentMines++;
    }
}
