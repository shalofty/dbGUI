package controllers;

import dataAccess.*;
import exceptions.GateKeeper;
import exceptions.Siren;
import javafx.beans.binding.Bindings;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
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

/**
 * CentralNervousSystem is the controller class for the main GUI
 * There's a lot of code here. I tried to spread it out throughout different classes and packages.
 * But it started to seem like some packages didn't make much sense, etc. SO there's a lot of code here.
 * I also tried to be creative with naming conventions to add some flair and humor to the project.
 *
 * I'm not sure if I'm supposed to comment every single line of code, but I did my best to comment the important stuff.
 * There's pretty much a comment explaining every line of code, just in case it's needed.
 *
 * */
public class CentralNervousSystem implements Initializable {


    @FXML public Connection connection = null;
    @FXML public PreparedStatement preparedStatement = null;
    @FXML public Tab appointmentsTab, customersTab, reportsTab, logTab;
    @FXML public TextArea caseFile, infraredGoggles, theCrimeScene;
    @FXML public static Button espionageButton, exportDBButton, logoutButton;
    @FXML public Label userCreds;
    @FXML public TextField appIDField, userIDField, locationField, typeField, titleField;
    @FXML public DatePicker datePicker;
    @FXML public ComboBox<String> contactsMenu = new ComboBox<>();
    @FXML public ComboBox<String> reportContactMenu = new ComboBox<>();
    @FXML public ComboBox<String> customersMenu = new ComboBox<>();
    @FXML public ComboBox<String> usersMenu = new ComboBox<>();
    @FXML public ComboBox<String> startHourBox = new ComboBox<>();
    @FXML public ComboBox<String> endHourBox = new ComboBox<>();
    @FXML public ComboBox<String> startMinBox = new ComboBox<>();
    @FXML public ComboBox<String> monthsMenu = new ComboBox<>();
    @FXML public TextArea descriptionTextArea;
    @FXML public RadioButton radioWeek, radioMonth;
    @FXML public TableColumn<Appointments, String> appIDColumn, titleColumn, descriptionColumn, locationColumn, typeColumn, customerIDAppointmentsColumn, userIDColumn, startColumn, endColumn, contactColumn;
    @FXML public TableColumn<Appointments, String> reportIDColumn, reportTitleColumn, reportDescriptionColumn, reportLocationColumn, reportTypeColumn, reportCIDColumn, reportStartColumn, reportEndColumn;
    @FXML public Button addAppointmentButton, modifyAppointmentButton, deleteAppointmentButton, clearSelectionButton;
    @FXML public TableView<Appointments> viewAppointments;
    @FXML public TableView<Appointments> viewContactReport;
    @FXML public ObservableList<Appointments> appointmentsList;
    @FXML public ObservableList<Appointments> contactSchedule;
    @FXML public Appointments selectedAppointment;
    @FXML public TextField customerRecordsIDField, customerNameField, addressField, postalField, phoneField;
    @FXML public ComboBox<String> countryMenu;
    @FXML public ComboBox<String> divisionMenu;
    @FXML public ObservableList<String> countryList = FXCollections.observableArrayList();
    @FXML public TableView<Customers> viewCustomers;
    @FXML public TableColumn<?, ?> customerIDRecordsColumn, nameColumn, phoneColumn, addressColumn, postalColumn, divisionColumn;
    @FXML public Button addCustomerButton, modifyCustomerButton, deleteCustomerButton, clearSelectedCustomerButton;
    @FXML public Button generateContactScheduleButton, chartButton, countByMonthTypeButton;
    @FXML public Customers selectedCustomer;
    @FXML public Tab customersReportTab;
    @FXML public BarChart<String, Integer> customersChart;
    @FXML public ObservableList<String> typesList;
    @FXML public ComboBox<String> typesMenu = new ComboBox<>();
    @FXML public ObservableList<String> monthsList = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    @FXML public TextField numberOfAppointments;

    /**
     * reportTypeMonth() is the method that runs when the user clicks the "Generate Report" button.
     * It gets the selected type and month from the menus, and then gets the number of appointments for that type and month.
     * */
    @FXML public void reportTypeMonth() throws Exception {
        try {
            String type = typesMenu.getValue(); // get the selected type
            String month = monthsMenu.getValue(); // get the selected month
            int monthNum = getMonthNum(month); // get the corresponding number for the selected month
            int appointments = QueryChronicles.countByTypeAndMonth(type, monthNum); // get the number of appointments
            numberOfAppointments.setText(String.valueOf(appointments)); // set the text field to the number of appointment
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * getMonthNum() is a helper method for reportTypeMonth().
     * */
    @FXML public int getMonthNum(String month) {
        int monthNum = 0;
        switch (month) {
            case "January":
                monthNum = 1;
                break;
            case "February":
                monthNum = 2;
                break;
            case "March":
                monthNum = 3;
                break;
            case "April":
                monthNum = 4;
                break;
            case "May":
                monthNum = 5;
                break;
            case "June":
                monthNum = 6;
                break;
            case "July":
                monthNum = 7;
                break;
            case "August":
                monthNum = 8;
                break;
            case "September":
                monthNum = 9;
                break;
            case "October":
                monthNum = 10;
                break;
            case "November":
                monthNum = 11;
                break;
            case "December":
                monthNum = 12;
                break;
        }
        return monthNum;
    }

    /**
     * generateSchedule generates a schedule for the selected contact
     * repurposes code from the intialize method to generate a table view of appointments for the selected contact
     * */
    @FXML public void generateSchedule() throws SQLException {
        try (Connection connection = JDBC.openConnection()) {
            String contactName = reportContactMenu.getValue(); // get the selected contact name
            int contactID = ContactAccess.findContactID(contactName); // get the contact ID
            contactSchedule = QueryChronicles.generateSchedule(connection , contactID); // get the contact schedule

            // lambda expression to set the cell value factory for the start time column
            reportStartColumn.setCellValueFactory(cellData -> { // set the cell value factory for the start time column
                Appointments appointment = cellData.getValue(); // get the value of the cell
                LocalDateTime start = appointment.getStartTime(); // get the start time
                // ZoneId localZone = ZoneId.of("Asia/Shanghai"); // get the local zone for Shanghai for testing
                ZoneId localZone = ZoneId.systemDefault(); // get the local zone, comment this out for testing other zones
                ZonedDateTime localStart = start.atZone(ZoneId.of("UTC")).withZoneSameInstant(localZone); // convert the start time to the local zone from UTC
                return new SimpleStringProperty(localStart.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))); // return the start time
            });

            // lambda expression to set the cell value factory for the end time column
            reportEndColumn.setCellValueFactory(cellData -> { // set the cell value factory for the end time column
                Appointments appointment = cellData.getValue(); // get the value of the cell
                LocalDateTime end = appointment.getEndTime(); // get the end time
                // ZoneId localZone = ZoneId.of("Asia/Shanghai"); // get the local zone for Shanghai
                ZoneId localZone = ZoneId.systemDefault(); // get the local zone, comment this out for testing other zones
                ZonedDateTime localStart = end.atZone(ZoneId.of("UTC")).withZoneSameInstant(localZone); // convert the end time to the local zone from UTC
                return new SimpleStringProperty(localStart.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))); // return the end time
            });

            // set the cell value factory for the other columns
            reportIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID")); // set the appointment ID column
            reportTitleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle")); // set the title column
            reportDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription")); // set the description column
            reportLocationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation")); // set the location column
            reportTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType")); // set the type column
            reportCIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID")); // set the customer ID column
            viewContactReport.setItems(contactSchedule); // set the items in the tableview to the contact schedule
        }
        catch (Exception e) {
            e.printStackTrace(); // print the stack trace
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            AgentFord.gatherIntel(caseFile); // Gather Intel
        }
    }

    /**
     * populateAppointmentsTable populates the appointments tableview with all appointments
     * This is my extra report that I have control over. I decided to go with something visual.
     * I wanted to make a bar chart that shows the number of appointments per month.
     * */
    @FXML public void populateCustomersChart() throws Exception {
        int[] months = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        for (int month : months) {
            int count = QueryChronicles.countAppointmentsByMonth(month);
            String monthName = Month.of(month).name();
            series.getData().add(new XYChart.Data<>(monthName, count));
        }

        customersChart.getData().add(series);
        chartButton.setDisable(true); // disables the chart button
    }

    /**
     * clearChart clears the chart when the user leaves the tab
     * */
    @FXML public void clearChart() {
        customersChart.getData().clear(); // clears the data from the chart
        chartButton.setDisable(false); // enables the chart button
    }

    /**
     * nextMonth populates the tableview with appointments within 30 days
     * */
    @FXML public void nextMonth() throws SQLException {
        if (radioMonth.isSelected()) {
            radioWeek.setSelected(false); // unselects the week radio button
            appointmentsList = AppointmentAccess.allAppointmentsWithin30Days(); // populates the appointments list with appointments within 30 days
            viewAppointments.setItems(appointmentsList); // sets the tableview to the appointments list
        }
        else {
            updateAppointments(); // updates the appointments tableview
        }
    }

    /**
     * nextWeek populates the tableview with appointments within 7 days
     * */
    @FXML public void nextWeek() throws SQLException {
        if (radioWeek.isSelected()) {
            radioMonth.setSelected(false); // unselects the month radio button
            appointmentsList = AppointmentAccess.allAppointmentsWithin7Days(); // populates the appointments list with appointments within 7 days
            viewAppointments.setItems(appointmentsList); // sets the tableview to the appointments list
        }
        else {
            updateAppointments(); // updates the appointments tableview
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
                userIDField.setText(String.valueOf(selectedAppointment.getUserID())); // Sets the user ID text field
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
            viewAppointments.getSelectionModel().clearSelection(); // Clears the selected appointment
            contactsMenu.getSelectionModel().clearSelection(); // Clears the contact combo box
            customersMenu.getSelectionModel().clearSelection(); // Clears the customer combo box
            usersMenu.getSelectionModel().clearSelection(); // Clears the user combo box

            // addAppointmentButton.setDisable(false); // Enables the add appointment button, need to test functionality with boolean bound methods
            Stream.of(appIDField, titleField).forEach(TextInputControl::clear); // Clears the appointment ID and title text fields
            Stream.of(userIDField, locationField, typeField, descriptionTextArea).forEach(TextInputControl::clear); // Clears the user ID, location, description and type text fields
            Stream.of(datePicker, startHourBox, endHourBox, startMinBox, startHourBox).forEach(c->c.setValue(null)); // Clears the start date, end date, start time and end time combo boxes
            Stream.of(modifyAppointmentButton, deleteAppointmentButton).forEach(c->c.setDisable(true)); // Disables the modify and delete appointment buttons
        }
        catch (Exception e) {
            e.printStackTrace();
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            contactsMenu.setPromptText("Contacts");
            usersMenu.setPromptText("Users");
            contactsMenu.setPromptText("Contacts");
            AgentFord.gatherIntel(caseFile); // Gather Intel
        }
    }

    /**
     * addAppointment adds a new appointment to the database
     * the startHourBox and endHourBox combo boxes are populated with times relative to the user's time zone, but only between the EST hours of business
     * this prevents the user from scheduling appointments outside of business hours
     * the logic for handling the time zone conversion is in the HotTubTimeMachine class and the initialize method in the MainController class
     * */
    @FXML public void addAppointment() throws Exception {
        try (Connection connection = JDBC.openConnection())
        {
            if (Siren.appointmentConfirm()) {
                // generating new appointment ID and getting the values from the text fields
                int newAppointmentID = NumberGenie.magicAppointment(); // generate new appointment ID
                String newTitle = titleField.getText();  // get title
                String newDescriptionText = descriptionTextArea.getText(); // get description
                String newLocation = locationField.getText(); // get location
                String newType = typeField.getText(); // get type
                String userName = usersMenu.getValue(); // get username
                String customerName = customersMenu.getValue(); // get customer name
                String contactName = contactsMenu.getValue(); // get contact name from the contacts menu box
                LocalDate localDate = datePicker.getValue(); // get the date from the date picker
                String localStartTime = startHourBox.getValue(); // get the start hour from the combo box
                String localEndTime = endHourBox.getValue(); // get the end hour from the combo box

                // finding ID's based on the names
                int userID = UserAccess.getUserID(userName); // get user ID
                int customerID = CustomerAccess.getCustomerID(customerName); // get customer ID
                int contactID = ContactAccess.findContactID(contactName); // find contact ID

                // time manipulation
                LocalTime[] localTimes = HotTubTimeMachine.timeTransmutation(localStartTime, localEndTime); // use the HotTubTimeMachine to format the times
                LocalTime transmutedStartTime = localTimes[0]; // get the start time
                LocalTime transmutedEndTime = localTimes[1]; // get the end time
                Timestamp[] timeStamps = HotTubTimeMachine.interdimensionalUTCWarpDrive(localDate, transmutedStartTime, transmutedEndTime); // use the HotTubTimeMachine to get the timestamps in UTC for storage
                Timestamp dbStartTime = timeStamps[0]; // get the start time
                Timestamp dbEndTime = timeStamps[1]; // get the end time

                // use timestamp to see if appointments overlap with any other appointments
                boolean overLap = QueryChronicles.CHECK_APPOINTMENT_OVERLAP_METHOD(connection, dbStartTime, dbEndTime); // checkAppointmentOverlap method
                if (overLap) {
                    Siren.overlapAlert(); // overlapAlert method
                    return;
                }
                else {
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
                    Siren.successAlert(); // successAlert method
                    updateAppointments(); // update the appointments table
                }
            }

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
        try (Connection connection = JDBC.openConnection())
        {
            if (selectedAppointment != null && Siren.appointmentConfirm()) { // if the user clicks OK
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
                Timestamp[] timeStamps = HotTubTimeMachine.interdimensionalUTCWarpDrive(localDate, localStartTime, localEndTime); // use the HotTubTimeMachine to get the timestamps in UTC for storage
                Timestamp dbStartTime = timeStamps[0]; // get the start time in UTC
                Timestamp dbEndTime = timeStamps[1]; // get the end time in UTC

                // use timestamp to see if appointments overlap with any other appointments
                boolean overLap = QueryChronicles.CHECK_APPOINTMENT_OVERLAP_METHOD(connection, dbStartTime, dbEndTime); // checkAppointmentOverlap method
                if (overLap) {
                    Siren.overlapAlert(); // overlapAlert method
                    return;
                }
                else {
                    // adding the new appointment to the database
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
                    Siren.successAlert(); // successAlert method
                    clearSelectedAppointment(); // clear the text fields
                    updateAppointments(); // update the appointments table
                }
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
        try (Connection connection = JDBC.openConnection())
        {
            // if the user clicks OK
            if (Siren.deleteConfirm()) {
                int appointmentID = selectedAppointment.getAppointmentID(); // get the appointment ID
                QueryChronicles.DELETE_APPOINTMENT_METHOD(connection, appointmentID); // delete the appointment
                clearSelectedAppointment(); // clear the selected appointment
                Siren.successAlert(); // display success alert
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

    /**
     * selectCustomer selects a customer from the tableview and displays the customer's information in the text fields
     * disables add button, enables modify and delete buttons to prevent pointer exceptions
     * uses addListener to listen for a change in the selected item, which helps with bugginess
     * */
    @FXML public void selectCustomer() {
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
    @FXML public void clearSelectedCustomer() throws RuntimeException {
        try {
            viewCustomers.getSelectionModel().clearSelection(); // clear the selection
            Stream.of(countryMenu, divisionMenu).forEach(c->c.getSelectionModel().clearSelection()); // clear the country and division menus
            countryMenu.setPromptText("Country"); // set the country menu prompt text
            divisionMenu.setDisable(true); // disable the division menu
            Stream.of(customerRecordsIDField,
                    customerNameField,
                    addressField,
                    postalField,
                    phoneField).forEach(TextInputControl::clear); // clear the text fields
            Stream.of(modifyCustomerButton, deleteCustomerButton).forEach(c -> c.setDisable(true)); // disable modify and delete buttons
        }
        catch (Exception e) {
            AgentFord.apprehendException(e, theCrimeScene); // if an exception is thrown, display the exception in the crime scene text area
        }
        finally {
            AgentFord.gatherIntel(caseFile); // Gather Intel
        }
    }

    /**
     * addCustomer adds a customer to the database
     * */
    @FXML public void addCustomer() throws SQLException {
        try (Connection connection = JDBC.openConnection())
        {
            if (Siren.addCustomerConfirm()) { // if the user clicks OK
                int customerID = NumberGenie.magicCustomer(); // get customer id
                String id = String.valueOf(customerID); // convert customer id to string
                String customerName = customerNameField.getText(); // get customer name
                String address = addressField.getText(); // get customer address
                String postalCode = postalField.getText(); // get customer postal code
                String phone = phoneField.getText(); // get customer phone
                String country = countryMenu.getValue(); // get customer country
                String division = divisionMenu.getValue(); // get customer division
                int divisionID = DivisionAccess.getDivisionID(division); // get division ID

                String[] strings = {address, phone, postalCode, country, customerName}; // create string array of address and phone

                if (GateKeeper.correctCustomer(strings)) {
                    QueryChronicles.INSERT_INTO_CUSTOMERS_METHOD(connection,
                                                                customerID,
                                                                customerName,
                                                                address,
                                                                postalCode,
                                                                phone,
                                                                divisionID); // insert customer into database
                    Siren.successAlert(); // display success alert
                    clearSelectedCustomer(); // clear selected customer
                    updateCustomersMenu(); // update customers menu to show new customer
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
        try (Connection connection = JDBC.openConnection())
        {
            // if the user clicks OK
            if (Siren.modifyCustomerConfirm()) {
                int customerID = selectedCustomer.getCustomerID(); // get customer id
                String customerName = customerNameField.getText(); // get customer name
                String address = addressField.getText(); // get customer address
                String postalCode = postalField.getText(); // get customer postal code
                String phone = phoneField.getText(); // get customer phone
                String country = countryMenu.getValue(); // get customer country
                String division = divisionMenu.getValue(); // get customer division
                int divisionID = DivisionAccess.getDivisionID(division); // get division ID

                String[] strings = {address, phone, postalCode, country, customerName}; // create string array of address and phone
                if (GateKeeper.correctCustomer(strings)) {
                    QueryChronicles.UPDATE_CUSTOMER_METHOD(connection,
                                                            customerID,
                                                            customerName,
                                                            address,
                                                            postalCode,
                                                            phone,
                                                            divisionID); // insert customer into database
                    Siren.successAlert(); // display success alert
                    clearSelectedCustomer(); // clear selected customer
                    updateCustomers(); // update customers tableview
                    updateCustomersMenu(); // update customers menu
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
        try (Connection connection = JDBC.openConnection())
        {
            // if the user clicks OK and the customer has no appointments
            if (Siren.deleteConfirm() && AppointmentAccess.noAppointments(selectedCustomer.getCustomerID())) {
                int customerID = selectedCustomer.getCustomerID(); // get customer id
                QueryChronicles.DELETE_CUSTOMER_METHOD(connection, customerID); // delete customer
                clearSelectedCustomer(); // clear selected customer
                updateCustomersMenu(); // update customers menu in add appointment to prevent null pointer exception
                Siren.successAlert(); // display success alert
            } else if (!AppointmentAccess.noAppointments(selectedCustomer.getCustomerID())) {
                Siren.hasAppointments(); // display error message
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
     * findAppointments finds appointments within 15 minutes of the current time
     * if an appointment is found, an alert is displayed
     * Called in the initialize method
     * */
    @FXML public void findAppointments() throws SQLException {
        ObservableList<Appointments> appointments = AppointmentAccess.allAppointments(); // get appointments
        for (Appointments appointment : appointments) {
            LocalDateTime now = LocalDateTime.now(); // get current, local time
            LocalDateTime appointmentTimeUTC = appointment.getStartTime(); // get appointment time
            LocalDateTime localDT = HotTubTimeMachine.convertFromUTC(appointmentTimeUTC); // convert appointment time to local time
            Duration duration = Duration.between(now, localDT); // get duration between now and appointment time
            long minutes = duration.toMinutes(); // convert duration to minutes
            if (minutes <= 15 && minutes >= 0) { // if the appointment is within 15 minutes
                Siren.fifteenMinuteAlert(appointment); // display appointment alert
            } else {
                Siren.noImpendingAppointments(); // display no appointments alert
                return; // return
            }
        }
    }

    /**
     * updateCustomersMenu updates the customers divisions menu in the add appointment screen
     * NOTE: I noticed a bugginess when it was set to on action in scenebuilder. I changed the listener to on click, and it seems much more responsive.
     * */
    @FXML public void updateDivisionsMenu() {
        countryMenu.valueProperty().addListener((observable, oldValue, newValue) -> {
            String selectedCountry = countryMenu.getValue(); // get selected country
            try {
                // if the selected country is not null
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
                        divisionMenu.setPromptText("State");
                        break;
                    case "Canada": // if the selected country is Canada, set the division menu to province
                        divisionMenu.setPromptText("Province");
                        break;
                    case "UK": // if the selected country is the UK, set the division menu to country
                        divisionMenu.setPromptText("Country");
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
     * addappFieldSpy disables the add appointment button if any of the fields are empty
     * I used this same idea in Software I
     * I used boolean bindings to bind the disable/enable property of the add appointment button to the text fields
     * The method is called as the cursor enters the appointment tab
     * So that all the fields must be filled in before the button is enabled
     * This is an effective way to prevent the user from adding an appointment with empty fields
     * And what I like to call Exception Prevention
     * */
    @FXML public void appointmentFieldSpy() {
        addAppointmentButton.disableProperty().bind(Bindings.createBooleanBinding(
                        () ->   !viewAppointments.getSelectionModel().isEmpty() ||
                                titleField.getText().isEmpty() ||
                                descriptionTextArea.getText().isEmpty() ||
                                locationField.getText().isEmpty() ||
                                typeField.getText().isEmpty() ||
                                usersMenu.getValue() == null ||
                                customersMenu.getValue() == null ||
                                contactsMenu.getValue() == null ||
                                datePicker.getValue() == null ||
                                startHourBox.getValue() == null ||
                                endHourBox.getValue() == null,
                        titleField.textProperty(),
                        descriptionTextArea.textProperty(),
                        locationField.textProperty(),
                        typeField.textProperty(),
                        usersMenu.valueProperty(),
                        customersMenu.valueProperty(),
                        contactsMenu.valueProperty(),
                        datePicker.valueProperty(),
                        startHourBox.valueProperty(),
                        endHourBox.valueProperty()
                )
        );
    }

    /**
     * addappFieldSpy disables the add appointment button if any of the fields are empty
     * * I used this same idea in Software I
     * I used boolean bindings to bind the disable/enable property of the add appointment button to the text fields
     * The method is called as the cursor enters the customer tab
     * So that all the fields must be filled in before the button is enabled
     * This is an effective way to prevent the user from adding an appointment with empty fields
     * And what I like to call Exception Prevention
     * */
    @FXML public void customerFieldSpy() {
        addCustomerButton.disableProperty().bind(Bindings.createBooleanBinding(
                        () ->   !viewCustomers.getSelectionModel().isEmpty() ||
                                customerNameField.getText().isEmpty() ||
                                addressField.getText().isEmpty() ||
                                postalField.getText().isEmpty() ||
                                phoneField.getText().isEmpty() ||
                                countryMenu.getValue() == null ||
                                divisionMenu.getValue() == null,
                        customerNameField.textProperty(),
                        addressField.textProperty(),
                        postalField.textProperty(),
                        phoneField.textProperty(),
                        countryMenu.valueProperty(),
                        divisionMenu.valueProperty()
                )
        );
    }

    /**
     * reportButtonSpy disables the report button if any of the fields are empty
     * Prevents null exceptions
     * */
    @FXML public void reportButtonSpy() {
        countByMonthTypeButton.disableProperty().bind(Bindings.createBooleanBinding(
                        () ->   !viewAppointments.getSelectionModel().isEmpty() ||
                                monthsMenu.getValue() == null ||
                                typesMenu.getValue() == null,
                        monthsMenu.valueProperty(),
                        typesMenu.valueProperty()
                )
        );
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
            e.printStackTrace(); // print the stack trace
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
     * exitApplication exits the application
     * */
    @FXML public void exitApplication() {
        System.exit(0); // close the application
    }

    /**
     * initialize initializes the controller class
     * @param url is the url
     * @param resourceBundle is the resource bundle
     * uses a lambda expression to set the cell value factory for the contact column considering constraints of the entity relationships
     * also incorporates streams to filter countries and first level divisions
     * this method also handles populating the time boxes with the appropriate time zones
     * I designed the code to handle the time boxes so that they would only populate the boxes with local times that are related to business hours
     * Being designed this way prevents the user from scheduling appointments outside of business hours
     * You can find code to test for different time zones, which were mentioned in the project outline, in the comments below
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // TODO
            findAppointments(); // Find Appointments with fifteen minutes of the current time
            AgentFord.frontDoorSurveillance(infraredGoggles); // Front Door Surveillance, tracks logins
            InetAddress ip = InetAddress.getLocalHost(); // get the local host
            String username = LoginController.getUsername(); // get the username
            userCreds.setText(username + " from " + ip); // set the user credentials label to the username

            usersMenu.setItems(UserAccess.getAllUserNames()); // set the user combo box to the usernames
            usersMenu.setPromptText("Users"); // set the prompt text for the user combo box

            customersMenu.setItems(CustomerAccess.getAllCustomerNameStrings()); // Sets the customer combo box
            customersMenu.setPromptText("Customers"); // set the prompt text for the customer combo box

            // set the default time zone to Shanghai for fun during testing, can be found in the code below
            // More values which can be used to test time zones
            // "US/Mountain" for Arizona, "US/Eastern" for New York, "Europe/London" for London, "Canada/Eastern" for Montreal
            // "Asia/Shanghai" for Shanghai, "Australia/Sydney" for Sydney, "Africa/Johannesburg" for Johannesburg just to check

            // set the start and end hour combo boxes, only adding hours between 8am and 10pm which are business hours
            IntStream.rangeClosed(8, 22).forEach(hour -> { // start at 8 and end at 22
                for (int minute = 0; minute < 60; minute += 30) { // increment by 30 minutes
                    if (hour == 22 && minute == 30) { // if the hour is 22 and the minute is 30, break
                        return;
                    }
                    LocalTime estTime = LocalTime.of(hour, minute); // create a local time object
                    ZoneId estZone = ZoneId.of("America/New_York"); // create a zone id object for est
                    ZonedDateTime estZonedTime = ZonedDateTime.of(LocalDate.now(), estTime, estZone); // create a zoned date time object for est
                    // ZoneId localZone = ZoneId.of("Asia/Shanghai"); // get the local zone for Shanghai for testing
                    ZoneId localZone = ZoneId.systemDefault(); // create a zone id object for the local time zone, comment this out for testing other zones
                    ZonedDateTime localZonedTime = estZonedTime.withZoneSameInstant(localZone); // create a zoned date time object for the local time zone
                    startHourBox.getItems().add(localZonedTime.format(DateTimeFormatter.ofPattern("h:mm a"))); // add the time to the start hour combo box
                    endHourBox.getItems().add(localZonedTime.format(DateTimeFormatter.ofPattern("h:mm a"))); // add the time to the end hour combo box
                }
            });

            // lambda expression to set the cell value factory for the start time column
            startColumn.setCellValueFactory(cellData -> { // set the cell value factory for the start time column
                Appointments appointment = cellData.getValue(); // get the value of the cell
                LocalDateTime start = appointment.getStartTime(); // get the start time
                // ZoneId localZone = ZoneId.of("Asia/Shanghai"); // get the local zone for Shanghai for testing
                ZoneId localZone = ZoneId.systemDefault(); // get the local zone, comment this out for testing other zones
                ZonedDateTime localStart = start.atZone(ZoneId.of("UTC")).withZoneSameInstant(localZone); // convert the start time to the local zone from UTC
                return new SimpleStringProperty(localStart.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))); // return the start time
            });

            // lambda expression to set the cell value factory for the end time column
            endColumn.setCellValueFactory(cellData -> { // set the cell value factory for the end time column
                Appointments appointment = cellData.getValue(); // get the value of the cell
                LocalDateTime end = appointment.getEndTime(); // get the end time
                // ZoneId localZone = ZoneId.of("Asia/Shanghai"); // get the local zone for Shanghai
                ZoneId localZone = ZoneId.systemDefault(); // get the local zone, comment this out for testing other zones
                ZonedDateTime localStart = end.atZone(ZoneId.of("UTC")).withZoneSameInstant(localZone); // convert the end time to the local zone from UTC
                return new SimpleStringProperty(localStart.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))); // return the end time
            });

            // observable list of all appointments
            ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList(AppointmentAccess.allAppointments());
            // observable list of all customers
            ObservableList<Customers> customersList = FXCollections.observableArrayList(CustomerAccess.getAllCustomers());

            // lambda expression to set the cell value factory for the contact column// create an observable list of appointments
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

            typesList = QueryChronicles.getAppointmentTypes(); // get the appointment types
            for(String type : typesList) {
                typesMenu.getItems().add(type); // add the types to the types menu
            }
            typesMenu.setValue("Types"); // set the prompt text for the types menu

            monthsMenu.setValue("Months"); // set the prompt text for the months menu
            monthsMenu.setItems(monthsList); // set the items in the months menu to the observable list

            ObservableList<String> contactsList = FXCollections.observableArrayList(ContactAccess.getContactNames()); // get the list of contact names from the db
            contactsMenu.setItems(contactsList); // set the items in the contact menu to the observable list
            contactsMenu.setPromptText("Contacts"); // set the prompt text for the contact menu
            reportContactMenu.setItems(contactsList); // set the items in the contact menu to the observable list
            reportContactMenu.setPromptText("Contacts"); // set the prompt text for the contact menu

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