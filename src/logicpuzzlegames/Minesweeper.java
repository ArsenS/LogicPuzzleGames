package logicpuzzlegames;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class Minesweeper implements GameModule {
    
    //Game grid coordinates and size constants for drawing to canvas
    //Upper left X,Y coordinates must be multiples of cell size to prevent misalignment
    final int UPPER_LEFT_X = 30; 
    final int UPPER_LEFT_Y = 30; 
    final int CELL_SIZE = 30;
    final int LOWER_RIGHT_X, LOWER_RIGHT_Y;
    final int WIDTH, HEIGHT;
    
    
    private Canvas canvas;
    private GraphicsContext gc;
    private EventHandler<MouseEvent> handler;
    private MinesweeperCell[][] gameGrid;
    private ArrayList<MinesweeperCell> mines = new ArrayList<>();
    private boolean firstMove = true;
    
    
    Minesweeper(Canvas canvas, int width, int height) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        WIDTH = width;
        HEIGHT = height;
        LOWER_RIGHT_X = UPPER_LEFT_X + (CELL_SIZE * WIDTH);
        LOWER_RIGHT_Y = UPPER_LEFT_Y + (CELL_SIZE * HEIGHT);
    }
    
    @Override
    public void initializeGameGrid() {
        this.gameGrid = new MinesweeperCell[WIDTH][HEIGHT];
        for (int i = 0; i < this.gameGrid.length; i++) {
            for (int j = 0; j < this.gameGrid[0].length; j++) {
                gameGrid[i][j] = new MinesweeperCell();
            }
        }
    }
    
    private void mineGameGrid(double x, double y) {
        int minesRemaining = (int) (WIDTH * HEIGHT * 0.15);
        System.out.println(minesRemaining+" mines to place.");
        MinesweeperCell firstCellClicked = getCurrentCell(x, y);
        while (minesRemaining > 0) {
            for (int i = 0; i < this.gameGrid.length; i++) {
                for (int j = 0; j < this.gameGrid[0].length; j++) {
                    MinesweeperCell currentCell = this.gameGrid[i][j];
                    if (currentCell != firstCellClicked && !currentCell.isMined() && Math.random() > 0.9 && minesRemaining > 0) {
                        currentCell.mineCell();
                        mines.add(currentCell);
                        incrementNeighborsCounter(i, j);
                        minesRemaining--;
                        if (minesRemaining == 0) break;
                    }
                }
            }   
        }
        System.out.println(minesRemaining+" mines remaining.");
    }
    
    private void incrementNeighborsCounter(int minedCellI, int minedCellJ) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (isWithinArray(minedCellI+i, minedCellJ+j) && !this.gameGrid[minedCellI+i][minedCellJ+j].isMined()) {
                    this.gameGrid[minedCellI+i][minedCellJ+j].incrementNumAdjacentMines();
                }
            }
        }
    }
    
    @Override
    public void drawStartingGrid() {
        this.gc.setFill(Color.CADETBLUE);
        this.gc.fillRect(UPPER_LEFT_X, UPPER_LEFT_Y, CELL_SIZE*WIDTH, CELL_SIZE*HEIGHT);
        this.gc.setStroke(Color.BLACK);
        for (int i = UPPER_LEFT_X; i <= LOWER_RIGHT_X; i+=CELL_SIZE) {
            this.gc.strokeLine(i, UPPER_LEFT_Y, i, LOWER_RIGHT_Y);
        }
        for (int i = UPPER_LEFT_Y; i <= LOWER_RIGHT_Y; i+=CELL_SIZE) {
            this.gc.strokeLine(UPPER_LEFT_X, i, LOWER_RIGHT_X, i);
        }
    }
    
    @Override
    public void setupEventHandlers() {
        
         this.handler = new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent e) {
       
                if (isWithinGrid(e.getX(), e.getY())) {
                    if (firstMove) {
                        mineGameGrid(e.getX(), e.getY());
                        firstMove = false;
                    }
                    
                    if (e.isPrimaryButtonDown()) {
                        revealCell(e.getX(), e.getY());
                        //System.out.println(e.getX()+","+e.getY());
                    } else if (e.isSecondaryButtonDown()) {
                        flagCell(e.getX(), e.getY());
                    }
                    if (gameIsWon() || gameIsOver()) {
                        disableEventHandler();
                    
                        String message = "You ";
                        if (gameIsWon()) {
                            message += "win!";
                        } else {
                            message += "lose...";
                        }
                        displayEndOfGameText(message);
                    }
                }
            }
        };
        this.canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this.handler);
    }
    
    private void disableEventHandler() {
        this.canvas.removeEventHandler(MouseEvent.MOUSE_PRESSED, this.handler);
    }
         
    public void solve() {
        mineGameGrid(UPPER_LEFT_X+5, UPPER_LEFT_Y+5);
        MinesweeperCell currentCell;
        for (int i = 0; i < this.gameGrid.length; i++) {
            for (int j = 0; j < this.gameGrid[0].length; j++) {
                currentCell = this.gameGrid[i][j];
                if (currentCell.isMined()) {
                    flagCell(UPPER_LEFT_X+(i*CELL_SIZE)+5, UPPER_LEFT_Y+(j*CELL_SIZE)+5);
                } else {
                    revealCell(UPPER_LEFT_X+(i*CELL_SIZE)+5, UPPER_LEFT_Y+(j*CELL_SIZE)+5);
                }
            }
        }
    }
    
    private boolean isWithinGrid(double x, double y) {
        return (x > UPPER_LEFT_X && x < LOWER_RIGHT_X && y > UPPER_LEFT_Y && y < LOWER_RIGHT_Y);
    }
    
    private boolean isWithinArray(int x, int y) {
        return (x >= 0 && y >= 0 && x < this.gameGrid.length && y < this.gameGrid[0].length);
    }
    
    private MinesweeperCell getCurrentCell(double x, double y) {
        return this.gameGrid[(int)(x/CELL_SIZE)-(UPPER_LEFT_X/CELL_SIZE)][(int)(y/CELL_SIZE)-(UPPER_LEFT_Y/CELL_SIZE)];
    }
    
    private void revealCell(double x, double y) {
        MinesweeperCell currentCell = getCurrentCell(x, y);
        currentCell.revealCell();
        if (currentCell.isMined()) {
            redrawCell(Color.DARKRED, x, y);
        } else {
            redrawCell(Color.LIGHTGRAY, x, y);
            if (currentCell.hasAdjacentMines()) {
                this.gc.setStroke(Color.DARKORANGE);
                this.gc.strokeText(Integer.toString(currentCell.getNumAdjacentMines()), x-(x%CELL_SIZE)+((int)CELL_SIZE * 0.33), y-(y%CELL_SIZE)+((int)CELL_SIZE * 0.66));
            }
        }
        if (!currentCell.hasAdjacentMines() && !currentCell.isMined()) {
            revealAllEmptyNeighbors(x, y);
        }
    }
    
    private void redrawCell(Color color, double x, double y) {
        this.gc.setFill(color);
        this.gc.fillRect(x-(x%CELL_SIZE)+1, y-(y%CELL_SIZE)+1, CELL_SIZE-2, CELL_SIZE-2);
    }
    
    private void revealAllEmptyNeighbors(double x, double y) {
        int clickedCellI = (int)(x/CELL_SIZE)-(UPPER_LEFT_X/CELL_SIZE);
        int clickedCellJ = (int)(y/CELL_SIZE)-(UPPER_LEFT_Y/CELL_SIZE);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (isWithinArray(clickedCellI+i, clickedCellJ+j) && !this.gameGrid[clickedCellI+i][clickedCellJ+j].isRevealed() && !this.gameGrid[clickedCellI+i][clickedCellJ+j].isMined() && !this.gameGrid[clickedCellI+i][clickedCellJ+j].isFlagged()) {
                   revealCell((clickedCellI+i+(UPPER_LEFT_X/CELL_SIZE))*CELL_SIZE, (clickedCellJ+j+(UPPER_LEFT_Y/CELL_SIZE))*CELL_SIZE);
                }
            }
        }
    }
       
    private void flagCell(double x, double y) {
        MinesweeperCell currentCell = getCurrentCell(x, y);
        if (!currentCell.isRevealed() && !currentCell.isFlagged()) {
            currentCell.flagCell();
            drawFlag(Color.CRIMSON, x, y);
        } else if (currentCell.isFlagged()) {
            unflagCell(x, y);
        }
    }
    
    private void unflagCell(double x, double y) {
        MinesweeperCell currentCell = getCurrentCell(x, y);
        if (currentCell.isFlagged()) {
            currentCell.unflagCell();
            redrawCell(Color.CADETBLUE, x, y);
        }
    }
    
    private void drawFlag(Color color, double x, double y) {
        this.gc.setFill(color);
        x = x-(x%CELL_SIZE)+1;
        y = y-(y%CELL_SIZE)+1;
        double[] xPoints = {x+(CELL_SIZE*0.15), x+(CELL_SIZE*0.15), x+(CELL_SIZE*0.85)};
        double[] yPoints = {y+(CELL_SIZE*0.15), y+(CELL_SIZE*0.85), y+(CELL_SIZE*0.5)};
        this.gc.fillPolygon(xPoints, yPoints, 3);   
    }
    
    public boolean gameIsWon() {
        for (MinesweeperCell mine: mines) {
            if (!mine.isFlagged()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean gameIsOver() {
        for (MinesweeperCell mine: mines) {
            if (mine.isRevealed()) {
                return true;
            }
        }
        return false;
    }
    
    private void displayEndOfGameText(String message) {
        this.gc.setStroke(Color.BLACK);
        this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
        this.gc.strokeText(message, 25, 25);
    }
}
