package net.tripsandticks.ultradotman.backend;

/**
 * Immutable snapshot of the input state. This provides initial conditions for
 * the Simulation.
 * 
 * A more advanced program, i.e. one where the user has less direct control
 * over the initial conditions, may want to include a mutable data type for user
 * inputs which then produces an immutable snapshot like this one.
 */
public class InputState {
    private final int red;
    private final int green;
    private final int blue;
    
    /* Abstraction function:
     *     AF(red, green, blue) is a color with red value "red", etc.
     * Rep invariant:
     *     red, blue, green are in [0,255]
     */
    
    public InputState(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        checkRep();
    }
    
    private void checkRep() {
        assert red >= 0 && red <= 255;
        assert blue >= 0 && blue <= 255;
        assert green >= 0 && green <= 255;
    }

    public int getRed() {
        return red;
    }

    public int getBlue() {
        return blue;
    }

    public int getGreen() {
        return green;
    }
}
