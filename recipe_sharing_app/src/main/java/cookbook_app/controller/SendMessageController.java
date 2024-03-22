package cookbook_app.controller;

import cookbook_app.Const;
import cookbook_app.SceneInfo;
import cookbook_app.model.SendMessage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import cookbook_app.App;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;




public class SendMessageController implements Initializable, Controller {
    public static SceneInfo sceneInfo = getInfo();

    private static SceneInfo getInfo() {
        Parent p = null;
        Controller controller = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + "SendMessage.fxml"));
            p = fxmlLoader.load();
            controller = (Controller) fxmlLoader.getController();
        } catch (IOException e) {
            System.out.println("Exception in loading " + "SendMessage.fxml");
            e.printStackTrace();
        }
        return new SceneInfo(new Scene(p), "Cookbook - Send Message", App.smallIcon, controller);
    }

    @FXML
    private TextArea MessageField;

    @FXML
    private ComboBox<String> Dropdown;

    private ObservableList<String> names;
    private Map<String, String> userMap = new HashMap<>();
    private String selectedUserName = "";
    private String selectedUserId = "";

    @FXML
    private Button Send;

    SimpleObjectProperty<ObservableList<SendMessage>> users =new SimpleObjectProperty<ObservableList<SendMessage>>(FXCollections.observableArrayList());

    String Message;

    @FXML
    private void sendOnClick(ActionEvent mouseEvent) {
       try{
        Message = MessageField.getText();
        if (!Message.isEmpty() && selectedUserId != "") {
            SendMessage sending = new SendMessage(selectedUserId, selectedUserName, Message);
            sending.commit();
            MessageField.clear();
            App.closeLast();
            
            Dropdown.valueProperty().set(null);
            //SendMessage.commit();
        }
        
       } catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    private void backOnClick(javafx.event.ActionEvent mouseEvent) {
        App.navBackward(false);
    }


    @FXML
    private void commit() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        users.addListener((observable, oldValue, newValue) -> {
            ObservableList<String> names =  FXCollections.observableArrayList();
            for (SendMessage item: newValue) {
                String id = item.getUserId();
                String name = item.getName();
                String idAndName = name + " [" + id + "]";
                names.add(idAndName);
                userMap.put(idAndName, id);
            } 
            Dropdown.setItems(names);
        });
        Dropdown.setOnAction(event -> {
            selectedUserName = Dropdown.getValue();
            selectedUserId = userMap.get(selectedUserName);
        });
    }

    @Override
    public void update() {
        MessageField.setText("");
        users.set(FXCollections.observableArrayList(SendMessage.getUsers(x -> true)));
        Message = "";
        Dropdown.valueProperty().set(null);
    }
}
