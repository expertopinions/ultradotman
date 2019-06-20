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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import net.tripsandticks.ultradotman.LinearPlot.PropertyAxis;

public class Main extends Application {
    
    private static final int ULTRADOTMAN_RADIUS = 40;
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 450;
    private static final Color TEXT_COLOR = Color.gray(0.8);

    private MorphologicalMatrix color = new MorphologicalMatrix(255, 0, 0);
    private MetricEvaluation ilities = new MetricEvaluation(color);
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
        root.getChildren().add(constructAxisPicker());
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
    
    private GridPane constructAxisPicker() {
        GridPane axisPicker = new GridPane();
        
        Label statusLabel = new Label();
        statusLabel.setTextFill(Color.RED);
        
        ToggleGroup xAxisToggle = constructAxisGroup(true);
        
        RadioButton xHue = new RadioButton("hue");
        RadioButton xSaturation = new RadioButton("saturation");
        RadioButton xValue = new RadioButton("value");
        
        xHue.setTextFill(TEXT_COLOR);
        xSaturation.setTextFill(TEXT_COLOR);
        xValue.setTextFill(TEXT_COLOR);
        
        xHue.setToggleGroup(xAxisToggle);
        xSaturation.setToggleGroup(xAxisToggle);
        xValue.setToggleGroup(xAxisToggle);
        
        GridPane.setMargin(xHue, new Insets(3));
        GridPane.setMargin(xSaturation, new Insets(3));
        GridPane.setMargin(xValue, new Insets(3));
        
        xSaturation.setSelected(true);
        
        xHue.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxis(PropertyAxis.HUE, true, statusLabel);
            }
        });
        xSaturation.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxis(PropertyAxis.SATURATION, true, statusLabel);
            }
        });
        xValue.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxis(PropertyAxis.VALUE, true, statusLabel);
            }
        });
        
        ToggleGroup yAxisToggle = constructAxisGroup(false);
        
        RadioButton yHue = new RadioButton("hue");
        RadioButton ySaturation = new RadioButton("saturation");
        RadioButton yValue = new RadioButton("value");
        
        yHue.setToggleGroup(yAxisToggle);
        ySaturation.setToggleGroup(yAxisToggle);
        yValue.setToggleGroup(yAxisToggle);
        
        yHue.setTextFill(TEXT_COLOR);
        ySaturation.setTextFill(TEXT_COLOR);
        yValue.setTextFill(TEXT_COLOR);
        
        GridPane.setMargin(yHue, new Insets(3));
        GridPane.setMargin(ySaturation, new Insets(3));
        GridPane.setMargin(yValue, new Insets(3));
        
        yValue.setSelected(true);
        
        yHue.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxis(PropertyAxis.HUE, false, statusLabel);
            }
        });
        ySaturation.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxis(PropertyAxis.SATURATION, false, statusLabel);
            }
        });
        yValue.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxis(PropertyAxis.VALUE, false, statusLabel);
            }
        });
        
        Label xAxisLabel = new Label("x:");
        Label yAxisLabel = new Label("y:");
        
        GridPane.setHalignment(xAxisLabel, HPos.CENTER);
        GridPane.setHalignment(yAxisLabel, HPos.CENTER);
        
        GridPane.setMargin(xAxisLabel, new Insets(3));
        GridPane.setMargin(yAxisLabel, new Insets(3));
        
        xAxisLabel.setTextFill(TEXT_COLOR);
        yAxisLabel.setTextFill(TEXT_COLOR);
        
        axisPicker.add(xAxisLabel, 0, 0);
        axisPicker.add(yAxisLabel, 1, 0);
        axisPicker.add(xHue, 0, 1);
        axisPicker.add(yHue, 1, 1);
        axisPicker.add(xSaturation, 0, 2);
        axisPicker.add(ySaturation, 1, 2);
        axisPicker.add(xValue, 0, 3);
        axisPicker.add(yValue, 1, 3);
        axisPicker.add(statusLabel, 0, 4, 2, 1);
        
        axisPicker.setTranslateX(400);
        
        return axisPicker;
    }
    
    private void changeAxis(PropertyAxis newAxis, boolean x, Label status) {
        try {
            if (x) plot.setXAxis(newAxis);
            else plot.setYAxis(newAxis);
            status.setText("");
        }
        catch (IllegalArgumentException e) {
            status.setText("axes must be different");
        }
        plot.updatePoints(plotGroup);
    }
    
    private static ToggleGroup constructAxisGroup(boolean x) {
        ToggleGroup axisToggle = new ToggleGroup();
        RadioButton hue = new RadioButton("hue");
        RadioButton saturation = new RadioButton("saturation");
        RadioButton value = new RadioButton("value");
        
        hue.setToggleGroup(axisToggle);
        saturation.setToggleGroup(axisToggle);
        value.setToggleGroup(axisToggle);
        
        return axisToggle;
    }
    
    private GridPane constructSettings() {
        GridPane settings = new GridPane();

        Label labelR = new Label("Red: ");
        Label labelG = new Label("Green: ");
        Label labelB = new Label("Blue: ");
        
        GridPane.setHalignment(labelR, HPos.RIGHT);
        GridPane.setHalignment(labelG, HPos.RIGHT);
        GridPane.setHalignment(labelB, HPos.RIGHT);
        
        labelR.setTextFill(TEXT_COLOR);
        labelG.setTextFill(TEXT_COLOR);
        labelB.setTextFill(TEXT_COLOR);

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
                    
                    color = new MorphologicalMatrix(red, green, blue);
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

                color = new MorphologicalMatrix(red, green, blue);
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
        ilities = new MetricEvaluation(color);
        tradeSpace.add(ilities);
        plot.updatePoints(plotGroup);
    }
}
