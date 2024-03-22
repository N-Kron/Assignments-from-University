package cookbook_app;


import cookbook_app.controller.Controller;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class SceneInfo {
    public Scene scene;
    public String title;
    public Image icon;
    public Controller controller;
    public boolean resizable = true;
    
    public SceneInfo(Scene scene, String title, Image icon, Controller controller) {
        this.controller = controller;
        this.title = title;
        this.scene = scene;
        this.icon = icon;
    }
}
