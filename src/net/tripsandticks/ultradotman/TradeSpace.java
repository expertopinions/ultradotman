package net.tripsandticks.ultradotman;

import java.util.ArrayList;
import java.util.List;

public class TradeSpace {
    private final List<EmergentProperties> traversal;
    public static final EmergentProperties UTOPIA = 
            new EmergentProperties(new Color(255, 255, 255));
    
    public TradeSpace() {
        this.traversal = new ArrayList<>();
    }
    
    public void add(EmergentProperties node) {
        traversal.add(node);
    }
    
    public List<EmergentProperties> getPath() {
        return new ArrayList<>(traversal);
    }
}
