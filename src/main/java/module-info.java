module application.effortloggerv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens application.effortloggerv2 to javafx.fxml;
    exports application.effortloggerv2;
}