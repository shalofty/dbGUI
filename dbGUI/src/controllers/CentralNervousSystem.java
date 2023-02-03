package controllers;

import dataAccess.*;
import exceptions.Confundo;
import exceptions.GateKeeper;
import exceptions.Verificatus;
import numericNexus.NumberGenie;
import helper.JDBC;
import theAgency.AgentFord;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.*;
import merlin.HotTubTimeMachine;

import java.net.InetAddress;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CentralNervousSystem implements Initializable {

    @FXML public Connection connection = null;
    @FXML public PreparedStatement preparedStatement = null;
    @FXML public Tab appointmentsTab, customersTab, reportsTab, logTab;
    @FXML public static TextArea theSacredScroll, infraredGoggles, theCrimeScene;
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
    @FXML public RadioButton radioWeek, radioMonth; // Radio Buttons
    @FXML public TableColumn<?, ?> appIDColumn, titleColumn, descriptionColumn, locationColumn, typeColumn, customerIDAppointmentsColumn, userIDColumn, contactColumn, startColumn, endColumn;
    @FXML public Button addAppointmentButton, modifyAppointmentButton, deleteAppointmentButton, clearSelectionButton;
    @FXML public TableView<Appointments> viewAppointments; // Appointments Table
    @FXML public ObservableList<Appointments> appointmentsList; // Observable List of Appointments
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
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            AgentFord.gatherIntel(theSacredScroll);
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
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
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
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
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
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
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
            GateKeeper.collectCredentials(); // collect credentials
            String password = GateKeeper.getPassword(); // get password
            // if the user clicks OK and enters a password
            // if the user clicks OK
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
                Confundo.businessHours(); // Alert the user that the appointment is not within business hours
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
            if (GateKeeper.verifyTraveler(userName, userID, password)) { // verifying the user
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
            connection.close(); // close the connection
            clearSelectedAppointment(); // clear the selected appointment
        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        } finally {
            connection = JDBC.closeConnection(); // close the connection
            updateAppointments(); // update the appointments table
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
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
    @FXML public void modifyAppointment() throws Exception {
        try {
            connection = JDBC.openConnection(); // establish connection, passing into insertIntoAppointment()
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // confirmation alert
            alert.setTitle("Confirmation");
            alert.setHeaderText("You are about to modify an appointment.");
            alert.setContentText("Are you sure you want to modify this appointment?");
            Optional<ButtonType> result = alert.showAndWait(); // show the alert
            if (selectedAppointment != null && result.get() == ButtonType.OK && result.isPresent()) { // if the user clicks OK
                GateKeeper.collectCredentials(); // collect credentials
                String collectedUsername = GateKeeper.getUsername();
                String collectedPassword = GateKeeper.getPassword();
                int appointmentID = selectedAppointment.getAppointmentID(); // getting the appointment ID
                String title = selectedAppointment.getAppointmentTitle(); // getting the title
                String location = selectedAppointment.getAppointmentLocation(); // getting the location
                String type = selectedAppointment.getAppointmentType(); // getting the type
                String descriptionText = selectedAppointment.getAppointmentDescription(); // getting the description
                int customerID = selectedAppointment.getCustomerID(); // getting the customer ID
                int userID = selectedAppointment.getUserID(); // getting the user ID
//                String userName = selectedAppointment.getUserName(userID); // getting the username for verification purposes
                int contactID = selectedAppointment.getContactID(); // get contact

                LocalDate localDate = datePicker.getValue(); // get date
                int startHour = Integer.parseInt(startHourBox.getValue()); // get start hour
                int startMin = Integer.parseInt(startMinBox.getValue()); // get start minute
                int endHour = Integer.parseInt(endHourBox.getValue()); // get end hour
                int endMin = Integer.parseInt(endMinBox.getValue()); // get end minute

                LocalTime localStartTime = LocalTime.of(startHour, startMin); // get start time
                LocalTime localEndTime = LocalTime.of(endHour, endMin); // get end time

                if (!HotTubTimeMachine.isWithinBusinessHours(localDate, localStartTime, localEndTime)) { // check if the appointment is within business hours
                    Confundo.businessHours(); // Alert the user that the appointment is not within business hours
                }

                LocalDateTime localStartDateTime = LocalDateTime.of(localDate, localStartTime); // get start date and time
                LocalDateTime localEndDateTime = LocalDateTime.of(localDate, localEndTime); // get end date and time

                LocalDateTime startUTC = HotTubTimeMachine.convertToUTC(LocalDateTime.from(localStartDateTime)); // convert start date and time to UTC
                LocalDateTime endUTC = HotTubTimeMachine.convertToUTC(LocalDateTime.from(localEndDateTime)); // convert end date and time to UTC

                // convert startUTC and endUTC to strings
                String startUTCString = startUTC.toString().replace("T", " ");
                String endUTCString = endUTC.toString().replace("T", " ");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // format the date and time
                LocalDateTime startDT = LocalDateTime.parse(startUTCString, formatter); // parse the start date and time
                LocalDateTime endDT = LocalDateTime.parse(endUTCString, formatter); // parse the end date and time

                // data verification and validation
                String[] strings = {title, location, type, descriptionText}; // array of strings
                int[] numbers = {customerID, userID, contactID}; // array of numbers
                TextField[] fields = {titleField, locationField, typeField}; // array of text fields

                boolean isTested = GateKeeper.dataCheck(strings, numbers, fields); // dataCheck method checks for empty fields
                boolean isVerified = GateKeeper.verifyTraveler(collectedUsername, userID, collectedPassword); // verifyTraveler method checks credentials

                if (isTested && isVerified) {
                    SQLQueries.UPDATE_APPOINTMENT_METHOD(connection,
                                                        appointmentID,
                                                        title,
                                                        descriptionText,
                                                        location,
                                                        type,
                                                        startDT,
                                                        endDT,
                                                        collectedUsername,
                                                        customerID,
                                                        userID,
                                                        contactID); // update the appointment
                    successAlert(); // successAlert method
                }
                else if (!isTested) {
                    System.out.println("Data is not tested.");
                }
                else {
                    System.out.println("An unknown error has occurred.");
                }
                clearSelectedAppointment(); // clear the text fields
            }
        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null) {
                connection.close();
            }
            updateAppointments(); // update the appointments table
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
        }
    }

    /**
     * deleteAppointment deletes an appointment from the database
     * */
    @FXML public void deleteAppointment() throws RuntimeException, SQLException {
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
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null) {
                connection.close(); // close the connection
            }
            updateAppointments(); // update the appointments table
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
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
                        AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
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
                        AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
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
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null) {
                connection.close(); // close the connection
            }
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
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
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
        }
    }

    // addCustomer
    @FXML public void addCustomer() throws SQLException {
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
                if (Verificatus.stringCheck(customerName, address, postalCode, phone, country, division)) {
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
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null) {
                connection.close(); // close the connection
            }
            updateCustomers(); // update customers tableview
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
        }
    }

    // modifyCustomer
    /**
     * modifyCustomer modifies the selected customer
     * */
    @FXML public void modifyCustomer() throws SQLException {
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
                System.out.println(customerID);
                String customerName = customerNameField.getText(); // get customer name
                String address = addressField.getText(); // get customer address
                String postalCode = postalField.getText(); // get customer postal code
                String phone = phoneField.getText(); // get customer phone
                String country = countryMenu.getValue(); // get customer country
                String division = divisionMenu.getValue(); // get customer division
                int divisionID = DivisionAccess.getDivisionID(division); // get division ID
                // if the customer fields are not empty, update the customer
                if (Verificatus.stringCheck(customerName, address, postalCode, phone, country, division)) {
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
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null) {
                connection.close(); // close the connection
            }
            updateCustomers(); // update customers tableview
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
        }

    }

    /**
     * deleteCustomer deletes a customer from the database
     * checks if the customer has any appointments
     * if the customer has appointments, an error message is displayed
     * */
    public void deleteCustomer() throws SQLException {
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
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null) {
                connection.close(); // close the connection
            }
            updateCustomers(); // update customers tableview
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
        }
    }

    // Data Validation Tools ///////////////////////////////////////////////////////////////////////////////////////////

    /**
     * findDivisions finds the divisions for the selected country
     * */
    public void findDivisions() throws SQLException {
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
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null) {
                connection.close(); // close the connection
            }
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
        }
    }

    /**
     * enables the division menu if a country is selected
     * prevents incorrect data from being entered
     * */
    public void enableDivisions() throws SQLException {
        if (countryMenu.getValue() != null) {
            findDivisions(); // find divisions for the selected country
            divisionMenu.setDisable(false); // enable division menu
        }
    }

    // a method that alerts the user asking for a password
    public void passwordAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // create an alert
        alert.setTitle("Password"); // set the title
        alert.setHeaderText("Oops! The password you entered was incorrect."); // set the header text
        alert.setContentText("Please enter the correct password."); // set the content text
        alert.showAndWait(); // show the alert
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
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null) {
                connection = JDBC.closeConnection(); // close the connection
            }
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
        }
    }

    /**
     * reportBack gathers intel, reports back to HQ when espionageButton is triggered
     * */
    @FXML public void reportBack() {
        try {
            AgentFord.deBriefing(theSacredScroll); // deBriefing method

        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
        }
    }

    /**
     * updateAppointments updates the appointments tableview
     * clears the tableview, creates a new observable list, establishes connection, sets the items in the tableview to the new observable list
     * */
    public void updateAppointments() throws SQLException {
        try {
            viewAppointments.getItems().clear(); // clear the items in the table
            ObservableList<Appointments> newAppointments = FXCollections.observableArrayList(AppointmentAccess.allAppointments()); // create a new observable list
            connection = JDBC.openConnection(); // establish connection
            viewAppointments.setItems(newAppointments); // set the items in the table to the appointments list
        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null) {
                connection.close(); // close the connection
            }
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
        }
    }

    /**
     * updateCustomers updates the customers tableview
     * clears the tableview, creates a new observable list, establishes connection, sets the items in the tableview to the new observable list
     * */
    public void updateCustomers() throws SQLException {
        try {
            viewCustomers.getItems().clear(); // clear the items in the table
            ObservableList<Customers> newCustomers = FXCollections.observableArrayList(CustomerAccess.getAllCustomers()); // create a new observable list
            connection = JDBC.openConnection(); // establish connection
            viewCustomers.setItems(newCustomers); // set the items in the table to the customers list
        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null) {
                connection.close(); // close the connection
            }
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
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
            String username = LoginController.getUsername(); // get the username
            userCreds.setText(username + " from " + ip); // set the user credentials label to the username
            usersMenu.setItems(UserAccess.getAllUserNames()); // set the user combo box to the usernames
            usersMenu.setValue("Users"); // set the user combo box to the usernames
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

            // observable list of all appointments
            ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList(AppointmentAccess.allAppointments());

            // lambda expression to set the cell value factory for the contact column// create an observable list of appointments
            TableColumn<Appointments, String> contactColumn = new TableColumn<>("Contact"); // create a new table column for the contact
            contactColumn.setCellValueFactory(cellData -> { // set the cell value factory for the contact column
                Appointments appointment = cellData.getValue(); // get the value of the cell
                int contactID = appointment.getContactID(); // get the contact ID
                String contactName = ""; // create a string for the contact name
                try { // try to get the contact name
                    contactName = ContactAccess.getContactName(contactID); // get the contact name
                } catch (SQLException e) { // catch the exception
                    AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
                }
                return new SimpleStringProperty(contactName); // return the contact name
            });
            viewAppointments.getColumns().add(3, contactColumn); // add the contact column to the table

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

            viewAppointments.setItems(appointmentsObservableList); // set the items in the table to the appointments list made above
            viewCustomers.setItems(customersList); // set the items in the table to the customers list
            connection.close(); // close the connection
        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null)
            {
                try {connection.close();}
                catch (SQLException e) {AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
                }
            }
            AgentFord.gatherIntel(theSacredScroll); // Gather Intel
        }
    }
}