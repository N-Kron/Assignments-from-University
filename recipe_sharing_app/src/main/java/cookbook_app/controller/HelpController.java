package cookbook_app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cookbook_app.App;
import cookbook_app.Const;
import cookbook_app.SceneInfo;
import cookbook_app.model.Article;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class HelpController implements Initializable, Controller {

    public static SceneInfo sceneInfo = getInfo();
    private static SceneInfo getInfo() {
        Parent p = null;
        Controller controller = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + "Help.fxml"));
            p = fxmlLoader.load();
            controller = (Controller)fxmlLoader.getController();
        } catch (IOException e) {
            System.out.println("Exception in loading " + "Help.fxml");
            e.printStackTrace(); 
        }
        return new SceneInfo(new Scene(p), "Cookbook - Help", App.smallIcon, controller);
    }

    @FXML
    private Button addButton;

    @FXML
    private HBox addHBox;

    @FXML
    private Button backButton;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Article> tbData;

    @FXML
    public TableColumn<Article, String> ArticleName;

    @FXML
    void backOnClick(ActionEvent event) {
        App.navBackward(false);
    }

    SimpleObjectProperty<ObservableList<Article>> articles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        //bindings
        ArticleName.setCellValueFactory(new PropertyValueFactory<>("Name"));

        articles = new SimpleObjectProperty<ObservableList<Article>>(FXCollections.observableArrayList(Article.getArticles(null)));
        tbData.setItems(articles.get());
        articles.addListener((observable, oldValue, newValue) -> {
            tbData.setItems(newValue);
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isBlank()) {
                articles.set(FXCollections.observableArrayList(Article.getArticles(newValue)));
            } else {
                articles.set(FXCollections.observableArrayList(Article.getArticles(null)));
            }
        });

        tbData.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Article selectedArticle = tbData.getSelectionModel().getSelectedItem();
                if (selectedArticle != null) {
                    App.selectedArticle.set(selectedArticle);
                    App.newWindow(false, ArticleController.sceneInfo, false);
                }
            }
        });
    }

    @Override
    public void update() {
        searchField.setText("");
        articles.set(FXCollections.observableArrayList(Article.getArticles(null)));
    }
}