package net.tripsandticks.ultradotman.frontend;

import java.util.List;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import net.tripsandticks.ultradotman.backend.PropertyAxis;
import net.tripsandticks.ultradotman.backend.TradeSpace;

/**
 * UI element for displaying a 2-dimensional projection of a traversal of the
 * trade space. See the Javadoc for AxisPicker for a less jargony explanation.
 * 
 * This LinearPlot displays utopia, which represents the best possible outcome
 * on all axes. I chose the color white to represent utopia, and it is drawn on
 * this plot as a white diamond. In a useful simulation, you could make utopia
 * just out of reach at all times as a scathing allegory of real life.
 * 
 * You may choose to make this LinearPlot more dynamic by, for example,
 * replaying previous simulations by clicking on points earlier in the
 * traversal.
 */
public class LinearPlot {
    private PropertyAxis xAxis, yAxis;
    private TradeSpace points;
    private int width, height, pointRadius;
    private static final double POINT_AREA_RATIO = Math.pow(2, -14);
    private final Color PLOT_COLOR = Color.ORANGE;
    private final Group plot;
    
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
        setup(xPosition, yPosition);
    }
    
    private void setup(int xPosition, int yPosition) {
        plot.setTranslateX(xPosition);
        plot.setTranslateY(yPosition);
    }
    
    public Group getPlot() {
        return plot;
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
                    // the positive Y axis direction is down, which is
                    // unintuitive, so i'm flipping it
                             height - scale(minY, maxY, yPath.get(i), false),
                                      pointRadius);
            point.setFill(PLOT_COLOR);
            plot.getChildren().add(point);
        }
        
        // draw connections between points
        for (int i = 1; i < xPath.size(); i++) {
            Line line = new Line(scale(minX, maxX, xPath.get(i-1), true),
                        height - scale(minY, maxY, yPath.get(i-1), false),
                                 scale(minX, maxX, xPath.get(i), true),
                        height - scale(minY, maxY, yPath.get(i), false));
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
        if ((max - min) == 0) return 0;
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
    
    public void changeAxes(PropertyAxis newX, PropertyAxis newY,
            boolean xFirst) throws IllegalArgumentException {
        if (xFirst) {
            yAxis = newY;
            setXAxis(newX);
        }
        else {
            xAxis = newX;
            setYAxis(newY);
        }
        updatePoints();
    }
}
