package net.tripsandticks.ultradotman;


public class MorphologicalMatrix {
    private int red;
    private int green;
    private int blue;
    
    public MorphologicalMatrix() {
        this.red = 0;
        this.blue = 0;
        this.green = 0;
    }
    
    public Color produceColor() {
        return new Color(red, green, blue);
    }

    
    public int getRed() {
        return red;
    }

    
    public int getGreen() {
        return green;
    }

    
    public int getBlue() {
        return blue;
    }

    
    public void setRed(int red) {
        this.red = red;
    }

    
    public void setGreen(int green) {
        this.green = green;
    }

    
    public void setBlue(int blue) {
        this.blue = blue;
    }
}
