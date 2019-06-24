package net.tripsandticks.ultradotman.frontend;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.tripsandticks.ultradotman.backend.InputState;
import net.tripsandticks.ultradotman.backend.Simulation;
import net.tripsandticks.ultradotman.backend.TradeSpace;

public class Main extends Application {
    
    static final int WINDOW_WIDTH = 600;
    static final int WINDOW_HEIGHT = 450;
    static final Color TEXT_COLOR = Color.gray(0.8);
    
    public static void main(String[] args) {
        launch();
    }
    
    @Override
    public void start(Stage stage) {
        InputState initialState = new InputState(255, 0, 0);
        Simulation initialResult = new Simulation(initialState);
        TradeSpace tradeSpace = new TradeSpace();
        
        final Group root = new Group();
        final SimulationRenderer renderer = new SimulationRenderer(initialResult);
        final LinearPlot plot = new LinearPlot(200, 200, 375, 225, tradeSpace);
        final AxisPicker axisPicker = new AxisPicker(plot, 375, 0);
        final InputInterface settings = new InputInterface(initialState, plot,
                                                 renderer, tradeSpace);
        
        stage.setTitle("ultradotman");
        
        root.getChildren().add(renderer.getGroup());
        root.getChildren().add(settings.getGroup());
        root.getChildren().add(plot.getPlot());
        root.getChildren().add(axisPicker.getGroup());
        
        final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setFill(renderer.getBackground());
        scene.setCamera(renderer.getCamera());
        
        stage.setScene(scene);
        stage.show();
    }
}
