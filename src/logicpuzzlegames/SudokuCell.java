package logicpuzzlegames;


public class SudokuCell {
    
    private final boolean editable;
    private int value, x, y; 
    
    SudokuCell(int value) {
        this.editable = false;
        this.value = value;
        this.x = 0;
        this.y = 0;
    }
    
    SudokuCell() {
        this.editable = true;
        this.value = 0;
        this.x = 0;
        this.y = 0;
    }

    public boolean isEditable() {
        return this.editable;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public void setValue(int newValue) {
        if (this.isEditable()) {
            this.value = newValue;
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    
    public void setXY(int x, int y) {
        if (this.x == 0) {
            this.x = x;
        }
        if (this.y == 0) {
            this.y = y;
        }
    }
}
