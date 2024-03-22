package cookbook_app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import cookbook_app.App;
import cookbook_app.Const;
import cookbook_app.SceneInfo;
import cookbook_app.model.ShopList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class ShoppingController implements Initializable, Controller {
    SimpleObjectProperty<ObservableList<ShopList>> ingres;
    
    public static SceneInfo sceneInfo = getInfo();
    private static SceneInfo getInfo() {
        Parent p = null;
        Controller controller = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + "ShoppingList.fxml"));
            p = fxmlLoader.load();
            controller = (Controller)fxmlLoader.getController();
        } catch (IOException e) {
            System.out.println("Exception in loading " + "ShoppingList.fxml");
            e.printStackTrace(); 
        }
        return new SceneInfo(new Scene(p), "Cookbook - Shopping List", App.smallIcon, controller);
    }

    
    @FXML
    private Button backButton;
    
    @FXML
    private TableView<ShopList> tableViewIng = new TableView<ShopList>();

    @FXML
    private TableColumn<ShopList, String> ingredient = new TableColumn<>("Ingredient");

    @FXML
    private TableColumn<ShopList, Double> amount = new TableColumn<>("Amount");

    @FXML
    TableColumn<ShopList, String> unit = new TableColumn<>("Unit");
    
    @FXML
    private TableColumn<ShopList, String> deleteIng = new TableColumn<>("");
    
    @FXML
    private void backOnClick(javafx.event.ActionEvent mousEvent) {
        App.navBackward(false);
    }
    
    @Override
    public void update() {
        ingres = new SimpleObjectProperty<ObservableList<ShopList>>(FXCollections.observableArrayList(ShopList.getShopIngrs()));
        tableViewIng.setItems(ingres.get());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        ingredient.setCellValueFactory(new PropertyValueFactory<ShopList, String>("ingr"));
        amount.setCellValueFactory(new PropertyValueFactory<ShopList, Double>("value"));
        unit.setCellValueFactory(new PropertyValueFactory<ShopList, String>("unit"));
        deleteIng.setCellValueFactory(new PropertyValueFactory<ShopList, String>(""));

        Callback<TableColumn<ShopList, String>, TableCell<ShopList, String>> costTColumn =
        new Callback<TableColumn<ShopList, String>, TableCell<ShopList, String>>() {

            @Override
            public TableCell<ShopList, String> call(TableColumn<ShopList, String> param) {
                final TableCell<ShopList, String> deleteCell = new TableCell<ShopList, String>() {
                    final Button delButton = new Button();

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            ImageView imageV = new ImageView(new Image(Const.c.iconsPath + "delete.png"));
                            imageV.setFitHeight(16);
                            imageV.setFitWidth(16);
                            delButton.setGraphic(imageV);
                            delButton.setOnAction(event -> {
                                ShopList entry = getTableView().getItems().get(getIndex());
                                entry.remove();
                                ingres.get().removeIf(x -> x.getIngr().equals(entry.getIngr()));
                            });
                            setText("");
                            setGraphic(delButton);
                        }
                    }
                };
                return deleteCell;
            }
        };

        deleteIng.setCellFactory(costTColumn);
        ingres = new SimpleObjectProperty<ObservableList<ShopList>>(FXCollections.observableArrayList(ShopList.getShopIngrs()));
        tableViewIng.setItems(ingres.get());
    }
}
