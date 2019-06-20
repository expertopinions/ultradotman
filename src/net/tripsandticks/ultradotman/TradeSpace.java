package net.tripsandticks.ultradotman;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.tripsandticks.ultradotman.LinearPlot.PropertyAxis;

public class TradeSpace {
    private final List<MetricEvaluation> traversal;
    public static final MetricEvaluation UTOPIA = 
            new MetricEvaluation(new MorphologicalMatrix(255, 255, 255));
    
    public TradeSpace() {
        this.traversal = new ArrayList<>();
    }
    
    public void add(MetricEvaluation node) {
        traversal.add(node);
    }
    
    public List<MetricEvaluation> getPath() {
        return new ArrayList<>(traversal);
    }
    
    public List<Double> getAxis(PropertyAxis axis) {
        switch (axis) {
        case HUE:
            return traversal.stream().map(e -> e.getHue())
                    .collect(Collectors.toList());
        case SATURATION:
            return traversal.stream().map(e -> e.getSaturation())
                    .collect(Collectors.toList());
        case VALUE:
            return traversal.stream().map(e -> e.getValue())
                    .collect(Collectors.toList());
        default:
            throw new RuntimeException("unreachable code");
        }
    }
}
