/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicpuzzlegames;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author FLD
 */
public class LogicPuzzleGames extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        //Group root = new Group();
        
        BorderPane root = new BorderPane();
        root.setPrefSize(400, 400);
        
        MenuBar menuBar = new MenuBar();
        Menu gameMenu = new Menu("Game");
        Menu minesweeperMenu = new Menu("New Minesweeper Game");
        MenuItem newEasyMinesweeperGame = new MenuItem("Easy (9x9)");
        MenuItem newMediumMinesweeperGame = new MenuItem("Medium (16x16)");
        MenuItem newHardMinesweeperGame = new MenuItem("Hard (16x30)");
        gameMenu.getItems().addAll(minesweeperMenu);
        minesweeperMenu.getItems().addAll(newEasyMinesweeperGame, newMediumMinesweeperGame, newHardMinesweeperGame);
        menuBar.getMenus().add(gameMenu);
        root.setTop(menuBar);
        
        
        newEasyMinesweeperGame.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                Canvas canvas = new Canvas(300, 300);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                root.setCenter(canvas);
                
                GameModule game = new Minesweeper(gc, 9, 9);
                game.initializeGameGrid();
                game.drawGameGrid();
                game.setupMouseEventHandlers(canvas);   
            }
        });
        
        newMediumMinesweeperGame.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                primaryStage.setWidth(600);
                primaryStage.setHeight(600);
                Canvas canvas = new Canvas(500, 500);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                root.setCenter(canvas);
                
                GameModule game = new Minesweeper(gc, 16, 16);
                game.initializeGameGrid();
                game.drawGameGrid();
                game.setupMouseEventHandlers(canvas);   
            }
        });
                
        newHardMinesweeperGame.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent e) {
                primaryStage.setWidth(950);
                primaryStage.setHeight(600);
                Canvas canvas = new Canvas(900, 500);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                root.setCenter(canvas);
                
                GameModule game = new Minesweeper(gc, 30, 16);
                game.initializeGameGrid();
                game.drawGameGrid();
                game.setupMouseEventHandlers(canvas);   
            }
        });
        

               
        primaryStage.setTitle("Canvas test"); 
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
