package cookbook_app.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.mariadb.jdbc.Connection;
import cookbook_app.App;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Tag {
    SimpleIntegerProperty idProp;
    SimpleStringProperty keywordProp;
    SimpleStringProperty userProp;

    public Tag(int id, String keyword, String user) {
        idProp = new SimpleIntegerProperty(id);
        keywordProp = new SimpleStringProperty(keyword);
        userProp = new SimpleStringProperty(user);
    }

    public boolean commit() {
        boolean res = true;
        if (getTag(x -> x.getKeyword().equals(getKeyword()) && (x.getUser() == getUser() || x.getUser().equals(getUser()))) == null) {
            try {
                Connection c = App.getConnection();
                String query = " insert into tag (tag_keyword, tag_user)"
                        + " values (?, ?)";

                PreparedStatement preparedStmt = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                preparedStmt.setString(1, getKeyword());
                if (getUser() == null) {
                    preparedStmt.setNull(2, java.sql.Types.VARCHAR);
                } else {
                    preparedStmt.setString(2, getUser());
                }

                preparedStmt.executeUpdate();
                ResultSet rs = preparedStmt.getGeneratedKeys();
                rs.next();
                // tag id from the db
                idProp = new SimpleIntegerProperty(rs.getInt(1));

                c.close();
            } catch (SQLException e) {
                System.out.println("SQLException in Tag.commit method! " + e.getMessage());
                res = false;
            }
        } else {
            System.out.println("Tag already exists");
            res = false;
        }
        return res;
    }

    public void delete() {
        try {
            Connection c = App.getConnection();

            String query = "delete from tag where tag_id = ?";
            PreparedStatement preparedStmt = c.prepareStatement(query);
            preparedStmt.setInt(1, getId());

            preparedStmt.executeUpdate();

            c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in Tag.delete method! " + e.getMessage());
        }

    }

    public static List<Tag> getUserTags(String keywordSub, User user) {
        if (keywordSub == null) keywordSub = "";
        List<Tag> tags = new ArrayList<Tag>();
        try {
            Connection c = App.getConnection();
            Boolean privileged = user != null ? user.getPrivileged() : false;
            String uid = user != null ? user.getUserId() : "";
            PreparedStatement ps = c.prepareStatement("SELECT * FROM tag WHERE tag_keyword like ? and (tag_user is null or ? or tag_user = ?)"); 
            ps.setString(1, "%" + keywordSub + "%");
            ps.setBoolean(2, privileged);
            ps.setString(3, uid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Tag tag = new Tag(rs.getInt("tag_id"), rs.getString("tag_keyword"), rs.getString("tag_user"));
                tags.add(tag);
            }
            c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in Tag.getUserTags method!! " + e.getMessage());
        }

        return tags;
    }

    public static List<Tag> getTags(Predicate<Tag> condition) {
        List<Tag> tags = new ArrayList<Tag>();
        try {
            Connection c = App.getConnection();
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM tag");
            while (rs.next()) {
                String uid = rs.getString("tag_user");
                Tag tag = new Tag(rs.getInt("tag_id"), rs.getString("tag_keyword"), uid);
                if (condition.test(tag)) {
                    tags.add(tag);
                }
            }
            c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in Tag.getTags method!! " + e.getMessage());
        }

        return tags;
    }

    public static Tag getTag(Predicate<Tag> condition) {
        List<Tag> tags = getTags(condition);
        return tags.size() > 0 ? tags.get(0) : null;
    }

    public static List<String> getTagsKeywords() {
        List<Tag> tags = Tag.getUserTags(null, App.signedUser.get());
        List<String> res = new ArrayList<>();
        for (Tag tag : tags) {
            res.add(tag.getKeyword());
        }
        return res;
    }

    public int getId() {
        return idProp.get();
    }

    public String getKeyword() {
        return keywordProp.get();
    }

    public String getUser() {
        return userProp.get();
    }

    public boolean matches(String sub) {
        return getKeyword().contains(sub);
    }
}
