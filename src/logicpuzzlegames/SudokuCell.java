package logicpuzzlegames;


public class SudokuCell {
    
    private final boolean editable;
    private int value;
    
    SudokuCell(int value) {
        this.editable = false;
        this.value = value;
    }
    
    SudokuCell() {
        this.editable = true;
        this.value = 0;
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
}
