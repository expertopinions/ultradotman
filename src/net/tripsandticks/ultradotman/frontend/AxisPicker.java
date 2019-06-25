package net.tripsandticks.ultradotman.frontend;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import net.tripsandticks.ultradotman.backend.PropertyAxis;

/**
 * UI element which allows the user to choose the x and y axis values from the
 * output of the simulation.
 * 
 * In the type of simulation for which this template serves as a foundation,
 * performance of the simulation is measured along several dimensions. In UDM,
 * these dimensions are hue, saturation, and value. In actual simulations, you
 * could have even more than three. The user may want to see how their design
 * decisions perform on these axes over time, but may have trouble
 * conceptualizing, say, a six-dimensional graph. Instead, we project the
 * result of these high-dimension outputs onto a 2D space and let the user
 * choose which axes to view at any given time.
 */
public class AxisPicker {
    private final Group node;
    private final LinearPlot plot;
    
    private ToggleGroup xToggle;
    private ToggleGroup yToggle;
    private Label status;
    
    AxisPicker(LinearPlot plot, int xPosition, int yPosition) {
        this.node = new Group();
        this.plot = plot;
        setUpNode(xPosition, yPosition);
    }
    
    public Group getGroup() {
        return node;
    }
    
    private void setUpNode(int xPosition, int yPosition) {
        GridPane picker = new GridPane();
        picker.setTranslateX(xPosition);
        picker.setTranslateY(yPosition);
        
        status = new Label();
        status.setTextFill(Color.RED);
        
        xToggle = constructAxisGroup(true);
        
        RadioButton xHue = new RadioButton("hue");
        RadioButton xSaturation = new RadioButton("saturation");
        RadioButton xValue = new RadioButton("value");
        
        xHue.setTextFill(Main.TEXT_COLOR);
        xSaturation.setTextFill(Main.TEXT_COLOR);
        xValue.setTextFill(Main.TEXT_COLOR);
        
        xHue.setToggleGroup(xToggle);
        xSaturation.setToggleGroup(xToggle);
        xValue.setToggleGroup(xToggle);
        
        GridPane.setMargin(xHue, new Insets(3));
        GridPane.setMargin(xSaturation, new Insets(3));
        GridPane.setMargin(xValue, new Insets(3));
        
        xSaturation.setSelected(true);
        
        yToggle = constructAxisGroup(false);
        
        RadioButton yHue = new RadioButton("hue");
        RadioButton ySaturation = new RadioButton("saturation");
        RadioButton yValue = new RadioButton("value");
        
        yHue.setToggleGroup(yToggle);
        ySaturation.setToggleGroup(yToggle);
        yValue.setToggleGroup(yToggle);
        
        yHue.setTextFill(Main.TEXT_COLOR);
        ySaturation.setTextFill(Main.TEXT_COLOR);
        yValue.setTextFill(Main.TEXT_COLOR);
        
        GridPane.setMargin(yHue, new Insets(3));
        GridPane.setMargin(ySaturation, new Insets(3));
        GridPane.setMargin(yValue, new Insets(3));
        
        yValue.setSelected(true);
        
        xHue.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxes(true);
            }
        });
        xSaturation.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxes(true);
            }
        });
        xValue.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxes(true);
            }
        });
        
        yHue.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxes(false);
            }
        });
        ySaturation.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxes(false);
            }
        });
        yValue.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxes(false);
            }
        });
        
        Label xAxisLabel = new Label("x:");
        Label yAxisLabel = new Label("y:");
        
        GridPane.setHalignment(xAxisLabel, HPos.CENTER);
        GridPane.setHalignment(yAxisLabel, HPos.CENTER);
        
        GridPane.setMargin(xAxisLabel, new Insets(3));
        GridPane.setMargin(yAxisLabel, new Insets(3));
        
        xAxisLabel.setTextFill(Main.TEXT_COLOR);
        yAxisLabel.setTextFill(Main.TEXT_COLOR);
        
        picker.add(xAxisLabel, 0, 0);
        picker.add(yAxisLabel, 1, 0);
        picker.add(xHue, 0, 1);
        picker.add(yHue, 1, 1);
        picker.add(xSaturation, 0, 2);
        picker.add(ySaturation, 1, 2);
        picker.add(xValue, 0, 3);
        picker.add(yValue, 1, 3);
        picker.add(status, 0, 4, 2, 1);
        
        node.getChildren().add(picker);
    }
    
    private void changeAxes(boolean xFirst) {
        try {
            plot.changeAxes(selectedAxis(xToggle), selectedAxis(yToggle),
                    xFirst);
            status.setText("");
        }
        catch (IllegalArgumentException iae) {
            status.setText("axes must be different");
        }
    }
    
    private static PropertyAxis selectedAxis(ToggleGroup toggleGroup) {
        RadioButton toggle = (RadioButton)toggleGroup.getSelectedToggle();
        switch(toggle.getText().substring(0, 1)) {
        case "h":
            return PropertyAxis.HUE;
        case "s":
            return PropertyAxis.SATURATION;
        case "v":
            return PropertyAxis.VALUE;
        default:
            throw new RuntimeException("unreachable code");
        }
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
}
