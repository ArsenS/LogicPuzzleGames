package logicpuzzlegames;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Sudoku implements GameModule {
    
    
    //Game grid coordinates and size constants for drawing to canvas
    //Upper left X,Y coordinates must be multiples of cell size to prevent misalignment
    final int UPPER_LEFT_X = 30;
    final int UPPER_LEFT_Y = 30;
    final int CELL_SIZE = 30;
    final int WIDTH = 9;
    final int HEIGHT = 9;
    final int LOWER_RIGHT_X, LOWER_RIGHT_Y;
    
    private Canvas canvas;
    private GraphicsContext gc;
    private String startingLayout;
    private SudokuCell[][] gameGrid;
    
    Sudoku (Canvas canvas, String startingLayout) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.startingLayout = startingLayout;
        LOWER_RIGHT_X = UPPER_LEFT_X + (HEIGHT * CELL_SIZE);
        LOWER_RIGHT_Y = UPPER_LEFT_Y + (WIDTH * CELL_SIZE);
    }
    
    @Override
    public void initializeGameGrid() {
        this.gameGrid = new SudokuCell[WIDTH][HEIGHT];
        for (int i = 0; i < this.gameGrid.length; i++) {
            for (int j = 0; j < this.gameGrid[0].length; j++) {
                char currentValue = this.startingLayout.charAt(i*9+j);
                if (currentValue == '0') {
                    this.gameGrid[i][j] = new SudokuCell();
                } else {
                    this.gameGrid[i][j] = new SudokuCell(Character.getNumericValue(currentValue));
                }
            }
        }
    };
    
    @Override
    public void drawStartingGrid() {
        for (int i = UPPER_LEFT_X; i <= LOWER_RIGHT_X; i += CELL_SIZE) {
            this.gc.setStroke(Color.LIGHTGRAY);
            this.gc.strokeLine(i, UPPER_LEFT_X, i, LOWER_RIGHT_X);
            this.gc.strokeLine(UPPER_LEFT_Y, i, LOWER_RIGHT_Y, i);
        }
        for (int i = UPPER_LEFT_X; i <= LOWER_RIGHT_X; i += CELL_SIZE) {
            if ((i-CELL_SIZE)%(3*CELL_SIZE) == 0) {
                this.gc.setStroke(Color.BLACK);
                this.gc.strokeLine(i, UPPER_LEFT_X, i, LOWER_RIGHT_X);
                this.gc.strokeLine(UPPER_LEFT_Y, i, LOWER_RIGHT_Y, i);
            }
        }
        this.gc.setStroke(Color.BLUE);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                int cellValue = this.gameGrid[i][j].getValue();
                if (cellValue > 0) {
                    this.gc.strokeText(Integer.toString(cellValue), (UPPER_LEFT_X + 10)+(i*CELL_SIZE), (UPPER_LEFT_Y+20)+(j*CELL_SIZE));
                }
            }
        }
    };
    
    @Override
    public void setupEventHandlers() {
        //TODO
    };
}
