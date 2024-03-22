package cookbook_app.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.lang.String;

import org.mariadb.jdbc.Connection;

import cookbook_app.App;
import javafx.beans.property.SimpleStringProperty;


public class SendMessage {

    private SimpleStringProperty idProp;
    private SimpleStringProperty nameProp;
    private SimpleStringProperty messageProp;

    public SendMessage(String id, String name) {
        idProp = new SimpleStringProperty(id);
        nameProp = new SimpleStringProperty(name);
    }

    public SendMessage(String id, String name, String message) {
        idProp = new SimpleStringProperty(id);
        nameProp = new SimpleStringProperty(name);
        messageProp = new SimpleStringProperty(message);
    }

    public boolean commit() {
        boolean res = true;
        try {
            Connection c = App.getConnection();
            String query = " insert into suggestion (suggestion_sender, suggestion_recipient, suggestion_recipe, suggestion_message, suggestion_seen)"
                    + " values (?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = c.prepareStatement(query);
            preparedStmt.setString(1, App.signedUser.get().getUserId());
            preparedStmt.setString(2, getUserId());
            preparedStmt.setInt(3, App.recipe.getRecipeID());
            preparedStmt.setString(4, getMessage());
            preparedStmt.setBoolean(5, false);

            preparedStmt.execute();
            c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in SendMessage.commit method! " + e.getMessage());
            res = false;
        }
        return res;
    }

    public void update() {
        SendMessage user = getUser(x -> x.getUserId().equals(getUserId()));
        if (user != null) {
            try {
                Connection c = App.getConnection();

                String query = "update users set user_name = ? where user_id = ?";
                PreparedStatement preparedStmt = c.prepareStatement(query);
                preparedStmt.setString(1, getName());
                preparedStmt.setString(2, getUserId());

                preparedStmt.executeUpdate();

                c.close();
            } catch (SQLException e) {
                System.out.println("SQLException in sendMessage method! " + e.getMessage());
            }
        }
    }

    public static List<SendMessage> getUsers(Predicate<SendMessage> condition) {
        List<SendMessage> users = new ArrayList<SendMessage>();
        try {
            Connection c = App.getConnection();
            ResultSet rs = c.createStatement().executeQuery("SELECT user_name, user_id FROM users");
            while (rs.next()) {
                SendMessage user = new SendMessage(rs.getString("user_id"), rs.getString("user_name"));

                if (condition.test(user)) {
                    users.add(user);
                }
            }
            c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in sendmessage.get method!! " + e.getMessage());
        }

        return users;
    }

    public static SendMessage getUser(Predicate<SendMessage> condition) {
        List<SendMessage> users = getUsers(condition);
        return users.size() > 0 ? users.get(0) : null;
    }

    public String getUserId() {
        return idProp.get();
    }

    public void setUserId(String userId) {
        this.idProp.set(userId);
        update();
    }

    public String getName() {
        return nameProp.get();
    }

    public void setName(String name) {
        this.nameProp.set(name);
        update();
    }

    public String getMessage() {
        return messageProp.get();
    }
    
}
