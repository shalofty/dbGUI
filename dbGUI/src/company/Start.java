package company;

import exceptions.ExceptionPolice;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("/views/loginMenu.fxml")); // sets the location of the loader to the loginMenu.fxml file
        Scene scene = new Scene(fxmlLoader.load(), 630, 415); // sets the scene to the loginMenu.fxml file
        stage.setResizable(false); // sets the stage to not be resizable
        stage.setTitle("Schedulizer | Stephan Haloftis | shaloft@wgu.edu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        try {
            launch(args); // launches the application
        }
        catch (Exception e) {
            ExceptionPolice.illegalActivity(e);
        }
    }
}