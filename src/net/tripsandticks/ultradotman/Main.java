package net.tripsandticks.ultradotman;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    
    static final int WINDOW_WIDTH = 600;
    static final int WINDOW_HEIGHT = 450;
    static final Color TEXT_COLOR = Color.gray(0.8);
    
    public static void main(String[] args) {
        launch();
    }
    
    @Override
    public void start(Stage stage) {

        InputState color = new InputState(255, 0, 0);
        TradeSpace tradeSpace = new TradeSpace();
        
        final Group root = new Group();
        final SimulationRenderer renderer = new SimulationRenderer(color);
        final LinearPlot plot = new LinearPlot(200, 200, 375, 225, tradeSpace);
        final InputInterface settings = new InputInterface(color, plot, renderer,
                tradeSpace);
        
        stage.setTitle("ultradotman");
        
        root.getChildren().add(renderer.getGroup());
        root.getChildren().add(settings.getGroup());
        root.getChildren().add(plot.getPlot());
        root.getChildren().add(plot.getAxisPicker());
        
        final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setFill(renderer.getBackground());
        scene.setCamera(renderer.getCamera());
        
        stage.setScene(scene);
        stage.show();
    }
}
