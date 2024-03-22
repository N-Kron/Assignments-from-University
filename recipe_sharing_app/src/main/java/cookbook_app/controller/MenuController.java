package cookbook_app.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cookbook_app.App;
import cookbook_app.Const;
import cookbook_app.SceneInfo;
import cookbook_app.model.User;
import cookbook_app.utilities.FileHelper;

public class MenuController implements Initializable, Controller {

  public static SceneInfo sceneInfo = getInfo();

  private static SceneInfo getInfo() {
    Parent p = null;
    Controller controller = null;
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + "Menu.fxml"));
      p = fxmlLoader.load();
      controller = (Controller) fxmlLoader.getController();
    } catch (IOException e) {
      System.out.println("Exception in loading " + "Menu.fxml");
      e.printStackTrace();
    }
    return new SceneInfo(new Scene(p), "Cookbook - Main menu", App.smallIcon, controller);
  }

  FileHelper fh = new FileHelper(Const.c.sessionFile);

  private Image signinImage = new Image(Const.c.iconsPath + "sign-in.png");

  private Image signoutImage = new Image(Const.c.iconsPath + "sign-out.png");

  @FXML
  private ImageView signImageView;

  @FXML
  private Button btnRecipes;

  @FXML
  private Button btnCreateRecipe;

  @FXML
  private Button btnTags;

  @FXML
  private Button btnMessages;

  @FXML
  private Button btnDinners;

  @FXML
  private Button btnHelp;

  @FXML
  private Button btnUser;

  @FXML
  private Button btnUsers;

  @FXML
  private Text notification;

  @FXML
  private Pane pane;

  @FXML
  private void helpOnClick(javafx.event.ActionEvent mouseEvent) {
    App.newWindow(false, HelpController.sceneInfo, false);
  }

  @FXML
  private void recipesOnClick(javafx.event.ActionEvent mouseEvent) {
    App.newWindow(false, RecipesController.sceneInfo, true);
  }

  @FXML
  private void createRecipeOnClick(javafx.event.ActionEvent mouseEvent) {
    App.newWindow(true, RecipeCreationController.sceneInfo, true);
  }

  @FXML
  private void tagsOnClick(javafx.event.ActionEvent mouseEvent) {
    App.newWindow(false, TagsController.sceneInfo, true);
  }

  @FXML
  private void messagesOnClick(javafx.event.ActionEvent mouseEvent) {
    App.newWindow(false, MessagesController.sceneInfo,false);
  }

  @FXML
  private void dinnersOnClick(javafx.event.ActionEvent mouseEvent) {
    App.newWindow(false, DinnerController.sceneInfo, true);
  }

  @FXML
  private void userOnClick(javafx.event.ActionEvent mouseEvent) {
    if (App.signedUser.get() == null) {
      FileHelper fh = new FileHelper(Const.c.sessionFile);
      String userHash = fh.load();
      if (userHash != null && !userHash.isBlank()) {
        var users = User.getAllUsers();
        User rememberedUser = null;
        for (User user : users) {
          if (User.hash(user.getUserId() + user.getName() + user.getPwd()).equals(fh.load())) {
            rememberedUser = user;
          }
        }
        if (rememberedUser != null) {
          App.signedUser.set(rememberedUser);
        } else {
          App.newWindow(true, LoginController.sceneInfo, false);
        }
      } else {
        App.newWindow(true, LoginController.sceneInfo, false);
      }
    } else {
      fh.save("");
      App.signedUser.set(null);
    }
    
  }

  @FXML
  private void usersOnClick(javafx.event.ActionEvent mouseEvent) {
    App.newWindow(false, UsersController.sceneInfo, false);
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    btnCreateRecipe.disableProperty().bind(App.signedUser.isNull());
    btnMessages.disableProperty().bind(App.signedUser.isNull());
    btnDinners.disableProperty().bind(App.signedUser.isNull());
    btnUsers.disableProperty().bind(Bindings.createBooleanBinding(
        () -> App.signedUser.get() == null ? true : !App.signedUser.get().getPrivileged(), App.signedUser));
    btnUser.textProperty().bind(Bindings.createStringBinding(
        () -> App.signedUser.get() != null ? App.signedUser.get().getName() : "Login/Sign up", App.signedUser));
    signImageView.imageProperty().bind(
        Bindings.createObjectBinding(() -> App.signedUser.get() == null ? signinImage : signoutImage, App.signedUser));
    App.signedUser.addListener((observable, oldValue, newValue) -> { //when user value changes update
      if (newValue != null) {
        if (App.getMessageCount() == 0) {
          notification.setText("");
        } else { 
        notification.setText(String.valueOf(App.getMessageCount())); // Call the update method to update the user's message count for my part
        }
      } else { notification.setText(""); }

    });
    btnMessages.prefWidthProperty().bind(pane.widthProperty());
    btnMessages.prefHeightProperty().bind(pane.heightProperty());

    
    notification.layoutXProperty().bind(Bindings.createDoubleBinding( () -> pane.widthProperty().get() * 0.8, pane.widthProperty()));
    notification.layoutYProperty().bind(Bindings.createDoubleBinding( () -> pane.heightProperty().get() * 0.2, pane.heightProperty()));
    

  }

  public void update() {
    User m = App.signedUser.get();
    App.signedUser.set(null);
    App.signedUser.set(m);
  }

}
