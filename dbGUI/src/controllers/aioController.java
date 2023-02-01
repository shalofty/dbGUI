package controllers;

import DataAccess.*;
import Exceptions.ExceptionHandler;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class aioController implements Initializable {

    @FXML public Tab appointmentsTab, customersTab, reportsTab, logTab;
    @FXML public Connection connection = null;
    @FXML public PreparedStatement preparedStatement = null;

    // Appointments Variables ///////////////////////////////////////////////////////////////////////////////////////////
    @FXML public TextField appIDField, contactIDField, userIDField, locationField, customerIDField, typeField, titleField;
    @FXML public DatePicker startDatePicker, endDatePicker;
    @FXML public ComboBox<String> startTimeBox = new ComboBox<>();
    @FXML public ComboBox<String> endTimeBox = new ComboBox<>();
    @FXML public ObservableList<String> times = FXCollections.observableArrayList("08:00", "09:00", "10:00", "11:00", "12:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00");
    @FXML public TextArea descriptionTextArea;
    @FXML public TableView<Appointments> viewAppointments;
    @FXML public RadioButton viewByWeek, viewByMonth;
    @FXML public TableColumn<?, ?> appIDColumn, titleColumn, descriptionColumn, locationColumn, typeColumn, customerIDAppointmentsColumn, userIDColumn, contactIDColumn, startColumn, endColumn;
    @FXML public Button addAppointmentButton, modifyAppointmentButton, deleteAppointmentButton, clearSelectionButton;
    @FXML public ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList(AppointmentAccess.getAllAppointments());
    @FXML public Appointments selectedAppointment;

    // Customer Variables ///////////////////////////////////////////////////////////////////////////////////////////////
    @FXML public TextField customerRecordsIDField, customerNameField, addressField, postalField, phoneField;
    @FXML public ComboBox<String> countryMenu;
    @FXML public ComboBox<String> divisionMenu;
    @FXML public ObservableList<String> countryObservableList = FXCollections.observableArrayList();
    @FXML public ObservableList<String> divisionObservableList = FXCollections.observableArrayList();
    @FXML public TableView<Customers> viewCustomers;
    @FXML public TableColumn<?, ?> customerIDRecordsColumn, nameColumn, phoneColumn, addressColumn, postalColumn, divisionColumn;
    @FXML public Button addCustomerButton, modifyCustomerButton, deleteCustomerButton, clearSelectedCustomerButton;
    @FXML public ObservableList<Customers> customersList = FXCollections.observableArrayList(CustomerAccess.getCustomers());
    @FXML public ObservableList<DivisionAccess> divisionsList = FXCollections.observableArrayList();
    @FXML public ObservableList<String> countryNames = FXCollections.observableArrayList();
    @FXML public ObservableList<String> divisionNames = FXCollections.observableArrayList();
    @FXML public Customers selectedCustomer;


    // Appointments Tab Methods ////////////////////////////////////////////////////////////////////////////////////////
    // viewByWeek
    // viewByMonth

    // timeConversion methods
    // militaryTime
    @FXML public String militaryTime(String time) {
        String[] timeArray = time.split(":");
        int hour = Integer.parseInt(timeArray[0]);
        int minute = Integer.parseInt(timeArray[1]);
        String ampm = timeArray[2].substring(2);

        if (ampm.equals("PM") && hour != 12) {
            hour += 12;
        } else if (ampm.equals("AM") && hour == 12) {
            hour = 0;
        }
        return String.format("%02d:%02d:00", hour, minute);
    }

    // converting users localdatetime to UTC
    @FXML public LocalDateTime convertToUTC(LocalDateTime localDateTime) {
        try {
            ZoneId localZone = ZoneId.systemDefault(); // Local Time Zone
            ZonedDateTime localZonedDateTime = localDateTime.atZone(localZone); // Local Date and Time in Local Time Zone
            Instant instant = localZonedDateTime.toInstant(); // Instant in UTC
            return instant.atZone(ZoneOffset.UTC).toLocalDateTime(); // Instant in UTC converted to LocalDateTime
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
            throw e;
        }
    }

    /**
     * isWithinBusinessHours checks if the appointment is within business hours
     * @param localStartDate - the start date of the appointment
     * @param localStartTime - the start time of the appointment
     * @param localEndDate - the end date of the appointment
     * @param localEndTime - the end time of the appointment
     * @return - true if the appointment is within business hours, false if not
     * */
    @FXML public boolean isWithinBusinessHours(LocalDate localStartDate,
                                               LocalTime localStartTime,
                                               LocalDate localEndDate,
                                               LocalTime localEndTime) {
        try {
            ZoneId easternZoneId = ZoneId.of("America/New_York"); // Eastern Time Zone
            LocalDateTime startDateTime = LocalDateTime.of(localStartDate, localStartTime); // Start Date and Time
            LocalDateTime endDateTime = LocalDateTime.of(localEndDate, localEndTime); // End Date and Time
            ZonedDateTime easternStartDateTime = startDateTime.atZone(easternZoneId); // Start Date and Time in Eastern Time Zone
            ZonedDateTime easternEndDateTime = endDateTime.atZone(easternZoneId); // End Date and Time in Eastern Time Zone

            LocalTime startTime = easternStartDateTime.toLocalTime(); // Start Time in Eastern Time Zone
            LocalTime endTime = easternEndDateTime.toLocalTime(); // End Time in Eastern Time Zone
            LocalTime openTime = LocalTime.of(7, 59); // Business Hours Start Time
            LocalTime closeTime = LocalTime.of(22, 1); // Business Hours End Time
            return startTime.isAfter(openTime) && endTime.isBefore(closeTime); // Returns true if appointment is within business hours
        } catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
            throw e;
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
                contactIDField.setText(String.valueOf(selectedAppointment.getContactID())); // Sets the contact ID text field
                userIDField.setText(String.valueOf(selectedAppointment.getUserID())); // Sets the user ID text field
                locationField.setText(String.valueOf(selectedAppointment.getAppointmentLocation())); // Sets the appointment location text field
                customerIDField.setText(String.valueOf(selectedAppointment.getCustomerID())); // Sets the customer ID text field
                typeField.setText(String.valueOf(selectedAppointment.getAppointmentType())); // Sets the appointment type text field
                descriptionTextArea.setText(String.valueOf(selectedAppointment.getAppointmentDescription())); // Sets the appointment description text field
                startDatePicker.setValue(selectedAppointment.getStartTime().toLocalDate()); // Sets the start date picker
                endDatePicker.setValue(selectedAppointment.getEndTime().toLocalDate()); // Sets the end date picker
                startTimeBox.setValue(selectedAppointment.getStartTime().toLocalTime().toString()); // Sets the start time combo box
                endTimeBox.setValue(selectedAppointment.getEndTime().toLocalTime().toString()); // Sets the end time combo box
                addAppointmentButton.setDisable(true); // Disables the add appointment button
                modifyAppointmentButton.setDisable(false); // Enables the modify appointment button
                deleteAppointmentButton.setDisable(false); // Enables the delete appointment button
            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
            throw e;
        }
    }

    /**
     * clearSelection clears the selected appointment and resets the buttons to their default state
     * */
    @FXML public void clearSelectedAppointment() {
        try {
            selectedAppointment = null; // Clears the selected appointment
            appIDField.clear(); // Clears the appointment ID text field
            titleField.clear(); // Clears the appointment title text field
            contactIDField.clear(); // Clears the contact ID text field
            userIDField.clear(); // Clears the user ID text field
            locationField.clear(); // Clears the appointment location text field
            customerIDField.clear(); // Clears the customer ID text field
            typeField.clear(); // Clears the appointment type text field
            descriptionTextArea.clear(); // Clears the appointment description text field
            startDatePicker.setValue(null); // Clears the start date picker
            endDatePicker.setValue(null); // Clears the end date picker
            startTimeBox.setValue(null); // Clears the start time combo box
            endTimeBox.setValue(null); // Clears the end time combo box
            addAppointmentButton.setDisable(false); // Enables the add appointment button
            modifyAppointmentButton.setDisable(true); // Disables the modify appointment button
            deleteAppointmentButton.setDisable(true); // Disables the delete appointment button
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
            throw e;
        }
    }

    /**
     * generateAppointmentID generates a new appointment ID
     * @return newID
     * */
    @FXML public int generateAppointmentID() throws RuntimeException {
        try {
            Random randy = new Random();
            int maxID = 9999; // max appointment ID is 9999
            int newID = randy.nextInt(maxID); // generate new ID
            // check if the new ID is already in use
            for (Appointments appointment : appointmentsList) {
                // if the new ID is already in use, generate a new ID
                if (appointment.getAppointmentID() == newID) {
                    return generateAppointmentID();
                }
            }
            return newID; // return the new ID
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
            throw e;
        }
    }

    /**
     * generateCustomerID generates a new customer ID
     * @return newID
     * */
    @FXML public int generateCustomerID() throws RuntimeException {
        try {
            Random randy = new Random();
            int maxID = 9999; // max customer ID is 9999
            int newID = randy.nextInt(maxID); // generate new ID
            // check if the new ID is already in use
            for (Customers customer : customersList) {
                // if the new ID is already in use, generate a new ID
                if (customer.getCustomerID() == newID) {
                    return generateCustomerID();
                }
            }
            return newID; // return the new ID
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
            throw e;
        }
    }

    /**
     * addAppointment adds a new appointment to the database
     * */
    @FXML public void addAppointment() throws RuntimeException {
        try {
            connection = JDBC.getConnection(); // establish connection, passing into insertIntoAppointment()
            // generating new appointment ID and getting the values from the text fields
            int newAppointmentID = generateAppointmentID(); // generate new appointment ID
            String newTitle = titleField.getText();  // get title
            String newDescriptionText = descriptionTextArea.getText(); // get description
            String newLocation = locationField.getText(); // get location
            String newType = typeField.getText(); // get type
            // getting the values from the date pickers
            LocalDate localStartDate = startDatePicker.getValue(); // get start date
            LocalDate localEndDate = endDatePicker.getValue(); // get end date
            // getting the values from the combo boxes
            // formatting the date and time values
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); // format time
            LocalTime localStartTime = LocalTime.parse(startTimeBox.getValue(), formatter); // get start time
            LocalTime localEndTime = LocalTime.parse(endTimeBox.getValue(), formatter); // get end time
            // checking if the appointment is within business hours
            if (!isWithinBusinessHours(localStartDate, localStartTime, localEndDate, localEndTime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Error: Appointment is not within business hours.");
                alert.showAndWait();
            }
            // combining the date and time values
            LocalDateTime localStartDateTime = LocalDateTime.of(localStartDate, localStartTime);
            LocalDateTime localEndDateTime = LocalDateTime.of(localEndDate, localEndTime);
            // convert to UTC for storage
            LocalDateTime utcStartDT = convertToUTC(localStartDateTime);
            LocalDateTime utcEndDT = convertToUTC(localEndDateTime);
            // formatting to String
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:");
            String startDT = dtf.format(utcStartDT);
            String endDT = dtf.format(utcEndDT);
            int newCustomerID = Integer.parseInt(customerIDField.getText()); // get customer ID
            int newUserID = Integer.parseInt(userIDField.getText()); // get user ID
            int newContactID = Integer.parseInt(contactIDField.getText()); // get contact ID

            // adding the new appointment to the database
            SQLQueries.INSERT_INTO_APPOINTMENTS_METHOD(connection,
                                                newAppointmentID,
                                                newTitle,
                                                newDescriptionText,
                                                newLocation,
                                                newType,
                                                startDT,
                                                endDT,
                                                newCustomerID,
                                                newUserID,
                                                newContactID);

            updateAppointments(); // update the appointments table
            clearSelectedAppointment(); // clear the selected appointment
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        } finally {
            connection = JDBC.closeConnection(); // close the connection
        }
    }

    /**
     * modifyAppointment modifies an appointment in the database
     * */
    @FXML public void modifyAppointment() throws RuntimeException {
        try {
            // open a connection, pass to updateAppoint() method below
            connection = JDBC.getConnection();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirmation");
            alert.setContentText("Are you sure you want to modify this appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if (selectedAppointment != null && result.isPresent() && result.get() == ButtonType.OK) {
                int appointmentID = selectedAppointment.getAppointmentID(); // getting the appointment ID
                String title = titleField.getText(); // getting the title
                String location = locationField.getText(); // getting the location
                String type = typeField.getText(); // getting the type
                String descriptionText = descriptionTextArea.getText(); // getting the description
                int customerID = Integer.parseInt(customerIDField.getText()); // getting the customer ID
                int contactID = Integer.parseInt(contactIDField.getText()); // getting the contact ID
                int userID = Integer.parseInt(userIDField.getText()); // getting the user ID
                LocalDateTime startDate = LocalDateTime.from(startDatePicker.getValue()); // getting the start date
                LocalDateTime endDate = LocalDateTime.from(endDatePicker.getValue()); // getting the end date
                LocalTime startTime = LocalTime.parse(startTimeBox.getValue()); // getting the start time
                LocalTime endTime = LocalTime.parse(endTimeBox.getValue()); // getting the end time
                // combining the date and time values
                SQLQueries.UPDATE_APPOINTMENT_METHOD(connection,
                                                    appointmentID,
                                                    title,
                                                    descriptionText,
                                                    location,
                                                    type,
                                                    startDate,
                                                    endDate,
                                                    customerID,
                                                    userID,
                                                    contactID);
                updateAppointments(); // update the appointments table
                clearSelectedAppointment(); // clear the text fields
                connection = JDBC.closeConnection(); // close connection
            }
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
        }
        finally {
            connection = JDBC.closeConnection(); // close the connection
        }
    }

    /**
     * deleteAppointment deletes an appointment from the database
     * */
    @FXML public void deleteAppointment() throws RuntimeException {
        try {
            connection = JDBC.getConnection(); // open a connection
            int appointmentID = selectedAppointment.getAppointmentID(); // get the appointment ID
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Appointment");
            alert.setHeaderText("Delete Appointment");
            alert.setContentText("Are you sure you want to delete this appointment?");
            Optional<ButtonType> result;
            result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                SQLQueries.DELETE_APPOINTMENT_METHOD(connection, appointmentID);
                updateAppointments(); // update the appointments table
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
            if (selectedCustomer != null) {
                customerIDField.setText(String.valueOf(selectedCustomer.getCustomerID())); // set the customer ID field
                customerNameField.setText(selectedCustomer.getCustomerName()); // set the customer name field
                addressField.setText(selectedCustomer.getCustomerAddress()); // set the address field
                postalField.setText(selectedCustomer.getPostalCode()); // set the postal code field
                phoneField.setText(selectedCustomer.getCustomerPhone()); // set the phone field
                divisionMenu.valueProperty().set(selectedCustomer.getCountryDivision()); // set the division menu

                ObservableList<CountryAccess> countryList = CountryAccess.getCountries(); // get the country list
                ObservableList<DivisionAccess> divisionList = DivisionAccess.getDivisions(); // get the division list

                // setting the country menu to the country of the selected customer
                // loop through the division list
                for (Division division : divisionList) {
                    // if the division id of the selected customer matches the division id of the division in the list
                    if (division.getDivisionID() == selectedCustomer.getDivisionID()) {
                        // loop through the country list
                        for (Country country : countryList) {
                            // if the country id of the selected customer matches the country id of the country in the list
                            if (country.getCountryID() == division.getCountryID() && selectedCustomer.getDivisionID() == division.getDivisionID()) {
                                // set the country menu to the country of the selected customer
                                countryMenu.valueProperty().set(String.valueOf(country.getCountryName()));
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
        }
        finally {
            if (connection != null) {
                connection = JDBC.closeConnection(); // close the connection
            }
        }
    }

    /**
     * clearSelectedCustomer clears the selected customer
     * enables add button, disables modify and delete buttons to prevent pointer exceptions
     * */
    public void clearSelectedCustomer() throws RuntimeException {
        try {
            selectedCustomer = null; // clear the selected customer
            customerIDField.clear(); // clear the customer ID field
            customerNameField.clear(); // clear the customer name field
            addressField.clear(); // clear the address field
            postalField.clear(); // clear the postal code field
            phoneField.clear(); // clear the phone field
            countryMenu.valueProperty().set(null); // clear the country menu
            divisionMenu.valueProperty().set(null); // clear the division menu
            addCustomerButton.setDisable(false); // enable add button
            modifyCustomerButton.setDisable(true); // disable modify button
            deleteCustomerButton.setDisable(true); // disable delete button
        }
        catch (Exception e) {
            ExceptionHandler.eAlert(e); // eAlert method
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
                int customerID = generateCustomerID(); // get customer id
                String customerName = customerNameField.getText(); // get customer name
                String address = addressField.getText(); // get customer address
                String postalCode = postalField.getText(); // get customer postal code
                String phone = phoneField.getText(); // get customer phone
                String country = countryMenu.getValue(); // get customer country
                String division = divisionMenu.getValue(); // get customer division
                int divisionID = DivisionAccess.getDivisionID(division); // get division ID
                SQLQueries.INSERT_INTO_CUSTOMERS_METHOD(connection,
                        customerID,
                        customerName,
                        address,
                        postalCode,
                        phone,
                        divisionID); // insert into customers
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
        }
    }

    // modifyCustomer
    @FXML public void modifyCustomer(ActionEvent event) {
        System.out.println("Modify Customer");
    }

    // deleteCustomer
    @FXML public void deleteCustomer(ActionEvent event) {
        System.out.println("Delete Customer");
    }

    // Data Validation Tools ///////////////////////////////////////////////////////////////////////////////////////////

    /**
     * checks if a field is empty and returns true if it is
     * */
    public boolean emptyAppointmentField() {
        return appIDField.getText().isEmpty()
                || contactIDField.getText().isEmpty()
                || userIDField.getText().isEmpty()
                || locationField.getText().isEmpty()
                || customerIDField.getText().isEmpty()
                || typeField.getText().isEmpty()
                || titleField.getText().isEmpty()
                || startTimeBox.getValue() == null
                || endTimeBox.getValue() == null
                || startDatePicker.getValue() == null
                || endDatePicker.getValue() == null; // return true if any of the fields are empty
    }

    /**
     * checks if a field is empty and returns true if it is
     * */
    public boolean emptyCustomerField() {
        return customerNameField.getText().isEmpty()
                || addressField.getText().isEmpty()
                || postalField.getText().isEmpty()
                || phoneField.getText().isEmpty()
                || countryMenu.getValue() == null
                || divisionMenu.getValue() == null; // return true if any of the fields are empty
    }

    // Initialize and Support  /////////////////////////////////////////////////////////////////////////////////////////

    // updateAppointments
    public void updateAppointments() {
        viewAppointments.setItems(appointmentsList); // set the items in the table to the appointments list
    }

    // updateCustomers
    public void updateCustomers() {
        try {
            viewCustomers.getItems().clear(); // clear the items in the table
            ObservableList<Customers> newCustomers = FXCollections.observableArrayList(CustomerAccess.getCustomers()); // create a new observable list
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // TODO
            // establishing connection to db
            connection = JDBC.openConnection();

            startTimeBox.setItems(times); // set the start time box items to the times list
            endTimeBox.setItems(times); // set the end time box items to the times list

            // set up the Appointment columns in the table, must match the names of the variables in the model
            appIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID")); // set the cell value factory for the appointment ID column
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle")); // set the cell value factory for the appointment title column
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription")); // set the cell value factory for the appointment description column
            locationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation")); // set the cell value factory for the appointment location column
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType")); // set the cell value factory for the appointment type column
            customerIDAppointmentsColumn.setCellValueFactory(new PropertyValueFactory<>("customerID")); // set the cell value factory for the appointment customer ID column
            userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID")); // set the cell value factory for the appointment user ID column
            contactIDColumn.setCellValueFactory(new PropertyValueFactory<>("contactID")); // set the cell value factory for the appointment contact ID column
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
        }
    }
}