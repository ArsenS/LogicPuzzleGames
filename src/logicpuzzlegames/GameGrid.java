/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicpuzzlegames;

import javafx.scene.canvas.Canvas;


public interface GameGrid {
    
    public void drawGameGrid();
    
    public void setupMouseEventHandlers(Canvas canvas);
    
    //public boolean isWithinGrid(int x, int y);
    
    //public boolean isWithinGrid(double x, double y);
    
    //public void selectGridZone(double x, double y);
    
    //public void addValueToGrid(int value);
}
