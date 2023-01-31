package controllers;

import DataAccess.*;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class aioController implements Initializable {

    @FXML public Tab appointmentsTab, customersTab, reportsTab, logTab;

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
//    @FXML public ObservableList<CountryAccess> countriesList = FXCollections.observableArrayList(CountryAccess.getCountries());
//    @FXML public ObservableList<DivisionAccess> divisionsList = FXCollections.observableArrayList(DivisionAccess.getDivisions());
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
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime localZonedDateTime = localDateTime.atZone(localZone);
        Instant instant = localZonedDateTime.toInstant();
        return instant.atZone(ZoneOffset.UTC).toLocalDateTime();
    }

    /**
     * isWithinBusinessHours checks if the appointment is within business hours
     * @param localStartDate - the start date of the appointment
     * @param localStartTime - the start time of the appointment
     * @param localEndDate - the end date of the appointment
     * @param localEndTime - the end time of the appointment
     * */
    @FXML public boolean isWithinBusinessHours(LocalDate localStartDate, LocalTime localStartTime, LocalDate localEndDate, LocalTime localEndTime) {
        ZoneId easternZoneId = ZoneId.of("America/New_York");
        LocalDateTime startDateTime = LocalDateTime.of(localStartDate, localStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(localEndDate, localEndTime);
        ZonedDateTime easternStartDateTime = startDateTime.atZone(easternZoneId);
        ZonedDateTime easternEndDateTime = endDateTime.atZone(easternZoneId);

        LocalTime startTime = easternStartDateTime.toLocalTime();
        LocalTime endTime = easternEndDateTime.toLocalTime();
        LocalTime openTime = LocalTime.of(7, 59);
        LocalTime closeTime = LocalTime.of(22, 1);
        return startTime.isAfter(openTime) && endTime.isBefore(closeTime);
    }


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
                startTimeBox.setValue(selectedAppointment.getStartTime().toLocalTime().toString());
                endTimeBox.setValue(selectedAppointment.getEndTime().toLocalTime().toString());
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
     * */
    @FXML public void clearSelectedAppointment() {
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
        startTimeBox.setValue(null);
        endTimeBox.setValue(null);
        addAppointmentButton.setDisable(false);
        modifyAppointmentButton.setDisable(true);
        deleteAppointmentButton.setDisable(true);
    }

    /**
     * generateAppointmentID generates a new appointment ID
     * @return newID
     * */
    @FXML public int generateAppointmentID() {
        Random randy = new Random();
        int maxID = 9999;
        int newID = randy.nextInt(maxID);
        for (Appointments appointment : appointmentsList) {
            if (appointment.getAppointmentID() == newID) {
                return generateAppointmentID();
            }
        } return newID;
    }

    /**
     * generateCustomerID generates a new customer ID
     * @return newID
     * */
    @FXML public int generateCustomerID() {
        Random randy = new Random();
        int maxID = 9999;
        int newID = randy.nextInt(maxID);
        for (Customers customer : customersList) {
            if (customer.getCustomerID() == newID) {
                return generateCustomerID();
            }
        } return newID;
    }

    /**
     * addAppointment adds a new appointment to the database
     * */
    @FXML public void addAppointment() {
        try {
            Connection connection = JDBC.getConnection(); // establish connection, passing into insertIntoAppointment()
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
                return;
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
            SQLQueries.insertIntoAppointments(connection,
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

            // updating the appointments table
            updateAppointments();
            // clearing the text fields
            clearSelectedAppointment();
            JDBC.closeConnection(); // close connection

        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error: " + e.getMessage() + "Cause: " + e.getCause());
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
            e.printStackTrace();
            alert.showAndWait();
        }
    }

    /**
     * modifyAppointment modifies an appointment in the database
     * */
    @FXML public void modifyAppointment() {
        try {
            // open a connection, pass to updateAppoint() method below
            Connection connection = JDBC.getConnection();
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
                SQLQueries.updateAppointment(connection,
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
                JDBC.closeConnection(); // close connection
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
     * */
    @FXML public void deleteAppointment() {
        try {
            Connection connection = JDBC.getConnection();
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

    // Customers Tab Methods ///////////////////////////////////////////////////////////////////////////////////////////

    /**
     * selectCustomer selects a customer from the tableview
     * disables add button, enables modify and delete buttons to prevent pointer exceptions
     * */
    public void selectCustomer(){
        try {
            JDBC.openConnection(); // establish connection
            selectedCustomer = viewCustomers.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null) {
                customerIDField.setText(String.valueOf(selectedCustomer.getCustomerID()));
                customerNameField.setText(selectedCustomer.getCustomerName());
                addressField.setText(selectedCustomer.getCustomerAddress());
                postalField.setText(selectedCustomer.getPostalCode());
                phoneField.setText(selectedCustomer.getCustomerPhone());
                divisionMenu.valueProperty().set(selectedCustomer.getCountryDivision());

                ObservableList<CountryAccess> countryList = CountryAccess.getCountries();
                ObservableList<DivisionAccess> divisionList = DivisionAccess.getDivisions();

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
            JDBC.closeConnection(); // close connection
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
     * clearSelectedCustomer clears the selected customer
     * enables add button, disables modify and delete buttons to prevent pointer exceptions
     * */
    public void clearSelectedCustomer() {
        selectedCustomer = null;
        customerIDField.clear();
        customerNameField.clear();
        addressField.clear();
        postalField.clear();
        phoneField.clear();
        countryMenu.valueProperty().set(null);
        divisionMenu.valueProperty().set(null);
        addCustomerButton.setDisable(false);
        modifyCustomerButton.setDisable(true);
        deleteCustomerButton.setDisable(true);
    }

    // addCustomer
    @FXML public void addCustomer(ActionEvent event) {
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Add Customer");
            alert.setHeaderText("Add Customer");
            alert.setContentText("Are you sure you want to add this customer?");
            Optional<ButtonType> result;
            result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Connection connection = JDBC.getConnection();
                int customerID = generateAppointmentID();
                String customerName = customerNameField.getText();
                String address = addressField.getText();
                String postalCode = postalField.getText();
                String phone = phoneField.getText();
                String country = countryMenu.getValue();
                String division = divisionMenu.getValue();
                JDBC.setPreparedStatement(connection, SQLQueries.INSERT_CUSTOMER);
                clearSelectedCustomer();
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
                || endDatePicker.getValue() == null;
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
                || divisionMenu.getValue() == null;
    }

    // Initialize and Support  /////////////////////////////////////////////////////////////////////////////////////////

    // updateAppointments
    public void updateAppointments() {
        viewAppointments.setItems(appointmentsList);
    }

    // updateCustomers
    public void updateCustomers() {
        viewCustomers.setItems(customersList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // TODO
            // establishing connection to db
            JDBC.openConnection();

            // set up the times for the start and end time combo boxes
            startTimeBox.setItems(times);
            endTimeBox.setItems(times);

            // set up the Appointment columns in the table, must match the names of the variables in the model
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

            // set up the Customer columns in the table, must match the names of the variables in the model
            customerIDRecordsColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            postalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
            divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

            // call getCountries method to populate an ObservableList to populate the country menu
            ObservableList<CountryAccess> countriesList = CountryAccess.getCountries();
            countriesList.stream().map(Country::getCountryName).forEach(countryObservableList::add);
            countryMenu.setItems(countryObservableList);

            // call getDivisions method to populate an ObservableList to populate the division menu
            ObservableList<DivisionAccess> divisionsList = DivisionAccess.getDivisions();
            divisionsList.stream().map(Division::getDivisionName).forEach(divisionObservableList::add);
            divisionMenu.setItems(divisionObservableList);

            // setItems for the tableviews
            viewAppointments.setItems(appointmentsList);
            viewCustomers.setItems(customersList);

            // update tables
            updateAppointments();
            updateCustomers();

            JDBC.closeConnection(); // close connection to db
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error: aioController.java initialize");
            alert.setContentText("Error: " + e.getMessage() + "Cause: " + e.getCause());
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
            alert.showAndWait();
        }
    }
}
