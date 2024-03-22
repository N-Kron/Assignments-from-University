package cookbook_app;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;

import org.mariadb.jdbc.Connection;

import cookbook_app.controller.MenuController;
import cookbook_app.model.Article;
import cookbook_app.model.Recipe;
import cookbook_app.model.User;
import cookbook_app.utilities.FileHelper;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene mainScene;
    private static Stage mainStage;
    public static Stack<SceneInfo> scenes = new Stack<>();
    private static Stack<Stage> stages = new Stack<>();
    public static SimpleObjectProperty<User> signedUser = new SimpleObjectProperty<User>(null);
    // needed when going from recipe search to recipe page
    public static Recipe recipe;
    public static double multiplier;
    private static FileHelper dbfh = new FileHelper(Const.c.dbconnection);

    public static Image smallIcon = new Image(Const.c.iconsPath + "chef-hat-xsmall.png");
    public static SimpleObjectProperty<Article> selectedArticle = new SimpleObjectProperty<Article>(null);

    @Override
    public void start(Stage stage) throws IOException {
        // Load the animated GIF image
        App.mainStage = stage;
        stage.setTitle("Cookbook");
        stage.getIcons().add(smallIcon);
        Image image = new Image(Const.c.animationsPath + "welcome-animation.gif");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(1280);
        imageView.setFitHeight(720);

        imageView.setCache(true);
        Group root = new Group(imageView);
        App.mainScene = new Scene(root);
        App.mainStage.setScene(App.mainScene);
        App.mainStage.show();

        // Play the animation and switch to the Home view once it's finished
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(6), event -> {
            newWindow(false, MenuController.sceneInfo, true);
        }));
        timeline.play();
    }

    public static Parent loadFXML(String fxml) {
        Parent result = null;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + fxml));
            result = fxmlLoader.load();
            App.mainStage.setScene(new Scene(result));
            App.mainStage.show();
        } catch (IOException e) {
            System.out.println("Exception in loading " + fxml);
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (Exception e) {
            System.out.println(e);
        }

        launch();
    }

    public static Connection getConnection() throws SQLException {
        String connectionString = App.dbfh.load();
        return (Connection) DriverManager.getConnection(connectionString);
    }


    public static void newWindow(Boolean extra, SceneInfo newSceneInfo, Boolean waitUntilLoaded) {
        Stage stage = null;
        if (newSceneInfo.controller != null && waitUntilLoaded) newSceneInfo.controller.update();
        if (extra) {
            stage = new Stage();
            stage.setResizable(newSceneInfo.resizable);
            stage.setScene(newSceneInfo.scene);
            stage.setTitle(newSceneInfo.title);
            stage.getIcons().add(newSceneInfo.icon);
            stage.initModality(Modality.APPLICATION_MODAL);
            stages.push(stage);
            stage.show();
        } else {
            scenes.push(newSceneInfo);
            mainStage.setResizable(newSceneInfo.resizable);
            mainStage.setScene(newSceneInfo.scene);
            mainStage.setTitle(newSceneInfo.title);
            mainStage.getIcons().add(newSceneInfo.icon);
            
            mainStage.show();
        }
        if (newSceneInfo.controller != null && !waitUntilLoaded)
            newSceneInfo.controller.update();
    }

    public static void closeLast() {
        stages.pop().hide();
    }

    public static void navBackward(boolean waitUntilLoaded) {
        if (!scenes.isEmpty()) {
            scenes.pop();
            var sceneinfo = scenes.peek();
            if (sceneinfo.controller != null && waitUntilLoaded)
                sceneinfo.controller.update();
            mainStage.setScene(sceneinfo.scene);
            mainStage.setTitle(sceneinfo.title);
            mainStage.getIcons().add(sceneinfo.icon);
            if (sceneinfo.controller != null && !waitUntilLoaded)
                sceneinfo.controller.update();
        }
    }

    public static void setIcon(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(smallIcon);
    }

    public static int getMessageCount() {
        int count = 0;
        try {
            if (App.signedUser.get() != null) {
                String user = App.signedUser.get().getUserId();
                Connection c = App.getConnection();
                PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) AS suggestion_count FROM suggestion WHERE suggestion_recipient = ? AND suggestion_seen = 0;");
                ps.setString(1, user);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    count = rs.getInt("suggestion_count");
                }
                c.close();
            } else {
                return count;
            }
        } catch (SQLException e) {
            System.out.println("SQLException in App.getMessageCount method!! " + e.getMessage());
        }
        return count;
    }
    
    public static void throwError(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(errorMessage);
        setIcon(alert);
        alert.show();
    }
}
