package logicpuzzlegames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class LogicPuzzleGames extends Application {
    
    Stage stage;
    BorderPane root;
    GameModule game;
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
        MenuItem newEasyGame = new MenuItem("Easy");
        setupSudokuMenuEventHandler(newEasyGame);
        MenuItem newMediumGame = new MenuItem("Medium");
        setupSudokuMenuEventHandler(newMediumGame);
        MenuItem newHardGame = new MenuItem("Hard");
        setupSudokuMenuEventHandler(newHardGame);
        MenuItem newVeryHardGame = new MenuItem("Very Hard");
        setupSudokuMenuEventHandler(newVeryHardGame);
        sudokuMenu.getItems().addAll(newEasyGame, newMediumGame, newHardGame, newVeryHardGame);
        return sudokuMenu;
    }
    
    private void setupSudokuButton() {
        Button validateBtn = new Button("Validate");
        validateBtn.setFocusTraversable(false);
        root.setBottom(validateBtn);
        root.setAlignment(validateBtn, Pos.CENTER);
        root.setMargin(validateBtn, new Insets(0, 0, 50, 0));
        setupButtonEventHandler(validateBtn);
    }
    
    private void setupButtonEventHandler(Button btn) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                System.out.println(((Sudoku)game).validateSolution());
            }
        });
    }

    
    private void setupSudokuMenuEventHandler(MenuItem selectedDifficulty) {
        selectedDifficulty.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                resizeStage(400, 450);
                Canvas canvas = new Canvas(350, 350);
                canvas.setFocusTraversable(true);
                root.setCenter(canvas);
                setupSudokuButton();
                
                String startLayout = getRandomStartLayout(selectedDifficulty.getText());
                //String test = "379000014060010070080009005435007000090040020000800436900700080040080050850000249";
                loadGameModule(new Sudoku(canvas, startLayout));
            }
        });
    }
    
    private String getRandomStartLayout(String difficulty) {
        
        Random rand = new Random();
        int selectedLayout = rand.nextInt(100);
        String sudokuLayout = "";
        String file = System.getProperty("user.dir")+"\\src\\logicpuzzlegames\\sudoku_config\\"+difficulty+".txt";
        Path filePath = Paths.get(file);
        
        try (InputStream in = Files.newInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                selectedLayout--;
                if (selectedLayout == 0) {
                    sudokuLayout = line;
                }
            }
        } catch (IOException x) {
            System.err.println(x);
        }
        return sudokuLayout;
    }
    
    private void loadGameModule(GameModule gameModule) {
        game = gameModule;
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
