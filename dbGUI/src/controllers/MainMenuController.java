package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController {
    @FXML public Button appointmentsButton, customersButton, reportsButton, exitButton;
    Stage stage;

    /**
     * @param event triggered when appointments button is pressed
     * */
    public void appointmentsButtonClicked(ActionEvent event) throws Exception {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(MainMenuController.class.getResource("/views/viewAppointments.fxml")));
            stage = (Stage) appointmentsButton.getScene().getWindow();
            Scene scene = new Scene(root, 900, 760);
            stage.setResizable(false);
            stage.setTitle("Schedulizer | Stephan Haloftis | shaloftis@wgu.edu");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred.");
            alert.setContentText("Error: " + e.getMessage() + "\nCause: " + e.getCause());
            alert.showAndWait();
        }
    }

    /**
     * @param event triggered when customers button is pressed
     * */
    public void customersButtonClicked(ActionEvent event) throws Exception {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(MainMenuController.class.getResource("/views/customerRecords.fxml")));
            stage = (Stage) customersButton.getScene().getWindow();
            Scene scene = new Scene(root, 566, 608);
            stage.setResizable(false);
            stage.setTitle("Schedulizer | Stephan Haloftis | shaloft@wgu.edu");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred.");
            alert.setContentText("Error: " + e.getMessage() + "\nCause: " + e.getCause());
            alert.showAndWait();
        }
    }

    /**
     * @param event triggered when reports button is pressed
     * */
    public void reportsButtonClicked(ActionEvent event) throws Exception {
        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(MainMenuController.class.getResource("/views/customerRecords.fxml"));
//            Scene scene = new Scene(fxmlLoader.load(), 630, 415);
//            stage.setResizable(false);
//            stage.setTitle("Schedulizer | Stephan Haloftis | shaloft@wgu.edu");
//            stage.setScene(scene);
//            stage.show();
            System.out.println("Reports window hasn't been created yet!");
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred.");
            alert.setContentText("Error: " + e.getMessage() + "\nCause: " + e.getCause());
            alert.showAndWait();
        }
    }

    /**
     * @param event triggered when exit button is pressed
     * */

    public void exitButtonClicked(ActionEvent event) {
        System.exit(0);
    }
}
