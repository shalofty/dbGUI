package controllers;

import DataAccess.AppointmentAccess;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Appointments;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public final class ViewAppointmentsController implements Initializable {
    @FXML public TextField appidField, titleField, conidField, useridField, locationField, cusidField, typeField;
    @FXML public TextArea detailsTextArea;
    @FXML public DatePicker startDatePicker, endDatePicker;
    @FXML private TableColumn<Appointments, Integer> appIDColumn;
    @FXML private TableColumn<Appointments, String> titleColumn, descriptionColumn, locationColumn, typeColumn, customerIDColumn, userIDColumn, contactIDColumn;
    @FXML private TableColumn<Appointments, Date> startColumn, endColumn;
    @FXML public TableView<Appointments> viewAppointments;
    @FXML public Button exitButton, addButton, modifyButton, deleteButton, viewMonthlyButton, viewWeeklyButton, viewAllButton;
    @FXML public ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList(AppointmentAccess.getAllAppointments());
    Stage stage;

    /**
     * @param event triggered when add button is pressed
     * */
    @FXML public void addAppointment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ViewAppointmentsController.class.getResource("/views/addAppointment.fxml"));
            Parent root = loader.load();
            stage = (Stage) addButton.getScene().getWindow();
            Scene scene = new Scene(root, 900, 760);
            stage.setResizable(false);
            stage.setTitle("Schedulizer | Stephan Haloftis | shaloft@wgu.edu");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }

    /**
     * @param event triggered when modify button is pressed
     * loads the main menu
     * */
    @FXML public void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ViewAppointmentsController.class.getResource("/views/mainMenu.fxml"));
            Parent root = loader.load();
            stage = (Stage) exitButton.getScene().getWindow();
            Scene scene = new Scene(root, 630, 415);
            stage.setResizable(false);
            stage.setTitle("Schedulizer | Stephan Haloftis | shaloft@wgu.edu");
            stage.setScene(scene);
    }
        catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There was an error returning to the previous menu.");
            alert.setContentText("Error: " + e.getMessage() + "Cause: " + e.getCause());
            alert.showAndWait();
        }
    }

    /**
     * initializes the view appointments scene
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set up the columns in the table, must match the names of the variables in the model
//        appIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
//        titleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
//        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
//        locationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
//        typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
//        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
//        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
//        contactIDColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
//        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
//        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
//
//        viewAppointments.setItems(appointmentsList);
//        System.out.println(viewAppointments.getItems()); // testing output to console
    }
}
