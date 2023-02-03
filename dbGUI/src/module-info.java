module dbGUI {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires java.base;
    requires java.sql;
    requires mysql.connector.java;
    opens company;
    opens controllers;
    opens dataAccess;
    opens helper;
    opens models;
    opens views;
    opens bundles;
    opens exceptions;
    opens merlin;
    opens numericNexus;
    opens theAgency;
}