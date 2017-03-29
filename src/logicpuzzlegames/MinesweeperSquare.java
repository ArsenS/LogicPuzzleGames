/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicpuzzlegames;

/**
 *
 * @author FLD
 */
public class MinesweeperSquare {
    
    private boolean revealed = false;
    private boolean mined = false;
    private boolean flagged = false;
    private int numAdjacentMines = 0;
    
    MinesweeperSquare() {};
    
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
    
    public int numAdjacentMines() {
        return this.numAdjacentMines;
    }
    
    public void revealSquare() {
        this.revealed = true;
    }
    
    public void mineSquare() {
        this.mined = true;
    }
    
    public void flagSquare() {
        this.flagged = true;
    }
    
    public void incrementNumAdjacentMines() {
        this.numAdjacentMines++;
    }
}
