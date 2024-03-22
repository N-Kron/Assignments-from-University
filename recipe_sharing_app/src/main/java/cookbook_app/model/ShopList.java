package cookbook_app.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cookbook_app.App;
import cookbook_app.controller.DinnerController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class ShopList {
    SimpleStringProperty weekProp;
    SimpleStringProperty ingrProp;
    SimpleDoubleProperty valueProp;
    SimpleStringProperty unitProp;
    SimpleStringProperty userProp;

    public ShopList (String w, String ing, Double val, String unit, String user) {
        setWeek(w);
        setIngr(ing);
        setValue(val);
        setUnit(unit);
        setUser(user);
    }

    // getting the ingredients (possibly modified) from the shop list table
    public static List<ShopList> getShopIngrs() {
        List<ShopList> ingrs = new ArrayList<ShopList>();
        try {
            Connection con = App.getConnection();
            String query = "select shop_week, shop_ingr, shop_value, ingr_unit, shop_user from shop_list"
                           + " join ingredient on ingr_name = shop_ingr where shop_week = (?) and shop_user = (?)";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.setString(1, DinnerController.theSelectedWeek);
            pStatement.setString(2, App.signedUser.get().getUserId());
            ResultSet set = pStatement.executeQuery();

            while (set.next()) {
                ShopList ing = new ShopList(set.getString("shop_week"),
                                            set.getString("shop_ingr"),
                                            set.getDouble("shop_value"),
                                            set.getString("ingr_unit"), 
                                            set.getString("shop_user"));
                ingrs.add(ing);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("Query in ShopList.getShopIngr() failed " + e.getMessage());
        }
        return ingrs;
    }

    // getting the ingredients for the shopping list from three different tables (first time)
    // plus inserting  them into the shopping list table
    public static List<ShopList> getIngrs() {
        List<ShopList> ingrs = new ArrayList<ShopList>();
        try {
            Connection con = App.getConnection();
            String query = "select dinner_week, content_ingr, sum(content_value * dinner_portion), ingr_unit, dinner_user from content"
                           + " join dinner on dinner_recipe = content_recipe join ingredient on ingr_name = content_ingr"
                           + " where dinner_week = (?) and dinner_user = (?) group by content_ingr";
            PreparedStatement pStatement = con.prepareStatement(query);
            pStatement.setString(1, DinnerController.theSelectedWeek);
            pStatement.setString(2, App.signedUser.get().getUserId());
            ResultSet set = pStatement.executeQuery();

            while (set.next()) {
                ShopList ing = new ShopList(set.getString("dinner_week"),
                                            set.getString("content_ingr"),
                                            set.getDouble("sum(content_value * dinner_portion)"),
                                            set.getString("ingr_unit"), 
                                            set.getString("dinner_user"));
                ingrs.add(ing);
            }

            query = "insert into shop_list(shop_week, shop_ingr, shop_value, shop_user) select dinner_week, content_ingr, sum(content_value * dinner_portion), dinner_user "
                    + "from content join dinner on dinner_recipe = content_recipe "
                    + "where dinner_week = (?) and dinner_user = (?) group by content_ingr";
            pStatement = con.prepareStatement(query);
            pStatement.setString(1, DinnerController.theSelectedWeek);
            pStatement.setString(2, App.signedUser.get().getUserId());
            pStatement.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println("Query in ShopList.getIngrs() failed " + e.getMessage());
        }
        return ingrs;
    }

    public void remove() {
        try {
            Connection con = App.getConnection();
            String query = "delete from shop_list where shop_week = (?) and shop_ingr = (?) and shop_user = (?)";
            PreparedStatement pState = con.prepareStatement(query);
            pState.setString(1, this.getWeek());
            pState.setString(2, this.getIngr());
            pState.setString(3, this.getUser());
            pState.executeQuery();
            
            con.close();
        } catch (Exception e) {
            System.out.println("Query in ShopList.remove() failed " + e.getMessage());
        }
    }
    
    public String getWeek() {
        return weekProp.get();
    }
    
    public String getIngr() {
        return ingrProp.get();
    }

    public Double getValue() {
        return valueProp.get();
    }

    public String getUnit() {
        return unitProp.get();
    }

    public String getUser() {
        return userProp.get();
    }

    public void setWeek(String weekProp) {
        this.weekProp = new SimpleStringProperty(weekProp);
    }

    public void setIngr(String ingrProp) {
        this.ingrProp = new SimpleStringProperty(ingrProp);
    }

    public void setValue(Double valueProp) {
        this.valueProp = new SimpleDoubleProperty(valueProp);
    }

    public void setUnit(String unitProp) {
        this.unitProp = new SimpleStringProperty(unitProp);
    }

    public void setUser(String userProp) {
        this.userProp = new SimpleStringProperty(userProp);
    }
}
