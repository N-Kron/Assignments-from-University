package cookbook_app.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cookbook_app.App;
import cookbook_app.Const;
import cookbook_app.SceneInfo;
import cookbook_app.model.User;

public class UsersController implements Initializable, Controller {

    // public static Stage stage = createStage();
    // private static Stage createStage() {
    //     Scene scene = new Scene(App.loadFXML("Users.fxml"));
    //     Stage usersStage =  new Stage();
    //     usersStage.setTitle("Users");
    //     usersStage.setScene(scene);
    //     usersStage.initModality(Modality.APPLICATION_MODAL);
    //     return usersStage;
    // }

    public static SceneInfo sceneInfo = getInfo();
    private static SceneInfo getInfo() {
        Parent p = null;
        Controller controller = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + "Users.fxml"));
            p = fxmlLoader.load();
            controller = (Controller)fxmlLoader.getController();
        } catch (IOException e) {
            System.out.println("Exception in loading " + "Users.fxml");
            e.printStackTrace(); 
        }
        return new SceneInfo(new Scene(p), "Cookbook - Users", App.smallIcon, controller);
    }

    @FXML
    private TableView<User> tbData;
    @FXML
    public TableColumn<User, Integer> userId;

    @FXML
    public TableColumn<User, String> userName;

    @FXML
    public TableColumn<User, String> userPwd;

    @FXML
    public TableColumn<User, User> userPrivilege;
    
    @FXML
    public TableColumn<User, String> userDelete;

    @FXML
    private TextField searchField;

    @FXML
    private Button backButton;


    SimpleObjectProperty<ObservableList<User>> users;

    boolean updateScheduled = false;

    @FXML
    private void backOnClick(javafx.event.ActionEvent mouseEvent) {
        App.navBackward(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userId.setCellValueFactory(new PropertyValueFactory<>("UserId"));
        userPwd.setCellValueFactory(new PropertyValueFactory<>("Pwd"));

        userName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        userName.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<User, String>>() {
                @Override public void handle(TableColumn.CellEditEvent<User, String> t) {
                    ((User)t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
                }
            });
        userName.setCellFactory(TextFieldTableCell.forTableColumn());

        userPrivilege.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        userPrivilege.setCellFactory(cell -> new TableCell<>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    CheckBox checkBox = new CheckBox();

                    // Set starting value:
                    checkBox.setSelected(item.getPrivileged());
                    
                    checkBox.selectedProperty().addListener((observable, oldValue, newValue) ->
                            item.setPrivileged(newValue));

                    setGraphic(checkBox);
                }       
            }
        });

        userDelete.setCellValueFactory(new PropertyValueFactory<>(""));
        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory
                = //
                new Callback<TableColumn<User, String>, TableCell<User, String>>() {
            @Override
            public TableCell<User,String> call(final TableColumn<User, String> param) {
                final TableCell<User, String> cell = new TableCell<User, String>() {
                    final Button btn = new Button();

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                User person = getTableView().getItems().get(getIndex());
                                person.delete();
                                users.get().removeIf(x -> x.getUserId().equals(person.getUserId()));
                            });
                            ImageView iv = new ImageView(new Image(Const.c.iconsPath + "delete.png"));
                            iv.setFitHeight(16);
                            iv.setFitWidth(16);
                            btn.setGraphic(iv);
                            setGraphic(btn);
                            setText("");
                        }
                    }
                };
                return cell;
            }
        };
        userDelete.setCellFactory(cellFactory);

        users = new SimpleObjectProperty<ObservableList<User>>(FXCollections.observableArrayList());
        tbData.setItems(users.get());
        users.addListener((observable, oldValue, newValue) -> {
            tbData.setItems(newValue);
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!updateScheduled) {
                updateScheduled = true;
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.schedule(this::reloadUsers, 300, TimeUnit.MILLISECONDS);
            };
            
        });
    }

    private void reloadUsers() {
        updateScheduled = false;
        String substring = searchField.getText();
        if (!substring.isBlank()) {
            users.set(FXCollections.observableArrayList(User.searchAllUsers(substring)));
        } else {
            users.set(FXCollections.observableArrayList(User.getAllUsers()));
        }
    }

    @Override
    public void update() {
        searchField.setText("");
        users.set(FXCollections.observableArrayList(User.getAllUsers()));
    }
}
