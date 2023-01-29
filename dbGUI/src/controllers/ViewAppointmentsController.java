package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Appointments;

public class ViewAppointmentsController {
    @FXML public TextField appidField, titleField, conidField, useridField, locationField, cusidField, typeField;
    @FXML public TextArea detailsTextArea;
    @FXML public TableView viewAppointments;
    @FXML public Button exitButton;
    @FXML public SplitMenuButton optionsButton, viewByButton;
    ObservableList<Appointments> appointmentsList;
}
