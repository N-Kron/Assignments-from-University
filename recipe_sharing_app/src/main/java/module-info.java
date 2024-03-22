module cookbook_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mariadb.jdbc;

    opens cookbook_app.controller to javafx.fxml, javafx.graphics;
    opens cookbook_app to javafx.fxml, javafx.graphics;
    opens cookbook_app.model to javafx.base;
    exports cookbook_app;
}
