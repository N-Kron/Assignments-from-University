package cookbook_app.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mariadb.jdbc.Connection;

import cookbook_app.App;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {
    private SimpleStringProperty idProp;
    private SimpleStringProperty nameProp;
    private SimpleStringProperty pwdProp;
    private SimpleBooleanProperty privilegedProp;

    public User (String id, String name, String pwd, boolean privileged) {
        idProp = new SimpleStringProperty(id);
        nameProp = new SimpleStringProperty(name);
        pwdProp = new SimpleStringProperty(pwd);
        privilegedProp = new SimpleBooleanProperty(privileged);
    }
    public boolean commit() {
        boolean res = true;
        try {
            Connection c = App.getConnection();
            // the mysql insert statement
            String query = " insert into users (user_id, user_name, user_pwd, user_privileged)"
            + " values (?, ?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = c.prepareStatement(query);
            preparedStmt.setString (1, getUserId());
            preparedStmt.setString (2, getName());
            preparedStmt.setString (3, getPwd());
            preparedStmt.setBoolean(4, getPrivileged());

            // execute the preparedstatement
            preparedStmt.execute();
            c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in User.commit method! " + e.getMessage());
            res = false;
        }
        return res;
    }
    public void update() {
        User user = getExactUser(getUserId(), null);
        if (user != null) {
            try {
                Connection c = App.getConnection();

                String query = "update users set user_name = ?, user_pwd = ?, user_privileged = ? where user_id = ?";
                PreparedStatement preparedStmt = c.prepareStatement(query);
                preparedStmt.setString (1, getName());
                preparedStmt.setString (2, getPwd());
                preparedStmt.setBoolean(3, getPrivileged());
                preparedStmt.setString (4, getUserId());

                // execute the java preparedstatement
                preparedStmt.executeUpdate();
                
                c.close();
            } catch (SQLException e) {
                System.out.println("SQLException in User.update method! " + e.getMessage());
            }
        }
        
    }
    public void delete() {
        User user = getExactUser(getUserId(), null);
        if (user != null) {
            try {
                Connection c = App.getConnection();

                String query = "delete from users where user_id = ?";
                PreparedStatement preparedStmt = c.prepareStatement(query);
                preparedStmt.setString (1, getUserId());

                // execute the java preparedstatement
                preparedStmt.executeUpdate();
                
                c.close();
            } catch (SQLException e) {
                System.out.println("SQLException in User.delete method! " + e.getMessage());
            }
        }
        
    }
    public static List<User> getAllUsers() {
        return searchAllUsers(null, null, null);
    }
    public static List<User> searchAllUsers(String userid, String pwd, String nameSubstr) {
        List<User> users = new ArrayList<User>();
        try {
            Connection c = App.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE " + (userid != null ? "user_id like ? and " : "") + (pwd != null ? "user_pwd = ? and ": "") + (nameSubstr != null ? "user_name like ? and " : "") + "true");
            int idx = 1;
            if (userid != null) {
                ps.setString(idx, "%" + userid+ "%");
                idx += 1;
            }
            if (pwd != null) {
                ps.setString(idx, pwd);
                idx += 1;
            }
            if (nameSubstr != null) {
                ps.setString(idx, "%" + nameSubstr + "%");
                idx += 1;
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getString("user_id"), rs.getString("user_name"), rs.getString("user_pwd"), rs.getBoolean("user_privileged"));
                users.add(user);
            }
        c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in User.getUsers method!! " + e.getMessage());
        }
        
        return users;
    }
    
    public static List<User> searchAllUsers(String useridOrNameSubstr) {
        List<User> users = new ArrayList<User>();
        try {
            Connection c = App.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE user_id like ? or user_name like ?");
            ps.setString(1, "%" + useridOrNameSubstr + "%");
            ps.setString(2, "%" + useridOrNameSubstr + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getString("user_id"), rs.getString("user_name"), rs.getString("user_pwd"), rs.getBoolean("user_privileged"));
                users.add(user);
            }
        c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in User.getUsers method!! " + e.getMessage());
        }
        
        return users;
    }
    
    public static User getExactUser(String userid, String pwd) {
        User user = null;
        try {
            Connection c = App.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE user_id = ? " + (pwd != null ? " and user_pwd = ? " : ""));
            ps.setString(1, userid);
            ps.setString(2, pwd);
            ResultSet rs = ps.executeQuery();
            rs.next(); 
            user = new User(rs.getString("user_id"), rs.getString("user_name"), rs.getString("user_pwd"), rs.getBoolean("user_privileged"));

        c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in User.getExactUser method!! " + e.getMessage());
        }
        return user;
    }
    public static String hash(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
 
            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserId() {
        return idProp.get();
    }


    public String getName() {
        return nameProp.get();
    }

    public void setName(String name) {
        this.nameProp.set(name);
        update();
    }

    public String getPwd() {
        return pwdProp.get();
    }

    public void setPwd(String pwd) {
        this.pwdProp.set(pwd);
        update();
    }
    public Boolean getPrivileged() {
        return privilegedProp.get();
    }

    public void setPrivileged(Boolean privilege) {
        this.privilegedProp.set(privilege);
        update();
    }
    
    public SimpleBooleanProperty isPrivileged() {
        return privilegedProp;
    }

}
