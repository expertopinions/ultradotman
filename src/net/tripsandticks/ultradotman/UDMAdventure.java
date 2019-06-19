package net.tripsandticks.ultradotman;

import java.util.concurrent.ThreadLocalRandom;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import net.tripsandticks.ultradotman.LinearPlot.PropertyAxis;

public class UDMAdventure extends Application {
    
    private static final int ULTRADOTMAN_RADIUS = 40;
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 450;

    private RGBColor color = new RGBColor(255, 0, 0);
    private EmergentProperties ilities = new EmergentProperties(color);
    private TradeSpace tradeSpace = new TradeSpace();
    
    private final Group root = new Group();
    private final PhongMaterial dotmanMaterial = new PhongMaterial();
    private LinearPlot plot = new LinearPlot(200, 200, PropertyAxis.SATURATION,
            PropertyAxis.VALUE, tradeSpace);
    private Group plotGroup = new Group();
    
    public static void main(String[] args) {
        launch();
    }
    
    @Override
    public void start(Stage stage) {
        stage.setTitle("ultradotman");
        
        tradeSpace.add(ilities);
        plot.updatePoints(plotGroup);
        plotGroup.setTranslateX(375);
        plotGroup.setTranslateY(225);
        
        construct3DElements();
        
        root.getChildren().add(constructSettings());
        root.getChildren().add(plotGroup);
        
        Camera camera = new PerspectiveCamera();
        
        final Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setFill(Color.color(0.05, 0.05, 0.05, 1.0));
        scene.setCamera(camera);
        
        stage.setScene(scene);
        stage.show();
    }
    
    private void construct3DElements() {
        updateMaterial();
        
        Sphere dotman = new Sphere(ULTRADOTMAN_RADIUS);
        dotman.setMaterial(dotmanMaterial);
        dotman.setTranslateX(WINDOW_WIDTH / 2);
        dotman.setTranslateY(WINDOW_HEIGHT / 2);

        root.getChildren().add(dotman);
        
        PointLight light = new PointLight();
        light.setColor(Color.gray(1));
        light.setTranslateX(WINDOW_WIDTH / 5);
        light.setTranslateY(WINDOW_HEIGHT / 6);
        light.setTranslateZ(-WINDOW_HEIGHT / 2);
        
        root.getChildren().add(light);
    }
    
    private GridPane constructSettings() {
        GridPane settings = new GridPane();

        Label labelR = new Label("Red: ");
        Label labelG = new Label("Green: ");
        Label labelB = new Label("Blue: ");
        
        GridPane.setHalignment(labelR, HPos.RIGHT);
        GridPane.setHalignment(labelG, HPos.RIGHT);
        GridPane.setHalignment(labelB, HPos.RIGHT);
        
        labelR.setTextFill(Color.gray(0.8));
        labelG.setTextFill(Color.gray(0.8));
        labelB.setTextFill(Color.gray(0.8));

        settings.add(labelR, 0, 2);
        settings.add(labelG, 0, 3);
        settings.add(labelB, 0, 4);

        TextField fieldR = new TextField(""+color.getRed());
        TextField fieldG = new TextField(""+color.getGreen());
        TextField fieldB = new TextField(""+color.getBlue());
        
        fieldR.setPrefWidth(75);
        fieldG.setPrefWidth(75);
        fieldB.setPrefWidth(75);

        settings.add(fieldR, 1, 2);
        settings.add(fieldG, 1, 3);
        settings.add(fieldB, 1, 4);

        Button go = new Button("EXECUTE");
        Label emptyLabel = new Label("");
        Button random = new Button("RANDOM");

        settings.add(go, 1, 5);
        settings.add(emptyLabel, 1, 1);
        settings.add(random, 1, 0);
        settings.setPadding(new Insets(5));

        go.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                try {
                    int red = Integer.parseInt(fieldR.getText());
                    int green = Integer.parseInt(fieldG.getText());
                    int blue = Integer.parseInt(fieldB.getText());
                    
                    color = new RGBColor(red, green, blue);
                    update();
                }
                catch (NumberFormatException e) {
                    System.err.println("invalid input");
                }
            }
        });

        random.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                int red = ThreadLocalRandom.current().nextInt(0,256);
                int green = ThreadLocalRandom.current().nextInt(0,256);
                int blue = ThreadLocalRandom.current().nextInt(0,256);

                fieldR.setText("" + red);
                fieldG.setText("" + green);
                fieldB.setText("" + blue);

                color = new RGBColor(red, green, blue);
                update();
            }
        });

        return settings;
    }
    
    private void update() {
        updateMaterial();
        updateIlities();
    }
    
    private void updateMaterial() {
        dotmanMaterial.setSpecularColor(Color.gray(0));
        dotmanMaterial.setDiffuseColor(Color.rgb(color.getRed(),
                color.getBlue(), color.getGreen()));
    }
    
    private void updateIlities() {
        ilities = new EmergentProperties(color);
        tradeSpace.add(ilities);
        plot.updatePoints(plotGroup);
    }
}
