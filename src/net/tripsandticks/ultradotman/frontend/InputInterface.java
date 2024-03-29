package net.tripsandticks.ultradotman.frontend;

import java.util.concurrent.ThreadLocalRandom;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import net.tripsandticks.ultradotman.backend.InputState;
import net.tripsandticks.ultradotman.backend.Simulation;
import net.tripsandticks.ultradotman.backend.TradeSpace;

/**
 * UI element for creating an InputState object.
 */
public class InputInterface {
    private final Group node;
    private final LinearPlot plot;
    private final SimulationRenderer renderer;
    private final TradeSpace space;
    
    private InputState color;
    private Simulation ilities;
    private Label status;
    
    public InputInterface(InputState color, LinearPlot plot,
            SimulationRenderer renderer, TradeSpace tradeSpace) {
        this.node = new Group();
        this.plot = plot;
        this.renderer = renderer;
        this.color = color;
        this.ilities = new Simulation(color);
        this.space = tradeSpace;
        
        space.add(ilities);
        setUpNode();
    }

    public Group getGroup() {
        return node;
    }

    private void setUpNode() {
        GridPane settings = new GridPane();
        
        status = new Label();
        status.setTextFill(Color.RED);
        status.setTextAlignment(TextAlignment.RIGHT);

        Label labelR = new Label("Red: ");
        Label labelG = new Label("Green: ");
        Label labelB = new Label("Blue: ");
        
        GridPane.setHalignment(labelR, HPos.RIGHT);
        GridPane.setHalignment(labelG, HPos.RIGHT);
        GridPane.setHalignment(labelB, HPos.RIGHT);
        
        labelR.setTextFill(Main.TEXT_COLOR);
        labelG.setTextFill(Main.TEXT_COLOR);
        labelB.setTextFill(Main.TEXT_COLOR);

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
        settings.add(status, 0, 6, 2, 1);
        settings.setPadding(new Insets(5));

        go.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                try {
                    int red = Integer.parseInt(fieldR.getText());
                    int green = Integer.parseInt(fieldG.getText());
                    int blue = Integer.parseInt(fieldB.getText());
                    
                    color = new InputState(red, green, blue);
                    update();
                }
                catch (NumberFormatException nfe) {
                    status.setText("invalid input");
                }
                catch (AssertionError ae) {
                    status.setText("values must be\nbetween 0 and 255");
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

                color = new InputState(red, green, blue);
                update();
            }
        });
        
        node.getChildren().add(settings);
    }
    
    private void update() {
        status.setText("");
        ilities = new Simulation(color);
        renderer.updateColor(ilities);
        space.add(ilities);
        plot.updatePoints();
    }
}
