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

                    GameModule game = new Minesweeper(canvas, gameWidth, gameHeight);
                    game.initializeGameGrid();
                    game.drawGameGrid();
                    game.setupEventHandlers();
                    //game.solve();
                }
            });    
    }
    
    private MenuItem setupSudokuMenu() {
        MenuItem newSudokuGame = new MenuItem("New Sudoku Game");
        setupSudokuMenuEventHandler();
        return newSudokuGame;
    }

    
    private void setupSudokuMenuEventHandler() {
        //TODO
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
