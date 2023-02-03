package company;

import controllers.LoginController;
import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(); // creates a new FXMLLoader object
            loader.setLocation(LoginController.class.getResource("/views/loginMenu.fxml")); // sets the location of the loader to the loginMenu.fxml file
            Scene scene; // sets the scene to the loginMenu.fxml file
            scene = new Scene(loader.load(), 630, 415);
            stage.setResizable(false); // sets the stage to not be resizable
            stage.setTitle("Schedulizer | Stephan Haloftis | shaloft@wgu.edu");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            launch(args); // launches the application
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}