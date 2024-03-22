package cookbook_app.utilities;

import cookbook_app.App;
import cookbook_app.model.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Storage {
  static List<Recipe> recipes = new ArrayList<Recipe>();
  static Map<String, List<Integer>> favorites = new HashMap<>();

  // public static void queryRecipes() {
  //   System.out.println("It just pulled from DB");
  //   try {
  //     Connection c = App.getConnection();
  //     ResultSet rs = c.createStatement().executeQuery("SELECT * FROM recipe");
  //     while (rs.next()) {
  //       Recipe recipe = new Recipe(rs.getString("recipe_name"), rs.getInt("recipe_id"), rs.getString("short_description"));
  //       int recipeId = rs.getInt("recipe_id");

  //       // Get the tags for the recipe
  //       PreparedStatement tagStmt = c.prepareStatement(
  //               "SELECT tag_keyword FROM tag t JOIN tagging tg ON t.tag_id = tg.applied_tag WHERE tg.tagged_recipe = ?");
  //       tagStmt.setInt(1, recipeId);
  //       ResultSet tagRs = tagStmt.executeQuery();
  //       while (tagRs.next()) {
  //         recipe.addTag(tagRs.getString("tag_keyword"));
  //       }
  //       tagRs.close();
  //       tagStmt.close();

        // Get the tags for the recipe
  //      PreparedStatement tagStmt = c.prepareStatement(
  //              "SELECT tag_keyword FROM tag t JOIN tagging tg ON t.tag_id = tg.applied_tag WHERE tg.tagged_recipe = ?");
  //      tagStmt.setInt(1, recipeId);
  //      ResultSet tagRs = tagStmt.executeQuery();
  //      while (tagRs.next()) {
  //        recipe.addTag(tagRs.getString("tag_keyword"));
  //      }
  //      tagRs.close();
  //      tagStmt.close();

  //       // Get the ingredients for the recipe
  //       PreparedStatement ingrStmt = c.prepareStatement(
  //               "SELECT content_ingr FROM content WHERE content_recipe = ?");
  //       ingrStmt.setInt(1, recipeId);
  //       ResultSet ingrRs = ingrStmt.executeQuery();
  //       while (ingrRs.next()) {
  //         String ingredientName = ingrRs.getString("content_ingr");
  //         recipe.addIngredient(ingredientName);
  //       }
  //       ingrRs.close();
  //       ingrStmt.close();

  //       recipes.add(recipe);
  //     }
  //     c.close();
  //   } catch (SQLException e) {
  //     System.out.println("SQLException in Recipe.getRecipes method!! " + e.getMessage());
  //   }
  // }

  public static boolean doesTagExist(String tag){
    boolean result = false;
    for (Recipe recipe : recipes){
      if (recipe.getTags().contains(tag)) {
        result = true;
        break;
      }
    }
    return result;
  }

  public static boolean doesIngredientExist(String ingredient) {
    boolean result = false;
    for (Recipe recipe : recipes){
      if (recipe.getContents().contains(ingredient)) {
        result = true;
        break;
      }
    }
    return result;
  }

  public static void filterFavoriteRecipes() {
    for (Recipe recipe : recipes) { recipe.setFavorite(false); }
    String userID = App.signedUser.get().getUserId();
    if (favorites.containsKey(userID)) {
      List<Integer> favoriteRecipeIds = favorites.get(userID);
      for (Integer recipeID : favoriteRecipeIds) {
        for (Recipe recipe : recipes) {
          if (recipe.getRecipeID() == recipeID) {
            recipe.setFavorite(true);
            break;
          }
        }
      }
    }
  }

}
