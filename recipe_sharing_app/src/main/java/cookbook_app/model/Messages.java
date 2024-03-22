package cookbook_app.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.mariadb.jdbc.Connection;

import cookbook_app.App;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Messages {
    private SimpleIntegerProperty Id; 
    private SimpleStringProperty SenderProp;
    private SimpleStringProperty RecipeProp;
    private SimpleStringProperty MessageProp;
    private SimpleBooleanProperty SeenProp;
    private SimpleObjectProperty<Recipe> recipe;
    

    public Messages (int suggestion_id, String Sender, Recipe recipeObj, String Message, boolean Seen) {
        Id = new SimpleIntegerProperty(suggestion_id);
        SenderProp = new SimpleStringProperty(Sender);
        recipe = new SimpleObjectProperty<Recipe>(recipeObj);
        RecipeProp = new SimpleStringProperty(recipeObj.getKeyword());
        MessageProp = new SimpleStringProperty(Message);
        SeenProp = new SimpleBooleanProperty(Seen);
    }
    public boolean commit() {
        boolean res = true;
        try {
            Connection c = App.getConnection();
            // the mysql insert statement
            String query = " insert into suggestion (suggestion_sender, suggestion_recipe, suggestion_message, suggestion_seen)"
            + " values (?, ?, ?, ?)";

            App.signedUser.get().getUserId();
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = c.prepareStatement(query);
            preparedStmt.setString (1, getSender());
            preparedStmt.setString (2, getRecipe());
            preparedStmt.setString (3, getMessage());
            preparedStmt.setBoolean(4, getSeen());

            // execute the preparedstatement
            preparedStmt.execute();
            c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in Messages.commit method! " + e.getMessage());
            res = false;
        }
        return res;
    }
    public void update() {
        Messages Messages = getMessages(x -> x.getMessage().equals(getMessage()));
        if (Messages != null) {
            try {
                Connection c = App.getConnection();

                String query = "update suggestion set suggestion_seen = ? where suggestion_Id = ?";
                PreparedStatement preparedStmt = c.prepareStatement(query);
                preparedStmt.setBoolean(1, getSeen());
                preparedStmt.setInt(2, getSuggestionId());
                

                // execute the java preparedstatement
                preparedStmt.executeUpdate();
                
                c.close();
            } catch (SQLException e) {
                System.out.println("SQLException in Messages.update method! " + e.getMessage());
            }
        }
        
    }



    public static List<Messages> getMessagess(Predicate<Messages> condition) {
        List<Messages> messagess = new ArrayList<>();
        try {
            Connection c = App.getConnection();
            String query = 
                           "SELECT s.suggestion_id, s.suggestion_sender, s.suggestion_message, s.suggestion_seen,"
                            + " r.recipe_name, r.recipe_id, r.short_description, r.description, r.base_portion"
                            + " FROM suggestion s"
                            + " JOIN recipe r ON s.suggestion_recipe = r.recipe_id"
                            + " WHERE s.suggestion_recipient = ?";
            PreparedStatement ps = c.prepareStatement(query);
            ps.setString(1, App.signedUser.get().getUserId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Recipe recipe = new Recipe(
                    rs.getString("recipe_name"),
                    rs.getInt("recipe_id"),
                    rs.getString("short_description"),
                    rs.getString("description"),
                    rs.getInt("base_portion"),
                    false // Assuming isFavorite is always false in this case
                );
                Messages message = new Messages(
                    rs.getInt("suggestion_id"),
                    rs.getString("suggestion_sender"),
                    recipe,
                    rs.getString("suggestion_message"),
                    rs.getBoolean("suggestion_seen")
                );
                if (condition.test(message)) {
                    messagess.add(message);
                }
            }
            c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in Messages.getMessagess method!! " + e.getMessage());
        }
        return messagess;
    }
    

    public static Messages getMessages(Predicate<Messages> condition) {
        List<Messages> Messagess = getMessagess(condition);
        return Messagess.size() > 0 ? Messagess.get(0) : null;
    }
    
    public String getMessage() {
        return MessageProp.get();
    }

    public void setMessage(String Message) {
        this.MessageProp.set(Message);
        update();
    }

    public String getRecipe() {
        return RecipeProp.get();
    }

    public void setRecipe(String Recipe) {
        this.RecipeProp.set(Recipe);
        update();
    }

    public Recipe getRecipeObj() {
        return recipe.get();
    }

    public void setSuggestionId(int suggestion_id){
        this.Id.set(suggestion_id);
        update();
    }

    public int getSuggestionId() {
        return Id.get();
    }

    public String getSender() {
        return SenderProp.get();
    }

    public void setSender(String Sender) {
        this.SenderProp.set(Sender);
        update();
    }
    public Boolean getSeen() {
        return SeenProp.get();
    }
    public void setSeen(Boolean Seen) {
        this.SeenProp.set(Seen);
        update();
    }

    public boolean matches(String sub) {
        return getMessage().contains(sub) || getRecipe().contains(sub);
    }
    
    public SimpleBooleanProperty isSeen() {
        return SeenProp;
    }

    
}
