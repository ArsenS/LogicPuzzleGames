package logicpuzzlegames;

import java.util.BitSet;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class Sudoku extends GameModule {
    
    
    //Size and position constants for drawing to canvas
    final int WIDTH = 9;
    final int HEIGHT = 9;
    final int LOWER_RIGHT_X, LOWER_RIGHT_Y;
    
    private EventHandler<MouseEvent> mouseHandler;
    private EventHandler<KeyEvent> keyHandler;
    private final String startingLayout;
    private SudokuCell[][] gameGrid;
    private SudokuCell currentCell;
    
    Sudoku (Canvas canvas, String startingLayout) {
        super(canvas);
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
        this.gc.setFont(Font.font("Century Gothic", FontWeight.LIGHT, 18));
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
        
        this.mouseHandler = new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent e) {
       
                if (isWithinGrid(e.getX(), e.getY())) {
                    selectCell(e.getX(), e.getY());
                }
            }
        };
        this.keyHandler = new EventHandler<KeyEvent>() {
            
            @Override
            public void handle(KeyEvent e) {
                
                try {
                    if (aCellIsSelected()) {
                        int typedValue = Integer.parseInt(e.getCharacter()); 
                        writeToCurrentCell(typedValue);
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("NaN");
                }
            }
        };
        this.canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseHandler);
        this.canvas.addEventHandler(KeyEvent.KEY_TYPED, this.keyHandler);
    }
    
    private boolean gridIsFull() {
        for (int i = 0; i < this.gameGrid.length; i++) {
            for (int j = 0; j < this.gameGrid[0].length; j++) {
                if (this.gameGrid[i][j].getValue() == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void disableEventHandlers() {
        this.canvas.removeEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseHandler);
        this.canvas.removeEventHandler(KeyEvent.KEY_TYPED, this.keyHandler);
    }
    
    private boolean isWithinGrid(double x, double y) {
        return (x > UPPER_LEFT_X && x < LOWER_RIGHT_X && y > UPPER_LEFT_Y && y < LOWER_RIGHT_Y);
    }

    private void selectCell(double x, double y) {
        SudokuCell clickedCell = getClickedCell(x, y);
        if (clickedCell.isEditable()) {
            clickedCell.setXY((int) (x - (x%CELL_SIZE)), (int) (y - (y%CELL_SIZE)));
            if (this.currentCell != null) {
                deselectCell(this.currentCell.getX(), this.currentCell.getY());
            }
            if (clickedCell == this.currentCell) {
                this.currentCell = null;
            } else {
                this.currentCell = clickedCell;
                this.gc.setStroke(Color.BLUE);
                this.gc.strokeRect(this.currentCell.getX()+3, this.currentCell.getY()+3, CELL_SIZE-6, CELL_SIZE-6);
            }   
        }
    }
    
    public boolean aCellIsSelected() {
        return this.currentCell != null;
    }
    
    private void deselectCell(double x, double y) {
        this.gc.clearRect(x+2, y+2, CELL_SIZE-4, CELL_SIZE-4);
        rewriteCellValue(this.currentCell.getX(), this.currentCell.getY());
    }
    
    public void writeToCurrentCell(int value) {
        this.currentCell.setValue(value);
        rewriteCellValue(this.currentCell.getX(), this.currentCell.getY());
    }
    
    private void rewriteCellValue(double x, double y) {
        this.gc.clearRect(this.currentCell.getX()+5, this.currentCell.getY()+5, CELL_SIZE-10, CELL_SIZE-10);
        SudokuCell cellToFill = getClickedCell(x, y);
        if (cellToFill.getValue() > 0) {
            this.gc.setStroke(Color.BLUE);
            this.gc.setFont(Font.font("Century Gothic", FontWeight.LIGHT, 18));
            this.gc.strokeText(Integer.toString(cellToFill.getValue()), x+10, y+20);   
        }
    }
    
    private SudokuCell getClickedCell(double x, double y) {
        return this.gameGrid[(int)(x/CELL_SIZE)-(UPPER_LEFT_X/CELL_SIZE)][(int)(y/CELL_SIZE)-(UPPER_LEFT_Y/CELL_SIZE)];
    }
    
    public boolean validateSolution() {
        boolean valid = validateAllRows() && validateAllCols() && validateAllSquares();
        if (valid && gridIsFull()) {
            displayEndOfGameText("You win!");
            disableEventHandlers();
        }
        if (valid) {
            eraseErrorText();
        } else drawErrorText();
        return valid;
    }
    
    private boolean validateAllCols() {
        for (int i = 0; i < this.gameGrid.length; i++) {
            if (!validateRow(i)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean validateCol(int i) {
        BitSet row = new BitSet(10);
        for (SudokuCell cell: this.gameGrid[i]) {
            int cellValue = cell.getValue();
            if (cellValue == 0) { continue; }
            else if (row.get(cellValue)) {
                return false;
            } else {
                row.set(cellValue);
            }
        }
        return true;        
    }
    
    private boolean validateAllRows() {
        for (int j = 0; j < this.gameGrid[0].length; j++) {
            if (!validateCol(j)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean validateRow(int j) {
        BitSet col = new BitSet(10);
        for (SudokuCell[] row: this.gameGrid) {
            int cellValue = row[j].getValue();
            if (cellValue == 0) { continue; }
            else if (col.get(cellValue)) {
                return false;
            } else {
                col.set(cellValue);
            }
        }
        return true;
    }
    
    private boolean validateAllSquares() {
        for (int i = 0; i < this.gameGrid.length; i+=3) {
            for (int j = 0; j < this.gameGrid[0].length; j+=3) {
                if (!validateSquare(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean validateSquare(int startI, int startJ) {
        BitSet square = new BitSet(10);
        for (int i = startI; i < startI+3; i++) {
            for (int j = startJ; j < startJ+3; j++) {
                int cellValue = this.gameGrid[i][j].getValue();
                if (cellValue == 0) { continue; }
                else if (square.get(cellValue)) {
                    return false;
                } else {
                    square.set(cellValue);
                }
            }
        }
        return true;
    }
    
    private void drawErrorText() {
        eraseErrorText();
        this.gc.setStroke(Color.FIREBRICK);
        this.gc.setFont(Font.font("Century Gothic", FontWeight.LIGHT, 20));
        this.gc.strokeText("There are errors in the solution.", 25, 335);
    }
    
    private void eraseErrorText() {
        this.gc.clearRect(0, 310, 320, 30);
    }
}
