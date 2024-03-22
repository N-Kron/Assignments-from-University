package cookbook_app.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cookbook_app.App;
import javafx.beans.property.SimpleStringProperty;

public class Article {
    SimpleStringProperty nameProp;
    SimpleStringProperty textProp;

    public Article(String name, String text) {
        nameProp = new SimpleStringProperty(name);
        textProp = new SimpleStringProperty(text);
    }

    public boolean commit() {
        boolean res = true;
        try {
            Connection c = App.getConnection();
            // the mysql insert statement
            String query = " insert into help_article values (?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = c.prepareStatement(query);
            preparedStmt.setString (1, getName());
            preparedStmt.setString(2, getText());

            // execute the preparedstatement
            preparedStmt.execute();
            c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in Article.commit method! " + e.getMessage());
            res = false;
        }
        return res;
    }

    public static List<Article> getArticles (String subString) {
        if (subString == null) {
            subString = "";
        }
        List<Article> articles = new ArrayList<Article>();
        try {
            Connection c = App.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM help_article WHERE article_name LIKE ?");
            ps.setString (1, "%" + subString + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Article article = new Article(rs.getString("article_name"), rs.getString("article_text"));
                articles.add(article);
            }
        c.close();
        } catch (SQLException e) {
            System.out.println("SQLException in Article.getArticles method!! " + e.getMessage());
        }
        return articles;
    }

    // public static Article getArticle(Predicate<Tag> condition) {
    //     List<Tag> tags = getTags(condition);
    //     return tags.size() > 0 ? tags.get(0) : null;
    // }
    
    public String getName() {
        return nameProp.get();
    }

    public String getText() {
        return textProp.get();
    }
}
