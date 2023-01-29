package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {
    @FXML public Button appointmentsButton, customersButton, reportsButton, exitButton;
    Stage stage;

    public void appointmentsButtonClicked(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainMenuController.class.getResource("/views/viewAppointments.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 630, 415);
            stage.setResizable(false);
            stage.setTitle("Schedulizer | Stephan Haloftis | shaloft@wgu.edu");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void customersButtonClicked(ActionEvent event) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainMenuController.class.getResource("/views/customerRecords.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 630, 415);
            stage.setResizable(false);
            stage.setTitle("Schedulizer | Stephan Haloftis | shaloft@wgu.edu");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

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
        }
    }

    public void exitButtonClicked(ActionEvent event) {
        System.out.println("Exit button clicked");
    }
}
