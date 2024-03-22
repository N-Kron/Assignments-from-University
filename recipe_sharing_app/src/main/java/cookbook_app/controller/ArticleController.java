package cookbook_app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cookbook_app.App;
import cookbook_app.Const;
import cookbook_app.SceneInfo;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

public class ArticleController implements Initializable, Controller {

    public static SceneInfo sceneInfo = getInfo();
    private static SceneInfo getInfo() {
        Parent p = null;
        Controller controller = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + "Article.fxml"));
            p = fxmlLoader.load();
            controller = (Controller)fxmlLoader.getController();
        } catch (IOException e) {
            System.out.println("Exception in loading " + "Article.fxml");
            e.printStackTrace(); 
        }
        return new SceneInfo(new Scene(p), "Cookbook - Article", App.smallIcon, controller);
    }

    @FXML
    private Label ArticleName;

    @FXML
    private Label ArticleText;

    @FXML
    private ScrollPane textPane;


    @FXML
    void backOnClick(ActionEvent event) {
        App.navBackward(false);
    }

    @Override
    public void update() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArticleName.textProperty().bind(Bindings.createStringBinding(() -> App.selectedArticle.get() != null ? App.selectedArticle.get().getName() : "Article not found", App.selectedArticle));
        ArticleText.textProperty().bind(Bindings.createStringBinding(() -> App.selectedArticle.get() != null ? App.selectedArticle.get().getText() : "Article not found", App.selectedArticle));
    }
}
