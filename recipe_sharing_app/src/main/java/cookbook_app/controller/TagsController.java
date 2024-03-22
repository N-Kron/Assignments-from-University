package cookbook_app.controller;

import javafx.beans.binding.Bindings;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
import cookbook_app.model.Tag;

public class TagsController implements Initializable, Controller {

    public static SceneInfo sceneInfo = getInfo();
    private static SceneInfo getInfo() {
        Parent p = null;
        Controller controller = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + "Tags.fxml"));
            p = fxmlLoader.load();
            controller = (Controller)fxmlLoader.getController();
        } catch (IOException e) {
            System.out.println("Exception in loading " + "Tags.fxml");
            e.printStackTrace(); 
        }
        return new SceneInfo(new Scene(p), "Cookbook - Tags", App.smallIcon, controller);
    }

    @FXML
    private TableView<Tag> tbData;

    @FXML
    public TableColumn<Tag, String> tagKeyword;
    
    @FXML
    public TableColumn<Tag, String> tagUser;

    @FXML
    public TableColumn<Tag, String> tagDelete;

    @FXML
    private TextField searchField;

    @FXML
    private TextField addField;
    
    @FXML
    private Button backButton;

    @FXML
    private HBox addHBox;

    @FXML
    private Button addButton;

    SimpleObjectProperty<ObservableList<Tag>> tags;

    boolean updateScheduled = false;

    @FXML
    private void backOnClick(javafx.event.ActionEvent mouseEvent) {
        App.navBackward(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tagKeyword.setCellValueFactory(new PropertyValueFactory<>("Keyword"));
        tagUser.setCellValueFactory(new PropertyValueFactory<>("User"));

        tagDelete.setCellValueFactory(new PropertyValueFactory<>(""));
        Callback<TableColumn<Tag, String>, TableCell<Tag, String>> cellFactory
                = //
                new Callback<TableColumn<Tag, String>, TableCell<Tag, String>>() {
            @Override
            public TableCell<Tag,String> call(final TableColumn<Tag, String> param) {
                final TableCell<Tag, String> cell = new TableCell<Tag, String>() {

                    final Button btn = new Button();

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                Tag tag = getTableView().getItems().get(getIndex());
                                tag.delete();
                                tags.get().removeIf(x -> x.getKeyword().equals(tag.getKeyword()));
                            });
                            ImageView iv = new ImageView(new Image(Const.c.iconsPath + "delete.png"));
                            iv.setFitHeight(16);
                            iv.setFitWidth(16);
                            btn.setGraphic(iv);
                            setGraphic(btn);
                            setText("");
                            btn.setDisable(App.signedUser.get() == null || (!App.signedUser.get().getPrivileged() && getTableView().getItems().get(getIndex()).getUser() == null));
                            //btn.disableProperty().bind( Bindings.createBooleanBinding(() -> App.signedUser.get() == null || (!App.signedUser.get().getPrivileged() && getTableView().getItems().get(getIndex()).getUser() == null), App.signedUser));
                        }
                    }
                };
                return cell;
            }
        };
        tagDelete.setCellFactory(cellFactory);

        tags = new SimpleObjectProperty<ObservableList<Tag>>(FXCollections.observableArrayList(Tag.getUserTags(null, App.signedUser.get())));
        tbData.setItems(tags.get());
        tags.addListener((observable, oldValue, newValue) -> {
            tbData.setItems(newValue);
        });

        ImageView iv = new ImageView(new Image(Const.c.iconsPath + "add.png"));
        iv.setFitHeight(15);
        iv.setFitWidth(15);
        addButton.setGraphic(iv);
        addButton.setOnAction(event -> {
            if (!addField.getText().isEmpty())
            {
                String uid = App.signedUser.get().getPrivileged() ? null : App.signedUser.get().getUserId();  
                Tag tag = new Tag(-1, addField.getText(), uid);
                if (!tag.commit()) {
                    App.throwError("Tag already exists");
                } else {
                    tags.get().add(tag); 
                }
                addField.setText("");
            }           
        });
        addButton.disableProperty().bind(Bindings.createBooleanBinding(() -> App.signedUser.get() == null, App.signedUser));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!updateScheduled) {
                updateScheduled = true;
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.schedule(this::reloadTags, 500, TimeUnit.MILLISECONDS);
              };
        });
    }
    public void reloadTags() {
        updateScheduled = false;
        tags.set(FXCollections.observableArrayList(Tag.getUserTags(searchField.getText().toLowerCase(), App.signedUser.get())));
    }
    @Override
    public void update() {
        reloadTags();
        searchField.setText("");
        addField.setText("");
    }
}
