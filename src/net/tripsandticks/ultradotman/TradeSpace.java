package net.tripsandticks.ultradotman;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.tripsandticks.ultradotman.LinearPlot.PropertyAxis;

public class TradeSpace {
    private final List<EmergentProperties> traversal;
    public static final EmergentProperties UTOPIA = 
            new EmergentProperties(new RGBColor(255, 255, 255));
    
    public TradeSpace() {
        this.traversal = new ArrayList<>();
    }
    
    public void add(EmergentProperties node) {
        traversal.add(node);
    }
    
    public List<EmergentProperties> getPath() {
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
