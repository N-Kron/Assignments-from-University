package cookbook_app.controller;


import javafx.beans.property.SimpleObjectProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.util.Duration;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
//import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.MouseButton;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
//import java.util.Comparator;

import cookbook_app.App;
import cookbook_app.Const;
import cookbook_app.SceneInfo;
import cookbook_app.model.Messages;
import cookbook_app.model.Recipe;

public class MessagesController implements Initializable, Controller {

    public static SceneInfo sceneInfo = getInfo();

    private static SceneInfo getInfo() {
        Parent p = null;
        Controller controller = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + "Messages.fxml"));
            p = fxmlLoader.load();
            controller = (Controller) fxmlLoader.getController();
        } catch (IOException e) {
            System.out.println("Exception in loading " + "Messages.fxml");
            e.printStackTrace();
        }
        return new SceneInfo(new Scene(p), "Cookbook - Messages", App.smallIcon, controller);
    }


    @FXML
    private TableView<Messages> tbData;

    @FXML
    private TableColumn<Messages, String> senderId;


    @FXML
    private TableColumn<Messages, String> recipeName;

    @FXML
    void doubleClick(javafx.event.ActionEvent mouseEvent) {
    }

    @FXML
    private TableColumn<Messages, String> PrivateMessage;

    @FXML
    private TableColumn<Messages, Messages> seen;

    SimpleObjectProperty<ObservableList<Messages>> MyMessages;

    @FXML
    private void backOnClick(javafx.event.ActionEvent mouseEvent) {
        App.navBackward(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        senderId.setCellValueFactory(new PropertyValueFactory<>("Sender"));
        recipeName.setCellValueFactory(new PropertyValueFactory<>("Recipe"));

        PrivateMessage.setCellValueFactory(new PropertyValueFactory<>("Message"));
        PrivateMessage.setCellFactory(column -> {
            return new TableCell<Messages, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setTooltip(null);
                    } else {
                        setText(item.replace("\n", " "));
                        Tooltip tooltip = new Tooltip(item);
                        tooltip.setShowDelay(Duration.seconds(0.5));
                        tooltip.setWrapText(true);
                        tooltip.setMaxWidth(600);
                        tooltip.setFont(Font.font("System", 14));
                        tooltip.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: black; -fx-border-radius: 0;");
                        tooltip.setAutoHide(false);
                        tooltip.setShowDuration(Duration.seconds(30));
                        setTooltip(tooltip);
                    }
                }
            };
        });

        seen.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        seen.setCellFactory(cell -> new TableCell<>() {
            @Override
            public void updateItem(Messages item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    CheckBox checkBox = new CheckBox();

                    // Set starting value:
                    checkBox.setSelected(item.getSeen());

                    checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> item.setSeen(newValue));
                    setGraphic(checkBox);
                }
            }
        });

        seen.setSortable(true);
        seen.setSortType(TableColumn.SortType.DESCENDING);
        seen.setComparator((b1, b2) -> Boolean.compare(b2.getSeen(), b1.getSeen()));
           
        tbData.getSortOrder().add(seen);
        tbData.sort(); 

        tbData.getSortOrder().add(seen);
        tbData.sort();

        MyMessages = new SimpleObjectProperty<ObservableList<Messages>>(
                FXCollections.observableArrayList(Messages.getMessagess(x -> true)));
        tbData.setItems(MyMessages.get());
        tbData.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) { // Double-click
                Recipe selectedRecipe = tbData.getSelectionModel().getSelectedItem().getRecipeObj();
                if (selectedRecipe != null) {
                    App.recipe = selectedRecipe;
                    App.newWindow(false, RecipeController.sceneInfo, true);
                }
            }
        });
        MyMessages.addListener((observable, oldValue, newValue) -> {
            tbData.setItems(newValue);
            tbData.getSortOrder().clear();
            tbData.getSortOrder().add(seen);
            tbData.sort();
        });
        
   
    
    }

    @Override
    public void update() {
        MyMessages.set(FXCollections.observableArrayList(Messages.getMessagess(x -> true)));
    }

}
