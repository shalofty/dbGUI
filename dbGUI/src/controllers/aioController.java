package controllers;

import DataAccess.AppointmentAccess;
import DataAccess.SQLQueries;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Appointments;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class aioController implements Initializable {

    @FXML public Tab appointmentsTab, customersTab, reportsTab, logTab;

    // Appointments Variables
    @FXML public TextField appIDField, contactIDField, userIDField, locationField, customerIDField, typeField, titleField;
    @FXML public DatePicker startDatePicker, endDatePicker;
    @FXML public TextArea descriptionTextArea;
    @FXML public TableView<Appointments> viewAppointments;
    @FXML public RadioButton viewByWeek, viewByMonth;
    @FXML public TableColumn appIDColumn, titleColumn, descriptionColumn, locationColumn, typeColumn, customerIDAppointmentsColumn, userIDColumn, contactIDColumn, startColumn, endColumn;
    @FXML public Button addAppointmentButton, modifyAppointmentButton, deleteAppointmentButton, clearSelectionButton;
    @FXML public ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList(AppointmentAccess.getAllAppointments());
    @FXML public Appointments selectedAppointment;

    // Customer Variables
    @FXML public TextField customerRecordsIDField, customerNameField, addressField, postalField, phoneField;
    @FXML public SplitMenuButton countryMenu, divisionMenu;
    @FXML public TableView viewCustomers;
    @FXML public TableColumn customerIDRecordsColumn, nameColumn, phoneColumn, addressColumn, postalColumn, divisionColumn;
    @FXML public Button addCustomerButton, modifyCustomerButton, deleteCustomerButton;


    // Appointments Tab Methods
    // viewByWeek
    // viewByMonth

    /**
     * selectAppointment selects an appointment from the tableview and populates the text fields
     * */
    @FXML public void selectAppointment() {
        try {
            selectedAppointment = viewAppointments.getSelectionModel().getSelectedItem();
            if (selectedAppointment != null) {
                appIDField.setText(String.valueOf(selectedAppointment.getAppointmentID()));
                titleField.setText(String.valueOf(selectedAppointment.getAppointmentTitle()));
                contactIDField.setText(String.valueOf(selectedAppointment.getContactID()));
                userIDField.setText(String.valueOf(selectedAppointment.getUserID()));
                locationField.setText(String.valueOf(selectedAppointment.getAppointmentLocation()));
                customerIDField.setText(String.valueOf(selectedAppointment.getCustomerID()));
                typeField.setText(String.valueOf(selectedAppointment.getAppointmentType()));
                descriptionTextArea.setText(String.valueOf(selectedAppointment.getAppointmentDescription()));
                startDatePicker.setValue(selectedAppointment.getStartTime().toLocalDate());
                endDatePicker.setValue(selectedAppointment.getEndTime().toLocalDate());
                addAppointmentButton.setDisable(true);
                modifyAppointmentButton.setDisable(false);
                deleteAppointmentButton.setDisable(false);
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error: " + e.getMessage() + "Cause: " + e.getCause());
            alert.showAndWait();
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
        }
    }

    /**
     * clearSelection clears the selected appointment and resets the buttons to their default state
     * @param event the event that triggers the method
     * */
    @FXML public void clearSelection(ActionEvent event) {
        selectedAppointment = null;
        appIDField.clear();
        titleField.clear();
        contactIDField.clear();
        userIDField.clear();
        locationField.clear();
        customerIDField.clear();
        typeField.clear();
        descriptionTextArea.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        addAppointmentButton.setDisable(false);
        modifyAppointmentButton.setDisable(true);
        deleteAppointmentButton.setDisable(true);
    }

    /**
     * generateAppointmentID generates a new appointment ID
     * @return newAppointmentID
     * */
    @FXML public int generateAppointmentID() {
        int newAppointmentID = 0;
        for (Appointments appointment : appointmentsList) {
            if (appointment.getAppointmentID() <= newAppointmentID) {
                newAppointmentID++;
            }
        } return newAppointmentID;
    }

    /**
     * addAppointment adds a new appointment to the database
     * @param event the event that triggers the method
     * @throws Exception
     * */
    @FXML public void addAppointment(ActionEvent event) throws Exception {
        // generating new appointment ID and getting the values from the text fields
        int newAppointmentID = generateAppointmentID();
        String newTitle = titleField.getText();
        String newLocation = locationField.getText();
        int newCustomerID = Integer.parseInt(customerIDField.getText());
        int newContactID = Integer.parseInt(contactIDField.getText());
        int newUserID = Integer.parseInt(userIDField.getText());
        String newType = typeField.getText();
        String newDescriptionText = descriptionTextArea.getText();
        LocalDateTime newStartDate = LocalDateTime.from(startDatePicker.getValue());
        LocalDateTime newEndDate = LocalDateTime.from(endDatePicker.getValue());
        // creating a new appointment object
        Appointments newAppointment = new Appointments(newAppointmentID, newTitle, newDescriptionText, newLocation, newType,
                newStartDate, newEndDate, newCustomerID, newUserID, newContactID);
        // adding the new appointment to the database
        SQLQueries.insertInto(newAppointmentID, newTitle, newDescriptionText, newLocation, newType,
                newStartDate, newEndDate, newCustomerID, newUserID, newContactID);
    }

    /**
     * modifyAppointment modifies an appointment in the database
     * @param event modifyAppointmentButton
     * */
    @FXML public void modifyAppointment(ActionEvent event) {
        try {
            if (selectedAppointment != null) {
                int appointmentID = selectedAppointment.getAppointmentID();
                String title = titleField.getText();
                String location = locationField.getText();
                int customerID = Integer.parseInt(customerIDField.getText());
                int contactID = Integer.parseInt(contactIDField.getText());
                int userID = Integer.parseInt(userIDField.getText());
                String type = typeField.getText();
                String descriptionText = descriptionTextArea.getText();
                LocalDateTime startDate = LocalDateTime.from(startDatePicker.getValue());
                LocalDateTime endDate = LocalDateTime.from(endDatePicker.getValue());
                SQLQueries.updateTable(appointmentID, title, descriptionText, location, type, startDate, endDate, customerID, userID, contactID);
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error: " + e.getMessage() + "Cause: " + e.getCause());
            alert.showAndWait();
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
        }
    }

    /**
     * deleteAppointment deletes an appointment from the database
     * @param event deleteAppointmentButton
     * */
    @FXML public void deleteAppointment(ActionEvent event) {
        try {
            Connection connection = JDBC.openConnection();
            int appointmentID = selectedAppointment.getAppointmentID();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Appointment");
            alert.setHeaderText("Delete Appointment");
            alert.setContentText("Are you sure you want to delete this appointment?");
            Optional<ButtonType> result;
            result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                AppointmentAccess.deleteAppointment(connection, appointmentID);
            } else {
                alert.close();
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error: " + e.getMessage() + "Cause: " + e.getCause());
            alert.showAndWait();
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
        }
    }

    // Customers Tab Methods

    // addCustomer
    @FXML public void addCustomer(ActionEvent event) {
        System.out.println("Add Customer");
    }

    // modifyCustomer
    @FXML public void modifyCustomer(ActionEvent event) {
        System.out.println("Modify Customer");
    }

    // deleteCustomer
    @FXML public void deleteCustomer(ActionEvent event) {
        System.out.println("Delete Customer");
    }

    // updateAppointments
    public void updateAppointments() {
        viewAppointments.setItems(appointmentsList);
    }

    // updateCustomers
    public void updateCustomers() {
//        viewCustomers.setItems(customersList);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO
        // set up the columns in the table, must match the names of the variables in the model
        appIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        customerIDAppointmentsColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        contactIDColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        viewAppointments.setItems(appointmentsList);
        System.out.println(viewAppointments.getItems()); // testing output to console

        // update tables
        updateAppointments();
        updateCustomers();
    }
}
