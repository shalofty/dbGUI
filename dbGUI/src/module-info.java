module dbGUI {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires java.base;
    requires java.sql;
    requires mysql.connector.java;
    opens company;
    opens controllers;
    opens DataAccess;
    opens Helper;
    opens Models;
    opens views;
}