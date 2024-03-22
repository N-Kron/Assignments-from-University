package cookbook_app.model;

import org.mariadb.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import cookbook_app.App;

public class Week {
    private String user;
    private String week;
    private int day;
    private int portion;
    private String recipe;
    private int recipeId;

    public Week (String theUser, String theWeek, int theDay, int thePortion, String rec, int recipeId) {
        setUser(theUser);
        setWeek(theWeek);
        setDay(theDay);
        setPortion(thePortion);
        setRecipe(rec);
        setRecipeId(recipeId);
    }

    public static ArrayList<Week> fetch() {
        User user = App.signedUser.get();
        ArrayList<Week> weeks = new ArrayList<>();
        try {
            Connection con = App.getConnection();
            String query = "select dinner_user, dinner_week, dinner_day, dinner_portion, recipe_name, recipe_id from dinner join recipe on recipe_id = dinner_recipe where dinner_user = (?);";
            PreparedStatement pState = con.prepareStatement(query);
            pState.setString(1, user.getUserId());
            ResultSet resSet = pState.executeQuery();

            while (resSet.next()) {
                Week week = new Week(resSet.getString("dinner_user"),
                                     resSet.getString("dinner_week"),
                                     resSet.getInt("dinner_day"),
                                     resSet.getInt("dinner_portion"),
                                     resSet.getString("recipe_name"),
                                     resSet.getInt("recipe_id"));
                weeks.add(week);
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Query in Week.fetch() failed " + e.getMessage());
        }
        return weeks;
    }

    public static ArrayList<String> fetchWeekNames() {
        ArrayList<String> names = new ArrayList<>();
        try {
            Connection con = App.getConnection();
            String query = "select weeks_name from weeks where weeks_user = (?)";
            PreparedStatement pstatement = con.prepareStatement(query);
            pstatement.setString(1, App.signedUser.get().getUserId());
            ResultSet set = pstatement.executeQuery();

            while (set.next()) {
                String name = set.getString("weeks_name");
                names.add(name);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Query in Week.fetchWeekNames() failed " + e.getMessage());
        }
        return names;
    }

    public String getUser() {
        return user;
    }

    public String getWeek() {
        return week;
    }

    public int getDay() {
        return day;
    }

    public int getPortion() {
        return portion;
    }

    public String getRecipe() {
        return recipe;
    }

    public int getRecipeId() {
        return recipeId;
    }

    private void setUser(String user) {
        this.user = user;
    }

    private void setWeek(String week) {
        this.week = week;
    }

    private void setDay(int day) {
        this.day = day;
    }

    private void setPortion(int thePortion) {
        this.portion = thePortion;
    }

    private void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    private void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
