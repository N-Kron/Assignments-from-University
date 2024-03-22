package cookbook_app.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import cookbook_app.App;
import cookbook_app.Const;
import cookbook_app.SceneInfo;
import cookbook_app.model.Recipe;
import cookbook_app.model.Week;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DinnerController implements Initializable, Controller{
    
    public static String theSelectedWeek;
    private ArrayList<String> weekNames = new ArrayList<String>();
    private ArrayList<Week> weeks;
    
    public static SceneInfo sceneInfo = getInfo();
    private static SceneInfo getInfo() {
        Parent p = null;
        Controller controller = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + "Dinner.fxml"));
            p = fxmlLoader.load();
            controller = (Controller)fxmlLoader.getController();
        } catch (IOException e) {
            System.out.println("Exception in loading " + "Dinner.fxml");
            e.printStackTrace(); 
        }
        return new SceneInfo(new Scene(p), "Cookbook - Dinner", App.smallIcon, controller);
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backButton;

    @FXML
    private Button addWeek;

    @FXML
    private TextField textField;

    @FXML
    private Button deleteWeek;

    @FXML
    private ListView<Button> fieldOne;
    
    @FXML
    private ListView<Button> fieldTwo;
    
    @FXML
    private ListView<Button> fieldThree;
    
    @FXML
    private ListView<Button> fieldFour;
    
    @FXML
    private ListView<Button> fieldFive;
    
    @FXML
    private ListView<Button> fieldSix;
    
    @FXML
    private ListView<Button> fieldSeven;

    @FXML
    private ListView<String> list;

    @FXML
    private Button addRecipe;

    @FXML
    private Button shopList;

    @FXML
    private void shopping() {
        if (weeks.isEmpty()) {
            String content = "Add a recipe to the dinner list bevore you get the shopping list.";
            Alert alan = new Alert(AlertType.INFORMATION, content);
            alan.show();
        } else {
            App.newWindow(false, ShoppingController.sceneInfo, false);
        }
    }

    @FXML
    private void addRecipe() {
        App.newWindow(false, RecipesController.sceneInfo,false);
    }

    @FXML
    private void backOnClick(javafx.event.ActionEvent mousEvent) {
        App.navBackward(false);
    }
    
    @FXML
    private void addWeek(javafx.event.ActionEvent mousEvent) {
        String theName = textField.getText();
        if (!theName.equals("")) {
            try {
                Connection c = App.getConnection();
                String query = "insert into weeks(weeks_user, weeks_name) values (?, ?)";
                PreparedStatement pState = c.prepareStatement(query);
                pState.setString(1, App.signedUser.get().getUserId());
                pState.setString(2, theName);
                pState.executeQuery();
                c.close();
                
                list.getItems().add(textField.getText());
                weekNames.add(theName);
                } catch (Exception e) {
                    System.out.println("Query in DinnerController.addWeek() failed " + e.getMessage());
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setContentText("That week already exists!");
                    alert.show();
                }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setContentText("You have not entered a name for the week");
            alert.show();
        }

        textField.clear();
    }

    @FXML
    private void deleteWeek(javafx.event.ActionEvent mousEvent) {
        try {
            Connection con = App.getConnection();
            String query = "delete from weeks where weeks_user = (?) and weeks_name = (?)";
            PreparedStatement pState = con.prepareStatement(query);
            pState.setString(1, App.signedUser.get().getUserId());
            pState.setString(2, theSelectedWeek);
            pState.executeQuery();
            
            con.close();
        } catch (Exception e) {
            System.out.println("Query in deleteWeek() failed " + e.getMessage());
        }
        weekNames.remove(theSelectedWeek);
        list.getItems().remove(theSelectedWeek);
    }

    private void clearFields() {
        fieldOne.getItems().clear();
        fieldTwo.getItems().clear();
        fieldThree.getItems().clear();
        fieldFour.getItems().clear();
        fieldFive.getItems().clear();
        fieldSix.getItems().clear();
        fieldSeven.getItems().clear();
    }

    @Override
    public void update() {
        shopList.setDisable(true);
        list.getSelectionModel().clearSelection();
        clearFields();
        list.getItems().clear();
        weekNames.clear();

        if (!(weeks == null)) {
            weeks.clear();
        }
        weeks = Week.fetch();
        weekNames = Week.fetchWeekNames();
        for (String name : weekNames) {
            list.getItems().add(name);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        shopList.setDisable(true);

        ImageView iv = new ImageView(new Image(Const.c.iconsPath + "add.png"));
        iv.setFitHeight(15);
        iv.setFitWidth(15);
        addWeek.setGraphic(iv);

        weeks = Week.fetch();
        weekNames = Week.fetchWeekNames();
        for (String name : weekNames) {
            list.getItems().add(name);
        }

        
        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                theSelectedWeek = list.getSelectionModel().getSelectedItem();
                if (!(theSelectedWeek == null)) {
                    shopList.setDisable(false);
                }
                clearFields();

                for (Week we : weeks) {
                    final Button recButton = new Button(we.getRecipe());
                    recButton.setOnAction(event -> {
                        App.recipe = Recipe.getRecipe(we.getRecipeId());
                        App.newWindow(false, RecipeController.sceneInfo, false);
                    });
                    recButton.getStylesheets().add("file:src/main/resources/cookbook_app/css/dinner2.css");
                    
                    if (we.getDay() == 1 && we.getWeek().equals(theSelectedWeek)) {
                        fieldOne.getItems().add(recButton);
                    }
                    else if (we.getDay() == 2 && we.getWeek().equals(theSelectedWeek)) {
                        fieldTwo.getItems().add(recButton);
                    }
                    else if (we.getDay() == 3 && we.getWeek().equals(theSelectedWeek)) {
                        fieldThree.getItems().add(recButton);
                    }
                    else if (we.getDay() == 4 && we.getWeek().equals(theSelectedWeek)) {
                        fieldFour.getItems().add(recButton);
                    }
                    else if (we.getDay() == 5 && we.getWeek().equals(theSelectedWeek)) {
                        fieldFive.getItems().add(recButton);
                    }
                    else if (we.getDay() == 6 && we.getWeek().equals(theSelectedWeek)) {
                        fieldSix.getItems().add(recButton);
                    }
                    else if (we.getDay() == 7 && we.getWeek().equals(theSelectedWeek)) {
                        fieldSeven.getItems().add(recButton);
                    }
                }
            }
    });
    }

}
