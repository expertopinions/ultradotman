package net.tripsandticks.ultradotman.frontend;

import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import net.tripsandticks.ultradotman.backend.Simulation;

/**
 * Renderer for the "simulation." The rest of the UI elements are drawn over
 * this.
 */
public class SimulationRenderer {
    private final Group node;
    private final Camera camera;
    private final Color background;
    private final PhongMaterial dotmanMaterial;
    private static final int ULTRADOTMAN_RADIUS = 40;
    
    SimulationRenderer(Simulation result) {
        this.node = new Group();
        this.dotmanMaterial = new PhongMaterial();
        this.camera = new PerspectiveCamera();
        this.background = Color.gray(0.05);
        
        updateColor(result);
        setUpNode();
    }
    
    private void setUpNode() {
        dotmanMaterial.setSpecularColor(Color.gray(0));
        
        Sphere dotman = new Sphere(ULTRADOTMAN_RADIUS);
        dotman.setMaterial(dotmanMaterial);
        dotman.setTranslateX(Main.WINDOW_WIDTH / 2);
        dotman.setTranslateY(Main.WINDOW_HEIGHT / 2);

        node.getChildren().add(dotman);
        
        PointLight light = new PointLight();
        light.setColor(Color.gray(1));
        light.setTranslateX(Main.WINDOW_WIDTH / 5);
        light.setTranslateY(Main.WINDOW_HEIGHT / 6);
        light.setTranslateZ(-Main.WINDOW_HEIGHT / 2);
        
        node.getChildren().add(light);
    }
    
    public Group getGroup() {
        return node;
    }

    
    public Camera getCamera() {
        return camera;
    }

    
    public Color getBackground() {
        return background;
    }
    
    public void updateColor(Simulation result) {
        dotmanMaterial.setDiffuseColor(Color.hsb(result.getHue(),
                result.getSaturation(), result.getValue()));
    }
}
