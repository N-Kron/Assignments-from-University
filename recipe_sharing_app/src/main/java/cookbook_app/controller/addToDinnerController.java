package cookbook_app.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import cookbook_app.App;
import cookbook_app.Const;
import cookbook_app.SceneInfo;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;

public class addToDinnerController implements Initializable, Controller {

    public static SceneInfo sceneInfo = getInfo();
    private static SceneInfo getInfo() {
        Parent p = null;
        Controller controller = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + "AddToDinner.fxml"));
            p = fxmlLoader.load();
            controller = (Controller)fxmlLoader.getController();
        } catch (IOException e) {
            System.out.println("Exception in loading " + "AddToDinner.fxml");
            e.printStackTrace(); 
        }
        return new SceneInfo(new Scene(p), "Cookbook - Add a Recipe to the Dinner List", App.smallIcon, controller);
    }
    
    @FXML
    private Button addRecipe;

    @FXML
    private RadioButton radioOne;

    @FXML
    private RadioButton radioTwo;

    @FXML
    private RadioButton radioThree;

    @FXML
    private RadioButton radioFour;

    @FXML
    private RadioButton radioFive;

    @FXML
    private RadioButton radioSix;

    @FXML
    private RadioButton radioSeven;

    @FXML
    private ChoiceBox<String> bar;

    private ToggleGroup radios;
    private int theDay;
    private String selWeek = null;

    @FXML
    private void addTheRecipe() {
        theDay = 0;
        if (radioOne.isSelected()) {
            theDay = 1;
        } else if (radioTwo.isSelected()) {
            theDay = 2;
        } else if (radioThree.isSelected()) {
            theDay = 3;
        } else if (radioFour.isSelected()) {
            theDay = 4;
        } else if (radioFive.isSelected()) {
            theDay = 5;
        } else if (radioSix.isSelected()) {
            theDay = 6;
        } else if (radioSeven.isSelected()) {
            theDay = 7;
        }

        if (selWeek == null || theDay == 0) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Please select a week and a day before you continue.\n"
                                 + "Make sure you have created a week in the Dinner Menu!");
            alert.show();
        } else {
            try {
                // this would requrie locking of a table in a remote database with mulitple users
                Connection c = App.getConnection();

                String query = "insert into dinner(dinner_week, dinner_day, dinner_user, dinner_recipe, dinner_portion) values (?, ?, ?, ?, ?)";
                PreparedStatement pState = c.prepareStatement(query);
                pState.setString(1, selWeek);
                pState.setInt(2, theDay);
                pState.setString(3, App.signedUser.get().getUserId());
                pState.setInt(4, App.recipe.getRecipeID());
                pState.setDouble(5, App.multiplier);
                pState.executeQuery();

                query = "insert into shop_list(shop_week, shop_ingr, shop_value, shop_user) select dinner_week, content_ingr, round((content_value / (base_portion / dinner_portion)), 2),"
                        + " dinner_user from content join recipe on recipe_id = content_recipe join dinner on recipe_id = dinner_recipe where recipe_id = (?)"
                        + " and dinner_user = (?) and dinner_portion = (?) and dinner_id = (select max(dinner_id) from dinner) on"
                        + " duplicate key update shop_value = shop_value + round((content_value / (base_portion / dinner_portion)), 2)";
                pState = c.prepareStatement(query);
                pState.setInt(1, App.recipe.getRecipeID());
                pState.setString(2, App.signedUser.get().getUserId());
                pState.setDouble(3, App.multiplier);
                pState.executeQuery();

                c.close();
                App.closeLast();
            } catch (Exception e) {
                System.out.println("Query in addToDinnerController.addTheRecipe() failed " + e.getMessage());
            }
        }
    }
    
    @Override
    public void update() {
        if (!(radios.getSelectedToggle() == null)) {
            radios.getSelectedToggle().setSelected(false);
        }
        bar.getItems().clear();
        bar.getItems().addAll(Week.fetchWeekNames());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        radios = new ToggleGroup();
        radioOne.setToggleGroup(radios);
        radioTwo.setToggleGroup(radios);
        radioThree.setToggleGroup(radios);
        radioFour.setToggleGroup(radios);
        radioFive.setToggleGroup(radios);
        radioSix.setToggleGroup(radios);
        radioSeven.setToggleGroup(radios);
        
        bar.getItems().addAll(Week.fetchWeekNames());
        bar.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                selWeek = bar.getSelectionModel().getSelectedItem();
            }
            
        });
        
    }
    
}
