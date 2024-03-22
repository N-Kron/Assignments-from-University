package cookbook_app.controller;

import cookbook_app.Const;
import cookbook_app.SceneInfo;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import cookbook_app.App;
import cookbook_app.utilities.TextFieldSuggest;
import cookbook_app.utilities.rcpConnector;
import cookbook_app.model.Comment;
import cookbook_app.model.Recipe;
import cookbook_app.model.Tag;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



public class RecipeController implements Initializable, Controller {

  @FXML
  private AnchorPane parentPane;

  @FXML
  private Label title;

  @FXML
  private Button dinnerButton;

  @FXML
  private Label subtitle;

  @FXML
  private ScrollPane textPane;

  @FXML
   private Label instructions;

  @FXML
  private VBox ingrBox;

  @FXML
  private Slider slider;

  @FXML
  private HBox tagPane;

  @FXML
  private VBox commsBox;

  @FXML
  private Button addCommButton;

  @FXML
  private Button sendMssgButton;

  private ArrayList<HBox> taglist = new ArrayList<HBox>(3);

  public static SceneInfo sceneInfo = getInfo();

  private Double[] ingr_values;

  private double multiplier;

  private Recipe recipe;

  private rcpConnector con = new rcpConnector();

  private boolean signedIn;

  private static SceneInfo getInfo() {
    Parent p = null;
    Controller controller = null;
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + "Recipe.fxml"));
      p = fxmlLoader.load();
      controller = (Controller)fxmlLoader.getController();
    } catch (IOException e) {
      System.out.println("Exception in loading " + "Recipe.fxml");
      e.printStackTrace();
    }
    return new SceneInfo(new Scene(p), "Cookbook - Recipes", App.smallIcon, controller);
  }

  @FXML
  private void backOnClick(javafx.event.ActionEvent mouseEvent) {
    title.setText("Loading...");
    subtitle.setText("Loading...");
    instructions.setText("Loading...");
    ingrBox.getChildren().clear();
    tagPane.getChildren().clear();
    App.navBackward(false);
  }

  @FXML
  private void addToDinner(javafx.event.ActionEvent mouseEvent) {
    App.recipe = recipe;
    App.multiplier = this.slider.getValue();
    if (App.multiplier == 0) {
      App.multiplier = 1;
    }
    App.newWindow(true, addToDinnerController.sceneInfo, false);
  }

  @FXML
  private void sendMessage(javafx.event.ActionEvent mouseEvent) {
    App.recipe = recipe;
    App.newWindow(true, SendMessageController.sceneInfo, false);
  }


  @FXML
  private void createComm(javafx.event.ActionEvent mouseEvent) {
    Comment comment = new Comment(App.signedUser.get().getUserId(), "", -1, recipe.getRecipeID(), signedIn);
    comment.commit();
    recipe.addComment(comment);
    VBox comm = makeComm(comment);
    commsBox.getChildren().add(comm);
    HBox nameNedit = (HBox) comm.getChildren().get(0);
    Button edit = (Button) nameNedit.getChildren().get(1);
    //xtPane.getHeight());
    textPane.layout();
    textPane.setVvalue(1.0);
    edit.fire();
  }

  
  EventHandler<javafx.event.ActionEvent> openCommField = new EventHandler<javafx.event.ActionEvent>() {
    @Override
    public void handle(javafx.event.ActionEvent event) {
      addCommButton.setDisable(true);
      Button origin = (Button) event.getSource();
      origin.setDisable(true);
      VBox box = (VBox) origin.getParent().getParent();
      Label label = (Label)box.getChildren().get(1);
      String text = label.getText();
      box.getChildren().remove(label);
      TextField textfield = new TextField(text);
      textfield.setOnAction(closeCommField);
      box.getChildren().add(textfield);
    };
  };


  EventHandler<javafx.event.ActionEvent> closeCommField = new EventHandler<javafx.event.ActionEvent>() {
    @Override
    public void handle(javafx.event.ActionEvent event) {
      addCommButton.setDisable(false);
      TextField origin = (TextField) event.getSource();
      String text = origin.getText();
      VBox comm = (VBox) origin.getParent();
      // adding comment to database
      int index = commsBox.getChildren().indexOf(comm);
      // Comment newComm = new Comment(App.signedUser.get().getUserId(), text, -1, signedIn);
      // VBox newCommBox = makeComm(newComm);
      if (text.length() == 0) {
        recipe.getComments().get(index).detele();
        commsBox.getChildren().remove(comm);
        recipe.delComment(index);
      } else {
        recipe.getComments().get(index).update(text);
      }
      comm.getChildren().remove(origin);
      Label label = new Label(text);
      label.setWrapText(true);
      comm.getChildren().add(label);
      HBox nameNedit = (HBox) comm.getChildren().get(0);
      Button edit = (Button) nameNedit.getChildren().get(1);
      edit.setDisable(false);
    };
  };


  @FXML
  ChangeListener<Number> numberChangeListener = (obs, old, val) -> {
    if (val.doubleValue() % 2 == 0) {
      multiplier = (double)val.intValue() / recipe.getBasePortion(); 
      updateIngr();
    }
  };
  

  EventHandler<javafx.event.ActionEvent> tagDel = new EventHandler<javafx.event.ActionEvent>() {
    @Override
    public void handle(javafx.event.ActionEvent event) {
      Button origin = (Button) event.getSource();
      align((HBox)origin.getParent());
      tagPane.getChildren().remove(origin.getParent());

      HBox box = (HBox) origin.getParent();
      Label text = (Label)box.getChildren().get(0);
      recipe.delTag(Tag.getTag(x -> x.getKeyword().equals(text.getText())));
      con.delTag(String.valueOf(recipe.getRecipeID()), text.getText());
    };
  };

  EventHandler<javafx.event.ActionEvent> openTextField = new EventHandler<javafx.event.ActionEvent>() {
    @Override
    public void handle(javafx.event.ActionEvent event) {
      Button origin = (Button) event.getSource();
      HBox box = (HBox) origin.getParent();
      TextField text = (TextField)box.getChildren().get(1);
      text.setManaged(true);
      text.setVisible(true);
      Insets insets = new Insets(0, 10, 1, 0);
      box.setPadding(insets);
    };
  };

  EventHandler<javafx.event.ActionEvent> addTag = new EventHandler<javafx.event.ActionEvent>() {
    @Override
    public void handle(javafx.event.ActionEvent event) {
      TextField origin = (TextField) event.getSource();
      String text = origin.getText();
      // adding tag to database and to storage
      List<Tag> tags = Tag.getUserTags(null, App.signedUser.get());
      Tag tag = null;
      for (Tag userTag : tags) {
        if (userTag.getKeyword().equals(text)) {
          tag = userTag;
          break;
        }
      }
      if (tag == null) {
        tag = new Tag(-1, text, App.signedUser.get().getUserId());
        if (App.signedUser.get().isPrivileged().get()) {
          tag = new Tag(-1, text, null);
        }
        tag.commit();
      }
      if (recipe.addTag(tag)) {
        taglist.add(makeTag(new HBox(10), tag, true));
      // + closing button box
      }
      origin.setManaged(false);
      origin.setVisible(false);
      HBox box = (HBox) origin.getParent();
      box.setPadding(new Insets(0, 0, 0, 0));
      origin.clear();
    };
  };

  // load the scene with relevant info
  public void load(Recipe recipe) {
    this.recipe = recipe;
    
    title.setText(recipe.getKeyword());
    subtitle.setText(recipe.getShortDesc());
    instructions.setText(recipe.getInstructions());
    recipe.loadIngredientsInfo();
    dinnerButton.setDisable(false);
    addCommButton.setDisable(false);
    sendMssgButton.setDisable(false);
    //rcpConnector con = new rcpConnector();
    //String id = String.valueOf(recipe.getRecipeID());

    String[] ingrs = recipe.getContentsString();
    ingr_values = new Double[ingrs.length];
    int i = 0;
    for (String ingr : ingrs) {
      ingr = ingr.replace("null", "");
      String value = ingr.substring(0, ingr.indexOf(' '));
      String content = ingr.substring(ingr.indexOf(' ') + 1);
      try {
        ingr_values[i] = Double.parseDouble(value);
      } catch (NumberFormatException exception) {
        // nothing
      }
      ingrBox.getChildren().add(makeIngr(value, content));
      i++;
    }
    slider.valueProperty().addListener(numberChangeListener);
    slider.setValue(recipe.getBasePortion());

    signedIn = true;
    try {
      App.signedUser.get().getPrivileged();
    } catch (NullPointerException notSingnedIn) {
      signedIn = false;
      dinnerButton.setDisable(true);
      addCommButton.setDisable(true);
      sendMssgButton.setDisable(true);
    }

    updateTags();
    updateComms();
  }


  @FXML
  private HBox makeIngr(String amount, String content) {
    Font font = new Font(15.0);
    Label value = new Label(amount);
    value.setFont(font);
    Label ingr_name = new Label(content);
    ingr_name.setFont(font);
    HBox box = new HBox(5.0, value, ingr_name);
    return box;
  }


  @FXML
  private VBox makeComm (Comment comm) {
    Label username = new Label(comm.getUser());
    username.setFont(new Font(13));
    username.setStyle("-fx-font-weight: bold");
    Button edit = new Button("edit");
    if (!(signedIn && (comm.getUser().equals(App.signedUser.get().getUserId()) || App.signedUser.get().isPrivileged().get()))) {
      edit.setVisible(false);
    }
    edit.setOnAction(openCommField);
    edit.setStyle("-fx-background-color: #acf86d; -fx-background-radius: 20px;");
    HBox nameNedit = new HBox(10.0, username, edit);
    Label comment = new Label(comm.getKeyword());
    nameNedit.setAlignment(Pos.CENTER_LEFT);
    comment.setWrapText(true);
    VBox commBox = new VBox(5.0, nameNedit, comment);
    return commBox;
  }


  @FXML 
  private HBox makeTag(HBox box, Tag tag, boolean userAdded) {
    // modify hbox to make it into a tag
    
    box.setAlignment(Pos.CENTER);
    box.setStyle("-fx-background-color: #c6f4a1; -fx-background-radius: 12px;");

    Label label = new Label(tag.getKeyword());
    label.setAlignment(Pos.CENTER);
    Font font = new Font("tagFont", 16);
    label.setFont(font);
    box.getChildren().add(label);
    Insets insets;

    if (signedIn && (tag.getUser() == null && App.signedUser.get().getPrivileged() ||
         tag.getUser() != null && tag.getUser().equals(App.signedUser.get().getUserId()))) {
      Button x = new Button("x");
      x.setOnAction(tagDel);
      x.setStyle("-fx-background-color: #acf86d; -fx-background-radius: 20px; -fx-font-size: 16px;");
      box.getChildren().add(x);
      insets = new Insets(0, 0, 1, 10);
    } else {
      insets = new Insets(0, 10, 1, 10);
    }
    
    box.setPadding(insets);
    if (userAdded) {
      tagPane.getChildren().add(tagPane.getChildren().size() -1, box);
    } else {
      tagPane.getChildren().add(tagPane.getChildren().size(), box);
    }
    
    return box;
  }


  @FXML
  private void align(HBox tag) { 
    // align other tags once one gets deleted
    double x = tag.getLayoutX();
    int lenght = tagPane.getChildren().size();
    Label label = (Label)tag.getChildren().get(0);

    for (int i = 0; i < lenght; i++) {
      HBox box = (HBox)tagPane.getChildren().get(i);
      if (box.getLayoutX() + label.getText().length() + 20 > x) {
        box.setLayoutX(box.getLayoutX() - label.getText().length() * 16);
      }

    }
  }

  
  @FXML
  private void updateTags() {
    tagPane.getChildren().clear();

    String userID = "null";
    if (signedIn) {
      userID = App.signedUser.get().getUserId();
    }


    recipe.loadTagsInfo();
    List<Tag> tags = recipe.getTags();
    
    for (int i = 0; i < tags.size(); i++){
      String user = tags.get(i).getUser();
      if (user == null || user.equals(userID)) {
        taglist.add(makeTag(new HBox(10), tags.get(i), false));
      }
    }

    
    // adding plus button that creates new tags
    HBox newTag = new HBox(10);
    newTag.setAlignment(Pos.CENTER);
    newTag.setStyle("-fx-background-color: #acf86d; -fx-background-radius: 12px;");
    Button plus = new Button("+");
    plus.setStyle("-fx-background-color: #acf86d; -fx-background-radius: 20px; -fx-font-size: 16px;");
    plus.setOnAction(openTextField);
    newTag.getChildren().add(plus);
    
    // text field to get the tag
    TextFieldSuggest text = new TextFieldSuggest();
    text.getEntries().addAll(Tag.getTagsKeywords());
    text.setVisible(false);
    text.setManaged(false);
    text.setStyle("-fx-background-color: #c6f4a1; -fx-background-radius: 16px;");
    text.setOnAction(addTag);
    newTag.getChildren().add(text);
    int max = 12;
	  text.textProperty().addListener((observable, oldValue, newValue) -> {
		if (newValue.length() > max) {
			String copy = text.getText().substring(0, max);
			text.setText(copy);
		}
	  });
    if (userID.equals("null")) {
      newTag.setVisible(false);
    }
    tagPane.getChildren().add(newTag);
  }


  @FXML
  private void updateIngr() {
    for (int i = 0; i < ingrBox.getChildren().size(); i++) {
      HBox box = (HBox) ingrBox.getChildren().get(i);
      Label value = (Label) box.getChildren().get(0);
      try {
        Double base_amount = ingr_values[i];
        Double amount = (double) Math.round(base_amount * multiplier * 100) / 100;
        value.setText((amount == 0.0) ? "" : String.valueOf(amount));
      } catch (Exception exception) {
        // nothing
      }
    }
  }


  @FXML
  private void updateComms() {
    commsBox.getChildren().clear();
    recipe.loadCommentsInfo();
    List<Comment> comms = recipe.getComments();
    for (Comment comm : comms) {
      commsBox.getChildren().add(makeComm(comm));
    }
  }


  @Override
  public void update() {
    load(App.recipe);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }
}
