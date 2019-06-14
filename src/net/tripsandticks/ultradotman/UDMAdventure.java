package net.tripsandticks.ultradotman;

import processing.core.PApplet;

public class UDMAdventure extends PApplet {
    
    private Color color = new Color(0, 0, 0);
    private static final int ULTRADOTMAN_SIZE = 40;

//    public static void main(String[] args) {
//        PApplet.main("UDMAdventure");
//    }
    
    @Override
    public void settings() {
        size(250,250);
    }
    
    @Override
    public void draw() {
        background(0);
        noStroke();
        
        int[] rgb = { color.getRed(), color.getGreen(), color.getBlue() };
        int red = rgb[0];
        int green = rgb[1];
        int blue = rgb[2];
        
        fill(red, green, blue);
        circle(width/2, height/2, ULTRADOTMAN_SIZE);
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
}
