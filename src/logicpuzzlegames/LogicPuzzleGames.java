package logicpuzzlegames;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class LogicPuzzleGames extends Application {
    
    Stage stage;
    BorderPane root;
    
    @Override
    public void start(Stage primaryStage) {
        
        stage = primaryStage;
        root = new BorderPane();
        root.setPrefSize(400, 400);
               
        MenuBar menuBar = setupMenuBar();        
        root.setTop(menuBar);

        stage.setTitle("Logic Puzzle Games"); 
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    private MenuBar setupMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu gameMenu = new Menu("Game");
        Menu minesweeperMenu = setupMinesweeperMenu();
        MenuItem newSudokuGame = setupSudokuMenu();
        gameMenu.getItems().addAll(minesweeperMenu, newSudokuGame);
        menuBar.getMenus().add(gameMenu);
        return menuBar;
    }
    
    private Menu setupMinesweeperMenu() {
        Menu minesweeperMenu = new Menu("New Minesweeper Game");
        MenuItem newEasyMinesweeperGame = new MenuItem("Easy (9x9)");
        setupMinesweeperMenuEventHandler(newEasyMinesweeperGame, 350, 350, 9, 9);
        MenuItem newMediumMinesweeperGame = new MenuItem("Medium (16x16)");
        setupMinesweeperMenuEventHandler(newMediumMinesweeperGame, 550, 550, 16, 16);
        MenuItem newHardMinesweeperGame = new MenuItem("Hard (16x30)");
        setupMinesweeperMenuEventHandler(newHardMinesweeperGame, 900, 600, 30, 16);
        minesweeperMenu.getItems().addAll(newEasyMinesweeperGame, newMediumMinesweeperGame, newHardMinesweeperGame);
        return minesweeperMenu;
    }
        
    private void setupMinesweeperMenuEventHandler(MenuItem selectedGameDifficulty, int canvasWidth, int canvasHeight, int gameWidth, int gameHeight) {
        selectedGameDifficulty.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e) {
                    resizeStage(canvasWidth+50, canvasHeight+50);
                    Canvas canvas = new Canvas(canvasWidth, canvasHeight);
                    root.setCenter(canvas);

                    loadGameModule(new Minesweeper(canvas, gameWidth, gameHeight));
                }
            });    
    }
    
    private MenuItem setupSudokuMenu() {
        Menu sudokuMenu = new Menu("New Sudoku Game");
        MenuItem newEasySudokuGame = new MenuItem("Easy");
        MenuItem newMediumSudokuGame = new MenuItem("Medium");
        MenuItem newHardSudokuGame = new MenuItem("Hard");
        MenuItem newVeryHardSudokuGame = new MenuItem("Very Hard");
        sudokuMenu.getItems().addAll(newEasySudokuGame, newMediumSudokuGame, newHardSudokuGame, newVeryHardSudokuGame);
        setupSudokuMenuEventHandler(sudokuMenu);
        return sudokuMenu;
    }

    
    private void setupSudokuMenuEventHandler(MenuItem newSudokuGame) {
        newSudokuGame.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                resizeStage(400, 400);
                Canvas canvas = new Canvas(350, 350);
                canvas.setFocusTraversable(true);
                root.setCenter(canvas);
                
                String test = "379000014060010070080009005435007000090040020000800436900700080040080050850000249";
                loadGameModule(new Sudoku(canvas, test));
            }
        });
    }
    
    private void loadGameModule(GameModule game) {
        game.initializeGameGrid();
        game.drawStartingGrid();
        game.setupEventHandlers();
        //((Minesweeper)game).solve();
    }
    
    private void resizeStage(int width, int height) {
        this.stage.setWidth(width);
        this.stage.setHeight(height);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
