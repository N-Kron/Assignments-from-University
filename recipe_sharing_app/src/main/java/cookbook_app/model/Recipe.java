package cookbook_app.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mariadb.jdbc.Statement;

import cookbook_app.App;
import javafx.beans.property.SimpleStringProperty;

public class Recipe {

  private SimpleStringProperty shortDescProp;
  private SimpleStringProperty keywordProp;
  private boolean favorite = false;
  private int basePortion;
  String instructions;
  List<Content> contents = new ArrayList<>();
  List<Comment> comments = new ArrayList<>();
  private int recipeID;
  private List<Tag> tags = new ArrayList<>();

  public Recipe(String name, String shortDesc, String descr, int portion) {
    keywordProp = new SimpleStringProperty(name);
    this.shortDescProp = new SimpleStringProperty(shortDesc);
    this.basePortion = portion;
    this.instructions = descr;
  }

  public Recipe(String name, int recipeID, String shortDesc, String descr, int portion, boolean favorite) {
    keywordProp = new SimpleStringProperty(name);
    this.recipeID = recipeID;
    this.shortDescProp = new SimpleStringProperty(shortDesc);
    this.basePortion = portion;
    this.instructions = descr;
    this.favorite = favorite;
  }

  public void upsert() {
    try {
      Connection c = App.getConnection();
      boolean newRecipe = false;
      if (getRecipeID() == 0) newRecipe = true;
      String query = "";
      if (newRecipe) {
      query = """
              INSERT INTO recipe (recipe_name, short_description, description, base_portion)
              VALUES (?, ?, ?, ?);
              """;
      } else {
      query = """
              UPDATE recipe SET recipe_name = ?, short_description = ?, description = ?, base_portion = ? WHERE recipe_id = ?
              """;
      }
      
      PreparedStatement ps = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, getKeyword());
      ps.setString(2, getShortDesc());
      ps.setString(3, getInstructions());
      ps.setInt(4, getBasePortion());
      if (!newRecipe) ps.setInt(5,getRecipeID());
      ps.executeUpdate();
      ResultSet keys = ps.getGeneratedKeys();    
      keys.next();  
      recipeID = keys.getInt(1);

      String tagsListStringified = "(0)";
      List<Object> params = new ArrayList<>();
      // query = "DELETE from tagging where tagged_recipe = ? and applied_tag not in " + tagsListStringified + ";";
      // ps = c.prepareStatement(query);
      // ps.setInt(1, getRecipeID());
      // ps.execute();
      // if (!tags.isEmpty()) { query = " INSERT IGNORE INTO tagging (tagged_recipe, applied_tag) Values ";
      //   tagsListStringified = tags.stream()
      //     .map(n -> String.valueOf(n.getKeyword()))
      //     .collect(Collectors.joining("','", "('", "')"));
      //   for (int i = 0; i < tags.size(); i++) {
      //     query += "(?, ?)";
      //     params.add(getRecipeID());
      //     params.add(tags.get(i).getId());
      //     if (i + 1 != tags.size()) query += ",";
      //   }
      //   query += ";";
      //   ps = c.prepareStatement(query);
      //   for (int i =0; i < params.size(); i++) {
      //     ps.setInt(i + 1, (int)params.get(i));
      //   }
      //   ps.execute();
      // }
      String contentListStringified = "(\"\")";
      if (!contents.isEmpty()) {
        contentListStringified = contents.stream()
            .map(n -> n.getIngrPrimaryKey())
            .collect(Collectors.joining("','", "('", "')"));
        query = " INSERT IGNORE INTO ingredient (ingr_name, ingr_unit) Values ";
        params.clear();
        for (int i = 0; i < contents.size(); i++) {
          query += "(?, ?)";
          params.add(contents.get(i).getIngrPrimaryKey());
          params.add(contents.get(i).getIngr().getUnit());
          if (i + 1 != contents.size()) query += ",";
        }
        query += ";";
        ps = c.prepareStatement(query);
        for (int i =0; i < params.size(); i+=2) {
          ps.setString(i + 1, (String)params.get(i));
          ps.setString(i + 2, (String)params.get(i+1));
        }
        ps.execute();
      }
      query = "DELETE from content where content_recipe = ? and content_ingr not in " + contentListStringified + ";";
      ps = c.prepareStatement(query);
      ps.setInt(1, getRecipeID());
      ps.execute();
      
      if (!contents.isEmpty()) {
        query = " INSERT IGNORE INTO content (content_recipe, content_ingr, content_value) Values ";
        params.clear();
        for (int i = 0; i < contents.size(); i++) {
          query += "(?, ?, ?)";
          params.add(getRecipeID());
          params.add(contents.get(i).getIngrPrimaryKey());
          params.add(contents.get(i).getValue());
          if (i + 1 != contents.size()) query += ",";
        }
        query += ";";
        ps = c.prepareStatement(query);
        for (int i =0; i < params.size(); i+=3) {
          ps.setInt(i + 1, (int)params.get(i));
          ps.setString(i + 2, (String)params.get(i+1));
          ps.setDouble(i + 3, (double)params.get(i+2));
        }
        ps.execute();
      }
      c.close();
    } catch (SQLException e) {
      System.out.println("SQLException in Recipe.upsert method!! " + e.getMessage());
    }
  }
  public static List<Recipe> getRecipesBase(String fulltext, List<Tag> tags, List<Ingredient> contents) {
    List<Recipe> recipes = new ArrayList<Recipe>();
    try {
      boolean tagSearch = false;
      String tagsListStringified = "";
      String contentsListStringified = "";

      if (tags != null && !tags.isEmpty()) {
        tagSearch = true;
        tagsListStringified = tags.stream()
            .map(n -> String.valueOf(n.getKeyword()))
            .collect(Collectors.joining("','", "('", "')"));
      }
      boolean ingrSearch = false;
      if (contents != null && !contents.isEmpty()) {
        ingrSearch = true;
        contentsListStringified = contents.stream()
            .map(n -> String.valueOf(n.getName()))
            .collect(Collectors.joining("','", "('", "')"));
      }
      User user = App.signedUser.get();
      Connection c = App.getConnection();
      String query = "SELECT r.recipe_name, r.recipe_id, r.short_description, r.base_portion, r.description "
          + (user != null ? ", f.target_user " : "") + " FROM recipe as r ";
      if (tagSearch) {
        query += """
            left join tagging as ting
            on r.recipe_id = ting.tagged_recipe

            left join tag as t
            on ting.applied_tag = t.tag_id
            """;
      }

      if (ingrSearch) {
        query += """
            left join content as c
            on r.recipe_id = c.content_recipe

            left join ingredient as i
            on c.content_ingr = i.ingr_name
            """;
      }
      if (user != null) {
        query += """
            left join favorite as f
            on r.recipe_id = f.favorite_recipe
            """;
      }

      query += " where true ";
      if (fulltext != null && !fulltext.isEmpty()) {
        query += " and r.recipe_name like ? ";
      }
      if (tagSearch) {
        query += " and t.tag_keyword in" + tagsListStringified;
      }
      if (ingrSearch) {
        query += " and i.ingr_name in" + contentsListStringified;
      }
      if (user != null) {
        query += " and (f.target_user = ? or f.target_user is null) ";

      }

      query += " group by r.recipe_id ";
      if (user != null) {
        query += " order by f.target_user desc";
      }

      PreparedStatement ps = c.prepareStatement(query);
      int idx = 1;
      if (fulltext != null && !fulltext.isEmpty()) {
        ps.setString(idx, "%" + fulltext + "%");
        idx++;
      }
      if (user != null) {
        ps.setString(idx, user.getUserId());
        idx++;
      }
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        boolean isFavorite = user != null ? rs.getString("target_user") != null : false;
        Recipe recipe = new Recipe(rs.getString("recipe_name"), rs.getInt("recipe_id"),
            rs.getString("short_description"), rs.getString("description"), rs.getInt("base_portion"), isFavorite);
        recipes.add(recipe);
      }
      c.close();
    } catch (SQLException e) {
      System.out.println("SQLException in Recipe.getRecipesBase method!! " + e.getMessage());
    }
    return recipes;
  }

  public static Recipe getRecipe(int id) {
    Recipe recipe = null;
    try {
      Connection c = App.getConnection();
      PreparedStatement ps = c.prepareStatement("SELECT recipe_id, recipe_name, short_description, description, base_portion FROM recipe WHERE recipe_id = ?");
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        recipe = new Recipe(rs.getString("recipe_name"),
            rs.getInt("recipe_id"),
            rs.getString("short_description"),
            rs.getString("description"),
            rs.getInt("base_portion"),
            false);
      }
      c.close();
    } catch (SQLException e) {
      System.out.println("SQLException in Recipe.getRecipe method!! " + e.getMessage());
    }
    return recipe;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public List<Comment> getComments() {
    return this.comments;
  }

  public void loadTagsInfo() {
    try {
      Connection c = App.getConnection();
      String query = "SELECT t.tag_id, t.tag_keyword, t.tag_user FROM recipe as r ";

      query += """
          left join tagging as ting
          on r.recipe_id = ting.tagged_recipe

          left join tag as t
          on ting.applied_tag = t.tag_id
          """;

      query += " where r.recipe_id = ? AND (t.tag_user is null OR t.tag_user = ?)";

      PreparedStatement ps = c.prepareStatement(query);
      ps.setInt(1, getRecipeID());
      String uid = App.signedUser.get() != null ? App.signedUser.get().getUserId() : "";
      ps.setString(2, uid);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        Tag tag = new Tag(rs.getInt("tag_id"), rs.getString("tag_keyword"), rs.getString("tag_user"));
        if (tag.getKeyword() != null) this.tags.add(tag);
      }
      c.close();
    } catch (SQLException e) {
      System.out.println("SQLException in Recipe.loadExtendedInfo method!! " + e.getMessage());
    }
  }


  public void loadIngredientsInfo() {
    try {
      Connection c = App.getConnection();
      String query = "SELECT ingr_name, ingr_unit, content_value FROM recipe as r ";

      query += """
          left join content as c
          on r.recipe_id = c.content_recipe

          left join ingredient as i
          on c.content_ingr = i.ingr_name
          """;

      query += " where r.recipe_id = ?";

      PreparedStatement ps = c.prepareStatement(query);
      ps.setInt(1, getRecipeID());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
          Content content = new Content(new Ingredient(rs.getString("ingr_name"),rs.getString("ingr_unit")), rs.getDouble("content_value"));
          this.contents.add(content);
      }
      c.close();
    } catch (SQLException e) {
      System.out.println("SQLException in Recipe.loadExtendedInfo method!! " + e.getMessage());
    }
  }

  
  public void loadCommentsInfo() {
    try {
      Connection c = App.getConnection();
      String query = "SELECT comment_user, comment_id, comment_text, comment_shared FROM comments";
      query += " where comment_recipe = ?";
          
      PreparedStatement ps = c.prepareStatement(query);
      ps.setInt(1, getRecipeID());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        Comment comm = new Comment(rs.getString("comment_user"),rs.getString("comment_text"),
                                    rs.getInt("comment_id"), getRecipeID(), rs.getBoolean("comment_shared"));
        this.comments.add(comm);
      }
    c.close();
    } catch (SQLException e) {
        System.out.println("SQLException in Recipe.loadCommentsInfo method!! " + e.getMessage());
    }
  }

  public List<Content> getContents() {
    return contents;
  }

  public String[] getContentsString() {
    String[] result = new String[contents.size()];
    int i = 0;
    for (Content content : contents) {
      result[i] = (content.getValue() + " " + content.getIngr().getUnit() + " " + content.getIngrPrimaryKey());
      i++;
    }
    return result;
  }

  public String getKeyword() {
    return keywordProp.get();
  }

  public String getShortDesc() {
    return shortDescProp.get();
  }
  
  public String getInstructions() {
    return instructions;
  }
  
  public int getRecipeID() {
    return recipeID; 
  }

  public int getBasePortion() {
    return basePortion; 
  }

  public boolean addTag(Tag tagToAdd) {
    boolean res = false;
    String query = "INSERT INTO tagging (tagged_recipe, applied_tag) VALUES (?, ?)";
    try {
      Connection c = App.getConnection();

      PreparedStatement preparedStmt = c.prepareStatement(query);
      preparedStmt.setInt(1, getRecipeID());
      preparedStmt.setInt(2, tagToAdd.getId());
      // execute the preparedstatement
      preparedStmt.executeQuery();
      c.close();
      res = true;
      this.tags.add(tagToAdd);
    } catch (SQLException e) {
      System.out.println("Cannot assign tag");
    }
    return res;
  }

  public void delTag(Tag tagToDel) {
    this.tags.remove(tagToDel);

    String query = "DELETE FROM tagging WHERE tagged_recipe = ? AND applied_tag = ?";
    try {
      Connection c = App.getConnection();

      PreparedStatement preparedStmt = c.prepareStatement(query);
      preparedStmt.setInt(1, getRecipeID());
      preparedStmt.setInt(2, tagToDel.getId());

      // execute the preparedstatement
      preparedStmt.executeQuery();
      c.close();
    } catch (SQLException e) {
      System.out.println("SQLException in delTag method! " + e.getMessage());
    }

  }

  public String getSubTitle() {
    return this.shortDescProp.get();
  }

  public void addContent(Content content) {
    this.contents.add(content);
  }

  public void addComment(Comment comment) {
    comments.add(comment);
  }

  public void replaceComment(int index, Comment comment) {
    comments.set(index, comment);
  } 

  public void delComment(int index) {
    comments.remove(index);
  }

  public void setFavorite(boolean newValue) {
    this.favorite = newValue;
  }

  public boolean getFavorite() {
    return this.favorite;
  }
}
