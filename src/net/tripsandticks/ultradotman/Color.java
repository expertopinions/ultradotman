package net.tripsandticks.ultradotman;


public class Color {
    private final int red;
    private final int blue;
    private final int green;
    
    /* Abstraction function:
     *     AF(red, green, blue) is a color with red value "red", etc.
     * Rep invariant:
     *     red, blue, green are in [0,255]
     */
    
    public Color(int red, int blue, int green) {
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
    
    /**
     * Converts the values of this color (stored in RGB) to HSV.
     * 
     * algorithm: https://en.wikipedia.org/wiki/HSL_and_HSV#From_RGB
     * @return an array of doubles representing { hue, saturation, value }
     */
    public double[] toHSV() {
        // normalize values to 1
        double normalRed = red / 255.;
        double normalGreen = green / 255.;
        double normalBlue = blue / 255.;
        
        double max = Math.max(normalRed, Math.max(normalGreen, normalBlue));
        double min = Math.min(normalRed, Math.min(normalGreen, normalBlue));
        
        // get hue
        double hue;
        if (normalRed == normalBlue && normalGreen == normalBlue) {
            hue = 0.;
        }
        else if (max == normalRed) {
            hue = 60. * (normalGreen - normalBlue) / (max - min);
        }
        else if (max == normalGreen) {
            hue = 60. * (2 + (normalBlue - normalRed) / (max - min));
        }
        else {
            hue = 60. * (4 + (normalRed - normalGreen) / (max - min));
        }
        
        while (hue < 0) hue = hue + 360.;
        while (hue >= 360) hue = hue - 360.;
        
        // get saturation
        final double saturation;
        if (normalRed == normalBlue && normalGreen == normalBlue) {
            saturation = 0.;
        }
        else {
            saturation = (max - min) / max;
        }
        
        // get value
        final double value = max;
        
        return new double[] { hue, saturation, value };
    }
}
