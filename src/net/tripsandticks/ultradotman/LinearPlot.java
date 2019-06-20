package net.tripsandticks.ultradotman;

import java.util.List;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class LinearPlot {
    public enum PropertyAxis { HUE, SATURATION, VALUE }
    private PropertyAxis xAxis, yAxis;
    private TradeSpace points;
    private int width, height, pointRadius;
    private static final double POINT_AREA_RATIO = Math.pow(2, -14);
    private final Color PLOT_COLOR = Color.ORANGE;
    
    /*
     * Representation invariant:
     *   xAxis != yAxis
     */
    
    LinearPlot(int width, int height,
            PropertyAxis xAxis, PropertyAxis yAxis,
            TradeSpace points) throws IllegalArgumentException {
        this.width = width;
        this.height = height;
        this.pointRadius = (int)(width * height * POINT_AREA_RATIO);
        
        if (xAxis == yAxis) throw new IllegalArgumentException("x and y axis"
                + " cannot match");
        
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.points = points;
    }
    
    private void plotPoints(Group plotGroup) {
        List<Double> xPath = points.getAxis(xAxis);
        List<Double> yPath = points.getAxis(yAxis);
        
        final double minX, maxX, minY, maxY;
        minX = xPath.stream().reduce(Double.POSITIVE_INFINITY, Math::min);
        maxX = xPath.stream().reduce(Double.NEGATIVE_INFINITY, Math::max);
        minY = yPath.stream().reduce(Double.POSITIVE_INFINITY, Math::min);
        maxY = yPath.stream().reduce(Double.NEGATIVE_INFINITY, Math::max);
        
        // plot points
        for (int i = 0; i < xPath.size(); i++) {
            Circle point = new Circle(scale(minX, maxX, xPath.get(i), true),
                                      scale(minY, maxY, yPath.get(i), false),
                                      pointRadius);
            point.setFill(PLOT_COLOR);
            plotGroup.getChildren().add(point);
        }
        
        // draw connections between points
        for (int i = 1; i < xPath.size(); i++) {
            Line line = new Line(scale(minX, maxX, xPath.get(i-1), true),
                                 scale(minY, maxY, yPath.get(i-1), false),
                                 scale(minX, maxX, xPath.get(i), true),
                                 scale(minY, maxY, yPath.get(i), false));
            line.setStroke(PLOT_COLOR);
            plotGroup.getChildren().add(line);
        }
        
        //plotGroup.getChildren().add(arrow());
    }

    private double scale(double min, double max, double value, boolean x) {
        int parameter = x ? width : height;
        // normalize value to be between 0 and 1 before multiplying by the
        // desired dimension length (width, height)
        return ((value - min) / (max - min)) * parameter;
    }
    
    public void updatePoints(Group plotGroup) {
        if (plotGroup.getChildren().size() > 0) plotGroup.getChildren().clear();
        plotPoints(plotGroup);
    }
    
    public void setXAxis(PropertyAxis newX) throws IllegalArgumentException {
        if (newX == yAxis) {
            throw new IllegalArgumentException("x and y axis cannot match");
        }
        xAxis = newX;
    }
    
    public void setYAxis(PropertyAxis newY) throws IllegalArgumentException {
        if (newY == xAxis) {
            throw new IllegalArgumentException("x and y axis cannot match");
        }
        yAxis = newY;
    }
    
    // TODO: arrows pointing in the direction of the tradespace nagivation?
//    private Polygon arrow() {
//        Polygon arrow = new Polygon();
//        arrow.getPoints().addAll(new Double[] {
//                0.0,  0.0,
//                5.0,  4.0,
//                0.0, -9.0,
//               -5.0,  4.0
//        });
//        arrow.setFill(PLOT_COLOR);
//        return arrow;
//    }
}
