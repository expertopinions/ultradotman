package net.tripsandticks.ultradotman;

import java.util.List;

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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class LinearPlot {
    public enum PropertyAxis { HUE, SATURATION, VALUE }
    private PropertyAxis xAxis, yAxis;
    private TradeSpace points;
    private int width, height, pointRadius;
    private static final double POINT_AREA_RATIO = Math.pow(2, -14);
    private final Color PLOT_COLOR = Color.ORANGE;
    private final Group plot;
    private final GridPane axisPicker;
    
    /*
     * Representation invariant:
     *   xAxis != yAxis
     */
    
    LinearPlot(int width, int height,
            int xPosition, int yPosition,
            TradeSpace points) throws IllegalArgumentException {
        this.width = width;
        this.height = height;
        this.pointRadius = (int)(width * height * POINT_AREA_RATIO);
        
        this.xAxis = PropertyAxis.SATURATION;
        this.yAxis = PropertyAxis.VALUE;
        
        if (xAxis == yAxis) throw new IllegalArgumentException("x and y axis"
                + " cannot match");
        this.points = points;
        
        this.plot = new Group();
        this.axisPicker = constructAxisPicker();
        setup(xPosition, yPosition);
    }
    
    private void setup(int xPosition, int yPosition) {
        plot.setTranslateX(xPosition);
        plot.setTranslateY(yPosition);
        axisPicker.setTranslateX(xPosition);
    }
    
    public Group getPlot() {
        return plot;
    }
    
    public GridPane getAxisPicker() {
        return axisPicker;
    }
    
    public void updatePoints() {
        if (plot.getChildren().size() > 0) plot.getChildren().clear();
        
        List<Double> xPath = points.getAxis(xAxis);
        List<Double> yPath = points.getAxis(yAxis);
        
        double utopiaX = utopiaAxis(true);
        double utopiaY = utopiaAxis(false);
        
        final double minX, maxX, minY, maxY;
        minX = xPath.stream().reduce(utopiaX, Math::min);
        maxX = xPath.stream().reduce(utopiaX, Math::max);
        minY = yPath.stream().reduce(utopiaY, Math::min);
        maxY = yPath.stream().reduce(utopiaY, Math::max);
        
        // plot points
        for (int i = 0; i < xPath.size(); i++) {
            Circle point = new Circle(scale(minX, maxX, xPath.get(i), true),
                                      scale(minY, maxY, yPath.get(i), false),
                                      pointRadius);
            point.setFill(PLOT_COLOR);
            plot.getChildren().add(point);
        }
        
        // draw connections between points
        for (int i = 1; i < xPath.size(); i++) {
            Line line = new Line(scale(minX, maxX, xPath.get(i-1), true),
                                 scale(minY, maxY, yPath.get(i-1), false),
                                 scale(minX, maxX, xPath.get(i), true),
                                 scale(minY, maxY, yPath.get(i), false));
            line.setStroke(PLOT_COLOR);
            plot.getChildren().add(line);
        }

        // draw utopia
        Polygon diamond = new Polygon();
        diamond.getPoints().addAll(new Double[] {
            0.0,  2.0,
            1.0,  0.0,
            0.0, -2.0,
           -1.0,  0.0
        });
        diamond.setScaleX(width * height * 1.1 * POINT_AREA_RATIO);
        diamond.setScaleY(width * height * 1.1 * POINT_AREA_RATIO);
        diamond.setFill(Color.WHITE);
        diamond.setTranslateX(scale(minX, maxX, utopiaX, true));
        diamond.setTranslateY(scale(minY, maxY, utopiaY, true));
        plot.getChildren().add(diamond);
    }
    
    private double utopiaAxis(boolean x) {
        PropertyAxis axis;
        if (x) axis = xAxis;
        else axis = yAxis;
        
        switch (axis) {
        case HUE:
            return TradeSpace.UTOPIA.getHue();
        case SATURATION:
            return TradeSpace.UTOPIA.getSaturation();
        case VALUE:
            return TradeSpace.UTOPIA.getValue();
        default:
            throw new RuntimeException("unreachable code");
        }
    }

    private double scale(double min, double max, double value, boolean x) {
        int parameter = x ? width : height;
        // normalize value to be between 0 and 1 before multiplying by the
        // desired dimension length (width, height)
        return ((value - min) / (max - min)) * parameter;
    }
    
    // checks if axes match before setting new axis
    private void setXAxis(PropertyAxis newX) throws IllegalArgumentException {
        if (newX == yAxis) {
            throw new IllegalArgumentException("x and y axis cannot match");
        }
        xAxis = newX;
    }
    
    private void setYAxis(PropertyAxis newY) throws IllegalArgumentException {
        if (newY == xAxis) {
            throw new IllegalArgumentException("x and y axis cannot match");
        }
        yAxis = newY;
    }
    
    private GridPane constructAxisPicker() {
        GridPane picker = new GridPane();
        
        Label statusLabel = new Label();
        statusLabel.setTextFill(Color.RED);
        
        ToggleGroup xAxisToggle = constructAxisGroup(true);
        
        RadioButton xHue = new RadioButton("hue");
        RadioButton xSaturation = new RadioButton("saturation");
        RadioButton xValue = new RadioButton("value");
        
        xHue.setTextFill(Main.TEXT_COLOR);
        xSaturation.setTextFill(Main.TEXT_COLOR);
        xValue.setTextFill(Main.TEXT_COLOR);
        
        xHue.setToggleGroup(xAxisToggle);
        xSaturation.setToggleGroup(xAxisToggle);
        xValue.setToggleGroup(xAxisToggle);
        
        GridPane.setMargin(xHue, new Insets(3));
        GridPane.setMargin(xSaturation, new Insets(3));
        GridPane.setMargin(xValue, new Insets(3));
        
        xSaturation.setSelected(true);
        
        ToggleGroup yAxisToggle = constructAxisGroup(false);
        
        RadioButton yHue = new RadioButton("hue");
        RadioButton ySaturation = new RadioButton("saturation");
        RadioButton yValue = new RadioButton("value");
        
        yHue.setToggleGroup(yAxisToggle);
        ySaturation.setToggleGroup(yAxisToggle);
        yValue.setToggleGroup(yAxisToggle);
        
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
                changeAxis(PropertyAxis.HUE, true, yAxisToggle, statusLabel);
            }
        });
        xSaturation.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxis(PropertyAxis.SATURATION, true, yAxisToggle, statusLabel);
            }
        });
        xValue.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxis(PropertyAxis.VALUE, true, yAxisToggle, statusLabel);
            }
        });
        
        yHue.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxis(PropertyAxis.HUE, false, xAxisToggle, statusLabel);
            }
        });
        ySaturation.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxis(PropertyAxis.SATURATION, false, xAxisToggle, statusLabel);
            }
        });
        yValue.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                changeAxis(PropertyAxis.VALUE, false, xAxisToggle, statusLabel);
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
        picker.add(statusLabel, 0, 4, 2, 1);
        
        return picker;
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
    
    private void changeAxis(PropertyAxis newAxis, boolean x,
            ToggleGroup oppositeToggle, Label status) {
        try {
            if (x) {
                yAxis = selectedAxis(oppositeToggle);
                setXAxis(newAxis);
            }
            else {
                xAxis = selectedAxis(oppositeToggle);
                setYAxis(newAxis);
            }
            status.setText("");
        }
        catch (IllegalArgumentException e) {
            status.setText("axes must be different");
        }
        updatePoints();
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
