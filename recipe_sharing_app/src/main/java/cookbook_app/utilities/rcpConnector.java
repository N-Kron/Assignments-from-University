package cookbook_app.utilities;

import org.mariadb.jdbc.Connection;

import cookbook_app.App;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class rcpConnector {
  
  public String getTitle(String id) { // recipe ID
    String query = "SELECT recipe_name FROM recipe WHERE recipe_id = ?";
    String title = getString(query, id);
    return title;
  }


  public String getSubTitle(String id) {
    String query = "SELECT short_description FROM recipe WHERE recipe_id = ?";
    String subTitle = getString(query, id);
    return subTitle;
  }


  public String getInstructions(String id) {
    String query = "SELECT description FROM recipe WHERE recipe_id = ?";
    String instructions = getString(query, id);
    return instructions;
  }


  public String[] getIngredients(String id) {
    //String query = "SELECT content_ingr FROM content WHERE content_recipe = ?";
    //String[] ingr = getStrings(query, id);
    String query = "SELECT content_value, ingr_unit, content_ingr FROM content INNER JOIN ingredient ON content_ingr = ingr_name WHERE content_recipe = ?";
    ArrayList<String> arstrings = new ArrayList<String>(10);
    try {
      Connection c = App.getConnection();
      
      PreparedStatement preparedStmt = c.prepareStatement(query);
      preparedStmt.setString (1, id);

      // execute the preparedstatement
      ResultSet rs = preparedStmt.executeQuery();
      while (rs.next()) {
        arstrings.add((rs.getString(1)) + " " + rs.getString(2) + " " + rs.getString(3));
      }
      String[] strings = new String[arstrings.size()];
      for (int i = 0; i < arstrings.size(); i++) {
        strings[i] = arstrings.get(i);
      }
      c.close();
      return strings;
    } catch (SQLException e) {
      System.out.println("SQLException in getString method! " + e.getMessage());
    }
    return null;
  }


  public String[] getTags(String id) {
    String query = "SELECT applied_tag FROM tagging WHERE tagged_recipe = ?";
    String[] tag_ids = getStrings(query, id);
    String[] tags = new String[tag_ids.length];
    query = "SELECT tag_keyword FROM tag WHERE tag_id = ?";
    for (int i = 0; i < tags.length; i++) {
      tags[i] = getString(query, tag_ids[i]);
    }
    return tags;
  }


  public String userCreated(String tag) {
    String query = "SELECT tag_user FROM tag WHERE tag_keyword = ?";
    String user = getString(query, tag);
    return user;
  }

  public boolean adminCreated(String tag) {
    String query = "SELECT tag_user FROM tag WHERE tag_keyword = ?";
    String user = getString(query, tag);
    if (user == null) {
      return true;
    }
    return false;
  }


  public void addTag(String id, String tag) {
    String query = "INSERT INTO tag (tag_keyword, tag_user) VALUES (?, ?)";
    String userID = App.signedUser.get().getUserId();
    setStrings(query, tag, userID);

    query = "SELECT tag_id FROM tag WHERE tag_keyword = ?";
    String tag_id = getString(query, tag);

    query = "INSERT INTO tagging (tagged_recipe, applied_tag) VALUES (?, ?)";
    setStrings(query, id, tag_id);
  }


  public void delTag(String id, String tag) {
    String query = "DELETE FROM tagging WHERE tagged_recipe = ? AND applied_tag = ?";
    setStrings(query, id, tag);
  }


  public void setStrings(String query, String one, String two) {

    try {
      Connection c = App.getConnection();

      PreparedStatement preparedStmt = c.prepareStatement(query);
      preparedStmt.setString (1, one);
      preparedStmt.setString(2, two);

      // execute the preparedstatement
      preparedStmt.executeQuery();
      c.close();
    } catch (SQLException e) {
      System.out.println("SQLException in getString method! " + e.getMessage());
    }

  }

  private String getString(String query, String id) {
    String string = null;
    try {
      Connection c = App.getConnection();

      PreparedStatement preparedStmt = c.prepareStatement(query);
      preparedStmt.setString (1, id);

      // execute the preparedstatement
      ResultSet rs = preparedStmt.executeQuery();
      while(rs.next()) {
        string = rs.getString(1);
      }

      c.close();
    } catch (SQLException e) {
      System.out.println("SQLException in getString method! " + e.getMessage());
    }
    return string;
  }

  private String[] getStrings(String query, String id) {
    ArrayList<String> arstrings = new ArrayList<String>(10);
    try {
      Connection c = App.getConnection();

      PreparedStatement preparedStmt = c.prepareStatement(query);
      preparedStmt.setString (1, id);

      // execute the preparedstatement
      ResultSet rs = preparedStmt.executeQuery();
      while (rs.next()) {
        arstrings.add(rs.getString(1));
      }
      String[] strings = new String[arstrings.size()];
      for (int i = 0; i < strings.length; i++) {
        strings[i] = arstrings.get(i);
      }
      c.close();
      return strings;
    } catch (SQLException e) {
      System.out.println("SQLException in getString method! " + e.getMessage());
    }
    return null;
  }
}
