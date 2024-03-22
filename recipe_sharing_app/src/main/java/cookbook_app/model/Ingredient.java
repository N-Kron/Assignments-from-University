package cookbook_app.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cookbook_app.App;
import javafx.beans.property.SimpleStringProperty;

public class Ingredient {
    SimpleStringProperty nameProp;
    SimpleStringProperty unitProp;

    public String getName() {
        return nameProp.get();
    }

    public String getUnit() {
        return unitProp.get();
    }

    public Ingredient(String name, String unit) {
        nameProp = new SimpleStringProperty(name);
        unitProp = new SimpleStringProperty(unit);
    }

    public static List<Ingredient> getIngredients(String nameSubString) {
        if (nameSubString == null) {
            nameSubString = "";
        }
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        try {
            Connection c = App.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM ingredient WHERE ingr_name LIKE ?");
            ps.setString(1, "%" + nameSubString + "%");
            ResultSet rs = ps.executeQuery(); 

            while (rs.next()) {
                Ingredient ingr = new Ingredient(rs.getString("ingr_name"), rs.getString("ingr_unit"));
                ingredients.add(ingr);
            }
            c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in Ingredient.getIngredients method!! " + e.getMessage());
        }
        
        return ingredients;
    }
    public static Ingredient getIngredient(String name) {
        Ingredient ingr = null;
        try {
            Connection c = App.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM ingredient WHERE ingr_name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery(); 
            if (rs.next()) {
                ingr = new Ingredient(rs.getString("ingr_name"), rs.getString("ingr_unit"));
            }
            c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in Ingredient.getIngredients method!! " + e.getMessage());
        }
        return ingr;
    }
    public static List<String> getIngredientNames() {
        List<Ingredient> ingrs = Ingredient.getIngredients(null);
        List<String> res = new ArrayList<>();
        for (Ingredient ingr : ingrs) {
            res.add(ingr.getName());
        }
        return res;
    }

    public static String getUnitName(String ingredientName) {
        Ingredient ingredient = getIngredient(ingredientName);
        return ingredient.getUnit();
    }
}
