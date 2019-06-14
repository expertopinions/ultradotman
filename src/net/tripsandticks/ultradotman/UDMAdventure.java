package net.tripsandticks.ultradotman;

import java.util.concurrent.ThreadLocalRandom;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import processing.core.PApplet;
import processing.core.PSurface;
import processing.javafx.PSurfaceFX;

public class UDMAdventure extends PApplet {
    
    private Color color = new Color(0, 0, 0);
    private EmergentProperties ilities = new EmergentProperties(color);
    private static final int ULTRADOTMAN_SIZE = 40;

    public static void main(String[] args) {
        PApplet.main("net.tripsandticks.ultradotman.UDMAdventure");
    }
    
    @Override
    public void settings() {
        size(250,250,FX2D);
    }
    
    @Override
    public void draw() {
        background(0);
        noStroke();
        
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        
        fill(red, green, blue);
        circle(width/2, height/2, ULTRADOTMAN_SIZE);
    }
    
    public void setColor(Color color) {
        this.color = color;
        this.ilities = new EmergentProperties(color);
    }
    
    @Override
    protected PSurface initSurface() {
        // source: https://stackoverflow.com/questions/28266274/how-to-embed-a-papplet-in-javafx
        PSurface surface1 = super.initSurface();

        final PSurfaceFX FXSurface = (PSurfaceFX) surface1;
        final Canvas canvas = (Canvas) FXSurface.getNative(); // canvas is the processing drawing
        final Stage stage = (Stage) canvas.getScene().getWindow(); // stage is the window

        stage.setTitle("ultradotman");
        canvas.widthProperty().unbind();
        canvas.heightProperty().unbind();
        
        GridPane settings = constructSettings();
        HBox ilityScreen = constructIlityScreen();

        final VBox vBox = new VBox(new HBox(settings, canvas), ilityScreen);
        final Scene newscene = new Scene(vBox); // Create a scene from the elements
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.setScene(newscene); // Replace the stage's scene with our new one.
            }
        });
        return surface1;
    }
    
    private GridPane constructSettings() {
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
                    setColor(new Color(red, green, blue));
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
                    
                    setColor(new Color(red, green, blue));
                }
                catch (NumberFormatException e) {
                    System.err.println("invalid input");
                }
            }
        });

        return settings;
    }
    
    private HBox constructIlityScreen() {
        HBox ilityScreen = new HBox();
        ilityScreen.setPadding(new Insets(5));
        ilityScreen.setSpacing(50);
        ilityScreen.setAlignment(Pos.BOTTOM_CENTER);

        Label labelH = new Label("hue: " + ilities.getHue());
        Label labelS = new Label("saturation: " + ilities.getSaturation());
        Label labelV = new Label("value: " + ilities.getValue());
        
        ilityScreen.getChildren().add(labelH);
        ilityScreen.getChildren().add(labelS);
        ilityScreen.getChildren().add(labelV);

        return ilityScreen;
    }
    
    // TODO: update ility screen
}
