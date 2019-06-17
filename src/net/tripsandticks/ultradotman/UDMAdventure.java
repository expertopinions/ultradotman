package net.tripsandticks.ultradotman;

import java.util.concurrent.ThreadLocalRandom;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class UDMAdventure extends Application {
    
    private RGBColor color = new RGBColor(0, 0, 0);
    private EmergentProperties ilities = new EmergentProperties(color);
    private static final int ULTRADOTMAN_RADIUS = 40;
    private static final int CANVAS_SIDE = 250;

    @Override
    public void start(Stage stage) {
        stage.setTitle("ultradotman");

        HBox ilityScreen = constructIlityScreen();
        Canvas canvas = constructCanvas();
        GridPane settings = constructSettings(ilityScreen, canvas);
        
        final VBox vBox = new VBox(new HBox(settings, canvas), ilityScreen);
        final Scene scene = new Scene(vBox);
        
        stage.setScene(scene);
        stage.show();
    }
    
    private Canvas constructCanvas() {
        Canvas canvas = new Canvas(CANVAS_SIDE, CANVAS_SIDE);
        updateCanvas(canvas);
        return canvas;
    }
    
    private void updateCanvas(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 250, 250);
        gc.setFill(Color.rgb(color.getRed(), color.getGreen(), color.getBlue()));
        gc.fillOval((CANVAS_SIDE - ULTRADOTMAN_RADIUS) / 2,
                    (CANVAS_SIDE - ULTRADOTMAN_RADIUS) / 2,
                    ULTRADOTMAN_RADIUS, ULTRADOTMAN_RADIUS);
    }

    private void setColor(RGBColor color) {
        this.color = color;
        this.ilities = new EmergentProperties(color);
    }
    
    private HBox constructIlityScreen() {
        HBox ilityScreen = new HBox();
        ilityScreen.setPadding(new Insets(5));
        ilityScreen.setSpacing(40);
        ilityScreen.setAlignment(Pos.BOTTOM_CENTER);

        Label labelH = new Label();
        Label labelS = new Label();
        Label labelV = new Label();
        
        ilityScreen.getChildren().add(labelH);
        ilityScreen.getChildren().add(labelS);
        ilityScreen.getChildren().add(labelV);
        
        updateIlityScreen(ilityScreen);

        return ilityScreen;
    }
    
    private void updateIlityScreen(HBox screen) {
        Label labelH = (Label)screen.getChildren().get(0);
        Label labelS = (Label)screen.getChildren().get(1);
        Label labelV = (Label)screen.getChildren().get(2);
        
        labelH.setText("hue: " + String.format("%.2fº", ilities.getHue()));
        labelS.setText("saturation: " + String.format("%.4f",
                                                    ilities.getSaturation()));
        labelV.setText("value: " + String.format("%.4f", ilities.getValue()));
    }
    
    private GridPane constructSettings(HBox ilityScreen, Canvas canvas) {
        GridPane settings = new GridPane();

        Label labelR = new Label("Red: ");
        Label labelG = new Label("Green: ");
        Label labelB = new Label("Blue: ");
        
        GridPane.setHalignment(labelR, HPos.RIGHT);
        GridPane.setHalignment(labelG, HPos.RIGHT);
        GridPane.setHalignment(labelB, HPos.RIGHT);

        settings.add(labelR, 0, 2);
        settings.add(labelG, 0, 3);
        settings.add(labelB, 0, 4);

        TextField fieldR = new TextField("0");
        TextField fieldG = new TextField("0");
        TextField fieldB = new TextField("0");

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
                    
                    setColor(new RGBColor(red, green, blue));
                    updateCanvas(canvas);
                    updateIlityScreen(ilityScreen);
                }
                catch (NumberFormatException e) {
                    System.err.println("invalid input");
                }
            }
        });

        random.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                try {
                    int red = ThreadLocalRandom.current().nextInt(0,256);
                    int green = ThreadLocalRandom.current().nextInt(0,256);
                    int blue = ThreadLocalRandom.current().nextInt(0,256);
                    
                    fieldR.setText("" + red);
                    fieldG.setText("" + green);
                    fieldB.setText("" + blue);
                    
                    setColor(new RGBColor(red, green, blue));
                    updateCanvas(canvas);
                    updateIlityScreen(ilityScreen);
                }
                catch (NumberFormatException e) {
                    System.err.println("invalid input");
                }
            }
        });

        return settings;
    }
    
    public static void main(String[] args) {
        launch();
    }
    
    
}
