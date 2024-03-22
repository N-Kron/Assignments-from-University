package cookbook_app.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cookbook_app.App;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Comment {
  // comment_user 	varchar(32) not null,
	// comment_recipe	integer not null,
  //   comment_id		integer auto_increment,
  //   comment_text	varchar(512) not null,
  //   comment_shared	boolean not null, 
  private SimpleStringProperty userProp;
  private SimpleIntegerProperty recipeProp; // this is the recipe ID
  private SimpleStringProperty keywordProp; // this is the comment text
  private SimpleIntegerProperty idProp;
  private boolean shared;
  
  public Comment(String user, String text, int id, int recipe, boolean shared) {
    this.userProp = new SimpleStringProperty(user);
    this.recipeProp = new SimpleIntegerProperty(recipe);
    this.keywordProp = new SimpleStringProperty(text);
    this.idProp = new SimpleIntegerProperty(id);
    this.shared = shared;
  }


  public boolean commit() {
    boolean res = true;
      try {
          Connection c = App.getConnection();
          // the mysql insert statement
          String query = " insert into comments (comment_user, comment_recipe, comment_text, comment_shared)"
          + " values (?, ?, ?, ?)";
          // create the mysql insert preparedstatement
          PreparedStatement preparedStmt = c.prepareStatement(query);
          preparedStmt.setString (1, getUser());
          preparedStmt.setInt(2, getRecipe());
          preparedStmt.setString(3, getKeyword());
          preparedStmt.setBoolean(4, isShared());
          // execute the preparedstatement
          preparedStmt.execute();
          // i need the real comment id
          query = "select max(comment_id) from comments ";
          PreparedStatement idStmt = c.prepareStatement(query);
          ResultSet rs = idStmt.executeQuery();
          rs.next();
          this.idProp = new SimpleIntegerProperty(rs.getInt(1));
          
          c.close();
      } catch (SQLException e) {
          System.out.println("SQLException in Comment.commit method! " + e.getMessage());
          res = false;
      }
    return res;
  }


  public boolean update(String text) {
    boolean res = true;
    try {
      Connection c = App.getConnection();
      // the mysql insert statement
      String query = "update comments set comment_text = ? where comment_id = ?";
      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = c.prepareStatement(query);
      preparedStmt.setString (1, text);
      preparedStmt.setInt(2, getId());
      // execute the preparedstatement
      preparedStmt.execute();
      c.close();
    } catch (SQLException e) {
      System.out.println("SQLException in Comment.update method! " + e.getMessage());
      res = false;
    }
    return res;
  }


  public boolean detele() {
    boolean res = true;
    try {
      Connection c = App.getConnection();
      // the mysql insert statement
      String query = "delete from comments where comment_id = ?";
      // create the mysql insert preparedstatement
      PreparedStatement preparedStmt = c.prepareStatement(query);
      preparedStmt.setInt(1, getId());
      // execute the preparedstatement
      preparedStmt.execute();
      c.close();
    } catch (SQLException e) {
      System.out.println("SQLException in Comment.delete method! " + e.getMessage());
      res = false;
    }
    return res;
  }


  public String getUser() {
    return userProp.get();
  }

  public int getRecipe() {
    return recipeProp.get();
  }

  public String getKeyword() {
    return keywordProp.get();
  }

  public int getId() {
    return idProp.get();
  }

  public boolean isShared() {
    return shared;
  }

}
