package net.tripsandticks.ultradotman;

public class EmergentProperties {
    private final double hue;
    private final double saturation;
    private final double value;
    
    public EmergentProperties(Color color) {
        final double[] hsv = color.toHSV();
        this.hue = hsv[0];
        this.saturation = hsv[1];
        this.value = hsv[2];
    }

    public double getHue() {
        return hue;
    }

    public double getSaturation() {
        return saturation;
    }

    public double getValue() {
        return value;
    }
}
