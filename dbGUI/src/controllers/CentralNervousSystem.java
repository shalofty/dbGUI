package controllers;

import DataAccess.*;
import Exceptions.ExceptionHandler;
import Exceptions.GateKeeper;
import NumericNexus.NumberGenie;
import Helper.JDBC;
import TheAgency.AgentFord;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Models.*;
import Merlin.HotTubTimeMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CentralNervousSystem implements Initializable {

    @FXML public Connection connection = null;
    @FXML public PreparedStatement preparedStatement = null;
    @FXML public Tab appointmentsTab, customersTab, reportsTab, logTab;
    @FXML public TextArea nightVisionGoggles, infraredGoggles;
    @FXML public static Button espionageButton, exportDBButton, logoutButton;

    // Appointments Variables ///////////////////////////////////////////////////////////////////////////////////////////
    @FXML public Label userCreds;
    @FXML public TextField appIDField, contactIDField, userIDField, locationField, customerIDField, typeField, titleField;
    @FXML public DatePicker datePicker;
    @FXML public ComboBox<String> contactsMenu = new ComboBox<>(); // Contacts Menu
    @FXML public ComboBox<String> customersMenu = new ComboBox<>(); // Customers Menu
    @FXML public ComboBox<String> usersMenu = new ComboBox<>(); // Users Menu
    @FXML public ComboBox<String> startHourBox = new ComboBox<>(); // Start Hour Menu
    @FXML public ComboBox<String> endHourBox = new ComboBox<>(); // End Hour Menu
    @FXML public ComboBox<String> startMinBox = new ComboBox<>(); // Start Minute Menu
    @FXML public ComboBox<String> endMinBox = new ComboBox<>(); // End Minute Menu
    @FXML public TextArea descriptionTextArea; // Description Text Area
    @FXML public TableView<Appointments> viewAppointments; // Appointments Table
    @FXML public RadioButton radioWeek, radioMonth; // Radio Buttons
    @FXML public TableColumn<?, ?> appIDColumn, titleColumn, descriptionColumn, locationColumn, typeColumn, customerIDAppointmentsColumn, userIDColumn, contactColumn, startColumn, endColumn;
    @FXML public Button addAppointmentButton, modifyAppointmentButton, deleteAppointmentButton, clearSelectionButton;
    @FXML public ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList(AppointmentAccess.allAppointments()); // Observable List of Appointments
    @FXML public Appointments selectedAppointment; // Selected Appointment

    // Customer Variables ///////////////////////////////////////////////////////////////////////////////////////////////
    @FXML public TextField customerRecordsIDField, customerNameField, addressField, postalField, phoneField;
    @FXML public ComboBox<String> countryMenu; // Country Menu
    @FXML public ComboBox<String> divisionMenu; // Division Menu
    @FXML public ObservableList<String> countryObservableList = FXCollections.observableArrayList();
    @FXML public ObservableList<String> divisionObservableList = FXCollections.observableArrayList();
    @FXML public TableView<Customers> viewCustomers; // Customers Table
    @FXML public TableColumn<?, ?> customerIDRecordsColumn, nameColumn, phoneColumn, addressColumn, postalColumn, divisionColumn;
    @FXML public Button addCustomerButton;
    @FXML public Button modifyCustomerButton;
    @FXML public Button deleteCustomerButton;
    @FXML public Button clearSelectedCustomerButton;
    @FXML public ObservableList<Customers> customersList = FXCollections.observableArrayList(CustomerAccess.getAllCustomers());
    @FXML public ObservableList<DivisionAccess> divisionsList = FXCollections.observableArrayList();
    @FXML public ObservableList<String> countryNames = FXCollections.observableArrayList();
    @FXML public ObservableList<String> divisionNames = FXCollections.observableArrayList();
    @FXML public Customers selectedCustomer;

    // Appointments Tab Methods ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * viewByWeek - a method that displays appointments that are within 7 days of the current date
     * */
    @FXML public void viewWeek() {
        try {
            if (radioWeek.isSelected()) { // if the week radio button is selected
                radioMonth.setSelected(false); // unselect the month radio button
                appointmentsList.clear(); // clear the appointments list
                appointmentsList.addAll(AppointmentAccess.allAppointmentsWithin7Days()); // add all appointments within 7 days to the list
                viewAppointments.setItems(appointmentsList); // set the appointments list to the table view
            }
            else {
                appointmentsList.clear(); // clear the appointments list
                appointmentsList.addAll(AppointmentAccess.allAppointments()); // add all appointments to the list
                viewAppointments.setItems(appointmentsList); // set the appointments list to the table view
            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e);
        }
        finally {
            AgentFord.gatherIntel(nightVisionGoggles);
        }
    }

    /**
     * viewByMonth - a method that displays appointments that are within 30 days of the current date
     * */
    @FXML public void viewMonth() {
        try {
            if (radioMonth.isSelected()) { // if the month radio button is selected
                radioWeek.setSelected(false); // unselect the week radio button
                appointmentsList.clear(); // clear the appointments list
                appointmentsList.addAll(AppointmentAccess.allAppointmentsWithin30Days()); // add all appointments within 30 days to the list
                viewAppointments.setItems(appointmentsList); // set the appointments list to the table view
            }
            else {
                appointmentsList.clear(); // clear the appointments list
                appointmentsList.addAll(AppointmentAccess.allAppointments()); // add all appointments to the list
                viewAppointments.setItems(appointmentsList); // set the appointments list to the table view
            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e);
        }
        finally {
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        }
    }

    /**
     * selectAppointment selects an appointment from the tableview and populates the text fields
     * */
    @FXML public void selectAppointment() {
        try {
            selectedAppointment = viewAppointments.getSelectionModel().getSelectedItem();
            if (selectedAppointment != null) {
                appIDField.setText(String.valueOf(selectedAppointment.getAppointmentID())); // Sets the appointment ID text field
                titleField.setText(String.valueOf(selectedAppointment.getAppointmentTitle())); // Sets the appointment title text field
                contactsMenu.setValue(String.valueOf(selectedAppointment.getContactName(selectedAppointment.getContactID()))); // Sets the contact combo box
                customersMenu.setItems(CustomerAccess.getAllCustomerNameStrings()); // Sets the customer combo box
                customersMenu.setValue(String.valueOf(selectedAppointment.getCustomerName(selectedAppointment.getCustomerID()))); // Sets the customer combo box
                usersMenu.setValue(String.valueOf(selectedAppointment.getUserName(selectedAppointment.getUserID()))); // Sets the user combo box
                locationField.setText(String.valueOf(selectedAppointment.getAppointmentLocation())); // Sets the appointment location text field
                typeField.setText(String.valueOf(selectedAppointment.getAppointmentType())); // Sets the appointment type text field
                descriptionTextArea.setText(String.valueOf(selectedAppointment.getAppointmentDescription())); // Sets the appointment description text field
                datePicker.setValue(selectedAppointment.getStartTime().toLocalDate()); // Sets the start date picker
//                endDatePicker.setValue(selectedAppointment.getEndTime().toLocalDate()); // Sets the end date picker
//                startTimeBox.setValue(selectedAppointment.getStartTime().toLocalTime().toString()); // Sets the start time combo box
//                endTimeBox.setValue(selectedAppointment.getEndTime().toLocalTime().toString()); // Sets the end time combo box

                startHourBox.setValue(String.valueOf(selectedAppointment.getStartTime().getHour())); // Sets the start hour combo box
                endHourBox.setValue(String.valueOf(selectedAppointment.getEndTime().getHour())); // Sets the end hour combo box
                startMinBox.setValue(String.valueOf(selectedAppointment.getStartTime().getMinute())); // Sets the start minute combo box
                endMinBox.setValue(String.valueOf(selectedAppointment.getEndTime().getMinute())); // Sets the end minute combo box

                addAppointmentButton.setDisable(true); // Disables the add appointment button
                modifyAppointmentButton.setDisable(false); // Enables the modify appointment button
                deleteAppointmentButton.setDisable(false); // Enables the delete appointment button
            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
        finally {
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        }
    }

    /**
     * clearSelection clears the selected appointment and resets the buttons to their default state
     * uses lambda expressions to clear the text fields, combo boxes and date pickers
     * */
    @FXML public void clearSelectedAppointment() {
        try {
            selectedAppointment = null; // Clears the selected appointment
            contactsMenu.setValue("Contacts"); // Clears the contact combo box
            customersMenu.setValue("Customers"); // Clears the customer combo box
            usersMenu.setValue("Users"); // Clears the user combo box
            addAppointmentButton.setDisable(false); // Enables the add appointment button
            Stream.of(appIDField, titleField).forEach(TextInputControl::clear); // Clears the appointment ID and title text fields
            Stream.of(userIDField, locationField, typeField, descriptionTextArea).forEach(TextInputControl::clear); // Clears the user ID, location, description and type text fields
            Stream.of(datePicker, startHourBox, endHourBox, startMinBox, startHourBox).forEach(c->c.setValue(null)); // Clears the start date, end date, start time and end time combo boxes
            Stream.of(modifyAppointmentButton, deleteAppointmentButton).forEach(c->c.setDisable(true)); // Disables the modify and delete appointment buttons
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
            throw e;
        }
        finally {
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        }
    }

    /**
     * addAppointment adds a new appointment to the database
     * */
    @FXML public void addAppointment() throws Exception {
        try {
            connection = JDBC.openConnection(); // establish connection, passing into insertIntoAppointment()
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION); // confirmation alert
            confirm.setTitle("Confirmation"); // set title
            confirm.setHeaderText("You are about to add an appointment."); // set header text
            confirm.setContentText("Are you sure you want to add this appointment?"); // set content text
            Optional<ButtonType> result = confirm.showAndWait(); // show alert and wait for user input
            String password = passwordPrompt(); // passwordPrompt method
            if (result.get() == ButtonType.OK) { // if the user clicks OK
                // generating new appointment ID and getting the values from the text fields
                int newAppointmentID = NumberGenie.magicAppointment(); // generate new appointment ID
                String newTitle = titleField.getText();  // get title
                String newDescriptionText = descriptionTextArea.getText(); // get description
                String newLocation = locationField.getText(); // get location
                String newType = typeField.getText(); // get type

                LocalDate localDate = datePicker.getValue(); // get date
                LocalTime localStartTime = LocalTime.of(Integer.parseInt(startHourBox.getValue()), Integer.parseInt(startMinBox.getValue())); // get start time
                LocalTime localEndTime = LocalTime.of(Integer.parseInt(endHourBox.getValue()), Integer.parseInt(endMinBox.getValue())); // get end time

                if (!HotTubTimeMachine.isWithinBusinessHours(localDate, localStartTime, localEndTime)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Appointment is outside of business hours.");
                    alert.setContentText("Please select a time between 8:00 AM and 10:00 PM.");
                    alert.showAndWait();
                }

                LocalDateTime startDT = HotTubTimeMachine.getDateTimeFromPickers(datePicker, startHourBox, endMinBox); // get start date and time
                LocalDateTime endDT = HotTubTimeMachine.getDateTimeFromPickers(datePicker, startHourBox, endMinBox); // get end date and time

                LocalDateTime startUTC = HotTubTimeMachine.convertToUTC(startDT); // convert start date and time to UTC
                LocalDateTime endUTC = HotTubTimeMachine.convertToUTC(endDT); // convert end date and time to UTC

                // convert startUTC and endUTC to strings
                String startUTCString = startUTC.toString();
                String endUTCString = endUTC.toString();

                int newCustomerID = CustomerAccess.getCustomerIDByName(customersMenu.getValue()); // find customer ID by name in the database

                String userName = usersMenu.getValue(); // get username from the users menu box
                int userID = UserAccess.getUserID(userName, password); // get user ID from the database using the username and password

                String contactName = contactsMenu.getValue(); // get contact name from the contacts menu box
                int contactID = ContactAccess.findContactID(contactName); // find contact ID

                // adding the new appointment to the database
                if (GateKeeper.verifyTraveler(userName, userID, password)) { // verifyTraveler method
                    SQLQueries.INSERT_INTO_APPOINTMENTS_METHOD(connection,
                                                                newAppointmentID,
                                                                newTitle,
                                                                newDescriptionText,
                                                                newLocation,
                                                                newType,
                                                                startUTCString,
                                                                endUTCString,
                                                                newCustomerID,
                                                                userID,
                                                                contactID); // insertIntoAppointment method
                    successAlert(); // successAlert method
                }
                clearSelectedAppointment(); // clear the selected appointment
            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
            throw e;
        } finally {
            connection = JDBC.closeConnection(); // close the connection
            updateAppointments(); // update the appointments table
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        }
    }

    /**
     * quick scratch code, create package for these later
     * */
    public void successAlert() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText("Success");
        successAlert.setContentText("Appointment successfully modified.");
        successAlert.showAndWait();
    }

    /**
     * modifyAppointment modifies an appointment in the database
     * */
    @FXML public void modifyAppointment() throws RuntimeException {
        try {
            connection = JDBC.openConnection(); // establish connection, passing into insertIntoAppointment()
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // confirmation alert
            alert.setTitle("Confirmation");
            alert.setHeaderText("You are about to modify an appointment.");
            alert.setContentText("Are you sure you want to modify this appointment?");
            Optional<ButtonType> result = alert.showAndWait(); // show the alert
            String password = passwordPrompt(); // passwordPrompt method
            if (selectedAppointment != null && appointmentFormVerification()) {
                int appointmentID = selectedAppointment.getAppointmentID(); // getting the appointment ID
                String title = selectedAppointment.getAppointmentTitle(); // getting the title
                String location = selectedAppointment.getAppointmentLocation(); // getting the location
                String type = selectedAppointment.getAppointmentType(); // getting the type
                String descriptionText = selectedAppointment.getAppointmentDescription(); // getting the description

                int customerID = selectedAppointment.getCustomerID(); // getting the customer ID
                int userID = selectedAppointment.getUserID(); // getting the user ID
                String userName = selectedAppointment.getUserName(userID); // getting the username

                int contactID = selectedAppointment.getContactID(); // get contact
                String contactName = selectedAppointment.getContactName(contactID); // get contact name

                // getting values from the selected appointment
                LocalDateTime appointmentStartTime = selectedAppointment.getStartTime(); // getting the start date
                LocalDateTime appointmentEndTime = selectedAppointment.getEndTime(); // getting the end date

                Stream.of(startHourBox, endHourBox).forEach(box -> box.setItems(HotTubTimeMachine.getHours())); // set the start and end time boxes to the times list
                Stream.of(startMinBox, endMinBox).forEach(box -> box.setItems(HotTubTimeMachine.getMinutes())); // set the start and end minute boxes to the minutes list

                if (GateKeeper.verifyTraveler(userName, userID, password)) { // verifyLogin method
                    SQLQueries.UPDATE_APPOINTMENT_METHOD(connection,
                                                        appointmentID,
                                                        title,
                                                        descriptionText,
                                                        location,
                                                        type,
                                                        appointmentStartTime,
                                                        appointmentEndTime,
                                                        customerID,
                                                        userID,
                                                        contactID); // update the appointment
                    successAlert(); // successAlert method
                }
                clearSelectedAppointment(); // clear the text fields
            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
        finally {
            connection = JDBC.closeConnection(); // close the connection
            updateAppointments(); // update the appointments table
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        } throw new RuntimeException("Error: Appointment could not be modified.");
    }

    /**
     * deleteAppointment deletes an appointment from the database
     * */
    @FXML public void deleteAppointment() throws RuntimeException {
        try {
            connection = JDBC.openConnection(); // open a connection
            int appointmentID = selectedAppointment.getAppointmentID(); // get the appointment ID
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Appointment");
            alert.setHeaderText("Delete Appointment");
            alert.setContentText("Are you sure you want to delete this appointment?");
            Optional<ButtonType> result;
            result = alert.showAndWait();
            // if the user clicks OK, delete the appointment
            if (result.isPresent() && result.get() == ButtonType.OK) {
                SQLQueries.DELETE_APPOINTMENT_METHOD(connection, appointmentID); // delete the appointment
                clearSelectedAppointment(); // clear the selected appointment
            } else {
                alert.close();
            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
        finally {
            connection = JDBC.closeConnection(); // close the connection
            updateAppointments(); // update the appointments table
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        }
    }

    // Customers Tab Methods ///////////////////////////////////////////////////////////////////////////////////////////

    /**
     * selectCustomer selects a customer from the tableview
     * disables add button, enables modify and delete buttons to prevent pointer exceptions
     * */
    public void selectCustomer() throws Exception {
        try {
            connection = JDBC.openConnection(); // establish connection
            selectedCustomer = viewCustomers.getSelectionModel().getSelectedItem(); // get the selected customer
            System.out.println(selectedCustomer);
            if (selectedCustomer != null) {
                customerRecordsIDField.setText(String.valueOf(selectedCustomer.getCustomerID())); // set the customer ID field
                customerNameField.setText(selectedCustomer.getCustomerName()); // set the customer name field
                addressField.setText(selectedCustomer.getCustomerAddress()); // set the address field
                postalField.setText(selectedCustomer.getPostalCode()); // set the postal code field
                phoneField.setText(selectedCustomer.getCustomerPhone()); // set the phone field

                // Brace yourselves, code is coming
                // set the division menu
                countryMenu.valueProperty().addListener((observable, oldValue, newValue) -> {
                    int selectedCountryID = 0;  // create a variable to hold the country ID
                    try { // try to get the selected country ID
                        // loop through the countries
                        for (Country country : CountryAccess.getCountries()) {
                            // if the country name equals the new value, set the country ID
                            if (country.getCountryName().equals(newValue)) {
                                selectedCountryID = country.getCountryID(); // set the country ID
                                break;
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    ObservableList<String> relatedDivisionsList = FXCollections.observableArrayList(); // create a new observable list
                    try { // try to get the selected country division using the country ID
                        // loop through the divisions
                        for (DivisionAccess division : DivisionAccess.getDivisions()) {
                            // if the country ID matches the selected country ID, add the division name to the list
                            if (division.getCountryID() == selectedCountryID) {
                                relatedDivisionsList.add(String.valueOf(division.getDivisionName())); // add the division name to the list
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    divisionMenu.setItems(relatedDivisionsList); // set the division menu
                });

                // set the country menu
                for (Division division : DivisionAccess.getDivisions()) {
                    // if the division ID matches the selected customer's division ID, set the division name
                    if (division.getDivisionID() == selectedCustomer.getDivisionID()) {
                        divisionMenu.setValue(division.getDivisionName()); // set the division name
                        // loop through the countries
                        for (Country country : CountryAccess.getCountries()) {
                            // if the country ID matches the division's country ID, set the country name
                            if (country.getCountryID() == division.getCountryID()) {
                                countryMenu.setValue(country.getCountryName()); // set the country name
                            }
                        }
                    }
                }

                // disable add button, enable modify and delete buttons to prevent pointer exceptions
                addCustomerButton.setDisable(true);
                modifyCustomerButton.setDisable(false);
                deleteCustomerButton.setDisable(false);
            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
            throw e;
        }
        finally {
            if (connection != null) {
                connection = JDBC.closeConnection(); // close the connection
            }
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        }
    }

    /**
     * clearSelectedCustomer clears the selected customer
     * enables add button, disables modify and delete buttons to prevent pointer exceptions
     * uses lambda expressions to clear the text fields, menus, and disable the buttons
     * */
    public void clearSelectedCustomer() throws RuntimeException {
        try {
            selectedCustomer = null; // clear the selected customer
            Stream.of(customerRecordsIDField,
                    customerNameField,
                    addressField,
                    postalField,
                    phoneField).forEach(TextInputControl::clear); // clear the text fields
            Stream.of(countryMenu, divisionMenu).forEach(c -> c.setValue(null)); // clear the country and division menus
            Stream.of(modifyCustomerButton, deleteCustomerButton).forEach(c -> c.setDisable(true)); // disable modify and delete buttons
            addCustomerButton.setDisable(false); // enable add button
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
        finally {
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        }
    }

    // addCustomer
    @FXML public void addCustomer() {
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Add Customer");
            alert.setHeaderText("Adding Customer to Database");
            alert.setContentText("Are you sure you want to add this customer?");
            Optional<ButtonType> result;
            result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                connection = JDBC.openConnection(); // establish connection
                int customerID = NumberGenie.magicCustomer(); // get customer id
                String customerName = customerNameField.getText(); // get customer name
                String address = addressField.getText(); // get customer address
                String postalCode = postalField.getText(); // get customer postal code
                String phone = phoneField.getText(); // get customer phone
                String country = countryMenu.getValue(); // get customer country
                String division = divisionMenu.getValue(); // get customer division
                int divisionID = DivisionAccess.getDivisionID(division); // get division ID
                // if the fields are not empty, insert into customers table
                if (customerFormVerification()) {
                    SQLQueries.INSERT_INTO_CUSTOMERS_METHOD(connection,
                                                            customerID,
                                                            customerName,
                                                            address,
                                                            postalCode,
                                                            phone,
                                                            divisionID);
                }
                else {
                    Alert emptyFieldsAlert = new Alert(Alert.AlertType.ERROR);
                    emptyFieldsAlert.setTitle("Error");
                    emptyFieldsAlert.setHeaderText("Error Adding Customer");
                    emptyFieldsAlert.setContentText("Please fill out all fields.");
                    emptyFieldsAlert.showAndWait();
                }
                clearSelectedCustomer(); // clear selected customer
            } else {
                alert.close();
            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
        finally {
            if (connection != null) {
                connection = JDBC.closeConnection(); // close the connection
            }
            updateCustomers(); // update customers tableview
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        }
    }

    // modifyCustomer
    /**
     * modifyCustomer modifies the selected customer
     * */
    @FXML public void modifyCustomer() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modify Customer");
            alert.setHeaderText("Modifying Customer in Database");
            alert.setContentText("Are you sure you want to modify this customer?");
            Optional<ButtonType> result;
            result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                connection = JDBC.openConnection(); // establish connection
                int customerID = selectedCustomer.getCustomerID(); // get customer id
                String customerName = customerNameField.getText(); // get customer name
                String address = addressField.getText(); // get customer address
                String postalCode = postalField.getText(); // get customer postal code
                String phone = phoneField.getText(); // get customer phone
                String country = countryMenu.getValue(); // get customer country
                String division = divisionMenu.getValue(); // get customer division
                int divisionID = DivisionAccess.getDivisionID(division); // get division ID
                // if the customer fields are not empty, update the customer
                if (customerFormVerification()) {
                    SQLQueries.UPDATE_CUSTOMER_METHOD(connection,
                                                        customerID,
                                                        customerName,
                                                        address,
                                                        postalCode,
                                                        phone,
                                                        divisionID); // update customers
                }
                else {
                    Alert emptyFieldsAlert = new Alert(Alert.AlertType.ERROR);
                    emptyFieldsAlert.setTitle("Error");
                    emptyFieldsAlert.setHeaderText("Error Adding Customer");
                    emptyFieldsAlert.setContentText("Please fill out all fields.");
                    emptyFieldsAlert.showAndWait();
                }
                clearSelectedCustomer(); // clear selected customer
            } else {
                alert.close();
            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
        finally {
            updateCustomers(); // update customers tableview
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        }

    }

    /**
     * deleteCustomer deletes a customer from the database
     * checks if the customer has any appointments
     * if the customer has appointments, an error message is displayed
     * */
    public void deleteCustomer() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setHeaderText("Deleting Customer from Database");
            alert.setContentText("Are you sure you want to delete this customer?");
            Optional<ButtonType> result;
            result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK && AppointmentAccess.noAppointments(selectedCustomer.getCustomerID())) {
                connection = JDBC.openConnection(); // establish connection
                int customerID = selectedCustomer.getCustomerID(); // get customer id
                SQLQueries.DELETE_CUSTOMER_METHOD(connection, customerID); // delete customer
                clearSelectedCustomer(); // clear selected customer
            } else {
                Alert hasAppointments = new Alert(Alert.AlertType.ERROR);
                hasAppointments.setTitle("Error");
                hasAppointments.setHeaderText("Customer has Appointments");
                hasAppointments.setContentText("This customer has appointments and cannot be deleted.");
                hasAppointments.showAndWait();
            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
        finally {
            updateCustomers(); // update customers tableview
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        }
    }

    // Data Validation Tools ///////////////////////////////////////////////////////////////////////////////////////////

    /**
     * findDivisions finds the divisions for the selected country
     * */
    public void findDivisions() {
        try {
            String country = countryMenu.getValue(); // get the country
            // if the country is not null
            if (country != null) {
                connection = JDBC.openConnection(); // establish connection
                JDBC.setPreparedStatement(connection, SQLQueries.GET_DIVISION_FOR_COUNTRY); // set the prepared statement
                PreparedStatement statement = JDBC.getPreparedStatement(); // get the prepared statement
                statement.setString(1, country); // set the country
                ResultSet resultSet = statement.executeQuery(); // execute the query
                // loop through the result set
                while (resultSet.next()) {
                    divisionObservableList.add(resultSet.getString("Division")); // add the division to the observable list
                    divisionMenu.setItems(divisionObservableList); // set the division menu to the observable list
                }

            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
        finally {
            if (connection != null) {
                connection = JDBC.closeConnection(); // close the connection
            }
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        }
    }

    /**
     * enables the division menu if a country is selected
     * prevents incorrect data from being entered
     * */
    public void enableDivisions() {
        if (countryMenu.getValue() != null) {
            findDivisions(); // find divisions for the selected country
            divisionMenu.setDisable(false); // enable division menu
        }
    }

    /**
     * customerLocationsLambda checks if the countryMenu and divisionMenu are empty
     * @return true if either field is empty
     * uses lambda expressions to check if any of the fields are empty
     * */
    public boolean customerLocationsCheck() {
        return Stream.of(countryMenu.getValue(),
                divisionMenu.getValue()).noneMatch(Objects::isNull);
    }

    /**
     * customerFieldsLambda checks if any of the customer fields are empty
     * @return true if any of the fields are empty
     * uses lambda expressions to check if any of the fields are empty
     * */
    public boolean customerFieldsCheck() {
        return Stream.of(customerNameField.getText(),
                addressField.getText(),
                postalField.getText(),
                phoneField.getText(),
                countryMenu.getValue(),
                divisionMenu.getValue()).noneMatch(String::isEmpty);
    }

    /**
     * customerFormVerification checks if any of the customer fields are empty
     * combines the customerFieldsCheck and customerLocationsCheck methods
     * created for readability
     * */
    public boolean customerFormVerification() {
        return customerFieldsCheck() && customerLocationsCheck(); // return true if both methods return true
    }

    /**
     * appointmentTimeLambda checks if startTimeBox, endTImeBox, startDatePicker and endDatePicker are empty
     * uses lambda expressions to check if any of the fields are empty
     * */
    public boolean emptyAppMenus() {
        return Stream.of(startHourBox.getValue(),
                        endHourBox.getValue(),
                        startMinBox.getValue(),
                        endMinBox.getValue(),
                        datePicker.getValue()).anyMatch(Objects::isNull);
    }

    /**
     * appointmentFieldsLambda checks if any of the appointment fields are empty
     *  uses lambda expressions to check if any of the fields are empty
     * */
    public boolean emptyAppFields() {
        return Stream.of(userIDField.getText(),
                        locationField.getText(),
                        typeField.getText(),
                        titleField.getText()).anyMatch(String::isEmpty);
    }

    /**
     * appointmentFormVerification checks if any of the appointment fields are empty
     * combines the appointmentFieldsCheck and appointmentTimeCheck methods
     * created for readability
     * */
    public boolean appointmentFormVerification() {
        return !emptyAppFields() && !emptyAppMenus(); // return true if both methods return true
    }

    // a method that alerts the user asking for a password
    public void passwordAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // create an alert
        alert.setTitle("Password"); // set the title
        alert.setHeaderText("Oops! The password you entered was incorrect."); // set the header text
        alert.setContentText("Please enter the correct password."); // set the content text
        alert.showAndWait(); // show the alert
    }

    // prompt the user to type in their password
    public String passwordPrompt() {
        try {
            TextInputDialog dialog = new TextInputDialog(); // create a text input dialog
            dialog.setTitle("Password Requirement"); // set the title
            dialog.setHeaderText("A password is required when performing database operations."); // set the header text
            dialog.setContentText("Please enter your password:"); // set the content text
            Optional<String> result = dialog.showAndWait(); // show the dialog
            if (result.isPresent()) {
                return result.get();
            }
        } catch (Exception e) {
            ExceptionHandler.eAlert(e);
            throw e;
        }
        finally {
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        }
        return null;
    }

    // Initialize and Support  /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * fifteenMinuteAlert checks if there is an appointment within fifteen minutes of the current local time
     * if there is an appointment within fifteen minutes, an alert is displayed
     * */
    public void fifteenMinuteAlert() {
        try {
            connection = JDBC.openConnection(); // establish connection
            JDBC.setPreparedStatement(connection, SQLQueries.GET_ALL_APPOINTMENTS_STATEMENT); // set the prepared statement
            PreparedStatement statement = JDBC.getPreparedStatement(); // get the prepared statement
            ResultSet resultSet = statement.executeQuery(); // execute the query
            // loop through the result set
            while (resultSet.next()) {
                // if the appointment is within fifteen minutes of the current local time
                if (resultSet.getTimestamp("Start").toLocalDateTime().isBefore(LocalDateTime.now().plusMinutes(15)) &&
                        resultSet.getTimestamp("Start").toLocalDateTime().isAfter(LocalDateTime.now())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointment Alert");
                    alert.setHeaderText("Appointment within 15 minutes");
                    alert.setContentText("You have an appointment within 15 minutes.");
                    alert.showAndWait();
                }
            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
        finally {
            if (connection != null) {
                connection = JDBC.closeConnection(); // close the connection
            }
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        }
    }

    /**
     * reportBack gathers intel, reports back to HQ when espionageButton is triggered
     * */
    @FXML public void reportBack() {
        AgentFord.deBriefing(nightVisionGoggles); // deBriefing method
    }

    /**
     * updateAppointments updates the appointments tableview
     * clears the tableview, creates a new observable list, establishes connection, sets the items in the tableview to the new observable list
     * */
    public void updateAppointments() {
        try {
            viewAppointments.getItems().clear(); // clear the items in the table
            ObservableList<Appointments> newAppointments = FXCollections.observableArrayList(AppointmentAccess.allAppointments()); // create a new observable list
            connection = JDBC.openConnection(); // establish connection
            viewAppointments.setItems(newAppointments); // set the items in the table to the appointments list
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
        finally {
            if (connection != null) {
                connection = JDBC.closeConnection(); // close the connection
            }
        }
    }

    /**
     * updateCustomers updates the customers tableview
     * clears the tableview, creates a new observable list, establishes connection, sets the items in the tableview to the new observable list
     * */
    public void updateCustomers() {
        try {
            viewCustomers.getItems().clear(); // clear the items in the table
            ObservableList<Customers> newCustomers = FXCollections.observableArrayList(CustomerAccess.getAllCustomers()); // create a new observable list
            connection = JDBC.openConnection(); // establish connection
            viewCustomers.setItems(newCustomers); // set the items in the table to the customers list
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
        finally {
            if (connection != null) {
                connection = JDBC.closeConnection(); // close the connection
            }
        }
    }

    /**
     * initialize initializes the controller class
     * @param url is the url
     * @param resourceBundle is the resource bundle
     * uses a lambda expression to set the cell value factory for the contact column considering constraints of the entity relationships
     * also incorporates streams to filter countries and first level divisions
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // TODO
            AgentFord.frontDoorSurveillance(infraredGoggles); // Front Door Surveillance, tracks logins
            fifteenMinuteAlert(); // fifteen minute alert
            InetAddress ip = InetAddress.getLocalHost(); // get the local host
            userCreds.setText(JDBC.getUsername() + " from " + ip); // set the user credentials label to the username
            connection = JDBC.openConnection();// establishing connection to db
            usersMenu.setItems(UserAccess.getAllUserNames()); // set the user combo box to the user names
            usersMenu.setValue("Users"); // set the user combo box to the user names
            customersMenu.setItems(CustomerAccess.getAllCustomerNameStrings()); // Sets the customer combo box
            customersMenu.setValue("Customers"); // Sets the customer combo box

            // lambda expression to populate hour boxes
            IntStream.rangeClosed(8, 22).forEach(hour -> {
                startHourBox.getItems().add(String.valueOf(hour)); // add the hours to the start hour combo box
                endHourBox.getItems().add(String.valueOf(hour)); // add the hours to the end hour combo box
            });

            // lambda expression to populate minute boxes
            IntStream.rangeClosed(0, 59).forEach(minute -> {
                startMinBox.getItems().add(String.valueOf(minute)); // add the minutes to the start minute combo box
                endMinBox.getItems().add(String.valueOf(minute)); // add the minutes to the end minute combo box
            });

            // set up the Appointment columns in the table, must match the names of the variables in the model
            appIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID")); // set the cell value factory for the appointment ID column
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle")); // set the cell value factory for the appointment title column
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription")); // set the cell value factory for the appointment description column
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation")); // set the cell value factory for the appointment location column
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType")); // set the cell value factory for the appointment type column
            customerIDAppointmentsColumn.setCellValueFactory(new PropertyValueFactory<>("customerID")); // set the cell value factory for the appointment customer ID column
            userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID")); // set the cell value factory for the appointment user ID column
            startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime")); // set the cell value factory for the appointment start time column
            endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime")); // set the cell value factory for the appointment end time column

            // lambda expression to set the cell value factory for the contact column
            ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList(AppointmentAccess.allAppointments()); // create an observable list of appointments
            TableColumn<Appointments, String> contactColumn = new TableColumn<>("Contact"); // create a new table column for the contact
            contactColumn.setCellValueFactory(cellData -> { // set the cell value factory for the contact column
                Appointments appointment = cellData.getValue(); // get the value of the cell
                int contactID = appointment.getContactID(); // get the contact ID
                String contactName = ""; // create a string for the contact name
                try { // try to get the contact name
                    contactName = ContactAccess.getContactName(contactID); // get the contact name
                } catch (SQLException e) { // catch the exception
                    ExceptionHandler.eAlert(e); // eAlert method
                }
                return new SimpleStringProperty(contactName); // return the contact name
            });
            viewAppointments.getColumns().add(3, contactColumn); // add the contact column to the table

            // set up the Customer columns in the table, must match the names of the variables in the model
            customerIDRecordsColumn.setCellValueFactory(new PropertyValueFactory<>("customerID")); // set the cell value factory for the customer ID column
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName")); // set the cell value factory for the customer name column
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress")); // set the cell value factory for the customer address column
            postalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode")); // set the cell value factory for the customer postal code column
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhone")); // set the cell value factory for the customer phone column
            divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID")); // set the cell value factory for the customer division ID column

            // call getCountries method to populate an ObservableList to populate the country menu
            ObservableList<CountryAccess> countriesList = CountryAccess.getCountries(); // get the list of countries from the db
            countriesList.stream().map(Country::getCountryName).forEach(countryObservableList::add); // add the country names to the observable list
            countryMenu.setItems(countryObservableList); // set the items in the country menu to the observable list

            // call getDivisions method to populate an ObservableList to populate the division menu
            ObservableList<DivisionAccess> divisionsList = DivisionAccess.getDivisions(); // get the list of divisions from the db
            divisionsList.stream().map(Division::getDivisionName).forEach(divisionObservableList::add); // add the division names to the observable list
            divisionMenu.setItems(divisionObservableList); // set the items in the division menu to the observable list

            ObservableList<String> contactsList = FXCollections.observableArrayList(ContactAccess.getContactNames()); // get the list of contact names from the db
            contactsMenu.setItems(contactsList); // set the items in the contact menu to the observable list

            viewAppointments.setItems(appointmentsList); // set the items in the table to the appointments list
            viewCustomers.setItems(customersList); // set the items in the table to the customers list
            connection = JDBC.closeConnection(); // close the connection
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
        finally {
            if (connection != null) {
                connection = JDBC.closeConnection(); // close the connection
            }
            AgentFord.gatherIntel(nightVisionGoggles); // Gather Intel
        }
    }
}