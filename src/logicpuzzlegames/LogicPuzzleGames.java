/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logicpuzzlegames;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
        root.setPrefSize(800, 600);
        
        MenuBar menuBar = new MenuBar();
        Menu gameMenu = new Menu("Game");
        MenuItem newGameMenuItem = new MenuItem("New game");
        gameMenu.getItems().addAll(newGameMenuItem);
        menuBar.getMenus().add(gameMenu);
        root.setTop(menuBar);
        
        Canvas canvas = new Canvas(500, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);
        
        /*
        GridPane GUIPane = new GridPane();
        GUIPane.setAlignment(Pos.TOP_LEFT);
        GUIPane.setVgap(5);
        GUIPane.setPadding(new Insets(50, 25, 25, 25));
        GUIPane.add(new Button("test btn"), 0, 0);
        GUIPane.add(new Button("test btn"), 0, 1);
        GUIPane.add(new Button("test btn"), 0, 2);
        GUIPane.setGridLinesVisible(true);
        root.setRight(GUIPane);
        */
        /*
        int num = 1;
        for (int i = 90; i < 360; i+=30) {
            for (int j = 90; j < 360; j+=30) {
                gc.strokeText(Integer.toString(num), j+10, i+20);
                num++;
            }
        }*/
        GameGrid game = new Minesweeper(gc, 9, 9);
        
        game.drawGameGrid();
        game.setupMouseEventHandlers(canvas);
               
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
