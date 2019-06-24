package net.tripsandticks.ultradotman.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.tripsandticks.ultradotman.frontend.LinearPlot.PropertyAxis;

/**
 * Stores the results of user inputs over time.
 */
public class TradeSpace {
    private final List<Simulation> traversal;
    public static final Simulation UTOPIA = 
            new Simulation(new InputState(255, 255, 255));
    
    public TradeSpace() {
        this.traversal = new ArrayList<>();
    }
    
    public void add(Simulation node) {
        traversal.add(node);
    }
    
    public List<Simulation> getPath() {
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
