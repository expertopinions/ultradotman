package net.tripsandticks.ultradotman;

public class EmergentProperties {
    private final double hue;
    private final double saturation;
    private final double value;
    
    public EmergentProperties(RGBColor color) {
        // normalize values to 1
        double normalRed = color.getRed() / 255.;
        double normalGreen = color.getGreen() / 255.;
        double normalBlue = color.getBlue() / 255.;
        
        double max = Math.max(normalRed, Math.max(normalGreen, normalBlue));
        double min = Math.min(normalRed, Math.min(normalGreen, normalBlue));
        
        // get hue
        double hueValue;
        if (normalRed == normalBlue && normalGreen == normalBlue) {
            hueValue = 0.;
        }
        else if (max == normalRed) {
            hueValue = 60. * (normalGreen - normalBlue) / (max - min);
        }
        else if (max == normalGreen) {
            hueValue = 60. * (2 + (normalBlue - normalRed) / (max - min));
        }
        else {
            hueValue = 60. * (4 + (normalRed - normalGreen) / (max - min));
        }
        
        while (hueValue < 0) hueValue = hueValue + 360.;
        while (hueValue >= 360) hueValue = hueValue - 360.;
        this.hue = hueValue;
        
        // get saturation
        if (normalRed == normalBlue && normalGreen == normalBlue) {
            saturation = 0.;
        }
        else {
            saturation = (max - min) / max;
        }
        
        // get value
        value = max;
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
