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


public class Minesweeper implements GameModule {
        
    GraphicsContext gc;
    MinesweeperSquare[][] gameGrid;
    
    //Game grid coordinates and size constants for drawing to canvas
    //Upper left X,Y coordinates must be multiples of square size to prevent misalignment
    final int UPPER_LEFT_X = 0; 
    final int UPPER_LEFT_Y = 0; 
    final int SQUARE_SIZE = 30;
    final int LOWER_RIGHT_X, LOWER_RIGHT_Y;
    final int WIDTH, HEIGHT;
    
    Minesweeper(GraphicsContext gc, int width, int height) {
        this.gc = gc;
        WIDTH = width;
        HEIGHT = height;
        LOWER_RIGHT_X = UPPER_LEFT_X + (SQUARE_SIZE * WIDTH);
        LOWER_RIGHT_Y = UPPER_LEFT_Y + (SQUARE_SIZE * HEIGHT);
    }
    
    @Override
    public void initializeGameGrid() {
        this.gameGrid = new MinesweeperSquare[WIDTH][HEIGHT];
        for (int i = 0; i < this.gameGrid.length; i++) {
            for (int j = 0; j < this.gameGrid[0].length; j++) {
                gameGrid[i][j] = new MinesweeperSquare();
            }
        }
        mineGameGrid();
    }
    
    private void mineGameGrid() {
        
        int minesRemaining = (int) (WIDTH * HEIGHT * 0.15);
        System.out.println("We goan get "+minesRemaining+" mines up in here.");
        for (int i = 0; i < this.gameGrid.length; i++) {
            for (int j = 0; j < this.gameGrid[0].length; j++) {
                if (Math.random() > 0.8 && minesRemaining > 0) {
                    this.gameGrid[i][j].mineSquare();
                    incrementNeighborsCounter(i, j);
                    minesRemaining--;
                }
            }
        }
        System.out.println(minesRemaining+" mines remaining");
    }
    
    private void incrementNeighborsCounter(int minedSquareI, int minedSquareJ) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (isWithinArray(minedSquareI+i, minedSquareJ+j) && !this.gameGrid[minedSquareI+i][minedSquareJ+j].isMined()) {
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
                        System.out.println(e.getX()+","+e.getY());
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
        gc.fillRect(UPPER_LEFT_X, UPPER_LEFT_Y, SQUARE_SIZE*WIDTH, SQUARE_SIZE*HEIGHT);
        gc.setStroke(Color.BLACK);
        for (int i = UPPER_LEFT_X; i <= LOWER_RIGHT_X; i+=SQUARE_SIZE) {
            gc.strokeLine(i, UPPER_LEFT_Y, i, LOWER_RIGHT_Y);
        }
        for (int i = UPPER_LEFT_Y; i <= LOWER_RIGHT_Y; i+=SQUARE_SIZE) {
            gc.strokeLine(UPPER_LEFT_X, i, LOWER_RIGHT_X, i);
        }
    }
    
    public boolean isWithinGrid(double x, double y) {
        return (x > UPPER_LEFT_X && x < LOWER_RIGHT_X && y > UPPER_LEFT_Y && y < LOWER_RIGHT_Y);
    }
    
    public boolean isWithinArray(int x, int y) {
        return (x >= 0 && y >= 0 && x < this.gameGrid.length && y < this.gameGrid[0].length);
    }
    
    private MinesweeperSquare getCurrentSquare(double x, double y) {
        return this.gameGrid[(int)(x/SQUARE_SIZE)-(UPPER_LEFT_X/SQUARE_SIZE)][(int)(y/SQUARE_SIZE)-(UPPER_LEFT_Y/SQUARE_SIZE)];
    }
    
    public void revealSquare(double x, double y) {
        MinesweeperSquare currentSquare = getCurrentSquare(x, y);
        currentSquare.revealSquare();
        if (currentSquare.isMined()) {
            gc.setFill(Color.DARKRED);
            gc.fillRect(x-(x%SQUARE_SIZE)+1, y-(y%SQUARE_SIZE)+1, SQUARE_SIZE-1, SQUARE_SIZE-1);
        } else {
            gc.setFill(Color.LIGHTGRAY);
            gc.fillRect(x-(x%SQUARE_SIZE)+1, y-(y%SQUARE_SIZE)+1, SQUARE_SIZE-1, SQUARE_SIZE-1);
            if (currentSquare.hasAdjacentMines()) {
                gc.setStroke(Color.DARKORANGE);
                gc.strokeText(Integer.toString(currentSquare.numAdjacentMines()), x-(x%SQUARE_SIZE)+((int)SQUARE_SIZE * 0.33), y-(y%SQUARE_SIZE)+((int)SQUARE_SIZE * 0.66));
            }
        }
        
        if (!currentSquare.hasAdjacentMines() && !currentSquare.isMined()) {
            revealAllEmptyNeighbors(x, y);
        }
    }
    
    public void revealAllEmptyNeighbors(double x, double y) {
        int clickedSquareI = (int)(x/SQUARE_SIZE)-(UPPER_LEFT_X/SQUARE_SIZE);
        int clickedSquareJ = (int)(y/SQUARE_SIZE)-(UPPER_LEFT_Y/SQUARE_SIZE);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (isWithinArray(clickedSquareI+i, clickedSquareJ+j) && !this.gameGrid[clickedSquareI+i][clickedSquareJ+j].isRevealed() && !this.gameGrid[clickedSquareI+i][clickedSquareJ+j].isMined() && !this.gameGrid[clickedSquareI+i][clickedSquareJ+j].isFlagged()) {
                   revealSquare((clickedSquareI+i+(UPPER_LEFT_X/SQUARE_SIZE))*SQUARE_SIZE, (clickedSquareJ+j+(UPPER_LEFT_Y/SQUARE_SIZE))*SQUARE_SIZE);
                }
            }
        }
    }
    
    public void flagSquare(double x, double y) {
        MinesweeperSquare currentSquare = getCurrentSquare(x, y);
        if (!currentSquare.isRevealed() && !currentSquare.isFlagged()) {
            currentSquare.flagSquare();
            gc.setFill(Color.CRIMSON);
            x = x-(x%SQUARE_SIZE)+1;
            y = y-(y%SQUARE_SIZE)+1;
            double[] xPoints = {x+(SQUARE_SIZE*0.15), x+(SQUARE_SIZE*0.15), x+(SQUARE_SIZE*0.85)};
            double[] yPoints = {y+(SQUARE_SIZE*0.15), y+(SQUARE_SIZE*0.85), y+(SQUARE_SIZE*0.5)};
            gc.fillPolygon(xPoints, yPoints, 3);   
        } else if (currentSquare.isFlagged()) {
            unflagSquare(x, y);
        }
    }
    
    public void unflagSquare(double x, double y) {
        MinesweeperSquare currentSquare = getCurrentSquare(x, y);
        if (currentSquare.isFlagged()) {
            currentSquare.unflagSquare();
            gc.setFill(Color.CADETBLUE);
            gc.fillRect(x-(x%SQUARE_SIZE)+2, y-(y%SQUARE_SIZE)+2, SQUARE_SIZE-2, SQUARE_SIZE-2);
        }
    }
    
}
