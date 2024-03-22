package cookbook_app.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cookbook_app.App;
import cookbook_app.Const;
import cookbook_app.SceneInfo;
import cookbook_app.model.User;
import cookbook_app.utilities.FileHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class LoginController implements Initializable, Controller {

    public static SceneInfo sceneInfo = getInfo();
    private static SceneInfo getInfo() {
        Parent p = null;
        Controller controller = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(new URL(Const.c.fxmlPath + "Login.fxml"));
            p = fxmlLoader.load();
            controller = (Controller) fxmlLoader.getController();
        } catch (IOException e) {
            System.out.println("Exception in loading " + "Login.fxml");
            e.printStackTrace(); 
        }
        var si = new SceneInfo(new Scene(p), "Cookbook - Log in or Sign up", App.smallIcon, controller);
        si.resizable = false;
        return si;
    }

    FileHelper fh = new FileHelper(Const.c.sessionFile);
    @FXML
    private TextField loginUserIdTextField;

    @FXML
    private PasswordField loginPasswordPasswordField;

    @FXML
    private CheckBox rememberMeCheckBox;

    @FXML
    private Button loginButton;
    
    @FXML
    private TextField signUpUserIdTextField;
    
    @FXML
    private TextField signUpNameTextField;

    @FXML
    private PasswordField signUpPasswordPasswordField;

    @FXML
    private PasswordField signUpRepeatPasswordPasswordField;

    @FXML
    private Button signUpButton;

    @FXML
    private void signOnClick(ActionEvent mouseEvent) {
        String id = loginUserIdTextField.getText();
        String pwd = loginPasswordPasswordField.getText();
        String pwdHash = User.hash(pwd);
        boolean error = false;
        String errorText = "";
        if (!id.isEmpty() && !pwd.isEmpty()) {
            User user = User.getExactUser(id, pwdHash);
            if (user != null) {
                App.signedUser.set(user);
                if (rememberMeCheckBox.isSelected()) {
                    fh.save(User.hash(user.getUserId() + user.getName() + user.getPwd()));
                }
                App.closeLast();
                loginUserIdTextField.clear();
                loginPasswordPasswordField.clear();
            } else {
                error = true;
                errorText = "User ID or/and password are incorrect";
            }
        } else if (id.isEmpty()) { 
            error = true;
            errorText = "User ID is empty";
        } else if (pwd.isEmpty()) {
            error = true;
            errorText = "Password is empty";
        }
        if (error) {
            App.throwError(errorText);
        }
    }

    @FXML
    private void signupOnClick(ActionEvent mouseEvent) {
        String id = signUpUserIdTextField.getText();
        String name = signUpNameTextField.getText();
        String pwd = signUpPasswordPasswordField.getText();
        String rptPwd = signUpRepeatPasswordPasswordField.getText();
        String pwdHash = User.hash(pwd);
        boolean error = false;
        String errorText = "";
        if (!id.isEmpty() && !name.isEmpty() && !pwd.isEmpty() && pwd.equals(rptPwd)) {
            if (User.getExactUser(id, null) == null) {
                User user = new User(id, name, pwdHash, false);
                user.commit();
                App.signedUser.set(user);
                App.closeLast();
                signUpUserIdTextField.clear();
                signUpNameTextField.clear();
                signUpPasswordPasswordField.clear();
                signUpRepeatPasswordPasswordField.clear();
            } else {
                error = true;
                errorText = "User already exists";
            }
        } else if (id.isEmpty()) { 
            error = true;
            errorText = "User ID is empty";
        } else if (name.isEmpty()) {
            error = true;
            errorText = "Name is empty";
        } else if (pwd.isEmpty()) {
            error = true;
            errorText = "Password is empty";
        } else if (!pwd.equals(rptPwd)) {
            error = true;
            errorText = "Passwords don't match";
        }
        if (error) {
            App.throwError(errorText);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    public void update() {
    }
    
}
