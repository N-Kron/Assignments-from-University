package cookbook_app.controller;

import cookbook_app.Const;
import cookbook_app.SceneInfo;
import cookbook_app.model.Content;
import cookbook_app.model.Ingredient;
import cookbook_app.model.Recipe;
import cookbook_app.utilities.TextFieldSuggest;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import cookbook_app.App;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class RecipeCreationController implements Initializable, Controller {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField titleField;

    @FXML
    private TextField subtitleField;

    @FXML
    private TextArea instructionsField;

    @FXML
    private VBox ingredientsList;

    public static SceneInfo sceneInfo = getInfo();

    private Boolean error = false;

    private List<String> ingredientNames = Ingredient.getIngredientNames();

    ChangeListener<String> handleInput = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue,
                String newValue) {
            StringProperty textProperty = (StringProperty) observable;
            TextInputControl textField = (TextInputControl) textProperty.getBean();
            ObservableList<String> classList = textField.getStyleClass();

            classList.remove("textfield__invalid");

            // 'newValue == null' happens when using TextFieldSuggest autocomplete
            if (newValue == null || !newValue.equals("")) {
                classList.remove("textfield__empty");
            } else {
                classList.add("textfield__empty");
            }
        }
    };

    ChangeListener<Boolean> handleUnfocus = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                Boolean newValue) {
            if (newValue) return;

            ReadOnlyProperty<?> property = (ReadOnlyProperty<?>) observable;
            TextFieldSuggest textField = (TextFieldSuggest) property.getBean();
            String ingredientName = textField.getText().trim();

            Parent container = textField.getParent();
            TextField unitField = (TextField) container.getChildrenUnmodifiable().get(1);

            // resetting prompt text in case it was previously deleted
            unitField.setPromptText("unit");

            Boolean ingredientExists = textField.getEntries().contains(ingredientName);
            if (ingredientExists) {
                unitField.setDisable(true);
                unitField.getStyleClass().add("textfield__disabled");

                String unit = Ingredient.getUnitName(ingredientName);
                unitField.setText(unit);

                // setting empty string for text to avoid error in parts of code that parse this field
                // setting empty string for prompt to turn field into empty line if unit is null
                if (unit == null || unit.equals("")) {
                    unitField.setText("");
                    unitField.setPromptText("");
                }
            } else {
                unitField.setDisable(false);
                unitField.getStyleClass().remove("textfield__disabled");
            }

        }
    };

    private static SceneInfo getInfo() {
        Parent p = null;
        Controller controller = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + "RecipeCreation.fxml"));
            p = fxmlLoader.load();
            controller = (Controller) fxmlLoader.getController();
        } catch (IOException e) {
            System.out.println("Exception in loading " + "RecipeCreation.fxml");
            e.printStackTrace();
        }
        var sceneInfo = new SceneInfo(new Scene(p), "Cookbook - Create Recipe", App.smallIcon, controller);
        sceneInfo.resizable = false;
        return sceneInfo;
    }

    @FXML
    private void backOnClick(javafx.event.ActionEvent mouseEvent) {
        App.navBackward(false);
    }

    @FXML
    private void handleSaveButtonClick() {
        error = false;

        validateTextInputControl(titleField);
        validateTextInputControl(subtitleField);
        validateTextInputControl(instructionsField);
        ArrayList<Content> ingredients = parseIngredientsFieldData();

        if (ingredients.size() == 0 && !error) {
            error = true;
            App.throwError("Please, add ingredients");
        }

        if (!error) {
            String title = titleField.getText().trim();
            String subtitle = subtitleField.getText().trim();
            String instructions = instructionsField.getText().trim();

            Recipe recipe = new Recipe(title, subtitle, instructions, 2);

            for (Content content : ingredients) {
                recipe.addContent(content);
            }

            recipe.upsert();
            App.closeLast();
        }

    }

    @FXML
    void addIngredient() {
        TextField amountField = new TextField();
        amountField.setPrefWidth(80);
        amountField.setAlignment(Pos.CENTER);
        amountField.setPromptText("amount");
        amountField.getStyleClass().add("textfield");
        amountField.getStyleClass().add("textfield__empty");
        amountField.textProperty().addListener(handleInput);

        TextField unitField = new TextField();
        unitField.setPrefWidth(120);
        unitField.setPromptText("unit");
        unitField.getStyleClass().add("textfield");
        unitField.getStyleClass().add("textfield__empty");
        unitField.textProperty().addListener(handleInput);

        TextFieldSuggest nameField = new TextFieldSuggest();
        nameField.getEntries().addAll(ingredientNames);
        nameField.setPrefWidth(400);
        nameField.setPromptText("ingredient");
        nameField.getStyleClass().add("textfield");
        nameField.getStyleClass().add("textfield__empty");
        nameField.focusedProperty().addListener(handleUnfocus);
        nameField.textProperty().addListener(handleInput);

        Button deleteButton = new Button();
        deleteButton.getStyleClass().add("button");
        deleteButton.getStyleClass().add("button__transparent");
        deleteButton.getStyleClass().add("button__delete");
        deleteButton.setOnAction(handleDeleteButtonClick);
        ImageView image = new ImageView(new Image(Const.c.iconsPath + "delete.png"));
        image.setFitHeight(16);
        image.setFitWidth(16);
        deleteButton.setGraphic(image);

        HBox container = new HBox(amountField, unitField, nameField, deleteButton);
        container.setSpacing(10);
        container.setAlignment(Pos.CENTER_LEFT);

        ingredientsList.getChildren().add(container);
    }

    private ArrayList<Content> parseIngredientsFieldData() {
        ArrayList<Content> parsedData = new ArrayList<>();

        ObservableList<Node> rows = ingredientsList.getChildren();
        for (Node row : rows) {
            Set<Node> fields = row.lookupAll(".textfield");

            Iterator<Node> fieldsIterator = fields.iterator();

            TextField amountField = (TextField) fieldsIterator.next();
            TextField unitField = (TextField) fieldsIterator.next();
            TextField nameField = (TextField) fieldsIterator.next();

            validateAmountTextField(amountField);
            validateTextInputControl(nameField);

            if (!error) {
                String amount = amountField.getText().trim();
                String unit = unitField.getText().trim();
                String name = nameField.getText().trim();

                Ingredient ingredient = new Ingredient(name, unit);
                Content content = new Content(ingredient, Double.parseDouble(amount));

                parsedData.add(content);
            }
        }

        return parsedData;
    }

    private void validateTextInputControl(TextInputControl inputControl) {
        String text = inputControl.getText().trim();

        if (text.length() == 0) {
            inputControl.getStyleClass().add("textfield__invalid");
            error = true;
        }
    }

    private void validateAmountTextField(TextField amountField) {
        String text = amountField.getText().trim();

        if (!canParseDouble(text)) {
            error = true;
            amountField.getStyleClass().add("textfield__invalid");
        }
    }

    private boolean canParseDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    EventHandler<javafx.event.ActionEvent> handleDeleteButtonClick = new EventHandler<javafx.event.ActionEvent>() {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            Node button = (Node) event.getTarget();
            HBox ingredient = (HBox) button.getParent();
            VBox ingredientList = (VBox) ingredient.getParent();
            ingredientList.getChildren().remove(ingredient);
        }
    };

    @Override
    public void update() {
        ObservableList<String> titleStyleList = titleField.getStyleClass();
        titleStyleList.add("textfield__empty");
        titleStyleList.remove("textfield__invalid");
        titleField.setText("");

        ObservableList<String> subtitleStyleList = subtitleField.getStyleClass();
        subtitleStyleList.add("textfield__empty");
        subtitleStyleList.remove("textfield__invalid");
        subtitleField.setText("");

        ObservableList<String> instructionsStyleList = instructionsField.getStyleClass();
        instructionsStyleList.add("textfield__empty");
        instructionsStyleList.remove("textfield__invalid");
        instructionsField.setText("");

        ingredientsList.getChildren().clear();
        Platform.runLater(this::renewSuggestions);
    }

    private void renewSuggestions() {
        ingredientNames = Ingredient.getIngredientNames();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        titleField.textProperty().addListener(handleInput);
        subtitleField.textProperty().addListener(handleInput);
        instructionsField.textProperty().addListener(handleInput);
    }
}
