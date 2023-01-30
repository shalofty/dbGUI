package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerRecordsController implements Initializable {
    @FXML public TextField idField, nameField, phoneField, addressField, postalField;
    @FXML public Button exitButton, addButton, modifyButton, deleteButton;
    @FXML public ComboBox countryMenu, divisionMenu;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
