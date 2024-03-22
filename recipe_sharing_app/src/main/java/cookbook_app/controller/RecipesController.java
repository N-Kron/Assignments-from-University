package cookbook_app.controller;

import cookbook_app.utilities.TextFieldSuggest;
import cookbook_app.utilities.rcpConnector;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.scene.input.ScrollEvent;

import cookbook_app.App;
import cookbook_app.Const;
import cookbook_app.SceneInfo;
import cookbook_app.model.Ingredient;
import cookbook_app.model.Recipe;
import cookbook_app.model.Tag;

public class RecipesController implements Initializable, Controller {

  public static SceneInfo sceneInfo = getInfo();
  SimpleObjectProperty<ObservableList<Recipe>> recipes = new SimpleObjectProperty<>(FXCollections.observableArrayList());
  //SimpleObjectProperty<ObservableList<Recipe>> allRecipes = new SimpleObjectProperty<>(recipes.get());
  List<Tag> tags = new ArrayList<>();
  List<Ingredient> ingredients = new ArrayList<>();
  List<HBox> HBoxFiltered = new ArrayList<>();
  boolean updateScheduled = false;

  private static SceneInfo getInfo() {
    Parent p = null;
    Controller controller = null;
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + "Recipes.fxml"));
      p = fxmlLoader.load();
      controller = (Controller) fxmlLoader.getController();
    } catch (IOException e) {
      System.out.println("Exception in loading " + "Recipes.fxml");
      e.printStackTrace();
    }
    return new SceneInfo(new Scene(p), "Cookbook - Recipes", App.smallIcon, controller);
  }

  @FXML
  private TableView<Recipe> tbData;

  @FXML
  public TableColumn<Recipe, String> recipeKeyword;

  @FXML
  public TableColumn<Recipe, String> recipeStar;

  @FXML
  private TextField searchField;

  //@FXML
  //private TextField addTagField;

  //@FXML
  //private TextField addIngFieldtf;

  @FXML
  private Button backButton;

  @FXML
  private HBox currentFilterHBox;

  @FXML
  private HBox addHBox;

  @FXML
  private Button addTagButton;

  @FXML
  private Button addIngButton;

  private TextFieldSuggest addTagField;

  private TextFieldSuggest addIngField;

  @FXML
  private void backOnClick(javafx.event.ActionEvent mouseEvent) {
    App.navBackward(false);
    tbData.refresh();
  }

  @FXML
  private void openRecipe(javafx.event.ActionEvent mouseEvent) {
  }

  @FXML
  private HBox modify(HBox box, String text, Boolean isTag) {
    // modify hbox to make it into a tag
    box.setAlignment(Pos.CENTER);
    if (isTag) {
      box.setStyle("-fx-background-color: #c6f4a1; -fx-background-radius: 12px;");
    } else {
      box.setStyle("-fx-background-color: #ADD8E6; -fx-background-radius: 12px;");
    }

    Label label = new Label(text);
    label.setAlignment(Pos.CENTER);
    Font font = new Font("tagFont", 16);
    label.setFont(font);

    Button x = new Button("x");
    x.setOnAction(tagDel);
    if (isTag) {
      x.setStyle("-fx-background-color: #acf86d; -fx-background-radius: 20px; -fx-font-size: 16px;");
    } else {
      x.setStyle("-fx-background-color: #9fd2e2; -fx-background-radius: 20px; -fx-font-size: 16px;");
    }

    Insets insets = new Insets(0, 0, 1, 10);
    box.getChildren().add(label);
    box.getChildren().add(x);
    box.setPadding(insets);
    currentFilterHBox.getChildren().add(box);

    return box;
  }

  @FXML
  private void align(HBox tag) {
    // align other tags once one gets deleted
    double x = tag.getLayoutX();
    int lenght = currentFilterHBox.getChildren().size();
    Label label = (Label) tag.getChildren().get(0);

    for (int i = 0; i < lenght; i++) {
      HBox box = (HBox) currentFilterHBox.getChildren().get(i);
      if (box.getLayoutX() + label.getText().length() + 20 > x) {
        box.setLayoutX(box.getLayoutX() - label.getText().length() * 16);
      }

    }
  }

  // Instance variable to keep track of how many recipes are currently displayed
  private int numDisplayedRecipes = 30;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    addTagField = new TextFieldSuggest();
    addHBox.getChildren().add(0, addTagField);
    addTagField.setStyle("-fx-border-color: #2D75E8;");
    addTagField.setLayoutX(350);
    addTagField.setPrefHeight(30);
    addTagField.setPrefWidth(125);
    addTagField.setPromptText("Filter by tag");
    // addTagField.getEntries().addAll(Tag.getTagsKeywords());

    addIngField = new TextFieldSuggest();
    addHBox.getChildren().add(2, addIngField);
    addIngField.setStyle("-fx-border-color: #2D75E8;");
    addIngField.setLayoutX(350);
    addIngField.setPrefHeight(30);
    addIngField.setPrefWidth(125);
    addIngField.setPromptText("Filter by ingredient");
    // addIngField.getEntries().addAll(Ingredient.getIngredientNames());

    tbData.setRowFactory(tv -> new TableRow<Recipe>() {
      private Tooltip tooltip = new Tooltip();

      @Override
      public void updateItem(Recipe recipe, boolean empty) {
        super.updateItem(recipe, empty);
        if (recipe == null) {
          setTooltip(null);
        } else {
          tooltip.setText(recipe.getShortDesc());
          setTooltip(tooltip);
        }
      }
    });
    recipeKeyword.setCellValueFactory(new PropertyValueFactory<>("Keyword"));

    recipeStar.setCellValueFactory(new PropertyValueFactory<>(""));
    Callback<TableColumn<Recipe, String>, TableCell<Recipe, String>> cellFactory =
      new Callback<TableColumn<Recipe, String>, TableCell<Recipe, String>>() {
      @Override
      public TableCell<Recipe, String> call(final TableColumn<Recipe, String> param) {
        final TableCell<Recipe, String> cell = new TableCell<Recipe, String>() {
          @Override
          public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
              setGraphic(null);
              setText(null);
            } else {
              Recipe recipe = getTableView().getItems().get(getIndex());
              String imagePath = recipe.getFavorite() ? "star.png" : "star_empty.png";
              ImageView iv = new ImageView(new Image(Const.c.iconsPath + imagePath));
              iv.setFitHeight(16);
              iv.setFitWidth(16);
              Button btn = new Button();
              btn.setGraphic(iv);
              btn.setOnAction(event -> {
                if (App.signedUser.get() != null) {
                  rcpConnector connector = new rcpConnector();
                  String sql;
                  if (!recipe.getFavorite()) {
                    recipe.setFavorite(true);
                    // insert into favorite table
                    sql = "INSERT INTO favorite (target_user, favorite_recipe) VALUES (?, ?)";
                  } else {
                    recipe.setFavorite(false);
                    // delete from favorite table
                    sql = "DELETE FROM favorite WHERE target_user = ? AND favorite_recipe = ?";
                  }
                  connector.setStrings(sql, App.signedUser.get().getUserId(), Integer.toString(recipe.getRecipeID()));
                  updateItem(item, empty);

                } else {
                  Alert a = new Alert(AlertType.INFORMATION);
                  a.setHeaderText("Sign in to mark recipe as favorite");
                  App.setIcon(a);
                  a.show();
                }
              });
              setGraphic(btn);
              setText("");
            }
          }
        };
        return cell;
      }
    };
    recipeStar.setCellFactory(cellFactory);

    tbData.setItems(recipes.get());

    tbData.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2) {
        Recipe selectedRecipe = tbData.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
          App.recipe = selectedRecipe;
          App.newWindow(false, RecipeController.sceneInfo, true);
        }
      }
    });

    // recipes.addListener((observable, oldValue, newValue) -> {
    //   // Update the tbData items when the recipes property changes
    //   tbData.setItems(newValue);
    // });

    App.signedUser.addListener((observable, oldValue, newValue) -> {
      if (newValue != oldValue || !newValue.getUserId().equals(oldValue.getUserId())) {
        currentFilterHBox.getChildren().clear();
        addTagField.clear();
        tags.clear();

        addIngField.clear();
        ingredients.clear();
        searchField.clear();
      }
    });

    ImageView iv = new ImageView(new Image(Const.c.iconsPath + "add.png"));
    iv.setFitHeight(15);
    iv.setFitWidth(15);
    addTagButton.setGraphic(iv);
    addTagButton.setOnAction(event -> {
      Tag tag = Tag.getTag(x -> x.getKeyword().equals(addTagField.getText()));
      if (tag == null) {
        App.throwError("Tag not found");
      } else {
        tags.add(tag);
        reloadRecipes();
        HBoxFiltered.add(modify(new HBox(10), tag.getKeyword(), true));
        addTagField.clear();
      }
    });
    ImageView v = new ImageView(new Image(Const.c.iconsPath + "add.png"));
    v.setFitHeight(15);
    v.setFitWidth(15);
    addIngButton.setGraphic(v);
    addIngButton.setOnAction(event -> {
      Ingredient ingredient = Ingredient.getIngredient(addIngField.getText().toLowerCase());
      if (ingredient == null) {
        App.throwError("Ingredient not found");
      } else {
        HBoxFiltered.add(modify(new HBox(10), ingredient.getName(), false));
        ingredients.add(ingredient);
        reloadRecipes();
        addIngField.clear();
      }
    });

    tbData.addEventFilter(ScrollEvent.ANY, event -> {
      if (!event.isConsumed() && event.getDeltaY() < 0) {
        ScrollBar scrollBar = getVerticalScrollbar(tbData);
        if (scrollBar != null && scrollBar.getValue() == 1.0) {
          int start = numDisplayedRecipes;
          numDisplayedRecipes += 30;
          if (numDisplayedRecipes > recipes.get().size()) {
            numDisplayedRecipes = recipes.get().size();
          }
          tbData.getItems().addAll(recipes.get().subList(start, numDisplayedRecipes));
          event.consume();
        }
      }
    });

    tbData.setOnMouseClicked(event -> {
      if (event.getClickCount() == 2) {
        Recipe selectedRecipe = tbData.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
          App.recipe = selectedRecipe;
          App.newWindow(false, RecipeController.sceneInfo, true);
        }
      }
    });

    recipes.addListener((observable, oldValue, newValue) -> {
      if (newValue.size() >= numDisplayedRecipes) {
        tbData.setItems(FXCollections.observableArrayList(newValue.subList(0, numDisplayedRecipes)));
      } else {
        tbData.setItems(newValue);
      }
    });
    searchField.textProperty().addListener((observable) -> {
      if (!updateScheduled) {
        updateScheduled = true;
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(this::reloadRecipes, 500, TimeUnit.MILLISECONDS);
      };
    });
  }

  private ScrollBar getVerticalScrollbar(TableView<?> table) {

    ScrollBar result = null;
    for (Node node : table.lookupAll(".scroll-bar")) {
      if (node instanceof ScrollBar) {
        ScrollBar bar = (ScrollBar) node;
        if (bar.getOrientation().equals(Orientation.VERTICAL)) {
          result = bar;
        }
      }
    }
    return result;
  }

  @Override
  public void update() {
    numDisplayedRecipes = 30;
    // tags.clear();
    // ingredients.clear();
    // HBoxFiltered.clear();
    reloadRecipes();
    ScrollBar sb = getVerticalScrollbar(tbData);
    if (sb != null) sb.setValue(0);
    Platform.runLater(this::renewSuggestions);
  }

  public void renewSuggestions() {
    addIngField.getEntries().clear();
    addIngField.getEntries().addAll(Ingredient.getIngredientNames());
    addTagField.getEntries().clear();
    addTagField.getEntries().addAll(Tag.getTagsKeywords());
  }

  public void reloadRecipes() {
    updateScheduled = false;
    recipes.set(FXCollections.observableArrayList(Recipe.getRecipesBase(searchField.getText().toLowerCase(), tags, ingredients)));
  }

  EventHandler<javafx.event.ActionEvent> tagDel = new EventHandler<javafx.event.ActionEvent>() {
    @Override
    public void handle(javafx.event.ActionEvent event) {
      Button origin = (Button) event.getSource();
      HBox parent = (HBox) origin.getParent();
      align(parent);
      currentFilterHBox.getChildren().remove(parent);

      // Remove filter from tags or ingredients
      Object firstChild = parent.getChildren().get(0);
      Label label = (Label) firstChild;
      String labelText = label.getText();
      tags.removeIf(x -> x.getKeyword().equals(labelText));
      ingredients.removeIf(x -> x.getName().equals(labelText));
      reloadRecipes();
    }
  };

  EventHandler<javafx.event.ActionEvent> addNewTag = new EventHandler<javafx.event.ActionEvent>() {
    @Override
    public void handle(javafx.event.ActionEvent event) {
      TextField origin = (TextField) event.getSource();
      String text = origin.getText();
      HBoxFiltered.add(modify(new HBox(10), text, false));
      origin.setManaged(false);
      origin.setVisible(false);
      HBox box = (HBox) origin.getParent();
      box.setPadding(new Insets(0, 0, 0, 0));
      origin.clear();
    }
  };
}
