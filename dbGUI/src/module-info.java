module dbGUI {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires java.sql;
    opens company;
    opens controllers;
}