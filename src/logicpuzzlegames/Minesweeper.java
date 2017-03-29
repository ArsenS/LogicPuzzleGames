/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicpuzzlegames;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


public class Minesweeper implements GameGrid {
    
    GraphicsContext gc;
    MinesweeperSquare[][] gameGrid;
    
    Minesweeper(GraphicsContext gc, int width, int height) {
        this.gc = gc;
        this.gameGrid = initializeGameGrid(width, height);
    }
    
    private  MinesweeperSquare[][] initializeGameGrid(int width, int height) {
        this.gameGrid = new MinesweeperSquare[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gameGrid[i][j] = new MinesweeperSquare();
            }
        }
        mineGameGrid();
        return gameGrid;
    }
    
    private void mineGameGrid() {
        
        int minesRemaining = 10;
        
        for (int i = 0; i < this.gameGrid.length; i++) {
            for (int j = 0; j < this.gameGrid[0].length; j++) {
                if (i==j) {
                    this.gameGrid[i][j].mineSquare();
                    incrementNeighborsCounter(i, j);
                }
            }
        }
    }
    
    private void incrementNeighborsCounter(int minedSquareI, int minedSquareJ) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (isWithinGrid(minedSquareI+i, minedSquareJ+j)) {
                    this.gameGrid[minedSquareI+i][minedSquareJ+j].incrementNumAdjacentMines(); 
                }
            }
        }
    }
    
    @Override
    public void setupMouseEventHandlers(Canvas canvas) {
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent e) {

                if (isWithinGrid(e.getX(), e.getY())) {
                    if (e.isPrimaryButtonDown()) {
                        revealSquare(e.getX(), e.getY());
                    } else if (e.isSecondaryButtonDown()) {
                        flagSquare(e.getX(), e.getY());
                    }
                }
            }
        });
    }
    
    @Override
    public void drawGameGrid() {
        gc.setFill(Color.CADETBLUE);
        gc.fillRect(90, 90, 270, 270);
        gc.setStroke(Color.BLACK);
        for (int i = 90; i <= 360; i+=30) {
            gc.strokeLine(90, i, 360, i);
            gc.strokeLine(i, 90, i, 360);
        }
    }
    
    public boolean isWithinGrid(double x, double y) {
        return (x > 90 && x < 360 && y > 90 && y < 360);
    }
    
    public boolean isWithinGrid(int x, int y) {
        return (x >= 0 && y >= 0 && x < this.gameGrid.length-1 && y < this.gameGrid[0].length);
    }
    
    private MinesweeperSquare getCurrentSquare(double x, double y) {
        return this.gameGrid[(int)(x/30)-3][(int)(y/30)-3];
    }
    
    public void revealSquare(double x, double y) {
        MinesweeperSquare currentSquare = getCurrentSquare(x, y);
        currentSquare.revealSquare();
        if (currentSquare.isMined()) {
            gc.setFill(Color.CRIMSON);
            gc.fillRect(x-(x%30)+1, y-(y%30)+1, 29, 29);
            gc.setFill(Color.BLACK);
            gc.fillOval(x-(x%30)+3, y-(y%30)+3, 25, 25);
        } else {
            gc.setFill(Color.LIGHTGRAY);
            gc.fillRect(x-(x%30)+1, y-(y%30)+1, 29, 29);
            if (currentSquare.hasAdjacentMines()) {
                gc.setStroke(Color.DARKORANGE);
                gc.strokeText(Integer.toString(currentSquare.numAdjacentMines()), x-(x%30)+12, y-(y%30)+20);
            }
        }
        
    }
    
    public void flagSquare(double x, double y) {
        MinesweeperSquare currentSquare = getCurrentSquare(x, y);
        if (!currentSquare.isRevealed()) {
            currentSquare.flagSquare();
            gc.setFill(Color.CADETBLUE);
            x = x-(x%30)+1;
            y = y-(y%30)+1;
            double[] xPoints = {x+4, x+4, x+24};
            double[] yPoints = {y+4, y+24, y+14};
            gc.fillPolygon(xPoints, yPoints, 3);   
        }
    }
    
}
