package controllers;

import dataAccess.*;
import exceptions.Confundo;
import javafx.beans.property.SimpleObjectProperty;
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
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CentralNervousSystem implements Initializable {

    @FXML public Connection connection = null;
    @FXML public PreparedStatement preparedStatement = null;
    @FXML public Tab appointmentsTab, customersTab, reportsTab, logTab;
    @FXML public TextArea caseFile, infraredGoggles, theCrimeScene;
    @FXML public static Button espionageButton, exportDBButton, logoutButton;

    // Appointments Variables ///////////////////////////////////////////////////////////////////////////////////////////
    @FXML public Label userCreds;
    @FXML public TextField appIDField, userIDField, locationField, typeField, titleField, startHourField, startMinField, endHourField, endMinField;
    @FXML public DatePicker datePicker;
    @FXML public ComboBox<String> contactsMenu = new ComboBox<>(); // Contacts Menu
    @FXML public ComboBox<String> customersMenu = new ComboBox<>(); // Customers Menu
    @FXML public ComboBox<String> usersMenu = new ComboBox<>(); // Users Menu
    @FXML public ComboBox<String> startHourBox = new ComboBox<>(); // Start Hour Menu
    @FXML public ComboBox<String> endHourBox = new ComboBox<>(); // End Hour Menu
    @FXML public ComboBox<String> startMinBox = new ComboBox<>(); // Start Minute Menu
    @FXML public ComboBox<String> endMinBox = new ComboBox<>(); // End Minute Menu
    @FXML public TextArea descriptionTextArea; // Description Text Area
    @FXML public RadioButton radioWeek, radioMonth, amRadio, pmRadio; // Radio Buttons
    @FXML public TableColumn<?, ?> appIDColumn, titleColumn, descriptionColumn, locationColumn, typeColumn, customerIDAppointmentsColumn, userIDColumn, contactColumn, startColumn, endColumn;
    @FXML public Button addAppointmentButton, modifyAppointmentButton, deleteAppointmentButton, clearSelectionButton;
    @FXML public TableView<Appointments> viewAppointments; // Appointments Table
    @FXML public ObservableList<Appointments> appointmentsList; // Observable List of Appointments
    @FXML public Appointments selectedAppointment; // Selected Appointment

    // Customer Variables ///////////////////////////////////////////////////////////////////////////////////////////////
    @FXML public TextField customerRecordsIDField, customerNameField, addressField, postalField, phoneField;
    @FXML public ComboBox<String> countryMenu; // Country Menu
    @FXML public ComboBox<String> divisionMenu; // Division Menu
    @FXML public ObservableList<String> countryList = FXCollections.observableArrayList();
    @FXML public ObservableList<String> divisionList = FXCollections.observableArrayList();
    @FXML public TableView<Customers> viewCustomers; // Customers Table
    @FXML public TableColumn<?, ?> customerIDRecordsColumn, nameColumn, phoneColumn, addressColumn, postalColumn, divisionColumn;
    @FXML public Button addCustomerButton;
    @FXML public Button modifyCustomerButton;
    @FXML public Button deleteCustomerButton;
    @FXML public Button clearSelectedCustomerButton;
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
            AgentFord.gatherIntel(caseFile); // gather intel
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
            AgentFord.gatherIntel(caseFile); // Gather Intel
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
                datePicker.setValue(selectedAppointment.getAppointmentDate()); // Sets the date picker

                LocalDateTime utcStartTime = selectedAppointment.getStartTime(); // Gets the start time of the appointment
                LocalDateTime utcEndTime = selectedAppointment.getEndTime(); // Gets the end time of the appointment

                LocalDateTime localStartTime = HotTubTimeMachine.convertFromUTC(utcStartTime); // Converts the start time to local time
                LocalDateTime localEndTime = HotTubTimeMachine.convertFromUTC(utcEndTime); // Converts the end time to local time

                LocalTime startTime = localStartTime.toLocalTime(); // Gets the start time of the appointment
                LocalTime endTime = localEndTime.toLocalTime(); // Gets the end time of the appointment

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a"); // Creates a formatter for the time

                startHourBox.setValue(String.valueOf(startTime.format(formatter))); // Sets the start hour combo box
                endHourBox.setValue(String.valueOf(endTime.format(formatter))); // Sets the end hour combo box

                addAppointmentButton.setDisable(true); // Disables the add appointment button
                modifyAppointmentButton.setDisable(false); // Enables the modify appointment button
                deleteAppointmentButton.setDisable(false); // Enables the delete appointment button
            }
        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            AgentFord.gatherIntel(caseFile); // Gather Intel
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
            AgentFord.gatherIntel(caseFile); // Gather Intel
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
            // generating new appointment ID and getting the values from the text fields
            int newAppointmentID = NumberGenie.magicAppointment(); // generate new appointment ID
            String newTitle = titleField.getText();  // get title
            String newDescriptionText = descriptionTextArea.getText(); // get description
            String newLocation = locationField.getText(); // get location
            String newType = typeField.getText(); // get type

            String userName = usersMenu.getValue(); // get username
            int userID = UserAccess.getUserID(userName); // get user ID

            String customerName = customersMenu.getValue(); // get customer name
            int customerID = CustomerAccess.getCustomerID(customerName); // get customer ID

            String contactName = contactsMenu.getValue(); // get contact name from the contacts menu box
            int contactID = ContactAccess.findContactID(contactName); // find contact ID

            LocalDate localDate = datePicker.getValue(); // get the date from the date picker

            String localStartHour = startHourBox.getValue(); // get the start hour from the combo box
            String localEndHour = endHourBox.getValue(); // get the end hour from the combo box

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a"); // create a DateTimeFormatter object

            LocalTime localStartTime = LocalTime.parse(localStartHour, formatter); // parse the start time
            LocalTime localEndTime = LocalTime.parse(localEndHour, formatter); // parse the end time

            Timestamp[] timeStamps = HotTubTimeMachine.interdimensionalWarpDrive(localDate, localStartTime, localEndTime); // use the HotTubTimeMachine to get the timestamps in UTC for storage
            Timestamp dbStartTime = timeStamps[0]; // get the start time
            System.out.println("dbStartTime: " + dbStartTime);
            Timestamp dbEndTime = timeStamps[1]; // get the end time

            // adding the new appointment to the database
            QueryChronicles.INSERT_INTO_APPOINTMENTS_METHOD(connection,
                                                        newAppointmentID,
                                                        newTitle,
                                                        newDescriptionText,
                                                        newLocation,
                                                        newType,
                                                        dbStartTime,
                                                        dbEndTime,
                                                        customerID,
                                                        userID,
                                                        contactID); // insertIntoAppointment method
            successAlert(); // successAlert method
            connection.close(); // close the connection
            clearSelectedAppointment(); // clear the selected appointment
        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        } finally {
            connection = JDBC.closeConnection(); // close the connection
            updateAppointments(); // update the appointments table
            AgentFord.gatherIntel(caseFile); // Gather Intel
        }
    }

    /**
     * modifyAppointment modifies an appointment in the database
     * */
    @FXML public void modifyAppointment() throws Exception {
        try (Connection connection = JDBC.openConnection()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // confirmation alert
            alert.setTitle("Confirmation");
            alert.setHeaderText("You are about to modify an appointment.");
            alert.setContentText("Are you sure you want to modify this appointment?");
            Optional<ButtonType> result = alert.showAndWait(); // show the alert
            if (selectedAppointment != null && result.get() == ButtonType.OK && result.isPresent()) { // if the user clicks OK
                int appointmentID = selectedAppointment.getAppointmentID(); // getting the appointment ID
                String title = titleField.getText(); // getting the title
                String location = locationField.getText(); // getting the location
                String type = typeField.getText(); // getting the type
                String descriptionText = descriptionTextArea.getText(); // getting the description

                String customerName = customersMenu.getValue(); // getting the customer name
                int customerID = CustomerAccess.getCustomerID(customerName); // getting the customer ID

                String userName = usersMenu.getValue(); // getting the username
                int userID = UserAccess.getUserID(userName); // getting the user ID

                String contactName = contactsMenu.getValue(); // getting the contact name
                int contactID = ContactAccess.findContactID(contactName); // getting the contact ID

                LocalDate localDate = datePicker.getValue(); // get the date from the date picker

                String localStartHour = startHourBox.getValue(); // get the start hour from the combo box
                String localEndHour = endHourBox.getValue(); // get the end hour from the combo box

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a"); // create a DateTimeFormatter object

                LocalTime localStartTime = LocalTime.parse(localStartHour, formatter); // parse the start time
                LocalTime localEndTime = LocalTime.parse(localEndHour, formatter); // parse the end time

                Timestamp[] timeStamps = HotTubTimeMachine.interdimensionalWarpDrive(localDate, localStartTime, localEndTime); // use the HotTubTimeMachine to get the timestamps in UTC for storage
                Timestamp dbStartTime = timeStamps[0]; // get the start time
                System.out.println("dbStartTime: " + dbStartTime);
                Timestamp dbEndTime = timeStamps[1]; // get the end time


                // data verification and validation, for use later
                String[] strings = {title, location, type, descriptionText}; // array of strings
                int[] numbers = {customerID, userID, contactID}; // array of numbers
                TextField[] fields = {titleField, locationField, typeField}; // array of text fields

                QueryChronicles.UPDATE_APPOINTMENT_METHOD(connection,
                                                    appointmentID,
                                                    title,
                                                    descriptionText,
                                                    location,
                                                    type,
                                                    dbStartTime,
                                                    dbEndTime,
                                                    customerID,
                                                    userID,
                                                    contactID); // update the appointment
                successAlert(); // successAlert method
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
            AgentFord.gatherIntel(caseFile); // Gather Intel
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
                QueryChronicles.DELETE_APPOINTMENT_METHOD(connection, appointmentID); // delete the appointment
                clearSelectedAppointment(); // clear the selected appointment
                successAlert(); // display success alert
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
            AgentFord.gatherIntel(caseFile); // Gather Intel
        }
    }

    // Customers Tab Methods ///////////////////////////////////////////////////////////////////////////////////////////

    /**
     * selectCustomer selects a customer from the tableview and displays the customer's information in the text fields
     * disables add button, enables modify and delete buttons to prevent pointer exceptions
     * uses addListener to listen for a change in the selected item, which helps with bugginess
     * */
    public void selectCustomer() {
        // add listener to the tableview
        viewCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                selectedCustomer = newValue; // set the selected customer
                if (selectedCustomer != null) {
                    customerRecordsIDField.setText(String.valueOf(selectedCustomer.getCustomerID())); // set the customer ID field
                    customerNameField.setText(selectedCustomer.getCustomerName()); // set the customer name field
                    addressField.setText(selectedCustomer.getCustomerAddress()); // set the address field
                    postalField.setText(selectedCustomer.getPostalCode()); // set the postal code field
                    phoneField.setText(selectedCustomer.getCustomerPhone()); // set the phone field
                    divisionMenu.setValue(selectedCustomer.getCountryDivision()); // set the division menu
                    int divisionID = selectedCustomer.getDivisionID(); // get the division ID from the selected customer
                    String countryName = CountryAccess.innerJoin(divisionID); // get the country name based on divisionID using a join
                    countryMenu.setValue(countryName); // set the country menu
                    addCustomerButton.setDisable(true); // disable add button, enable modify and delete buttons to prevent pointer exceptions
                    modifyCustomerButton.setDisable(false); // disable add button, enable modify and delete buttons to prevent pointer exceptions
                    deleteCustomerButton.setDisable(false); // disable add button, enable modify and delete buttons to prevent pointer exceptions
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                AgentFord.gatherIntel(caseFile); // Gather Intel
            }
        });

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
            AgentFord.gatherIntel(caseFile); // Gather Intel
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
                String id = String.valueOf(customerID); // convert customer id to string
                String customerName = customerNameField.getText(); // get customer name
                String address = addressField.getText(); // get customer address
                String postalCode = postalField.getText(); // get customer postal code
                String phone = phoneField.getText(); // get customer phone
                String country = countryMenu.getValue(); // get customer country
                String division = divisionMenu.getValue(); // get customer division
                int divisionID = DivisionAccess.getDivisionID(division); // get division ID

                QueryChronicles.INSERT_INTO_CUSTOMERS_METHOD(connection,
                                                        customerID,
                                                        customerName,
                                                        address,
                                                        postalCode,
                                                        phone,
                                                        divisionID);

                clearSelectedCustomer(); // clear selected customer
                updateCustomersMenu(); // update customers menu to show new customer
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
            AgentFord.gatherIntel(caseFile); // Gather Intel
        }
    }

    /**
     * modifyCustomer modifies the selected customer
     * NOTE: slight bug with this method I've noticed, and with the customer tab in general.
     * Sometimes I have to double click the entries in order for the data to show properly, although not always.
     * I'm not sure why this is happening, but it's not a huge deal for now.
     * */
    @FXML public void modifyCustomer() throws SQLException {
        try (Connection connection = JDBC.openConnection()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modify Customer");
            alert.setHeaderText("Modifying Customer in Database");
            alert.setContentText("Are you sure you want to modify this customer?");
            Optional<ButtonType> result;
            result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int customerID = selectedCustomer.getCustomerID(); // get customer id
                String customerName = customerNameField.getText(); // get customer name
                String address = addressField.getText(); // get customer address
                String postalCode = postalField.getText(); // get customer postal code
                String phone = phoneField.getText(); // get customer phone
                String country = countryMenu.getValue(); // get customer country
                String division = divisionMenu.getValue(); // get customer division
                int divisionID = DivisionAccess.getDivisionID(division); // get division ID

                QueryChronicles.UPDATE_CUSTOMER_METHOD(connection,
                                                    customerID,
                                                    customerName,
                                                    address,
                                                    postalCode,
                                                    phone,
                                                    divisionID); // update customers

                clearSelectedCustomer(); // clear selected customer
                updateCustomers(); // update customers tableview
                updateCustomersMenu(); // update customers menu
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
            AgentFord.gatherIntel(caseFile); // Gather Intel
        }

    }

    /**
     * deleteCustomer deletes a customer from the database
     * checks if the customer has any appointments
     * if the customer has appointments, an error message is displayed
     * */
    @FXML public void deleteCustomer() throws SQLException {
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
                QueryChronicles.DELETE_CUSTOMER_METHOD(connection, customerID); // delete customer
                clearSelectedCustomer(); // clear selected customer
                updateCustomersMenu(); // update customers menu in add appointment to prevent null pointer exception
                successAlert(); // display success alert
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
            AgentFord.gatherIntel(caseFile); // Gather Intel
        }
    }

    /**
     * updateCustomersMenu updates the customers divisions menu in the add appointment screen
     * NOTE: I've noticed a sort of buginess with this feature, sometimes a double click is necessary. I'm not sure why this is happening.
     * */
    @FXML public void updateDivisionsMenu() {
        countryMenu.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                String selectedCountry = countryMenu.getValue(); // get selected country
                if (selectedCountry != null) {
                    divisionMenu.setItems(null); // clear division menu
                    int countryID = CountryAccess.getCountryNamebyID(selectedCountry); // get country ID
                    ObservableList<String> divisions = DivisionAccess.getDivisionList(countryID); // get divisions
                    divisionMenu.setItems(divisions); // set divisions menu
                    divisionMenu.setDisable(false); // enable divisions menu
                } else {
                    divisionMenu.setDisable(true);
                    return; // if no country is selected, return
                }
                // set the division menu to the appropriate value based on the selected country
                switch (selectedCountry) {
                    case "U.S": // if the selected country is the U.S, set the division menu to state
                        divisionMenu.setValue("State");
                        break;
                    case "Canada": // if the selected country is Canada, set the division menu to province
                        divisionMenu.setValue("Province");
                        break;
                    case "UK": // if the selected country is the UK, set the division menu to country
                        divisionMenu.setValue("Country");
                        break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                AgentFord.gatherIntel(caseFile); // Gather Intel
            }
        });
    }

    /**
     * reportBack gathers intel, reports back to HQ when espionageButton is triggered
     * @method deBriefing exports the intel to a text file
     * @method apprehendException displays the exception in the crime scene text area
     * @method gatherIntel gathers intel and displays it in the text area
     * */
    @FXML public void reportBack() {
        try {
            AgentFord.deBriefing(caseFile); // deBriefing method exports the intel to a text file

        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            AgentFord.gatherIntel(caseFile); // Gather Intel
        }
    }

    /**
     * quick scratch code, create package for these later
     * */
    public void successAlert() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText("Success");
        successAlert.setContentText("Operation successful.");
        successAlert.showAndWait();
    }

    /**
     * updateAppointments updates the appointments tableview
     * clears the tableview, creates a new observable list, establishes connection, sets the items in the tableview to the new observable list
     * */
    @FXML public void updateAppointments() throws SQLException {
        try {
            viewAppointments.getItems().clear(); // clear the items in the table
            ObservableList<Appointments> newAppointments = FXCollections.observableArrayList(AppointmentAccess.allAppointments()); // create a new observable list
            viewAppointments.setItems(newAppointments); // set the items in the table to the appointments list
        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null) {
                connection.close(); // close the connection
            }
            AgentFord.gatherIntel(caseFile); // Gather Intel
        }
    }

    /**
     * updateCustomers updates the customers tableview
     * clears the tableview, creates a new observable list, establishes connection, sets the items in the tableview to the new observable list
     * */
    @FXML public void updateCustomers() throws SQLException {
        try {
            viewCustomers.getItems().clear(); // clear the items in the table
            ObservableList<Customers> newCustomers = FXCollections.observableArrayList(CustomerAccess.getAllCustomers()); // create a new observable list
            viewCustomers.setItems(newCustomers); // set the items in the table to the customers list
        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null) {
                connection.close(); // close the connection
            }
            AgentFord.gatherIntel(caseFile); // Gather Intel
        }
    }

    /**
     * updateCustomersMenu updates the customers menu
     * clears the menu, creates a new observable list, establishes connection, sets the items in the menu to the new observable list
     * */
    @FXML public void updateCustomersMenu() throws SQLException {
        try {
            customersMenu.getItems().clear(); // clear the items in the table
            ObservableList<String> newCustomers = FXCollections.observableArrayList(CustomerAccess.getAllCustomerNameStrings()); // create a new observable list
            customersMenu.setItems(newCustomers); // set the items in the table to the customers list
        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null) {
                connection.close(); // close the connection
            }
            AgentFord.gatherIntel(caseFile); // Gather Intel
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
            InetAddress ip = InetAddress.getLocalHost(); // get the local host
            String username = LoginController.getUsername(); // get the username
            userCreds.setText(username + " from " + ip); // set the user credentials label to the username

            usersMenu.setItems(UserAccess.getAllUserNames()); // set the user combo box to the usernames
            usersMenu.setValue("Users"); // set the user combo box to the usernames

            customersMenu.setItems(CustomerAccess.getAllCustomerNameStrings()); // Sets the customer combo box
            customersMenu.setValue("Customers"); // Sets the customer combo box

            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai")); // set the default time zone to Shanghai for fun during testing
            // US/Mountain for Arizona, US/Eastern for New York, Europe/London for London, Canada/Eastern for Montreal
            // Asia/Shanghai for Shanghai, Australia/Sydney for Sydney, Africa/Johannesburg for Johannesburg just to check

            // set the start and end hour combo boxes, only adding hours between 8am and 10pm which are business hours
            IntStream.rangeClosed(8, 22).forEach(hour -> { // start at 8 and end at 22
                for (int minute = 0; minute < 60; minute += 30) { // increment by 30 minutes
                    if (hour == 22 && minute == 30) { // if the hour is 22 and the minute is 30, break
                        return;
                    }
                    LocalTime estTime = LocalTime.of(hour, minute); // create a local time object
                    ZoneId estZone = ZoneId.of("America/New_York"); // create a zone id object for est
                    ZonedDateTime estZonedTime = ZonedDateTime.of(LocalDate.now(), estTime, estZone); // create a zoned date time object for est
                    ZoneId localZone = ZoneId.systemDefault(); // create a zone id object for the local time zone
                    ZonedDateTime localZonedTime = estZonedTime.withZoneSameInstant(localZone); // create a zoned date time object for the local time zone
                    startHourBox.getItems().add(localZonedTime.format(DateTimeFormatter.ofPattern("h:mm a"))); // add the time to the start hour combo box
                    endHourBox.getItems().add(localZonedTime.format(DateTimeFormatter.ofPattern("h:mm a"))); // add the time to the end hour combo box
                }
            });

            // lambda expression to populate minute boxes
            IntStream.rangeClosed(0, 59).forEach(minute -> {
                startMinBox.getItems().add(String.valueOf(minute)); // add the minutes to the start minute combo box
                endMinBox.getItems().add(String.valueOf(minute)); // add the minutes to the end minute combo box
            });

            // observable list of all appointments
            ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList(AppointmentAccess.allAppointments());
            // observable list of all customers
            ObservableList<Customers> customersList = FXCollections.observableArrayList(CustomerAccess.getAllCustomers());

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

            // lambda expression to set the cell value factory for the start time column
            TableColumn<Appointments, String> startColumn = new TableColumn<>("Start (Local)"); // create a new table column for the start time
            startColumn.setCellValueFactory(cellData -> { // set the cell value factory for the start time column
                Appointments appointment = cellData.getValue(); // get the value of the cell
                LocalDateTime start = appointment.getStartTime(); // get the start time
                ZoneId localZone = ZoneId.systemDefault(); // get the local zone
                ZonedDateTime localStart = start.atZone(ZoneId.of("UTC")).withZoneSameInstant(localZone); // convert the start time to the local zone
                return new SimpleStringProperty(localStart.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))); // return the start time
            });
            viewAppointments.getColumns().add(6, startColumn); // add the start time column to the table

            // lambda expression to set the cell value factory for the end time column
            TableColumn<Appointments, String> endColumn = new TableColumn<>("End (Local)"); // create a new table column for the end time
            endColumn.setCellValueFactory(cellData -> { // set the cell value factory for the end time column
                Appointments appointment = cellData.getValue(); // get the value of the cell
                LocalDateTime end = appointment.getEndTime(); // get the end time
                ZoneId localZone = ZoneId.systemDefault(); // get the local zone
                ZonedDateTime localStart = end.atZone(ZoneId.of("UTC")).withZoneSameInstant(localZone); // convert the end time to the local zone
                return new SimpleStringProperty(localStart.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))); // return the end time
            });
            viewAppointments.getColumns().add(7, endColumn); // add the end time column to the table

            // set up the Appointment columns in the table, must match the names of the variables in the model
            appIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID")); // set the cell value factory for the appointment ID column
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle")); // set the cell value factory for the appointment title column
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription")); // set the cell value factory for the appointment description column
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation")); // set the cell value factory for the appointment location column
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType")); // set the cell value factory for the appointment type column
            customerIDAppointmentsColumn.setCellValueFactory(new PropertyValueFactory<>("customerID")); // set the cell value factory for the appointment customer ID column
            userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID")); // set the cell value factory for the appointment user ID column

            // set up the Customer columns in the table, must match the names of the variables in the model
            customerIDRecordsColumn.setCellValueFactory(new PropertyValueFactory<>("customerID")); // set the cell value factory for the customer ID column
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName")); // set the cell value factory for the customer name column
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress")); // set the cell value factory for the customer address column
            postalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode")); // set the cell value factory for the customer postal code column
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhone")); // set the cell value factory for the customer phone column
            divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID")); // set the cell value factory for the customer division ID column

            if (countryMenu.getValue() == null) {
                // call getCountries method to populate an ObservableList to populate the country menu
                ObservableList<CountryAccess> countriesList = CountryAccess.getCountries(); // get the list of countries from the db
                countriesList.stream().map(Country::getCountryName).forEach(countryList::add); // add the country names to the observable list
                countryMenu.setItems(countryList); // set the items in the country menu to the observable list
            }

            ObservableList<String> contactsList = FXCollections.observableArrayList(ContactAccess.getContactNames()); // get the list of contact names from the db
            contactsMenu.setItems(contactsList); // set the items in the contact menu to the observable list

            viewAppointments.setItems(appointmentsList); // set the items in the table to the appointments list made above
            viewCustomers.setItems(customersList); // set the items in the table to the customers list
        }
        catch (Exception e) {
            e.printStackTrace();
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            if (connection != null)
            {
                try {connection.close();}
                catch (SQLException e) {AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
                }
            }
            AgentFord.gatherIntel(caseFile); // Gather Intel
        }
    }
}