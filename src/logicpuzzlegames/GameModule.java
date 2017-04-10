package logicpuzzlegames;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public abstract class GameModule {
    
    //Game grid coordinates and size constants for drawing to canvas
    protected int UPPER_LEFT_X = 30;
    protected int UPPER_LEFT_Y = 30;
    protected int CELL_SIZE = 30;
    
    Canvas canvas;
    GraphicsContext gc;
    
    public GameModule(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }
    
    public abstract void initializeGameGrid();
    
    public abstract void drawStartingGrid();
    
    public abstract void setupEventHandlers();
    
    void displayEndOfGameText(String message) {
        gc.setStroke(Color.BLACK);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
        gc.strokeText(message, 25, 25);
    }    
}
